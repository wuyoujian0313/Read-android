package com.read.mobile.env;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.read.mobile.R;

public abstract class BaseFragment extends Fragment implements OnClickListener {

	private View view;
	private ImageView titleTv;
	private View leftView;
	private View right01View;
	private ImageView right01Iv;
	private View right02View;
	private ImageView right02Iv;
	private View rightTView;
	private TextView rightTV;
	// private NGCProgressDialog dialog;
	protected ProgressDialog mProgressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(getLayoutId(), null);
		titleTv = (ImageView) findViewById(R.id.top_title_tv);
		titleTv.setImageResource(getTitle(0));
		initViews();
		return view;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	protected View findViewById(int id) {
		return view.findViewById(id);
	}

	/**
	 * 布局ID
	 * 
	 * @return
	 */
	protected abstract int getLayoutId();

	/**
	 * 初始化View
	 * 
	 * @return
	 */
	protected abstract void initViews();

	/**
	 * 获取Title
	 * 
	 * @return
	 */
	/**
	 * 设置标题
	 */
	protected int getTitle(int resId) {
		if (resId != 0) {
			return resId;
		}
		return R.drawable.title_bar;
	};

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
	}

	protected synchronized void showProgressDialog(String message) {
		try {
			if (mProgressDialog == null) {
				mProgressDialog = new ProgressDialog(getActivity());
				mProgressDialog.setIndeterminate(true);
				mProgressDialog.setCanceledOnTouchOutside(false);
				mProgressDialog.setCancelable(false);
			}
			mProgressDialog.setMessage(message);
			mProgressDialog.show();
		} catch (Exception e) {
		}
	}

	protected synchronized void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

}
