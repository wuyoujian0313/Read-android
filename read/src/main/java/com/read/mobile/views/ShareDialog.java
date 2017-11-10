package com.read.mobile.views;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.read.mobile.R;
import com.tandong.bottomview.view.BottomView;

/**
 * Created by monch on 15/4/18.
 */
public class ShareDialog {

	// 弹出的view
	private BottomView bottomView;

	private Activity context;

	private ShareClickListener shareClickListener;

	public ShareDialog(Activity context, ShareClickListener shareClickListener) {
		this.context = context;
		this.shareClickListener = shareClickListener;
	}

	/**
	 * 弹出dialog
	 */
	public void shows() {
		if (bottomView != null) {
			bottomView.dismissBottomView();
			bottomView = null;
		}
		View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.view_share, null);
		TextView tvWeibo = (TextView) view.findViewById(R.id.tv_share_weibo);
		TextView tvWechat = (TextView) view.findViewById(R.id.tv_share_wechat);
		TextView tvMoment = (TextView) view.findViewById(R.id.tv_share_moment);
		TextView tvQQ = (TextView) view.findViewById(R.id.tv_share_qq);

		TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
		tvWeibo.setOnClickListener(new OnClickListener());
		tvWechat.setOnClickListener(new OnClickListener());
		tvMoment.setOnClickListener(new OnClickListener());
		tvCancel.setOnClickListener(new OnClickListener());
		tvQQ.setOnClickListener(new OnClickListener());

		bottomView = new BottomView(context, R.style.BottomViewTheme_Defalut, view);
		bottomView.setAnimation(R.style.BottomToTopAnim);
		if (context != null && !context.isFinishing()) {
			bottomView.showBottomView(true);
		}
	}

	/**
	 * destroy
	 */
	public void destroy() {
		if (bottomView != null) {
			bottomView.dismissBottomView();
			bottomView = null;
		}
	}

	/**
	 * Dialog的点击事件
	 */
	private class OnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_share_weibo:
				if (shareClickListener != null)
					shareClickListener.onSinaClick();
				break;
			case R.id.tv_share_wechat:
				if (shareClickListener != null)
					shareClickListener.onWechatClick();
				break;
			case R.id.tv_share_moment:
				if (shareClickListener != null)
					shareClickListener.onCiecleClick();
				break;
			case R.id.tv_share_qq:
				if (shareClickListener != null)
					shareClickListener.onQQClick();
				break;
			default:
				break;
			}
			if (bottomView != null) {
				bottomView.dismissBottomView();
			}
		}
	}

	public interface ShareClickListener {
		public void onWechatClick();

		public void onCiecleClick();

		public void onSinaClick();

		public void onQQClick();
	}

}
