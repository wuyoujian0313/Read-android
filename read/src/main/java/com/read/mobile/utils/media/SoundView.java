package com.read.mobile.utils.media;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.read.mobile.R;

public class SoundView extends View {

	AudioRecordMedia mediaRecorder;
	Paint mPaint;
	Bitmap talk0 = null;
	Bitmap talk1 = null;
	Bitmap talk2 = null;
	Bitmap talk3 = null;
	Bitmap talk4 = null;
	Bitmap talk5 = null;
	Bitmap talk6 = null;

	public boolean playStatus = false;

	private int index = 0;

	int delayMilliseconds = 50;

	private float x;
	private float y;

	public int screenWidth;

	public SoundView(Context context, AttributeSet att) {
		super(context, att);
		x = 0;
		y = 0;
		init(context);
	}

	private void init(Context context) {
		try {
			Resources resources = getResources();
			talk0 = BitmapFactory.decodeResource(resources, R.drawable.talk1);
			talk1 = BitmapFactory.decodeResource(resources, R.drawable.talk2);
			talk2 = BitmapFactory.decodeResource(resources, R.drawable.talk3);
			talk3 = BitmapFactory.decodeResource(resources, R.drawable.talk4);
			talk4 = BitmapFactory.decodeResource(resources, R.drawable.talk5);
			talk5 = BitmapFactory.decodeResource(resources, R.drawable.talk6);
		} catch (Exception ex) {
		}

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.WHITE);
		mediaRecorder = null;
	}

	public void setRecorder(AudioRecordMedia recorder) {
		mediaRecorder = recorder;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// canvas.drawBitmap(talk3, 15, 20, mPaint);//TEST
		if (mediaRecorder != null && playStatus) {

			int maxAmplitude = 0;
			try {
				maxAmplitude = mediaRecorder.getMaxAmplitude();
			} catch (Exception ex) {
				maxAmplitude = 0;
			}

			int num = 0;
			// if (maxAmplitude > 0 && maxAmplitude < 32767) {
			// if (maxAmplitude < 7000) {
			// num = maxAmplitude / 1000;
			// } else {
			// num = maxAmplitude / 4681;
			// }
			if (maxAmplitude > 0 && maxAmplitude < 32767) {
				if (maxAmplitude < 2000) {
					num = maxAmplitude / 286;
				} else if (maxAmplitude >= 2000 && maxAmplitude < 7000) {
					num = maxAmplitude / 1000;
				} else {
					num = maxAmplitude / 4681;
				}
			} else if (maxAmplitude >= 32767) {
				num = 6;
			} else {
				num = 0;
			}
			if (num > 6) {
				num = 6;
			}
			if (num >= index) {
				index = num;
			}

			if (index == 0) {
				drawtalk(index, canvas);
			} else {
				drawtalk(index, canvas);
				index--;
			}
		}
		if (playStatus) {
			postInvalidateDelayed(delayMilliseconds);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
	}

	public void drawtalk(int num, Canvas canvas) {
		int talk_x = 0;
		int talk_y = 0;
		int displayDensity = getResources().getDisplayMetrics().densityDpi;

		if (screenWidth > 1000 && displayDensity < 500 && displayDensity > 320) {
			talk_x = (int) (x + 34);
			talk_y = (int) (y + 50);
		}
		// 1280x720
		else if (screenWidth > 700 && displayDensity > 300) {
			talk_x = (int) (x + 22);
			talk_y = (int) (y + 35);
		}
		// 1280x720
		else if (screenWidth > 320 && displayDensity < 300 && displayDensity > 160) {
			talk_x = (int) (x + 15);
			talk_y = (int) (y + 25);
		} else if (screenWidth <= 320 || displayDensity <= 160) {
			talk_x = (int) (x + 8);
			talk_y = (int) (y + 13);
		}
		switch (num) {
		case 1:
			if (talk0 != null) {
				canvas.drawBitmap(talk0, talk_x, talk_y, mPaint);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			if (talk1 != null) {
				canvas.drawBitmap(talk1, talk_x, talk_y, mPaint);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case 3:
			if (talk2 != null) {
				canvas.drawBitmap(talk2, talk_x, talk_y, mPaint);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case 4:
			if (talk3 != null) {
				canvas.drawBitmap(talk3, talk_x, talk_y, mPaint);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case 5:
			if (talk4 != null) {
				canvas.drawBitmap(talk4, talk_x, talk_y, mPaint);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case 6:
			if (talk5 != null) {
				canvas.drawBitmap(talk5, talk_x, talk_y, mPaint);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		int displayDensity = getResources().getDisplayMetrics().densityDpi;
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			// Logger.d("measureHeight screenWidth = " + screenWidth);
			if (screenWidth > 1000 && displayDensity < 500 && displayDensity > 320) {
				result = 200;
			}

			// 1280x720
			if (screenWidth > 700 && displayDensity > 300) {
				result = 150;
			}
			// 1280x720
			if (screenWidth > 320 && displayDensity > 160 && displayDensity < 300) {
				result = 100;
			}
			if (screenWidth <= 320 || displayDensity <= 160) {
				result = 50;
			}
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		int displayDensity = getResources().getDisplayMetrics().densityDpi;
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			// Logger.d("measureWidth screenWidth = " + screenWidth +
			// "  displayDensity = " + displayDensity);
			if (screenWidth > 1000 && displayDensity < 500 && displayDensity > 320) {
				result = 120;
			}
			// 1280x720
			if (screenWidth > 700 && displayDensity > 300) {
				result = 95;
			}
			// 1280x720 782x480 density = 240
			if (screenWidth > 320 && displayDensity < 300 && displayDensity > 160) {
				result = 70;
			}
			if (screenWidth <= 320 || displayDensity <= 160) {
				result = 45;
			}
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}
}
