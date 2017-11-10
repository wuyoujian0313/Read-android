package com.read.mobile.utils.media;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.os.SystemClock;

import com.read.mobile.env.BaseApp;

public class AudioMedia extends Thread {
	/** 播放位置 */
	private int mDuration = -1;
	/** 文件名 */
	private String fileName = null;
	/** 是否循环播放 */
	private boolean isLoop = false;

	/** 播放器 */
	private MultiPlayer mPlayer = null;
	/** 是否结束播放 */
	private boolean isEnd = false;

	private AudioMediaPlayCompletedListener mediaPlayCompletedListener;

	public interface AudioMediaPlayCompletedListener {
		public void audioPlayCompleted();
	}

	/**
	 * 构造函数
	 */
	public AudioMedia() {
		mPlayer = new MultiPlayer();
	}

	public void setListener(AudioMediaPlayCompletedListener listener) {
		mediaPlayCompletedListener = listener;
	}

	/**
	 * 设置数据
	 * 
	 * @param path
	 *            要播放的数据路径
	 */
	public void setDataSource(String path) {
		if (mPlayer != null) {
			mPlayer.setDataSource(path);
		}
	}

	public void setAudioFile(String file) {
		fileName = file;
	}

	/**
	 * 暂停播放
	 */
	public void pause() {
		if (mPlayer != null) {
			mPlayer.pause();
		}
	}

	/**
	 * 开始播放
	 */
	public void play() {
		if (mPlayer != null) {
			mPlayer.pause();
			mPlayer.release();
			mPlayer = null;
		}
		mPlayer = new MultiPlayer();
		setDataSource(fileName);

		mPlayer.start();
		mPlayer.setLoop(isLoop);

	}

	/**
	 * 是否正在播放
	 * 
	 * @return 正在播放返回true
	 */
	public boolean isPlay() {

		return null != mPlayer ? mPlayer.isPlaying() : false;
	}

	/**
	 * 设置音量
	 * 
	 * @param v
	 *            设置后的音量
	 */
	public void setVolume(float v) {
		if (mPlayer != null) {
			mPlayer.setVolume(v);
		}
	}

	/**
	 * 关闭声音
	 */
	public void colseVolume() {
		VolumeClose vc = new VolumeClose();
		vc.start();
	}

	/**
	 * 销毁
	 */
	public void destroy() {
		if (mPlayer != null) {
			if (mPlayer.isPlaying()) {
				mPlayer.stop();
			}
			mPlayer.release();
			mPlayer = null;
		}
	}

	/**
	 * 设置是否循环播放
	 * 
	 * @param b
	 *            true，循环播放
	 */
	public void setLoop(boolean b) {
		isLoop = b;
	}

	/**
	 * 播放
	 */
	public void run() {
		try {
			// if (mPlayer.isInitialized())
			play();
		} catch (Exception ex) {
		}
	}

	private class VolumeClose extends Thread {
		/** 当前音量 */
		private int vol = SystemFunction.getSystemVolume() - 1;

		/**
		 * 音量关闭操作
		 */
		public void run() {
			int one = 20;
			int two = 40;
			int tmp = 0;
			while (vol > 0) {
				setVolume(vol);
				vol--;
				SystemClock.sleep(50);
				// tmp = two;
				// two += one;
				// one = tmp;
			}
			pause();
		}
	}

	private class MultiPlayer {
		/** 播放器 */
		private MediaPlayer mMediaPlayer = new MediaPlayer();
		/** 是否已初始化 */
		private boolean mIsInitialized = false;

		/**
		 * 构造函数
		 */
		public MultiPlayer() {
			mMediaPlayer.setWakeMode(BaseApp.getInstance(), PowerManager.PARTIAL_WAKE_LOCK);
		}

		/**
		 * 设置播放数据路径
		 * 
		 * @param path
		 *            路径
		 */
		public void setDataSource(String path) {
			try {
				mMediaPlayer.reset();
				mMediaPlayer.setOnPreparedListener(null);
				mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					public void onCompletion(MediaPlayer arg0) {
						if (mMediaPlayer.isLooping() == false) {
							isEnd = true;
							if (null != mediaPlayCompletedListener) {
								mediaPlayCompletedListener.audioPlayCompleted();
							}
							// mMediaPlayer.reset();
							// mMediaPlayer.pause();
						}
					}
				});

				mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
					public boolean onError(MediaPlayer mp, int what, int extra) {
						return false;
					}
				});

				if (path.startsWith("content://")) {
					mMediaPlayer.setDataSource(BaseApp.getInstance(), Uri.parse(path));
				} else {
					File file = new File(path);
					FileInputStream fis = new FileInputStream(file);
					mMediaPlayer.setDataSource(fis.getFD());
					fis.close();
					// mMediaPlayer.setDataSource(path);
				}
				int mode = UserSharedPreferencesHelper.getSpeakerMode();
				if (mode == UserSharedPreferencesHelper.SPEAKER_NORMAL) {
					mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				} else if (mode == UserSharedPreferencesHelper.SPEAKER_IN_CALL) {
					mMediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
				}
				mMediaPlayer.prepare();
			} catch (IOException ex) {
				mIsInitialized = false;
				return;
			} catch (IllegalArgumentException ex) {
				mIsInitialized = false;
				return;
			}
			mIsInitialized = true;
		}

		public void release() {
			mMediaPlayer.release();
		}

		/**
		 * 设置循环播放
		 * 
		 * @param b
		 *            true循环播放
		 */
		public void setLoop(boolean b) {
			mMediaPlayer.setLooping(b);
		}

		/**
		 * 是否已初始化
		 * 
		 * @return true已初始化
		 */
		public boolean isInitialized() {
			return mIsInitialized;
		}

		/**
		 * 开始播放
		 */
		public void start() {
			mMediaPlayer.start();
			isEnd = false;
		}

		/**
		 * 停止播放
		 */
		public void stop() {
			mMediaPlayer.pause();
			mMediaPlayer.stop();
			mMediaPlayer.reset();
			mIsInitialized = false;
			isEnd = true;
		}

		/**
		 * 暂停播放
		 */
		public void pause() {
			mMediaPlayer.pause();
			mMediaPlayer.reset();
			mIsInitialized = false;
		}

		/**
		 * 是否正在播放
		 * 
		 * @return true正在播放
		 */
		public boolean isPlaying() {
			return mMediaPlayer.isPlaying();
		}

		/**
		 * 音频长度
		 * 
		 * @return 音频播放长度
		 */
		public long duration() {
			return mMediaPlayer.getDuration();
		}

		/**
		 * 当前播放位置
		 * 
		 * @return 位置
		 */
		public long position() {
			return mMediaPlayer.getCurrentPosition();
		}

		/**
		 * 定位播放位置
		 * 
		 * @param whereto
		 *            要定位的位置
		 * @return 定位的位置
		 */
		public long seek(long whereto) {
			mMediaPlayer.seekTo((int) whereto);
			return whereto;
		}

		/**
		 * 设置音量
		 * 
		 * @param vol
		 *            要设置的音量
		 */
		public void setVolume(float vol) {
			mMediaPlayer.setVolume(vol, vol);
		}
	}

}
