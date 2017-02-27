package com.yang.eric.onestep.view.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.yang.eric.onestep.R;
import com.yang.eric.onestep.contract.ZhiHuDailyContract;
import com.yang.eric.onestep.model.adapter.ZhihuDailyNewsAdapter;
import com.yang.eric.onestep.model.bean.ZhihuDailyNews;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZhihuDailyFragment extends Fragment implements ZhiHuDailyContract.View{

	private RecyclerView recyclerView;
	private SwipeRefreshLayout refresh;
	private FloatingActionButton fab;
	private ZhihuDailyNewsAdapter adapter;

	private int mYear = Calendar.getInstance().get(Calendar.YEAR);
	private int mMonth = Calendar.getInstance().get(Calendar.MONTH);
	private int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

	private ZhiHuDailyContract.Presenter presenter;
	public ZhihuDailyFragment() {}

	public static ZhihuDailyFragment newInstance(){
		return new ZhihuDailyFragment();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list,container,false);

		initViews(view);

		presenter.start();

		refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				presenter.refresh();
			}
		});

		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

			boolean isSlidingToLast = false;
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

				LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
				//当不滚动时
				if(newState == RecyclerView.SCROLL_STATE_IDLE){
					//获取最后一个完全显示的item position
					int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
					int totalItemCout = manager.getItemCount();

					//判断是否滚动到底部并且是向下滑动
					if (lastVisibleItem == (totalItemCout - 1) && isSlidingToLast) {
						Calendar c = Calendar.getInstance();
						c.set(mYear, mMonth, --mDay);
						presenter.loadMore(c.getTimeInMillis());
					}
				}
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				isSlidingToLast = dy > 0;

				//隐藏或者显示fab
				if(dy > 0){
					fab.hide();
				}else {
					fab.show();
				}
			}
		});
		return view;
	}

	@Override
	public void setPresenter(ZhiHuDailyContract.Presenter presenter) {
		if(presenter != null){
			this.presenter = presenter;
		}
	}

	@Override
	public void initViews(View view) {
		recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
		recyclerView.setHasFixedSize(true);

		//修复不显示数据的问题
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		refresh = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
		//设置下拉刷新的按钮的颜色
		refresh.setColorSchemeResources(R.color.colorPrimary);

		fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
		fab.setRippleColor(getResources().getColor(R.color.colorPrimaryDark));
	}

	@Override
	public void showPickDialog() {
		DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				Calendar temp = Calendar.getInstance();
				temp.clear();
				temp.set(year, monthOfYear, dayOfMonth);
				presenter.loadPosts(temp.getTimeInMillis(), true);
			}
		},mYear, mMonth, mDay);

		dialog.setMaxDate(Calendar.getInstance());
		Calendar minDate = Calendar.getInstance();
		minDate.set(2013, 5, 20);
		dialog.setMinDate(minDate);
		dialog.vibrate(false);

		dialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
	}

	@Override
	public void showLoading() {
		refresh.post(new Runnable() {
			@Override
			public void run() {
				refresh.setRefreshing(true);
			}
		});
	}

	@Override
	public void stopLoading() {
		refresh.post(new Runnable() {
			@Override
			public void run() {
				refresh.setRefreshing(false);
			}
		});
	}

	@Override
	public void showResults(ArrayList<ZhihuDailyNews.StoriesBean> list) {
		if (adapter == null){
			adapter = new ZhihuDailyNewsAdapter(list, getContext());
			adapter.setItemListener(new ZhihuDailyNewsAdapter.OnRecyclerViewOnClickListener() {
				@Override
				public void OnItemClick(View v, int position) {
					presenter.startReading(position);
				}
			});
			recyclerView.setAdapter(adapter);
		}else {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void showError() {
		Snackbar.make(fab, R.string.loaded_failed, Snackbar.LENGTH_INDEFINITE)
				.setAction(R.string.retry, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						presenter.refresh();
					}
				})
				.show();
	}
}
