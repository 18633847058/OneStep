package com.yang.eric.onestep.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yang.eric.onestep.R;
import com.yang.eric.onestep.view.fragment.ZhihuDailyFragment;

/**
 * Created by Yang on 2017/2/24.
 */

public class MainPagerAdapter extends FragmentPagerAdapter{

	private String[] titles;
	private final Context context;
	private ZhihuDailyFragment zhihuDailyFragment;

	public ZhihuDailyFragment getZhihuDailyFragment() {
		return zhihuDailyFragment;
	}

	public MainPagerAdapter(FragmentManager fm,
	                        Context context,
	                        ZhihuDailyFragment zhihuDailyFragment) {
		super(fm);
		this.context = context;
		titles = new String[] {
				context.getResources().getString(R.string.zhihu_daily),
//				context.getResources().getString(R.string.guokr_handpick),
//				context.getResources().getString(R.string.douban_moment)
		};
		this.zhihuDailyFragment = zhihuDailyFragment;
	}

	@Override
	public Fragment getItem(int position) {
		return zhihuDailyFragment;
	}

	@Override
	public int getCount() {
		return titles.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}
}
