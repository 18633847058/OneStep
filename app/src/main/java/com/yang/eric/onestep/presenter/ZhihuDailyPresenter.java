package com.yang.eric.onestep.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yang.eric.onestep.Api;
import com.yang.eric.onestep.contract.ZhiHuDailyContract;
import com.yang.eric.onestep.model.bean.ZhihuDailyNews;
import com.yang.eric.onestep.model.database.DatabaseHelper;
import com.yang.eric.onestep.model.https.HttpsUtils;
import com.yang.eric.onestep.model.https.OnStringListener;
import com.yang.eric.onestep.util.DateFormatter;
import com.yang.eric.onestep.util.NetworkState;

import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Yang on 2017/2/17.
 */

public class ZhihuDailyPresenter implements ZhiHuDailyContract.Presenter {

	private ZhiHuDailyContract.View view;
	private Context context;
	private HttpsUtils httpsUtils;

	private DateFormatter formatter = new DateFormatter();
	private Gson gson = new Gson();

	private ArrayList<ZhihuDailyNews.StoriesBean> list = new ArrayList<>();

	private DatabaseHelper databaseHelper;
	private SQLiteDatabase database;

	public ZhihuDailyPresenter(Context context, ZhiHuDailyContract.View view) {
		this.context = context;
		this.view = view;
		this.view.setPresenter(this);
		httpsUtils = HttpsUtils.getInstance();
		databaseHelper = new DatabaseHelper(context, "History.db", null, 5);
		database = databaseHelper.getWritableDatabase();
	}

	@Override
	public void loadPosts(final long date, final boolean clearing) {
		if(clearing){
			view.showLoading();
		}

		if(NetworkState.networkConnected(context)){
			httpsUtils.doGet(Api.ZHIHU_HISTORY + formatter.ZhihuDailyDateFormat(date), new OnStringListener() {
				@Override
				public void onSuccess(Reader result) {
					try {
						ZhihuDailyNews post = gson.fromJson(result, ZhihuDailyNews.class);
						ContentValues values = new ContentValues();

						if(clearing){
							list.clear();
						}

						for(ZhihuDailyNews.StoriesBean storiesBean : post.getStories()){
							list.add(storiesBean);
							if(!queryIfIDExists(storiesBean.getId())){
								database.beginTransaction();
								try {
									DateFormat format = new SimpleDateFormat("yyyyMMdd");
									Date date = format.parse(post.getDate());
									values.put("zhihu_id",storiesBean.getId());
									values.put("zhihu_news",gson.toJson(storiesBean));
									values.put("zhihu_content","");
									values.put("zhihu_time",date.getTime() / 1000);
									database.insert("Zhihu", null, values);
									values.clear();
									database.setTransactionSuccessful();
								} catch (ParseException e) {
									e.printStackTrace();
								} finally {
									database.endTransaction();
								}
							}
							view.showResults(list);
						}
					} catch (JsonSyntaxException e) {
						view.showError();
					}
					view.showLoading();
				}

				@Override
				public void onError() {
					view.showLoading();
					view.showError();
				}
			});
		}else {
			if(clearing) {
				list.clear();

				Cursor cursor = database.query("Zhihu", null, null, null, null, null, null);
				if(cursor.moveToFirst()){
					do {
						ZhihuDailyNews.StoriesBean storiesBean = gson.fromJson(
								cursor.getString(cursor.getColumnIndex("zhihu_news")),
								ZhihuDailyNews.StoriesBean.class);
						list.add(storiesBean);
					} while (cursor.moveToNext());
				}
				cursor.close();
				view.stopLoading();
				view.showResults(list);
			}else {
				view.showError();
			}
		}
	}

	@Override
	public void refresh() {
		loadPosts(Calendar.getInstance().getTimeInMillis(), true);
	}

	@Override
	public void loadMore(long date) {
		loadPosts(date, false);
	}

	@Override
	public void startReading(int position) {
		Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void feelLucky() {
		Toast.makeText(context, "be lucky!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void start() {
		loadPosts(Calendar.getInstance().getTimeInMillis(), true);
	}
	private boolean queryIfIDExists(int id){

		Cursor cursor = database.query("Zhihu",null,null,null,null,null,null);
		if (cursor.moveToFirst()){
			do {
				if (id == cursor.getInt(cursor.getColumnIndex("zhihu_id"))){
					return true;
				}
			} while (cursor.moveToNext());
		}
		cursor.close();

		return false;
	}
}
