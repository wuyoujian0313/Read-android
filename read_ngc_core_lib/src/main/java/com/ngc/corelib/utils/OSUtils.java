package com.ngc.corelib.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 
* @Title: OSUtils.java
* @Package: com.ngc.libs.os
* @author:luoyunlong@gmail.com
 */
public class OSUtils {

	/**
	 * 获取当前进程的名称
	 * @param context
	 * @return
	 */
	public static String getCurrentProcessName(Context context) {
		String currentProcessName = null;
		int pid = android.os.Process.myPid();
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos;
		try {
			infos = am.getRunningAppProcesses();
			for (RunningAppProcessInfo info : infos) {
				if (info.pid == pid) {
					currentProcessName = info.processName;
					break;
				}
			}
		} catch (Exception e) {
			// isolatedProcess 进程可能不具备获取 getRunningAppProcesses 权限
		}
		return currentProcessName;
	}

	/**
	 * 判断某一Service是否在运行
	 * @param context
	 * @param className
	 * @return
	 */
	public static boolean isServiceRunning(Context context, String className) {
		boolean isRunning = false;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos = am.getRunningServices(30);
		for (int i = 0; i < infos.size(); i++) {
			if (infos.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

}
