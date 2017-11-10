package com.ngc.corelib.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngc.corelib.R;

public class NGCProgressDialog extends DialogFragment {

	private View view;
	private ValueAnimator mProAnim;
	private String message;

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.dialog_progress, null);
		dialog.setContentView(view);
		TextView tv_msg = (TextView) view.findViewById(R.id.message);
		tv_msg.setText(message);

		final ImageView mLoadingImg = (ImageView) view.findViewById(R.id.img_loading);
		mProAnim = ObjectAnimator.ofInt(0, 360);
		mProAnim.setDuration(1000);
		mProAnim.setRepeatMode(ObjectAnimator.RESTART);
		mProAnim.setRepeatCount(-1);
		mProAnim.setInterpolator(new LinearInterpolator());
		mProAnim.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator anim) {
				mLoadingImg.setRotation(Float.valueOf(anim.getAnimatedValue().toString()));
			}
		});
		mProAnim.start();

		return dialog;
	}

	@Override
	public void dismiss() {
		if (null != getActivity() && !getActivity().isFinishing()) {
			super.dismissAllowingStateLoss();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

}
