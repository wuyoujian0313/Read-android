package com.read.mobile.views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ngc.corelib.utils.Tips;
import com.read.mobile.R;

public class ChangeNameDialog extends DialogFragment {
	private View view;
	private EditText nameET;
	private View close;
	private OnInputOkListener listener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.dialog_change_name, null);
		nameET = (EditText) view.findViewById(R.id.dialog_name_et);
		close = view.findViewById(R.id.close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				nameET.setText("");
				close.setVisibility(View.GONE);
			}
		});
		nameET.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if (arg0.length() > 0) {
					close.setVisibility(View.VISIBLE);
				} else {
					close.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
		view.findViewById(R.id.dialog_btn_ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(getText())) {
					Tips.tipLong(getActivity(), "请输入您的昵称~");
				} else {
					dismiss();
					if (listener != null) {
						listener.inputOk(getText());
					}
				}
			}
		});
		view.findViewById(R.id.dialog_btn_cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		dialog.setContentView(view);
		return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public String getText() {
		return nameET.getText().toString();
	}

	public void setOnInputOkListener(OnInputOkListener listener) {
		this.listener = listener;
	}

	@Override
	public void dismiss() {
		if (null != getActivity() && !getActivity().isFinishing()) {
			super.dismissAllowingStateLoss();
		}
	}

	public static interface OnInputOkListener {
		public void inputOk(String str);
	}
}
