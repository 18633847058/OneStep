package com.yang.eric.onestep.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yang.eric.onestep.R;
import com.yang.eric.onestep.contract.ZhiHuDailyContract;
import com.yang.eric.onestep.model.bean.ZhihuDailyNews;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZhihuDailyFragment extends Fragment implements ZhiHuDailyContract.View{


	public ZhihuDailyFragment() {}

	public static ZhihuDailyFragment newInstance(){
		return new ZhihuDailyFragment();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		TextView textView = new TextView(getActivity());
		textView.setText(R.string.hello_blank_fragment);
		return textView;
	}

	@Override
	public void setPresenter(ZhiHuDailyContract.Presenter presenter) {

	}

	@Override
	public void initViews(View view) {

	}

	@Override
	public void showPickDialog() {

	}

	@Override
	public void showLoading() {

	}

	@Override
	public void showResults(ArrayList<ZhihuDailyNews.StoriesBean> list) {

	}

	@Override
	public void showError() {

	}
}
