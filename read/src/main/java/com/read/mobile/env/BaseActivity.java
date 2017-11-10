package com.read.mobile.env;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.read.mobile.R;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends com.ngc.corelib.env.BaseActivity {

	private ImageView titleTv;
	private View leftView;
	private View right01View;
	private ImageView right01Iv;
	private View right02View;
	private ImageView right02Iv;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(getLayoutResID());
		initData();
		titleTv = (ImageView) findViewById(R.id.top_title_tv);
		titleTv.setImageResource(getTitle(0));

		initViwes();

	}

	/**
	 * 设置标题
	 */
	protected int getTitle(int resId) {
		if (resId != 0) {
			return resId;
		}
		return R.drawable.title_bar;
	};

	protected void initData() {

	}

	/**
	 * 显示左侧返回按钮
	 * 
	 * @param listener
	 */
	protected void showLeftBack(OnClickListener listener) {
		leftView = findViewById(R.id.top_back_view);
		leftView.setVisibility(View.VISIBLE);
		leftView.setOnClickListener((listener == null) ? this : listener);
	}

	/**
	 * 显示右侧第一个按钮
	 * 
	 * @param listener
	 */
	protected void showRight01Back(int drawableID, OnClickListener listener) {
		right01View = findViewById(R.id.top_right_view_01);
		right01Iv = (ImageView) findViewById(R.id.top_right_iv_01);
		right01Iv.setImageResource(drawableID);
		right01View.setVisibility(View.VISIBLE);
		right01View.setOnClickListener(listener);
	}

	/**
	 * 显示右侧第二个按钮
	 * 
	 * @param listener
	 */
	protected void showRight02Back(int drawableID, OnClickListener listener) {
		right02View = findViewById(R.id.top_right_view_02);
		right02Iv = (ImageView) findViewById(R.id.top_right_iv_02);
		right02Iv.setImageResource(drawableID);
		right02View.setVisibility(View.VISIBLE);
		right02View.setOnClickListener(listener);
	}

	protected View rightTView;
	protected TextView rightTV;

	/**
	 * 显示右侧第二个按钮
	 * 
	 * @param listener
	 */
	protected void showRightTV(String text, OnClickListener listener) {
		rightTView = findViewById(R.id.top_right_tv_view);
		rightTV = (TextView) findViewById(R.id.top_right_tv);
		rightTV.setText(text);
		rightTView.setVisibility(View.VISIBLE);
		rightTView.setOnClickListener(listener);
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.top_back_view) {
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onResume(this);
	}
}
