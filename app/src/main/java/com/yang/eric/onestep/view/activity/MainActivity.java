package com.yang.eric.onestep.view.activity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.yang.eric.onestep.R;
import com.yang.eric.onestep.view.fragment.MainFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

	private MainFragment mainFragment;

	private NavigationView navigationView;
	private DrawerLayout drawerLayout;
	private Toolbar toolbar;
	public static final String ACTION_BOOKMARKS = "com.yang.eric.bookmarks";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViews();
		if(savedInstanceState != null){
			mainFragment = (MainFragment) getSupportFragmentManager().getFragment(savedInstanceState, "MainFragment");
		}else {
			mainFragment = MainFragment.newInstance();
		}

		if(!mainFragment.isAdded()){
			getSupportFragmentManager().beginTransaction()
					.add(R.id.layout_fragment, mainFragment, "MainFragment")
					.commit();
		}
		String action = getIntent().getAction();

//		if (action.equals(ACTION_BOOKMARKS)) {
////			showBookmarksFragment();
//			navigationView.setCheckedItem(R.id.nav_bookmarks);
//		} else {
//			showMainFragment();
//			navigationView.setCheckedItem(R.id.nav_home);
//		}
	}

	private void initViews() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this,
				drawerLayout,
				toolbar,
				R.string.navigation_drawer_open,
				R.string.navigation_drawer_close);
		drawerLayout.addDrawerListener(toggle);
		toggle.syncState();

		navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
	}

	private void showMainFragment() {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.show(mainFragment);
		fragmentTransaction.commit();

		toolbar.setTitle(R.string.app_name);
	}
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		drawerLayout.closeDrawer(GravityCompat.START);

		switch (item.getItemId()){
			case R.id.nav_home:
				showMainFragment();
				break;
			case R.id.nav_bookmarks:
				Toast.makeText(MainActivity.this, "bookmarks", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_change_theme:
				// change the day/night mode after the drawer closed
				drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
					@Override
					public void onDrawerSlide(View drawerView, float slideOffset) {

					}

					@Override
					public void onDrawerOpened(View drawerView) {

					}

					@Override
					public void onDrawerClosed(View drawerView) {
						SharedPreferences sp =  getSharedPreferences("user_settings",MODE_PRIVATE);
						if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
								== Configuration.UI_MODE_NIGHT_YES) {
							sp.edit().putInt("theme", 0).apply();
							AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
						} else {
							sp.edit().putInt("theme", 1).apply();
							AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
						}
						getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
						recreate();
					}

					@Override
					public void onDrawerStateChanged(int newState) {

					}
				});
				break;
			case R.id.nav_settings:
				Toast.makeText(MainActivity.this, "settings", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_about:
				Toast.makeText(MainActivity.this, "about", Toast.LENGTH_SHORT).show();
				break;
		}
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(mainFragment.isAdded()){
			getSupportFragmentManager().putFragment(outState, "MainFragment", mainFragment);
		}
	}
}
