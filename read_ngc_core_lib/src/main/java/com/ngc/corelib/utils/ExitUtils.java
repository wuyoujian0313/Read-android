package com.ngc.corelib.utils;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.Toast;

/**
 * 退出时toast询问是否退出的工具类
 * 
 */
public class ExitUtils {

	// 计时器
	private MyCount mc;
	private boolean isExit;
	private boolean hasTask;

	private static ExitUtils exitUtils = null;

	private ExitUtils() {
	}

	public static ExitUtils getInstance() {
		if (exitUtils == null) {
			exitUtils = new ExitUtils();
		}
		return exitUtils;
	}

	public boolean isExit(Activity activity) {
		if (isExit == false) {
			isExit = true;
			Toast.makeText(activity, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			if (!hasTask) {
				hasTask = true;
				mc = new MyCount(2000, 1000);
				mc.start();
			}
		} else {
			activity.finish();
			return true;
		}
		return false;
	}

	/* 定义一个倒计时的内部类 */
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			isExit = false;
			hasTask = false;
		}

		@Override
		public void onTick(long millisUntilFinished) {

		}
	}
}
