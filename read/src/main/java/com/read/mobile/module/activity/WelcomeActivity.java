package com.read.mobile.module.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.read.mobile.R;
import com.read.mobile.utils.SaveUtils;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_welcome);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = null;
				if (TextUtils.isEmpty(SaveUtils.getUserId())) {
					intent = new Intent(WelcomeActivity.this, LoginActivity.class);
				} else {
					intent = new Intent(WelcomeActivity.this, MainActivity.class);
				}
				startActivity(intent);
				finish();
			}
		}, 3000);
	}

	@Override
	public void onBackPressed() {
	}

}
