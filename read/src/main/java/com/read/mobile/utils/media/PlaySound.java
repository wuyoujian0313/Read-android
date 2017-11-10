package com.read.mobile.utils.media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ProgressBar;

import com.ngc.corelib.utils.Logger;
import com.ngc.corelib.utils.Utils;
import com.read.mobile.env.BaseApp;
import com.read.mobile.utils.media.RecorderUtils.PlayCompleteI;

public class PlaySound {

	public static void playSound(final String url, String path, final PlayCompleteI completeI, final ProgressBar bar) {

		RecorderUtils.stopPlay();

		if (TextUtils.isEmpty(path) && !TextUtils.isEmpty(url)) {
			path = BaseApp.getSharedPreferences().getString(url, "");
		}

		final String tempPath = path;

		// 路径存在且文件存在
		if (!TextUtils.isEmpty(path)) {
			Logger.e("!TextUtils.isEmpty(path)=" + !TextUtils.isEmpty(path));
			// TODO 动画播放
			new Thread(new Runnable() {
				@Override
				public void run() {
					Logger.e("Runnable=");
					RecorderUtils.playRecord(tempPath, completeI, bar);
				}
			}).start();
		} else if (!TextUtils.isEmpty(url)) {
			final String file_path = getAmrPath("file_" + System.currentTimeMillis());
			Logger.e("!TextUtils.isEmpty(url)=" + !TextUtils.isEmpty(url));
			Logger.e("file_path=" + file_path);
			new AsyncTask<Object, Void, Void>() {
				@Override
				protected Void doInBackground(Object... arg0) {
					// from web
					try {
						File file = new File(file_path);
						URL imageUrl = new URL(url);
						HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
						conn.setConnectTimeout(30000);
						conn.setReadTimeout(30000);
						conn.setInstanceFollowRedirects(true);
						InputStream is = conn.getInputStream();
						OutputStream os = new FileOutputStream(file);
						CopyStream(is, os);
						os.close();
						conn.disconnect();
						/**
						 * PS保存
						 */
						// FriendChatTable.updateSoundData(database,
						// entity.getMessage().getMessageBody()
						// .getSoundUrl(), file_path);
						BaseApp.getSharedPreferences().edit().putString(url, file_path).commit();
						if (!TextUtils.isEmpty(file_path)) {
							RecorderUtils.playRecord(file_path, completeI, bar);
						}

					} catch (Throwable ex) {
						ex.printStackTrace();
					}
					return null;
				}

				protected void onPostExecute(Void result) {
				};
			}.execute();
		}

	}

	// 获取文件手机路径
	private static String getAmrPath(String path) {
		File dir = new File(Environment.getExternalStorageDirectory(), "my");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(Environment.getExternalStorageDirectory(), "my/" + path + ".amr");
		return file.getAbsolutePath();
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
}
