package com.ngc.corelib.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.ngc.corelib.http.fileupload.StringUtils;
import com.ngc.corelib.imageloader.BaseConfigure;

/**
 * @Title:SystemInfo.java
 * @Copyright:丝路通顺(北京)技术有限公司
 * @Description:TODO
 * @author:Yunlong Luo
 * @data:2014年8月11日
 * @version: v1.0
 */
public class SystemInfo {

	/**
	 * 设置屏幕
	 * 
	 * @param flag
	 */
	protected void setFullScreen(boolean flag, Activity activity) {

		if (flag) {
			activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

	/**
	 * wifi开启检查
	 * 
	 * @return
	 */
	protected boolean enabledWifi(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED ? true : false;
	}

	/**
	 * 3G开启检查
	 * 
	 * @return
	 */
	protected boolean enabledNetwork(Context context) {
		ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo();
		return info != null && info.isAvailable() ? true : false;
	}

	/**
	 * 获取手机唯一标识
	 * 
	 * @return
	 */
	public String getDeviceID(Context context) {
		TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telMgr.getDeviceId();
	}

	/**
	 * 系统版本号
	 * 
	 * @return
	 */
	protected String getRelease() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * 用户手机型号
	 * 
	 * @return
	 */
	protected String getModel() {
		return Build.MODEL;
	}

	/**
	 * 获取用户手机号
	 * 
	 * @return
	 */
	protected String getMobileNo(Context context) {
		TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telMgr.getLine1Number();
	}

	/**
	 * @Title: getVersionName
	 * @Description: 取得当前VersionName
	 * @param context
	 * @return String 返回类型
	 */

	public static String getVersionName(Context context) {

		String versionName = "";
		try {
			versionName = getPackageInfo(context).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * @Description: 获取版本信息
	 * */
	private static PackageInfo getPackageInfo(Context context) throws NameNotFoundException {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		return packageInfo;
	}

	/**
	 * 设备标识号
	 */
	private static String deviceId = null;

	/**
	 * 获取手机号码(支持部分制式)
	 * 
	 * @param context
	 * @return
	 */
	public static String getPhoneNumber(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getLine1Number();
	}

	/**
	 * 获取当前系统版本（如 11）
	 * 
	 * @return
	 */
	public static int getSystemVersionCode() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 获取手机串号
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		if (deviceId == null) {
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			deviceId = telephonyManager.getDeviceId();
		}
		if (TextUtils.isEmpty(deviceId)) {
			// 仍然是为空时
			deviceId = StringUtils.join("NO IMEI - ", System.currentTimeMillis());
		}
		return deviceId;
	}

	/**
	 * 获取ip
	 * 
	 * @return
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}

	/**
	 * 获取机器总内存
	 * 
	 * @param context
	 * @return
	 */
	public static long getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();

		} catch (IOException e) {
		}
		return initial_memory;
	}

	/**
	 * 获取分配的内存大小
	 * 
	 * @param context
	 * @return
	 */
	public static int getMemoryClass(Context context) {
		return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
	}

	/**
	 * 获取本地客户端版本号(纯显示)
	 * 
	 * @return
	 */
	public static String getVersion(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			// int versionCode = pi.versionCode;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
			// // 追加显示versionCode
			// StringBuilder buffer = new StringBuilder();
			// buffer.append(versionName).append("-").append(versionCode);
			// versionName = buffer.toString();
		} catch (Exception e) {
		}
		return versionName;
	}

	/**
	 * 获取本地客户端版本号(CODE)
	 * 
	 * @return
	 */
	public static String getVersionCode(Context context) {
		int versionName = -1;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionCode;
		} catch (Exception e) {
		}
		return String.valueOf(versionName);
	}

	/**
	 * 客户端关闭
	 */
	public static void shutdown() {
		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);
		System.exit(0);
	}

	/**
	 * 手机型号
	 * 
	 * @return
	 */
	public static String getDeviceType() {
		return Build.MODEL;// model
	}

	/**
	 * 系统内核版本如：2.3.7
	 * 
	 * @return
	 */
	public static String getSystemVersion() {
		return Build.VERSION.RELEASE;// firmware version
	}

	/**
	 * 获取本应用的包名
	 * 
	 * @param context
	 * @return
	 */
	public static String getPackageName(Context context) {
		String packageName = null;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			packageName = pi.packageName;
		} catch (Exception e) {
		}
		return packageName;
	}

	/**
	 * 获取本应用的包名
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppMetaData(Context context, String key) {
		String data = null;
		try {
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);
			data = appInfo.metaData.getString(key);
		} catch (Exception e) {
		}
		return data;
	}

	/**
	 * 获取缓存大小
	 * 
	 * @return
	 */
	public static String getCacheSize(Context context) {
		if (BaseConfigure.isSdCard) {
		}
		long size = 0l;
		// 机身缓存
		File file = new File(context.getCacheDir().getPath());
		if (file.exists()) {
			size += FileUtils.getFileSize(file);
		}
		if (BaseConfigure.isSdCard) {
			// SD卡缓存
			file = new File(context.getExternalCacheDir().getPath());
			if (file.exists()) {
				size += FileUtils.getFileSize(file);
			}
		}
		return FileUtils.prettyBytes(size);
	}

}
