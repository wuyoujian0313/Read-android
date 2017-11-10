package com.read.mobile.module.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ngc.corelib.utils.ActivitysManager;
import com.ngc.corelib.utils.SystemInfo;
import com.ngc.corelib.views.NGCCommonDialog;
import com.read.mobile.R;
import com.read.mobile.env.BaseActivity;
import com.read.mobile.utils.SaveUtils;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class SettingActivity extends BaseActivity {

	private TextView versionTV;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_setting;
	}

	@Override
	protected void initViwes() {

		showLeftBack(null);

		versionTV = (TextView) findViewById(R.id.setting_version_tv);
		versionTV.setText(SystemInfo.getVersionName(this));

		findViewById(R.id.setting_chang_pass_ll).setOnClickListener(this);
		findViewById(R.id.setting_callback_ll).setOnClickListener(this);
		findViewById(R.id.setting_version_ll).setOnClickListener(this);
		findViewById(R.id.setting_logout_btn).setOnClickListener(this);

		initUmeng();
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.setting_chang_pass_ll:
			Intent intent = new Intent(this, ChangePassActivity.class);
			startActivity(intent);
			break;
		case R.id.setting_callback_ll:
			FeedbackAgent agent = new FeedbackAgent(this);
			agent.startFeedbackActivity();
			agent.sync();
			break;
		case R.id.setting_version_ll:
			UmengUpdateAgent.update(this);
			break;
		case R.id.setting_logout_btn: {
			final NGCCommonDialog dialog1 = new NGCCommonDialog();
			dialog1.setMessage("您确定要退出当前账户？");
			dialog1.setPositiveBtn("确定", new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog1.dismiss();
					Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
					SaveUtils.saveBookList("");
					SaveUtils.logout();
					startActivity(intent);
					ActivitysManager.finishAllActivity();
				}
			});
			dialog1.setNegativeBtn("取消", new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog1.dismiss();
				}
			});
			dialog1.show(getSupportFragmentManager(), "EXIT");
		}
			break;
		}
	}

	// **********************************************Umeng*************************************************88
	private void initUmeng() {
		// 如果想程序启动时自动检查是否需要更新， 把下面两行代码加在Activity 的onCreate()函数里。
		UmengUpdateAgent.setUpdateOnlyWifi(true); // 目前我们默认在Wi-Fi接入情况下才进行自动提醒。如需要在其他网络环境下进行更新自动提醒，则请添加该行代码
		UmengUpdateAgent.setUpdateAutoPopup(true);
		UmengUpdateAgent.setUpdateListener(updateListener);
		UmengUpdateAgent.setDownloadListener(new UmengDownloadListener() {

			@Override
			public void OnDownloadStart() {
				Toast.makeText(SettingActivity.this, "download start", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void OnDownloadUpdate(int progress) {
				Toast.makeText(SettingActivity.this, "download progress : " + progress + "%", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void OnDownloadEnd(int result, String file) {
				Toast.makeText(SettingActivity.this, "download file path : " + file, Toast.LENGTH_SHORT).show();
			}
		});
		UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
			@Override
			public void onClick(int status) {
				switch (status) {
				case UpdateStatus.Update:
					Toast.makeText(SettingActivity.this, "User chooses update.", Toast.LENGTH_SHORT).show();
					break;
				case UpdateStatus.Ignore:
					Toast.makeText(SettingActivity.this, "User chooses ignore.", Toast.LENGTH_SHORT).show();
					break;
				case UpdateStatus.NotNow:
					Toast.makeText(SettingActivity.this, "User chooses cancel.", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
		// UmengUpdateAgent.forceUpdate(Setting.this);
	}

	UmengUpdateListener updateListener = new UmengUpdateListener() {
		@Override
		public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
			switch (updateStatus) {
			case 0: // has update
				Log.i("--->", "callback result");
				UmengUpdateAgent.showUpdateDialog(SettingActivity.this, updateInfo);
				break;
			case 1: // has no update
				Toast.makeText(SettingActivity.this, "当前为最新版本", Toast.LENGTH_SHORT).show();
				break;
			case 2: // none wifi
				Toast.makeText(SettingActivity.this, "有最新版本，请连接WiFi更新", Toast.LENGTH_SHORT).show();
				break;
			case 3: // time out
				Toast.makeText(SettingActivity.this, "更新超时", Toast.LENGTH_SHORT).show();
				break;
			case 4: // is updating
				/*
				 * Toast.makeText(mContext, "正在下载更新...", Toast.LENGTH_SHORT)
				 * .show();
				 */
				break;
			}
		}
	};
}
