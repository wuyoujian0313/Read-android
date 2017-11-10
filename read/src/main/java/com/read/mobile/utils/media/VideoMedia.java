package com.read.mobile.utils.media;

import java.util.Formatter;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

/***
 * 视频播放<br>
 * </pre> 支持on line stream
 * 
 * @author onpalm
 * 
 */
public class VideoMedia extends Activity implements OnClickListener {

	/**
	 * 视频view
	 */
	private VideoView mVideoView = null;
	/** 视频文件 */
	private String file = null;
	/** 视频长度 */
	private int mDuration = -1;

	private int bufferPercent = 0;

	private boolean exit = false;

	private Context context = null;

	private TextView bufferView = null;
	private LinearLayout bufferBack = null;

	private boolean error = true;

	private PopupWindow onlineControl = null;
	/** 在线控制键是否已显示 */
	private boolean controlShow = false;

	private long delayTime = 3000;

	private boolean isOnline = false;

	private final static int FADE_OUT = 100;

	private final static int SHOW_PROGRESS = 200;

	private ProgressBar mProgress = null;

	private TextView mEndTime = null;

	private TextView mCurrentTime = null;
	private ImageButton mPauseButton = null;

	private StringBuilder mFormatBuilder;
	private Formatter mFormatter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Intent intent = getIntent();
		Bundle exData = intent.getExtras();
		file = exData.getString("file");

		// setContentView(R.layout.video_play);
		// mVideoView = (VideoView) findViewById(R.id.video_play);
		// bufferView = (TextView) findViewById(R.id.video_buffer);
		// bufferBack = (LinearLayout) findViewById(R.id.video_buffer_back);

		if (SystemFunction.isOnLineMedia(file)) {
			isOnline = true;
		} else {
			MediaController nc = new MediaController(this);
			mVideoView.setMediaController(nc);
		}

		mVideoView.setVideoURI(Uri.parse(file));

		mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			public void onCompletion(MediaPlayer arg0) {
				if (mVideoView != null)
					mVideoView.stopPlayback();
				exit = true;
				finish();
			}
		});

		mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			public boolean onError(MediaPlayer mp, int what, int extra) {
				if (bufferView != null) {
					bufferView.setVisibility(View.GONE);
				}
				error = false;
				return false;
			}
		});

		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			public void onPrepared(MediaPlayer mp) {

			}
		});

		if (SystemFunction.isOnLineMedia(file)) {
			int[] dislayArr = SystemFunction.getDisplay(this);
			if (dislayArr[0] <= 320) {
				bufferBack.setPadding(0, 210, 0, 0);
			} else if (dislayArr[1] >= 800) {
				bufferBack.setPadding(0, 430, 0, 0);
			}
			bufferBack.setVisibility(View.VISIBLE);
			BufferEvent be = new BufferEvent();
			be.start();
			dislayArr = null;
		}

		mVideoView.requestFocus();
		play();
	}

	private void initFloatWindow() {
		// LayoutInflater ml = LayoutInflater.from(context);
		// View fw = ml.inflate(R.layout.media_controller, null);
		// fw.setOnTouchListener(controlTouch);
		// fw.setOnKeyListener(controlKey);
		// onlineControl = new PopupWindow(fw, LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT);
		// mFormatBuilder = new StringBuilder();
		// mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
		// mEndTime = (TextView) fw.findViewById(R.id.time);
		// mCurrentTime = (TextView) fw.findViewById(R.id.time_current);
		// mProgress = (ProgressBar)
		// fw.findViewById(R.id.mediacontroller_progress);
		// if (mProgress != null) {
		// mProgress.setMax(1000);
		// }
		// mPauseButton = (ImageButton) fw.findViewById(R.id.pause);
		// mPauseButton.setOnClickListener(mPauseListener);
	}

	private OnTouchListener controlTouch = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			showOnlineControl();
			return false;
		}
	};

	private OnKeyListener controlKey = new OnKeyListener() {

		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
				showOnlineControl();
			return false;

		}

	};

	private View.OnClickListener mPauseListener = new View.OnClickListener() {
		public void onClick(View v) {
			// if (mVideoView == null)
			// return;
			// if (mVideoView.isPlaying()) {
			// mVideoView.pause();
			// mPauseButton.setImageResource(R.drawable.ic_media_play);
			// } else {
			// mVideoView.start();
			// mPauseButton.setImageResource(R.drawable.ic_media_pause);
			//
			// }
			// show();
		}
	};

	private synchronized void showOnlineControl() {
		if (onlineControl == null) {
			initFloatWindow();
		}

		if (controlShow) {
			mHandler.removeMessages(FADE_OUT);
			onlineControl.dismiss();
			controlShow = false;
		} else {
			try {
				setProgress();
				onlineControl.setFocusable(true);
				onlineControl.setTouchable(true);
				onlineControl.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
				controlShow = true;
				show();
			} catch (Exception ex) {
			}
		}
	}

	private void show() {
		Message msg = mHandler.obtainMessage(FADE_OUT);
		mHandler.removeMessages(FADE_OUT);
		mHandler.sendMessageDelayed(msg, delayTime);
	}

	private int setProgress() {
		if (mVideoView == null) {
			return 0;
		}
		int position = mVideoView.getCurrentPosition();
		int duration = mVideoView.getDuration();
		if (mProgress != null) {
			if (duration > 0) {
				// use long to avoid overflow
				long pos = 1000L * position / duration;
				mProgress.setProgress((int) pos);
			}
			int percent = mVideoView.getBufferPercentage();
			mProgress.setSecondaryProgress(percent * 10);
		}

		if (mEndTime != null)
			mEndTime.setText(stringForTime(duration));
		if (mCurrentTime != null)
			mCurrentTime.setText(stringForTime(position));

		return position;
	}

	private String stringForTime(int timeMs) {
		int totalSeconds = timeMs / 1000;

		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		mFormatBuilder.setLength(0);
		if (hours > 0) {
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
		} else {
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (isOnline) {
			int keyCode = event.getAction();
			if (keyCode == MotionEvent.ACTION_UP)
				showOnlineControl();
			return false;
		}
		return true;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int pos;
			switch (msg.what) {
			case FADE_OUT:
				showOnlineControl();
				break;
			case SHOW_PROGRESS:
				pos = setProgress();
				if (controlShow && mVideoView.isPlaying()) {
					msg = obtainMessage(SHOW_PROGRESS);
					sendMessageDelayed(msg, 1000 - (pos % 1000));
				}
				break;
			}
		}
	};

	private DialogInterface.OnCancelListener progressCancel = new DialogInterface.OnCancelListener() {
		public void onCancel(DialogInterface dialog) {
			exit = true;
			finish();
		}
	};

	/**
	 * 播放
	 */
	private void play() {
		if (file != null) {
			try {
				if (mDuration == -1) {
					mVideoView.start();
				} else {
					mVideoView.start();
					mVideoView.seekTo(mDuration);
				}

			} catch (Exception ex) {
			}
		}
	}

	/**
	 * 点击事件
	 * 
	 * @param v
	 *            点击的view
	 */
	public void onClick(View v) {
		if (mVideoView.isPlaying()) {
			mVideoView.pause();
		} else {
			mVideoView.start();
		}
	}

	/**
	 * 开始事件
	 */
	public void onStart() {
		super.onStart();
		// mVideoView.prepareAsync();
	}

	/**
	 * resume事件
	 */
	public void onResume() {
		super.onResume();
		// play();
	}

	/**
	 * 暂停事件
	 */
	public void onPause() {
		super.onPause();
		if (mVideoView != null) {
			mVideoView.pause();
			mDuration = mVideoView.getCurrentPosition();
		}
		finish();
	}

	/**
	 * 销毁事件
	 */
	public void onDestroy() {
		super.onDestroy();
		exit = true;
		// Global.context = old;
		if (mVideoView != null)
			mVideoView.stopPlayback();
		mVideoView = null;

	}

	private Handler progressHandler = new Handler() {
		public void handleMessage(Message msg) {
			// Bundle data = msg.getData();
			if (bufferView != null) {
				bufferView.setText(bufferPercent + "");
				bufferView.invalidate();
			}
			if (bufferPercent >= 99) {
				bufferBack.setVisibility(View.GONE);
			}

		}
	};

	private boolean exitKey = false;

	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			exitKey = true;
			return true;
		} else if (exitKey && event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
			exitKey = false;

			if (mVideoView != null) {
				mVideoView.stopPlayback();
				mVideoView = null;
			}
			finish();

		}
		return super.dispatchKeyEvent(event);
	}

	public boolean onSearchRequested() {
		return true;
	}

	private class BufferEvent extends Thread {
		public void run() {

			while (!exit) {
				if (mVideoView == null) {
					exit = true;
					return;
				}
				int bf = mVideoView.getBufferPercentage();
				if (bf != bufferPercent) {
					bufferPercent = bf;
					Message msg = progressHandler.obtainMessage();
					progressHandler.sendMessage(msg);
				}
				if (bufferPercent >= 99) {
					exit = true;
					break;
				}
				SystemClock.sleep(100);
			}
		}
	}

}
