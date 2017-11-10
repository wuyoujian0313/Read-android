package com.ngc.corelib.listener;

import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class NGCGestureListener implements OnGestureListener {

	private static final int SWIPE_MAX_OFF_PATH = 100;
	private static final int SWIPE_MIN_DISTANCE = 100;
	private static final int SWIPE_THRESHOLD_VELOCITY = 500;

	public Context context;

	public NGCGestureListener(Context context) {
		this.context = context;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

		if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
			return false;
		if ((e1.getX() - e2.getX()) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
		} else if ((e2.getX() - e1.getX()) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
			((Activity) context).finish();
		}
		return true;
	}

}
