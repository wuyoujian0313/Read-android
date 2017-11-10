package com.ngc.corelib.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.ngc.corelib.R;

public class PicDialogUtils {

	private static Dialog share_dialog;
	private static Activity activity;

	// *******************************************请求***********************************************

	private static final String filePath = "/storage/sdcard0/need/img";
	private static String fileName;

	/**
	 * 显示dialog
	 */
	public static void showOpenPhotoDialog(Activity activity) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		fileName = getPhotoFileName();
		tempFile = new File(file, fileName);
		PicDialogUtils.activity = activity;
		if (!activity.isFinishing()) {
			if (share_dialog == null) {
				share_dialog = new Dialog(activity, R.style.share_dialog);
				View content_view = LayoutInflater.from(activity).inflate(R.layout.dialog_photo_item_click, null);
				LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				share_dialog.addContentView(content_view, params);
				// 空白
				View blank_view = content_view.findViewById(R.id.photo_other_view);
				blank_view.setOnClickListener(listener);
				// 打开相机
				View moments_view = content_view.findViewById(R.id.photo_open_camare);
				moments_view.setOnClickListener(listener);
				// 打开图库
				View cancel_delete = content_view.findViewById(R.id.photo_open_galerry);
				cancel_delete.setOnClickListener(listener);
				// 取消
				View cancel_view = content_view.findViewById(R.id.photo_share_cancel);
				cancel_view.setOnClickListener(listener);

			}
			if (!activity.isFinishing() && share_dialog != null && !share_dialog.isShowing()) {
				share_dialog.show();
			}

		}
	}

	public static void dismissDialog() {
		if (share_dialog != null) {
			share_dialog.dismiss();
			share_dialog = null;
		}
	}

	/* 用来标识请求gallery的activity */
	public static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 用来表示照相机请求 的activity */
	public static final int PHOTO_PICKED_CAMARE_DATA = 3022;

	private static OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (view.getId() == R.id.photo_other_view || view.getId() == R.id.photo_share_cancel) {
			} else if (view.getId() == R.id.photo_open_camare) {
				startCamearPicCut(activity);
			} else if (view.getId() == R.id.photo_open_galerry) {
				startImageCaptrue(activity);
			}
			dismissDialog();
		}
	};
	// 创建一个以当前时间为名称的文件
	static File tempFile;

	// 使用系统当前日期加以调整作为照片的名称
	private static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	public static File getFile() {
		return tempFile;
	}

	public static String getFilePath() {
		return filePath + "/" + fileName;
	}

	private static void startCamearPicCut(Activity activity) {
		// TODO Auto-generated method stub
		// 调用系统的拍照功能
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		intent.putExtra("camerasensortype", 2);// 调用前置摄像头
		intent.putExtra("autofocus", true);// 自动对焦
		intent.putExtra("fullScreen", false);// 全屏
		intent.putExtra("showActionIcons", false);
		// 指定调用相机拍照后照片的储存路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
		activity.startActivityForResult(intent, PHOTO_PICKED_CAMARE_DATA);
	}

	private static void startImageCaptrue(Activity activity) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		activity.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
	}
}
