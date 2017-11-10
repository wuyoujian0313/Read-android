package com.read.mobile.module.activity;

import android.content.Intent;
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
import com.read.mobile.beans.UserLoginRequest;
import com.read.mobile.beans.UserLoginResult;
import com.read.mobile.constants.Contacts;
import com.read.mobile.env.BaseActivity;
import com.read.mobile.utils.SaveUtils;

public class LoginActivity extends BaseActivity {

	private EditText mobileET;
	private EditText passET;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_login;
	}

	@Override
	protected void initViwes() {

		mobileET = (EditText) findViewById(R.id.login_mobile_et);
		passET = (EditText) findViewById(R.id.login_pass_et);

		findViewById(R.id.login_btn).setOnClickListener(this);
		findViewById(R.id.login_regist).setOnClickListener(this);
		findViewById(R.id.login_forgot).setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.login_btn:
			login();
			break;
		case R.id.login_forgot: {
			Intent intent = new Intent(this, ForgotActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.login_regist: {
			Intent intent = new Intent(this, RegistActivity.class);
			startActivity(intent);
		}
			break;
		}
	}

	private void login() {
		String mobileStr = mobileET.getText().toString();
		String passStr = passET.getText().toString();
		if (TextUtils.isEmpty(mobileStr)) {
			Tips.tipShort(this, "手机号不能为空");
			return;
		}
		if (!MatchUtils.isMobileRight(mobileStr)) {
			Tips.tipShort(this, "请输入正确的手机号");
			return;
		}
		if (TextUtils.isEmpty(passStr)) {
			Tips.tipShort(this, "密码不能为空");
			return;
		}
		if (!MatchUtils.isPwdRight(passStr)) {
			Tips.tipShort(this, "请输入6-20为数字或字母密码");
			return;
		}
		UserLoginRequest request = new UserLoginRequest();
		request.setPhone(mobileStr);
		request.setPasswd(MD5Util.getMD5String(passStr));

		NetAsyncTask asyncTask = new NetAsyncTask(UserLoginResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result.getHead().getCode().equals("20001")) {
					SaveUtils.login(((UserLoginResult) result).getData());
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				} else {
					Tips.tipLong(LoginActivity.this, result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				showProgressDialog("正在请求~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				Tips.tipLong(LoginActivity.this, e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_USER_LOGIN);
	}

}
