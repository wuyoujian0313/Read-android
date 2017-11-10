package com.read.mobile.utils;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.text.TextUtils;

public class Utils {
	/**
	 * 格式化时间
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatTime(String time) {
		if (!TextUtils.isEmpty(time)) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
			// SimpleDateFormat sdf = new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(Long.valueOf(time));
		} else
			return "00/00";
	}
}
