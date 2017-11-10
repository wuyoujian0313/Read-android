package com.read.mobile.module.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ngc.corelib.encrypt.md5.MD5Util;
import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.NetAsyncTask;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.utils.MatchUtils;
import com.ngc.corelib.utils.Tips;
import com.read.mobile.R;
import com.read.mobile.beans.ChangePasswdRequest;
import com.read.mobile.beans.ChangePasswdResult;
import com.read.mobile.constants.Contacts;
import com.read.mobile.env.BaseActivity;

public class ChangePassActivity extends BaseActivity {

	private EditText oldPassET;
	private EditText newPassET;
	private EditText reNewPassET;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_change_pass;
	}

	@Override
	protected void initViwes() {

		showLeftBack(null);

		oldPassET = (EditText) findViewById(R.id.change_pass_old_et);
		newPassET = (EditText) findViewById(R.id.change_pass_new_et);
		reNewPassET = (EditText) findViewById(R.id.change_pass_re_et);

		findViewById(R.id.change_pass_btn).setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.change_pass_btn:
			changePasswd();
			break;
		}
	}

	private void changePasswd() {
		String oldPass = oldPassET.getText().toString();
		String newPass = newPassET.getText().toString();
		String rePass = reNewPassET.getText().toString();

		if (TextUtils.isEmpty(oldPass)) {
			Tips.tipShort(this, "请输入您的原密码");
			return;
		}
		if (!MatchUtils.isPwdRight(oldPass)) {
			Tips.tipShort(this, "请输入6-20为数字或字母原密码");
			return;
		}
		if (TextUtils.isEmpty(newPass)) {
			Tips.tipShort(this, "请输入您的新密码");
			return;
		}
		if (!MatchUtils.isPwdRight(newPass)) {
			Tips.tipShort(this, "请输入6-20为数字或字母新密码");
			return;
		}
		if (!newPass.equals(rePass)) {
			Tips.tipShort(this, "您两次输入的新密码不一致");
			return;
		}

		ChangePasswdRequest request = new ChangePasswdRequest();
		request.setNewPasswd(MD5Util.getMD5String(newPass));
		request.setOldPasswd(MD5Util.getMD5String(oldPass));

		NetAsyncTask asyncTask = new NetAsyncTask(ChangePasswdResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					Tips.tipLong(ChangePassActivity.this, "修改成功~");
					finish();
				} else {
					Tips.tipLong(ChangePassActivity.this, result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				showProgressDialog("正在请求~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				Tips.tipLong(ChangePassActivity.this, e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_USER_CHANGEPASSWD);
	}

}
