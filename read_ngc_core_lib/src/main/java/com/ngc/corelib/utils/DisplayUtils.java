package com.ngc.corelib.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DisplayUtils {
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取屏幕的宽
	 * 
	 * @param activity
	 * @return
	 */
	public static int getScreenWidth(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		return metrics.widthPixels;
	}

	/**
	 * 获取屏幕的高
	 * 
	 * @param activity
	 * @return
	 */
	public static int getScreenHeight(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}
}
