package com.yang.eric.onestep.model.https;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yang on 2017/2/17.
 */

public class HttpsUtils {

	private OkHttpClient client;
	private static HttpsUtils instance = null;

	private HttpsUtils() {
		this.client = new OkHttpClient();
	}
	public static synchronized HttpsUtils getInstance(){
		if(instance==null){
			instance=new HttpsUtils();
		}
		return instance;
	}
	public void doGet(String url ,final OnStringListener listener){
		final Request request = new Request.Builder()
				.url(url)
				.build();
		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				listener.onError(e);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				listener.onSuccess(response);
			}
		});
	}
}
