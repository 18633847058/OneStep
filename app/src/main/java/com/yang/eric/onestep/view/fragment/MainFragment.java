package com.yang.eric.onestep.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yang.eric.onestep.R;
import com.yang.eric.onestep.presenter.ZhihuDailyPresenter;
import com.yang.eric.onestep.view.adapter.MainPagerAdapter;

import java.util.Random;

public class MainFragment extends Fragment {

	private Context context;

	private TabLayout tabLayout;
	private FloatingActionButton fab;

	private ZhihuDailyFragment zhihuDailyFragment;
	private ZhihuDailyPresenter zhihuDailyPresenter;


	private MainPagerAdapter adapter;


	public MainFragment() {}

	public static MainFragment newInstance() {
		return new MainFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.context = getActivity();
		if(savedInstanceState != null){
			FragmentManager manager = getChildFragmentManager();
			zhihuDailyFragment = (ZhihuDailyFragment) manager.getFragment(savedInstanceState, "zhihu");
		}else {
			zhihuDailyFragment = ZhihuDailyFragment.newInstance();
		}
		zhihuDailyPresenter = new ZhihuDailyPresenter(context, zhihuDailyFragment);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		initView(view);
		setHasOptionsMenu(true);
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				if(tab.getPosition() == 1){
					fab.hide();
				}else {
					fab.show();
				}
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(tabLayout.getSelectedTabPosition() == 0){
					adapter.getZhihuDailyFragment().showPickDialog();
				}else if(tabLayout.getSelectedTabPosition() == 2){
					adapter.getZhihuDailyFragment().showPickDialog();
				}
			}
		});
		return view;
	}

	private void initView(View view) {
		tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
		fab = (FloatingActionButton) view.findViewById(R.id.fab);
		ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
		viewPager.setOffscreenPageLimit(3);
		adapter = new MainPagerAdapter(getChildFragmentManager(), context,
				zhihuDailyFragment);
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if(id == R.id.action_feel_lucky){
			feelLucky();
		}
		return true;
	}

	private void feelLucky() {
		Random random = new Random();
		int type = random.nextInt(3);
		switch (type){
			case 0:
				zhihuDailyPresenter.feelLucky();
				break;
			default:
				Toast.makeText(context, type + "", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		FragmentManager manager = getChildFragmentManager();
		manager.putFragment(outState, "zhihu", zhihuDailyFragment);
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}
}
