package com.yang.eric.onestep;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by Yang on 2017/2/21.
 */

public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		// the 'theme' has two value, 0 and 1
		// 0 --> day theme, 1 --> night theme
		if (getSharedPreferences("user_settings",MODE_PRIVATE).getInt("theme", 0) == 0) {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
		} else {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
		}
	}
}
