package com.ngc.corelib.utils;

import com.ngc.corelib.BuildConfig;

import android.util.Log;

/**
 * 自定义Log类，通过 清单控制是否输出日志
 * 
 * @see Core_qianpin_android项目清单中android:debuggable属性来取决于是否输出 <br>
 *      使用方法：直接调用输出;
 * 
 * @author huangyuan
 * 
 */
public class Logger {

	public static void e(String msg) {
		if (BuildConfig.DEBUG) {
			Log.e("NGC", msg);
		}
	}

	public static void d(String msg) {
		if (BuildConfig.DEBUG) {
			Log.d("NGC", msg);
		}
	}

	public static void i(String msg) {
		if (BuildConfig.DEBUG) {
			Log.i("NGC", msg);
		}
	}
}
