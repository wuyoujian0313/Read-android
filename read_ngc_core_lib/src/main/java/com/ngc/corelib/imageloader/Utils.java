package com.ngc.corelib.imageloader;

import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

public class Utils {
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

	/**
	 * 将图片变成圆角矩形的形状 pixels为90时 是圆形
	 * 
	 * @param bitmap
	 *            图片
	 * @param pixels
	 *            角度
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
	
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap getCircleBitmap(Bitmap bitmap, int width, int height) {
		Bitmap croppedBitmap = scaleCenterCrop(bitmap, width, height);
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();

		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);

		int radius = 0;
		if (width > height) {
			radius = height / 2;
		} else {
			radius = width / 2;
		}
		canvas.drawCircle(width / 2, height / 2, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(croppedBitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
		int sourceWidth = source.getWidth();
		int sourceHeight = source.getHeight();

		float xScale = (float) newWidth / sourceWidth;
		float yScale = (float) newHeight / sourceHeight;
		float scale = Math.max(xScale, yScale);

		float scaledWidth = scale * sourceWidth;
		float scaledHeight = scale * sourceHeight;

		float left = (newWidth - scaledWidth) / 2;
		float top = (newHeight - scaledHeight) / 2;

		RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

		Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
		Canvas canvas = new Canvas(dest);
		canvas.drawBitmap(source, null, targetRect, null);

		return dest;
	}
        
	/**
	 * 将图片切出一个正方形
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap cutBmp(Bitmap bmp) {
		Bitmap result;
		int w = bmp.getWidth();// 输入长方形宽
		int h = bmp.getHeight();// 输入长方形高
		
		int nw;// 输出正方形宽
		if (w > h) {
			// 宽大于高
			nw = h;
			result = Bitmap.createBitmap(bmp, (w - nw) / 2, 0, nw, nw);
		} else if (w < h) {
			// 高大于宽
			nw = w;
			result = Bitmap.createBitmap(bmp, 0, (h - nw) / 2, nw, nw);
		} else {
			return bmp;
		}
		return result;
	}
}