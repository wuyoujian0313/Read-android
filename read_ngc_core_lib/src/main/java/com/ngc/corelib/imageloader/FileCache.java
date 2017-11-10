package com.ngc.corelib.imageloader;

import java.io.File;

import android.content.Context;
import android.util.Log;

public class FileCache {

	private File cacheDir;

	public FileCache(Context context) {
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			Log.e("QCBao", "context==null = " + (context == null));
			if (context != null) {
				Log.e("QCBao", "context.getExternalCacheDir()==null = " + (context.getExternalCacheDir() == null));
			}
			cacheDir = new File(context.getExternalCacheDir().getPath() + File.separator + BaseConfigure.APP_PATH_IMAGE);
		} else
			cacheDir = new File(context.getCacheDir().getPath() + File.separator + BaseConfigure.APP_PATH_IMAGE);
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	public File getFile(String url) {
		// I identify images by hashcode. Not a perfect solution, good for the
		// demo.
		String filename = String.valueOf(url.hashCode());
		// Another possible solution (thanks to grantland)
		// String filename = URLEncoder.encode(url);
		File f = new File(cacheDir, filename);
		return f;

	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}

}