package com.read.mobile.module.activity;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ngc.corelib.encrypt.md5.MD5Util;
import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.NetAsyncTask;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.utils.MatchUtils;
import com.ngc.corelib.utils.Tips;
import com.read.mobile.R;
import com.read.mobile.beans.GetCaptchaRequest;
import com.read.mobile.beans.GetCaptchaResult;
import com.read.mobile.beans.UserRegisterRequest;
import com.read.mobile.beans.UserRegisterResult;
import com.read.mobile.constants.Contacts;
import com.read.mobile.env.BaseActivity;

public class RegistActivity extends BaseActivity {

	private EditText mobileET;
	private EditText passET;
	private EditText codeET;
	private Button getCodeBtn;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_forgot;
	}

	@Override
	protected void initViwes() {
		mobileET = (EditText) findViewById(R.id.forgot_mobile_et);
		passET = (EditText) findViewById(R.id.forgot_pass_et);
		codeET = (EditText) findViewById(R.id.forgot_code_et);
		getCodeBtn = (Button) findViewById(R.id.get_code_btn);
		getCodeBtn.setOnClickListener(this);
		findViewById(R.id.forgot_btn).setOnClickListener(this);
		((Button) findViewById(R.id.forgot_btn)).setText("注册");
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.get_code_btn:
			getCode();
			break;
		case R.id.forgot_btn:
			regist();
			break;
		}
	}

	/**
	 * 获取验证码
	 */
	private void getCode() {
		String mobileStr = mobileET.getText().toString();
		if (TextUtils.isEmpty(mobileStr)) {
			Tips.tipShort(this, "手机号不能为空");
			return;
		}
		if (!MatchUtils.isMobileRight(mobileStr)) {
			Tips.tipShort(this, "请输入正确的手机号");
			return;
		}
		// TODO
		GetCaptchaRequest request = new GetCaptchaRequest();
		request.setPhone(mobileStr);
		request.setType("1");

		NetAsyncTask asyncTask = new NetAsyncTask(GetCaptchaResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					Tips.tipLong(RegistActivity.this, "发送成功");
					new Thread(new SendCodeThread()).start();
				} else {
					Tips.tipLong(RegistActivity.this, result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				showProgressDialog("正在请求~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				Tips.tipLong(RegistActivity.this, e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_USER_GETCAPTCHA);

	}

	private int second = 60;

	/**
	 * 发送验证码的线程
	 * 
	 * @author Luoyunlong0328
	 * 
	 */
	private class SendCodeThread implements Runnable {
		@Override
		public void run() {
			while (second > 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				second--;
				handler.sendEmptyMessage(-1);
			}
		}
	}

	/**
	 * Handler
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case -1:
				getCodeBtn.setText(second + "秒");
				if (second == 0) {
					second = 60;
					getCodeBtn.setEnabled(true);
					getCodeBtn.setOnClickListener(RegistActivity.this);
					getCodeBtn.setText("获取验证码");
				}
				break;
			}
		};
	};

	private void regist() {
		String mobileStr = mobileET.getText().toString();
		String passStr = passET.getText().toString();
		String codeStr = codeET.getText().toString();
		if (TextUtils.isEmpty(mobileStr)) {
			Tips.tipShort(this, "手机号不能为空");
			return;
		}
		if (!MatchUtils.isMobileRight(mobileStr)) {
			Tips.tipShort(this, "请输入正确的手机号");
			return;
		}
		if (TextUtils.isEmpty(codeStr)) {
			Tips.tipLong(RegistActivity.this, "请输入验证码");
			return;
		}
		if (!MatchUtils.isVerifycationCodeRight(codeStr)) {
			Tips.tipLong(RegistActivity.this, "请输入4位数字验证码");
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
		UserRegisterRequest request = new UserRegisterRequest();
		request.setPhone(mobileStr);
		request.setPasswd(MD5Util.getMD5String(passStr));
		request.setCaptcha(codeStr);

		NetAsyncTask asyncTask = new NetAsyncTask(UserRegisterResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					// TODO 注册成功
					Tips.tipLong(RegistActivity.this, "恭喜您，注册成功！请登录~");
					finish();
				} else {
					Tips.tipLong(RegistActivity.this, result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				showProgressDialog("正在请求~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				Tips.tipLong(RegistActivity.this, e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_USER_REGISTER);

	}

}
