package com.ngc.corelib.env;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Resources.NotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.ngc.corelib.R;
import com.ngc.corelib.listener.NGCGestureListener;
import com.ngc.corelib.systembar.SystemBarTintManager;
import com.ngc.corelib.utils.ActivitysManager;
import com.ngc.corelib.views.NGCProgressDialog;
import com.ngc.corelib.zxing.activity.CaptureActivity;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener {

	// private NGCGestureListener gestureListener;
	// private GestureDetector detector;

	// private NGCProgressDialog dialog;

	protected ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		// gestureListener = new NGCGestureListener(this);
		// detector = new GestureDetector(this, gestureListener);

		ActivitysManager.addActivity(this);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(getLayoutResID());

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.ngc_color);

		if (this instanceof CaptureActivity) {
			initViwes();
		}

	}

	/**
	 * 设置是否可用手势
	 * 
	 * @param gestureDetectorEnable
	 */
	// protected void setGestureDetectorEnable(boolean gestureDetectorEnable) {
	// this.gestureDetectorEnable = gestureDetectorEnable;
	// }

	/**
	 * 获取布局文件id
	 * 
	 * @return
	 */
	protected abstract int getLayoutResID();

	/**
	 * 初始化View
	 */
	protected abstract void initViwes();

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// if (detector.onTouchEvent(event))
	// return true;
	// else
	// return false;
	// }

	/**
	 * 手势
	 */
	// public boolean dispatchTouchEvent(MotionEvent ev) {
	// if (gestureDetectorEnable)
	// detector.onTouchEvent(ev);
	// return super.dispatchTouchEvent(ev);
	// }

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	// protected void showProgressDialog(String message) {
	// dialog = new NGCProgressDialog();
	// dialog.setCancelable(false);
	// dialog.setMessage(message);
	// dialog.show(getSupportFragmentManager(), "dialog");
	// }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (mProgressDialog != null && !this.isFinishing()) {
			mProgressDialog.dismiss();
		}
		super.onSaveInstanceState(outState);
	}

	// protected void dismissProgressDialog() {
	// if (dialog != null && !this.isFinishing()) {
	// dialog.dismiss();
	// }
	// }
	public synchronized void showProgressDialog(String message) {
		try {
			if (mProgressDialog == null) {
				mProgressDialog = new ProgressDialog(this);
				mProgressDialog.setIndeterminate(true);
				mProgressDialog.setCanceledOnTouchOutside(false);
			}
			mProgressDialog.setMessage(message);
			mProgressDialog.show();
		} catch (NotFoundException e) {
		}
	}

	public synchronized void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
}
