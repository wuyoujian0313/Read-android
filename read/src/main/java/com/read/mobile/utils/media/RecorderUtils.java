package com.read.mobile.utils.media;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

import com.ngc.corelib.utils.Logger;

public class RecorderUtils {

	// *************************************播放**************************************
	private static MediaPlayer mediaPlayer;
	private static boolean playState = false; // 播放状态

	private static PlayCompleteI completeI;
	private static ProgressBar bar;

	private static Timer mTimer;
	private static TimerTask mTimerTask;

	public static void playRecord(String file_url, final PlayCompleteI completeI, final ProgressBar bar) {
		RecorderUtils.completeI = completeI;
		RecorderUtils.bar = bar;
		Logger.e("playRecord=====" + file_url);
		if (!playState) {
			mediaPlayer = new MediaPlayer();
			try {
				// 模拟器里播放传url，真机播放传getAmrPath()
				mediaPlayer.setDataSource(file_url);
				Logger.e("难道我没走到这？？？");
				mediaPlayer.prepare();
				mTimer = new Timer();
				mTimerTask = new TimerTask() {
					@Override
					public void run() {
						if (bar != null) {
							Logger.e("mediaPlayer.getCurrentPosition()--->" + mediaPlayer.getCurrentPosition());
							Logger.e("mediaPlayer.getDuration()--->" + mediaPlayer.getDuration());
							bar.setProgress((int) (100 * (Double.valueOf(mediaPlayer.getCurrentPosition()) / mediaPlayer
									.getDuration())));
							bar.setTag((int) (100 * (Double.valueOf(mediaPlayer.getCurrentPosition()) / mediaPlayer
									.getDuration())));
						}
					}
				};
				mTimer.schedule(mTimerTask, 0, 400);
				mediaPlayer.start();
				playState = true;
				// 设置播放结束时监听
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						Logger.e("我检测到播放完成了===0");
						handler.sendEmptyMessage(0x00);
						if (playState) {
							playState = false;
						}
					}
				});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				Logger.e("IllegalArgumentException--->" + e.getMessage());
			} catch (IllegalStateException e) {
				e.printStackTrace();
				Logger.e("IllegalStateException--->" + e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				Logger.e("IOException--->" + e.getMessage());
			}

		} else {
			Logger.e("else----mediaPlayer.isPlaying()---->" + mediaPlayer.isPlaying());
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
				playState = false;
			} else {
				playState = false;
			}
			Logger.e("else----mediaPlayer.isPlaying()---->" + mediaPlayer.isPlaying());
			mediaPlayer = new MediaPlayer();
			try {
				// 模拟器里播放传url，真机播放传getAmrPath()
				mediaPlayer.setDataSource(file_url);
				Logger.e("难道我没走到这？？？");
				mediaPlayer.prepare();
				mTimer = new Timer();
				mTimerTask = new TimerTask() {
					@Override
					public void run() {
						if (bar != null) {
							bar.setProgress((int) (100 * (Double.valueOf(mediaPlayer.getCurrentPosition()) / mediaPlayer
									.getDuration())));
							bar.setTag((int) (100 * (Double.valueOf(mediaPlayer.getCurrentPosition()) / mediaPlayer
									.getDuration())));
						}
					}
				};
				mTimer.schedule(mTimerTask, 0, 10);
				mediaPlayer.start();
				playState = true;
				// 设置播放结束时监听
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						Logger.e("我检测到播放完成了===0");
						handler.sendEmptyMessage(0x00);
						if (playState) {
							playState = false;
						}
					}
				});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				Logger.e("IllegalArgumentException--->" + e.getMessage());
			} catch (IllegalStateException e) {
				e.printStackTrace();
				Logger.e("IllegalStateException--->" + e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				Logger.e("IOException--->" + e.getMessage());
			}
		}
	}

	private static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x00:
				if (completeI != null) {
					Logger.e("我检测到播放完成了===1");
					completeI.onPlayComplete();
				}
				Logger.e("我检测到播放完成了===2");
				break;
			}
		};
	};

	public static void stopPlay() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}

	public static MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public interface PlayCompleteI {
		public void onPlayComplete();
	}

}
