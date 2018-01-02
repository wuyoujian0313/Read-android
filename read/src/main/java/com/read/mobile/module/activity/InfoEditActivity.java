package com.read.mobile.module.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.ngc.corelib.encrypt.SignUtil;
import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.NetAsyncTask;
import com.ngc.corelib.http.bean.BaseFileRequest;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.http.fileupload.UploadFileAsyncTask;
import com.ngc.corelib.utils.BitmapUtils;
import com.ngc.corelib.utils.Options;
import com.ngc.corelib.utils.PicDialogUtils;
import com.ngc.corelib.utils.Tips;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.read.mobile.R;
import com.read.mobile.beans.UpdateUserInfoRequest;
import com.read.mobile.beans.UpdateUserInfoResult;
import com.read.mobile.beans.UserUploadImgResult;
import com.read.mobile.constants.Contacts;
import com.read.mobile.env.BaseActivity;
import com.read.mobile.observer.ReadAgent;
import com.read.mobile.utils.SaveUtils;

public class InfoEditActivity extends BaseActivity {

	private ImageView photoIV;
	private EditText nameET;
	private EditText signET;

	private ImageLoader loader;
	private DisplayImageOptions options;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_edit_info;
	}

	@Override
	protected void initViwes() {
		showLeftBack(null);
		showRightTV("完成", new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				updateUserInfo();
			}
		});

		loader = ImageLoader.getInstance();
		options = Options.getIconOptions();

		photoIV = (ImageView) findViewById(R.id.edit_info_photo_iv);
		if (!TextUtils.isEmpty(SaveUtils.getUserAvatar())) {
			loader.displayImage(SaveUtils.getUserAvatar(), photoIV, options);
		}
		nameET = (EditText) findViewById(R.id.edit_info_name_et);
		nameET.setText(SaveUtils.getUserNick());
		signET = (EditText) findViewById(R.id.edit_info_sign_et);
		signET.setText(SaveUtils.getUserSign());

		findViewById(R.id.edit_info_photo_ll).setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.edit_info_photo_ll:
			PicDialogUtils.showOpenPhotoDialog(this);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 头像或背景选择返回
		case PicDialogUtils.PHOTO_PICKED_WITH_DATA:
			if (data != null) {
				startPhotoZoom(data.getData(), 150);
			}
			break;
		case PicDialogUtils.PHOTO_PICKED_CAMARE_DATA:
			startPhotoZoom(Uri.fromFile(PicDialogUtils.getFile()), 150);
			break;
		case PHOTO_REQUEST_CUT:
			if (data != null) {
				setPicToView(data);
			}
			break;
		}
	}

	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	private void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	// 将进行剪裁后的图片显示到UI界面上
	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			final Bitmap photo = bundle.getParcelable("data");
			photoIV.setImageBitmap(BitmapUtils.getRoundedCornerBitmap(photo, 180));
			if (PicDialogUtils.getFile().exists()) {
				PicDialogUtils.getFile().delete();
			}
			save(photo, new File(PicDialogUtils.getFilePath()));
		}
	}

	/**
	 * 图片上传
	 * 
	 * @param bitmap
	 * @param file
	 */
	public void save(final Bitmap bitmap, File file) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
		} catch (Exception e) {
		}
		BaseFileRequest request = new BaseFileRequest();

		List<BasicNameValuePair> paramsList = new ArrayList<>();
		BasicNameValuePair pair = new BasicNameValuePair("u", SaveUtils.getUserId());
		paramsList.add(pair);
		String sign = SignUtil.signNameValue(paramsList, "fdf4da319ea90b3cdb861887c77a75ec");
		BasicNameValuePair signPair = new BasicNameValuePair("sign", sign);
		paramsList.add(signPair);
		request.setFile(file);
		request.setParamsList(paramsList);
		UploadFileAsyncTask asyncTask = new UploadFileAsyncTask(UserUploadImgResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result != null) {
					dismissProgressDialog();
					if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
						Tips.tipLong(InfoEditActivity.this, "上传成功");
						SaveUtils.setUserAvatar(((UserUploadImgResult) result).getData().getAvatar());
						ReadAgent.geUshareAgent().notifyUploadImgSeccessObserverObserver(
								((UserUploadImgResult) result).getData().getAvatar());
					} else {
						Tips.tipLong(InfoEditActivity.this, result.getHead().getMessage());
					}
				} else {
					Tips.tipShort(InfoEditActivity.this, "网络请求失败");
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				showProgressDialog("正在上传~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				Tips.tipShort(InfoEditActivity.this, e.getMessage());
			}
		}, request, "");
		asyncTask.execute(Contacts.URL_USER_UPLOADIMG);
	}

	private void updateUserInfo() {
		final String nick = nameET.getText().toString();
		final String mood = signET.getText().toString();
		if (TextUtils.isEmpty(nick)) {
			Tips.tipShort(this, "请输入您的昵称");
			return;
		}
		if (TextUtils.isEmpty(mood)) {
			Tips.tipShort(this, "请输入您的心情签名");
			return;
		}
		UpdateUserInfoRequest request = new UpdateUserInfoRequest();
		request.setNick(nick);
		request.setMood("啊啊啊啊");

		NetAsyncTask asyncTask = new NetAsyncTask(UpdateUserInfoResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					SaveUtils.setUserName(nick);
					SaveUtils.setUserSign(mood);
					Tips.tipLong(InfoEditActivity.this, "修改成功");
					ReadAgent.geUshareAgent().notifyMondifyNameSeccessObservers(nick, mood);
					finish();
				} else {
					Tips.tipLong(InfoEditActivity.this, result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				showProgressDialog("正在请求~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				Tips.tipLong(InfoEditActivity.this, e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_USER_UPDATEUSERINFO);
	}
}
