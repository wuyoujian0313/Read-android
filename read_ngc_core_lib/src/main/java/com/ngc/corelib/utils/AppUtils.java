package com.ngc.corelib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppUtils {
	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 * @throws Exception
	 */
	public static String getVersionName(Context context) throws Exception {
		PackageManager manager = context.getPackageManager();
		PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
		String versionName = info.versionName;
		return versionName;
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 * @throws Exception
	 */
	public static int getVersionCode(Context context) throws Exception {
		PackageManager manager = context.getPackageManager();
		PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
		int versionCode = info.versionCode;
		return versionCode;
	}

}
