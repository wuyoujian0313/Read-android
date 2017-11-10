package com.ngc.corelib.views;

import com.ngc.corelib.R;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class NGCCommonDialog extends DialogFragment implements OnClickListener {

	private View view;
	private TextView Msg;
	private Button ok;
	private Button center;
	private OnClickListener listener;
	private String positiveName;
	private String message;
	private boolean isSingle = false;
	private Button cancel;
	private OnClickListener singleListener;
	private OnClickListener negativeCallback;
	private String negativeName;

	public NGCCommonDialog() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.dialog_common, null);
		dialog.setContentView(view);
		Msg = (TextView) view.findViewById(R.id.alert);

		Msg.setText(message);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.cancel);
		center = (Button) view.findViewById(R.id.center);
		center.setOnClickListener(this);
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);

		if (!TextUtils.isEmpty(positiveName)) {
			ok.setText(positiveName);
		}

		if (!TextUtils.isEmpty(negativeName)) {
			cancel.setText(negativeName);
		}

		if (isSingle) {
			center.setVisibility(View.VISIBLE);
			ok.setVisibility(View.GONE);
			view.findViewById(R.id.diver).setVisibility(View.GONE);
			cancel.setVisibility(View.GONE);
		}

		return dialog;
	}

	@Override
	public void dismiss() {
		if (null != getActivity() && !getActivity().isFinishing()) {
			super.dismissAllowingStateLoss();
		}
	}

	public void setMessage(String msg) {
		this.message = msg;
	}

	public void setPositiveBtn(String name, View.OnClickListener listener) {
		this.listener = listener;
		this.positiveName = name;
	}

	public void setSingleButton(boolean isSingle) {
		this.isSingle = isSingle;
	}

	public void setSingleCallback(View.OnClickListener singleListener) {
		this.singleListener = singleListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ok) {
			dismiss();
			listener.onClick(v);
		} else if (v.getId() == R.id.center) {
			dismiss();
			singleListener.onClick(v);
		} else if (v.getId() == R.id.cancel) {
			dismiss();
			negativeCallback.onClick(v);
		}
	}

	public void setNegativeBtn(String name, View.OnClickListener rightCallback) {
		this.negativeName = name;
		this.negativeCallback = rightCallback;
	}

}
