package com.read.mobile.utils.media;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.read.mobile.env.BaseApp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

public class SystemFunction {

	/**
	 * 取当前时
	 */
	public static String getSystemtime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E");
		return sdf.format(new Date());
	}

	/**
	 * 取煤体系统音量
	 * 
	 * @return 媒体的音量
	 */
	public static int getSystemVolume() {
		AudioManager mAudioManager = (AudioManager) BaseApp.getInstance().getSystemService(Context.AUDIO_SERVICE);
		return mAudioManager.getStreamVolume(AudioManager./* STREAM_SYSTEM */STREAM_MUSIC);
	}

	/**
	 * 是否为在线播放的媒体
	 * 
	 * @param url
	 *            要播放的媒体的url或文件名
	 * @return 如果是在线播放的媒体，返回true，否则返回false
	 */
	public static boolean isOnLineMedia(String url) {
		if (url == null)
			return false;
		url = url.toLowerCase();
		return url.startsWith("http://") || url.startsWith("rtsp://") || url.startsWith("https://");
	}

	/***
	 * 获取屏幕分辨率
	 * 
	 * @param context
	 * @return
	 */
	public static int[] getDisplay(Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int[] display = new int[2];
		display[0] = metric.widthPixels;
		display[1] = metric.heightPixels;
		return display;
	}

	public static int[] getConstantForLoadMore() {
		// Get value for loadMore in Manifest
		int[] params = new int[2];
		try {
			ApplicationInfo ai = BaseApp.getInstance().getPackageManager()
					.getApplicationInfo(BaseApp.getInstance().getPackageName(), PackageManager.GET_META_DATA);
			if (ai != null) {
				Bundle bundle = ai.metaData;
				params[0] = Integer.parseInt(bundle.getString("numberOfFirstLoadMsg"));
				params[1] = Integer.parseInt(bundle.getString("numberOfLoadMoreMsg"));
			}
		} catch (NotFoundException e) {

		} catch (NameNotFoundException e) {
		}
		return params;
	}

	/**
	 * Utility function. Unbinds all drawables in a given view (and its child
	 * tree).
	 * 
	 * @param findViewById
	 *            Root view of the tree to unbind
	 */
	public static void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}

		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			try {
				((ViewGroup) view).removeAllViews();
			} catch (UnsupportedOperationException ignore) {
				// if can't remove all view (e.g. adapter view) - no problem
			}
		}
	}

	public static int getStatusBarHeiht(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = context.getApplicationContext().getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
		}
		return sbar;
	}

	/***
	 * 获取媒体库中对应路径
	 * 
	 * @param _uri
	 * @param context
	 * @return
	 */
	public static String getPath(Uri _uri, Context context) {
		String selectedImagePath = null;
		// 1:MEDIA GALLERY --- query from MediaStore.Images.Media.DATA
		Cursor cursor = context.getContentResolver().query(_uri,
				new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);

		if (cursor != null) {
			cursor.moveToFirst();
			selectedImagePath = cursor.getString(0);
			cursor.close();
		} else {
			selectedImagePath = _uri.getPath();
		}
		return selectedImagePath;
	}

	public static boolean isMINUSystem() {
		if ("XIAOMI".equals(Build.BRAND.toUpperCase())) {
			return true;
		}
		String fingerPrint = android.os.Build.FINGERPRINT.toUpperCase();
		if (fingerPrint.contains("XIAOMI") || fingerPrint.contains("MIUI")) {
			return true;
		}
		return false;

	}

	public static boolean isTargetActivityRunning(Context context, String activityName) {

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		// get the info from the currently running task
		List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

		if (activityName.equals(taskInfo.get(0).topActivity.getClassName())) {
			return true;
		} else {
			return false;
		}
	}

	public static void inviteFriendsBySMS(String mobile, String content) {
		if (mobile == null || mobile.length() <= 0) {
			return;
		}
		try {
			Intent smsintent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + mobile));
			smsintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			smsintent.putExtra("sms_body", content);
			BaseApp.getInstance().getApplicationContext().startActivity(smsintent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getScreenWidth(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		return display.getWidth();
		// Point size = new Point();
		// display.getSize(size);
		// return size.x;
	}

	public static int getScreenHeight(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		return display.getHeight();
		// Point size = new Point();
		// display.getSize(size);
		// return size.y;
	}

	public static RunningTaskInfo getCurrentActivity(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		RunningTaskInfo info = manager.getRunningTasks(1).get(0);
		// String shortClassName = info.topActivity.getShortClassName(); //类名
		// String className = info.topActivity.getClassName(); //完整类名
		// String packageName = info.topActivity.getPackageName(); //包名
		return info;
	}

	// public static void updateUIAfterDataChanged(Context context, int orgid) {
	// RunningTaskInfo info = SystemFunction.getCurrentActivity(context);
	// boolean contains =
	// info.topActivity.getClassName().toString().contains(OrganizationActivity.class.getPackage().getName());
	// //如果是在组织界面并且是在组织变动时需要删除的组织里，才会回到组织界面
	// boolean isLastOrg = orgid ==
	// AppSharedPreferencesHelper.getSharedPreferences().getInt(AppSharedPreferencesHelper.LAST_ORG,
	// -1);
	// if (contains && isLastOrg) {
	// Intent intent = new Intent(context, MainActivity.class);
	// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// intent.putExtra(MainActivity.WHICH_ACTIVITY, MainActivity.RADIO_ORG);
	// context.startActivity(intent);
	// }
	// }

	/**
	 * is the device memory OK
	 * 
	 * @return true if the memory is OK; otherwise false
	 */
	public static boolean isDeviceMemoryOK() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		long size = blockSize * availableBlocks;
		if (size > 1024 * 1024) {
			return true;
		}
		return false;
	}

}
