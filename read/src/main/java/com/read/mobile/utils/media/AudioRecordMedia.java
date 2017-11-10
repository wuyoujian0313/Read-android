package com.read.mobile.utils.media;

import java.io.IOException;

import android.media.MediaRecorder;

public class AudioRecordMedia {
	private String fileName = null;
	private AudioRecorder mAudioRecorder = null;

	public AudioRecordMedia() {
		mAudioRecorder = new AudioRecorder();
	}

	public void destroy() {
		if (mAudioRecorder != null) {
			// mAudioRecorder.stop();
			mAudioRecorder.release();
			mAudioRecorder = null;
		}
	}

	public void setOutFileName(String file) {
		fileName = file;

	}

	public void setOutputPath(String path) {
		if (mAudioRecorder != null) {
			mAudioRecorder.setOutputPath(path);
		}

	}

	public void stop() {
		if (mAudioRecorder != null) {
			mAudioRecorder.stop();
		}
	}

	public void start() {
		if (mAudioRecorder != null) {
			mAudioRecorder.release();
			mAudioRecorder = null;
		}
		mAudioRecorder = new AudioRecorder();
		setOutputPath(fileName);
		mAudioRecorder.start();

	}

	private class AudioRecorder {
		private MediaRecorder recorder = new MediaRecorder();
		/** 是否已初始化 */
		private boolean mIsInitialized = false;

		/**
		 * 构造函数
		 */
		public AudioRecorder() {
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			/**
			 * mediaRecorder.setOutputFormat代表输出文件的格式。该语句必须在setAudioSource之后，
			 * 在prepare之前。
			 * OutputFormat内部类，定义了音频输出的格式，主要包含MPEG_4、THREE_GPP、RAW_AMR……等。
			 */
			recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			/**
			 * mediaRecorder.setAddioEncoder()方法可以设置音频的编码
			 * AudioEncoder内部类详细定义了两种编码：AudioEncoder.DEFAULT、AudioEncoder.AMR_NB
			 */
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		}

		/**
		 * 设置播放数据路径
		 * 
		 * @param path
		 *            路径
		 */
		public void setOutputPath(String path) {
			try {
				recorder.setOutputFile(path);
				recorder.prepare();
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
			recorder.release();
		}

		/**
		 * 开始录音
		 */
		public void start() {
			recorder.start();
		}

		public void stop() {
			recorder.stop();
			recorder.release();
		}

		public int getMaxAmplitude() {
			return recorder.getMaxAmplitude();
		}

	}

	public int getMaxAmplitude() {
		return mAudioRecorder.getMaxAmplitude();
	}

}
