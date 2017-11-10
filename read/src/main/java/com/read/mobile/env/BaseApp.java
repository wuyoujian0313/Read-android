package com.read.mobile.env;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ngc.corelib.env.NGCApp;

public class BaseApp extends NGCApp {

	/**
	 * 全局共享参数对象
	 */
	private static SharedPreferences pref;

	@Override
	public void onCreate() {
		super.onCreate();
		// 地图调用

		pref = PreferenceManager.getDefaultSharedPreferences(this);
		instance = this;

	}

	private static Application instance;

	public static Application getInstance() {
		if (instance == null)
			throw new IllegalStateException();
		return instance;
	}

	/**
	 * 获取共享参数对象
	 * 
	 * @return
	 */
	public static SharedPreferences getSharedPreferences() {
		return pref;
	}
}
