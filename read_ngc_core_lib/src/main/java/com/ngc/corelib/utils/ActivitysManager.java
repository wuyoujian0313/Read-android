package com.ngc.corelib.utils;


import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

public class ActivitysManager {
	private static Stack<Activity> mActivityStack = new Stack<Activity>();
	private static Activity mActivity;

	/**
	 * 添加Activity到堆栈
	 */
	public static void addActivity(Activity mActivity) {
		if (mActivity != null) {
			mActivityStack.push(mActivity);
			System.out.println("添加一个Activity，目前" + mActivityStack.size()
					+ "个Activity");
		}
	}

	/**
	 * @return 当前堆栈中的Activity的数量
	 */
	public static int activityCount() {
		return mActivityStack.size();
	}

	/**
	 * 删除栈底Activity,只保留栈顶
	 */
	public static void removeFirstActivity() {

		while (mActivityStack.size() > 1) {
			mActivity = mActivityStack.elementAt(0);
			if (mActivity != null) {
				mActivity.finish();
				mActivityStack.removeElementAt(0);
			}
			System.out.println("目前" + mActivityStack.size() + "个Activity");
		}
	}

	/**
	 * 删除堆栈中最后一个压入的Activity
	 */
	public static void finishFinalOneActivity() {
		if (mActivityStack.size() > 0) {
			mActivity = mActivityStack.pop();
			if (mActivity != null) {
				mActivity.finish();
				mActivity = null;
				System.out.println("删除一个Activity，目前" + mActivityStack.size()
						+ "个Activity");
			}
		}
	}

	public static void finishFinalOneActivityNoAnim() {
		if (mActivityStack.size() > 0) {
			mActivity = mActivityStack.pop();
			if (mActivity != null) {
				mActivity.finish();
				mActivity = null;
				System.out.println("删除一个Activity，目前" + mActivityStack.size()
						+ "个Activity");
			}
		}
	}

	/**
	 * 删除堆栈中最后几个压入的Activity
	 */
	public static void finishFinalActivity(int mCount) {
		if (mCount > 0 && mActivityStack.size() > mCount) {
			while (mCount > 0) {
				finishFinalOneActivity();
				mCount--;
			}
			System.out.println("删除Activity，目前" + mActivityStack.size()
					+ "个Activity");
		}
	}

	/**
	 * 只保留几个Activity
	 * 
	 * @param mCount 保留的Activity的数量
	 * @param isAnim 是否带有转换动画
	 */
	public static void OnlyActivity(int mCount, boolean isAnim) {
		if (mCount > 0 && mActivityStack.size() > mCount) {
			mCount = mActivityStack.size() - mCount;
			if (isAnim) {
				while (mCount > 0) {
					finishFinalOneActivity();
					mCount--;
				}
			} else {
				while (mCount > 0) {
					finishFinalOneActivityNoAnim();
					mCount--;
				}
			}
			System.out.println("保留Activity，目前" + mActivityStack.size()
					+ "个Activity");
		}
	}

	/**
	 * 回到首页，保留一个Activity
	 */
	public static void OnlyOneActivity() {
		while (mActivityStack.size() > 1) {
			finishFinalOneActivity();
		}
		System.out.println("删除Activity，目前" + mActivityStack.size()
				+ "个Activity");
	}

	/**
	 * 回到首页，保留一个Activity
	 */
	public static void OnlyOneActivityNoAnim() {
		while (mActivityStack.size() > 1) {
			finishFinalOneActivityNoAnim();
		}
		System.out.println("删除Activity，目前" + mActivityStack.size()
				+ "个Activity");
	}

	public static void popToMain() {
		while (mActivityStack.size() > 2) {
			finishFinalOneActivityNoAnim();
		}
	}

	/**
	 * 取出当前Activity（堆栈中最后一个压入的）
	 */

	public static Activity currentActivity() {
		return mActivityStack.pop();
	}

	/**
	 * 结束指定的Activity
	 */

	public static void finishActivity(Activity mActivity) {
		if (mActivity != null) {
			mActivityStack.remove(mActivity);
			mActivity.finish();
			mActivity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public static void finishActivity(Class<?> mClass) {
		for (Activity mActivity : mActivityStack) {
			if (mActivity.getClass().equals(mClass)) {
				mActivityStack.remove(mActivity);
				finishActivity(mActivity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public static void finishAllActivity() {
		while (mActivityStack.size() > 0) {
			finishFinalOneActivity();
		}
		System.out.println("删除Activity，目前" + mActivityStack.size()
				+ "个Activity");
	}

	/**
	 * 完全退出应用程序
	 */
	public static void AppExit(Context context) {

		try {

			finishAllActivity();

			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);

		} catch (Exception e) {
		}

	}

}
