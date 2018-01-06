package com.read.mobile.module.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngc.corelib.encrypt.SignUtil;
import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.NetAsyncTask01;
import com.ngc.corelib.http.bean.BaseFileRequest;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.http.fileupload.UploadFileAsyncTask;
import com.ngc.corelib.utils.Logger;
import com.ngc.corelib.utils.Options;
import com.ngc.corelib.utils.Tips;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.read.mobile.R;
import com.read.mobile.beans.BookItem;
import com.read.mobile.beans.ChildAddResult;
import com.read.mobile.beans.UserUploadImgResult;
import com.read.mobile.constants.Contacts;
import com.read.mobile.env.BaseActivity;
import com.read.mobile.observer.ReadAgent;
import com.read.mobile.utils.ImageDisplayer;
import com.read.mobile.utils.PermissionUitls;
import com.read.mobile.utils.SaveUtils;

public class NoteWriteActivity extends BaseActivity {

	private Button selectBtn;
	private ImageView imgIV;
	private TextView nameTV;
	private TextView authorTV;
	private TextView pressTV;
	private EditText noteET;
	private View bookView;

	private Button takePhotoBtn;
	private BookItem bookItem;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_note_write;
	}

	@Override
	protected void initViwes() {
		showLeftBack(null);
		showRightTV("完成", new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// writeNotes();
				if (bookItem.isAdd()) {
					upload();
				} else {
					writeNotes();
				}

			}
		});
		imageLoader = ImageLoader.getInstance();
		options = Options.getListOptions();
		selectBtn = (Button) findViewById(R.id.note_write_btn);
		selectBtn.setOnClickListener(this);
		takePhotoBtn = (Button) findViewById(R.id.note_takephoto_btn);
		takePhotoBtn.setOnClickListener(this);
		imgIV = (ImageView) findViewById(R.id.note_write_img);
		nameTV = (TextView) findViewById(R.id.note_write_name);
		authorTV = (TextView) findViewById(R.id.note_write_author);
		pressTV = (TextView) findViewById(R.id.note_write_press);
		noteET = (EditText) findViewById(R.id.note_write_et);
		bookView = findViewById(R.id.write_book_view);
		bookView.setOnClickListener(this);
		bookView.setVisibility(View.GONE);
		selectBtn.setVisibility(View.VISIBLE);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("filePath", path);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (TextUtils.isEmpty(path)) {
			path = savedInstanceState.getString("filePath");
		}
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
			case R.id.note_write_btn:
			case R.id.write_book_view:
				// TODO
				Intent intent = new Intent(this, BookSearchActivity.class);
				startActivityForResult(intent, 0xFF);
				break;
			case R.id.note_takephoto_btn: {
				final String checkPermissinos[] = {Manifest.permission.CAMERA,
						Manifest.permission.WRITE_EXTERNAL_STORAGE};
				PermissionUitls.mContext = this;
				if(!PermissionUitls.isGetAllPermissionsByList(checkPermissinos) ) {
					new AlertDialog
							.Builder(this)
							.setTitle("提示信息")
							.setMessage("该功能需要您接受应用对一些关键权限（拍照）的申请，如之前拒绝过，可到手机系统的应用管理授权设置界面再次设置。")
							.setPositiveButton("确认", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									checkPermission("takephoto",PermissionUitls.PERMISSION_CAMERA_CODE,checkPermissinos);
								}
							}).show();
				} else {
					takePhoto();
				}
			}
		}

	}

	//请求相机权限
	private void checkPermission(final String functionName, int permissionCode, String[] permissions) {
		PermissionUitls.PermissionListener permissionListener = new PermissionUitls.PermissionListener() {
			@Override
			public void permissionAgree() {
				takePhoto();
			}

			@Override
			public void permissionReject() {

			}
		};
		PermissionUitls permissionUitls = PermissionUitls.getInstance(null, permissionListener);
		permissionUitls.permssionCheck(permissionCode,permissions);
	}

	private void setInfo(BookItem bookItem) {
		selectBtn.setVisibility(View.GONE);
		bookView.setVisibility(View.VISIBLE);
		if (bookItem != null) {
			if (bookItem.isAdd()) {
				takePhotoBtn.setVisibility(View.VISIBLE);
			} else {
				takePhotoBtn.setVisibility(View.GONE);
			}
			if (!TextUtils.isEmpty(bookItem.getPic_big())) {
				imageLoader.displayImage(bookItem.getPic_big(), imgIV, options);
			}
			if (!TextUtils.isEmpty(bookItem.getName())) {
				nameTV.setText(bookItem.getName());
			} else {
				nameTV.setText("");
			}
			if (!TextUtils.isEmpty(bookItem.getAuthor())) {
				authorTV.setText(bookItem.getAuthor());
			} else {
				authorTV.setText("");
			}
			if (!TextUtils.isEmpty(bookItem.getPress())) {
				pressTV.setText(bookItem.getPress());
			} else {
				pressTV.setText("");
			}
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {

		Logger.e("arg0---" + arg0);
		Logger.e("arg1---" + arg1);

		if (arg0 == 0xFF && arg1 == RESULT_OK) {
			bookItem = (BookItem) arg2.getSerializableExtra("BookItem");
			setInfo(bookItem);
		} else if (arg0 == TAKE_PICTURE) {
			// TODO
			takePhotoBtn.setVisibility(View.GONE);
			ImageDisplayer.getInstance(this).displayBmp(imgIV, null, path);
		}
	}

	/**
	 * 添加小孩信息
	 */
	private void writeNotes() {
		String noteStr = noteET.getText().toString();
		if (TextUtils.isEmpty(noteStr)) {
			Tips.tipLong(this, "请输入笔记内容");
			return;
		}
		// WriteNotesRequest request = new WriteNotesRequest();
		// request.setAuthor(bookItem.getAuthor());
		// request.setBookName(bookItem.getName());
		// request.setContent(noteStr);
		// request.setIsbn(bookItem.getIsbn());
		// request.setPic(bookItem.getPic());
		// request.setPress(bookItem.getPress());
		// request.setSource(bookItem.isAdd() ? "2" : "1");
		// request.setType("1");
		// request.setU(SaveUtils.getUserId());

		if (bookItem.isAdd() && TextUtils.isEmpty(path)) {
			Tips.tipLong(this, "请拍照书的封面");
			return;
		}

		List<BasicNameValuePair> request = new ArrayList<>();
		BasicNameValuePair pair01 = new BasicNameValuePair("u", SaveUtils.getUserId());
		BasicNameValuePair pair02 = new BasicNameValuePair("source", bookItem.isAdd() ? "2" : "1");
		BasicNameValuePair pair03 = new BasicNameValuePair("type", "1");
		BasicNameValuePair pair04 = new BasicNameValuePair("bookName", bookItem.getName());
		BasicNameValuePair pair05 = new BasicNameValuePair("author", bookItem.getAuthor());
		BasicNameValuePair pair06 = new BasicNameValuePair("press", bookItem.getPress());
		BasicNameValuePair pair07 = new BasicNameValuePair("pic", bookItem.getPic());
		BasicNameValuePair pair08 = new BasicNameValuePair("isbn", bookItem.getIsbn());
		request.add(pair01);
		request.add(pair02);
		request.add(pair03);
		request.add(pair04);
		request.add(pair05);
		request.add(pair06);
		request.add(pair07);
		request.add(pair08);
		BasicNameValuePair pair09 = new BasicNameValuePair("content", noteStr);
		request.add(pair09);
		String sign = SignUtil.signNameValue(request, "fdf4da319ea90b3cdb861887c77a75ec");
		BasicNameValuePair signPair = new BasicNameValuePair("sign", sign);
		request.add(signPair);

		NetAsyncTask01 asyncTask = new NetAsyncTask01(ChildAddResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					Tips.tipLong(NoteWriteActivity.this, "添加成功");
					ReadAgent.geUshareAgent().notifyUpdateNoteObserver(false);
				} else {
					Tips.tipLong(NoteWriteActivity.this, result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				showProgressDialog("正在请求~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				Tips.tipLong(NoteWriteActivity.this, e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_BOOK_WRITE_NOTES);
	}

	private void upload() {
		String noteStr = noteET.getText().toString();
		if (TextUtils.isEmpty(noteStr)) {
			Tips.tipLong(this, "请输入笔记内容");
			return;
		}
		BaseFileRequest request = new BaseFileRequest();
		List<BasicNameValuePair> paramsList = new ArrayList<>();
		BasicNameValuePair pair01 = new BasicNameValuePair("u", SaveUtils.getUserId());
		BasicNameValuePair pair02 = new BasicNameValuePair("source", bookItem.isAdd() ? "2" : "1");
		BasicNameValuePair pair03 = new BasicNameValuePair("type", "1");
		BasicNameValuePair pair04 = new BasicNameValuePair("bookName", bookItem.getName());
		BasicNameValuePair pair05 = new BasicNameValuePair("author", bookItem.getAuthor());
		BasicNameValuePair pair06 = new BasicNameValuePair("press", bookItem.getPress());
		// BasicNameValuePair pair07 = new BasicNameValuePair("pic",
		// bookItem.getPic());
		BasicNameValuePair pair08 = new BasicNameValuePair("isbn", bookItem.getIsbn());
		paramsList.add(pair01);
		paramsList.add(pair02);
		paramsList.add(pair03);
		paramsList.add(pair04);
		paramsList.add(pair05);
		paramsList.add(pair06);
		// paramsList.add(pair07);
		paramsList.add(pair08);
		String sign = SignUtil.signNameValue(paramsList, "fdf4da319ea90b3cdb861887c77a75ec");
		BasicNameValuePair signPair = new BasicNameValuePair("sign", sign);
		paramsList.add(signPair);
		BasicNameValuePair pair09 = new BasicNameValuePair("content", noteStr);
		paramsList.add(pair09);
		request.setParamsList(paramsList);
		request.setFile(new File(path));
		UploadFileAsyncTask asyncTask = new UploadFileAsyncTask(UserUploadImgResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result != null) {
					dismissProgressDialog();
					if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
						Tips.tipLong(NoteWriteActivity.this, "添加成功");
						ReadAgent.geUshareAgent().notifyUpdateNoteObserver(false);
					} else {
						Tips.tipLong(NoteWriteActivity.this, result.getHead().getMessage());
					}
				} else {
					Tips.tipShort(NoteWriteActivity.this, "网络请求失败");
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				showProgressDialog("正在上传~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				Tips.tipShort(NoteWriteActivity.this, e.getMessage());
			}
		}, request, "pic");
		asyncTask.execute(Contacts.URL_BOOK_WRITE_NOTES);
	}

	private String path = "";
	private static final int TAKE_PICTURE = 0x000001;
	private Uri cameraUri;

	public void takePhoto() {

		String filePath = Environment.getExternalStorageDirectory() +
				File.separator + Environment.DIRECTORY_DCIM + File.separator;
		File vFile = new File(filePath + "readImage" + File.separator , String.valueOf(System
				.currentTimeMillis()) + ".jpg");

		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			if (!vFile.exists()) {
				File vDirPath = vFile.getParentFile();
				vDirPath.mkdirs();
			} else {
				if (vFile.exists()) {
					vFile.delete();
				}
			}
			path = vFile.getAbsolutePath();

			Intent openCameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			if (Build.VERSION.SDK_INT<24){
				Uri imageUri = Uri.fromFile(vFile);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			}else{
				//兼容android7.0 使用共享文件的形式
				ContentValues contentValues = new ContentValues(1);
				contentValues.put(MediaStore.Images.Media.DATA, path);
				Uri uri = getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			}
			startActivityForResult(openCameraIntent, TAKE_PICTURE);
		}
	}
}
