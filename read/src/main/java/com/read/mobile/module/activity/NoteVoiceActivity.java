package com.read.mobile.module.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ngc.corelib.encrypt.SignUtil;
import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.http.bean.CopyOfBaseFileRequest;
import com.ngc.corelib.http.fileupload.CopyOfUploadFileAsyncTask;
import com.ngc.corelib.utils.Logger;
import com.ngc.corelib.utils.Options;
import com.ngc.corelib.utils.Tips;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.read.mobile.R;
import com.read.mobile.beans.BookItem;
import com.read.mobile.beans.UserUploadImgResult;
import com.read.mobile.constants.Contacts;
import com.read.mobile.env.BaseActivity;
import com.read.mobile.observer.ReadAgent;
import com.read.mobile.utils.ImageDisplayer;
import com.read.mobile.utils.PermissionUitls;
import com.read.mobile.utils.SaveUtils;
import com.read.mobile.utils.media.AudioMedia;
import com.read.mobile.utils.media.AudioRecordMedia;
import com.read.mobile.utils.media.PlaySound;
import com.read.mobile.utils.media.RecorderUtils.PlayCompleteI;
import com.read.mobile.utils.media.SoundView;

public class NoteVoiceActivity extends BaseActivity implements OnClickListener {
	private Button selectBtn;
	private ImageView imgIV;
	private TextView nameTV;
	private TextView authorTV;
	private TextView pressTV;
	private View bookView;

	private Button takePhotoBtn;

	private BookItem bookItem;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	private View requestVoiceView;
	private ImageView requestVoiceIV;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_note_voice;
	}

	@Override
	protected void initViwes() {
		showLeftBack(null);
		showRightTV("完成", new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				upload();
			}
		});

		imageLoader = ImageLoader.getInstance();
		options = Options.getListOptions();

		selectBtn = (Button) findViewById(R.id.note_write_btn);
		selectBtn.setOnClickListener(this);
		takePhotoBtn = (Button) findViewById(R.id.note_takephoto_btn);
		takePhotoBtn.setOnClickListener(this);
		imgIV = (ImageView) findViewById(R.id.note_write_img);
		nameTV = (TextView) findViewById(R.id.note_write_name);
		authorTV = (TextView) findViewById(R.id.note_write_author);
		pressTV = (TextView) findViewById(R.id.note_write_press);
		bookView = findViewById(R.id.write_book_view);
		bookView.setVisibility(View.GONE);
		selectBtn.setVisibility(View.VISIBLE);

		bookView.setOnClickListener(this);

		requestVoiceView = findViewById(R.id.call_request_view);
		requestVoiceIV = (ImageView) findViewById(R.id.call_request_voice_iv);

		initSoundView();
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.note_write_btn:
		case R.id.write_book_view: {
			Intent intent = new Intent(this, BookSearchActivity.class);
			startActivityForResult(intent, 0xFF);
			break;
		}
		case R.id.note_takephoto_btn: {

			final String checkPermissinos[] = {Manifest.permission.CAMERA,
					Manifest.permission.WRITE_EXTERNAL_STORAGE};
			PermissionUitls.mContext = this;
			if(!PermissionUitls.isGetAllPermissionsByList(checkPermissinos) ) {
				new AlertDialog
						.Builder(this)
						.setTitle("提示信息")
						.setMessage("该功能需要您接受应用对一些关键权限（拍照）的申请，如之前拒绝过，可到手机系统的应用管理授权设置界面再次设置。")
						.setPositiveButton("确认", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								checkPermission("takephoto",PermissionUitls.PERMISSION_CAMERA_CODE,checkPermissinos);
							}
						}).show();
			} else {
				takePhoto();
			}
		}
			break;
		}
	}

	//请求相机权限
	private void checkPermission(final String functionName, int permissionCode, String[] permissions) {
		PermissionUitls.PermissionListener permissionListener = new PermissionUitls.PermissionListener() {
			@Override
			public void permissionAgree() {
				takePhoto();
			}

			@Override
			public void permissionReject() {

			}
		};
		PermissionUitls permissionUitls = PermissionUitls.getInstance(null, permissionListener);
		permissionUitls.permssionCheck(permissionCode,permissions);
	}

	private void setInfo(BookItem bookItem) {
		selectBtn.setVisibility(View.GONE);
		bookView.setVisibility(View.VISIBLE);
		if (bookItem != null) {
			if (bookItem.isAdd()) {
				takePhotoBtn.setVisibility(View.VISIBLE);
			} else {
				takePhotoBtn.setVisibility(View.GONE);
			}

			if (!TextUtils.isEmpty(bookItem.getPic_big())) {
				imageLoader.displayImage(bookItem.getPic_big(), imgIV, options);
			}
			if (!TextUtils.isEmpty(bookItem.getName())) {
				nameTV.setText(bookItem.getName());
			} else {
				nameTV.setText("");
			}
			if (!TextUtils.isEmpty(bookItem.getAuthor())) {
				authorTV.setText(bookItem.getAuthor());
			} else {
				authorTV.setText("");
			}
			if (!TextUtils.isEmpty(bookItem.getPress())) {
				pressTV.setText(bookItem.getPress());
			} else {
				pressTV.setText("");
			}
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg0 == 0xFF && arg1 == RESULT_OK) {
			bookItem = (BookItem) arg2.getSerializableExtra("BookItem");
			setInfo(bookItem);
		} else if (arg0 == TAKE_PICTURE) {
			// TODO
			takePhotoBtn.setVisibility(View.GONE);
			ImageDisplayer.getInstance(this).displayBmp(imgIV, null, path);
		}
	}

	private LinearLayout mSoundLayout;
	private SoundView mSoundView;
	private ImageView mSoundImage;
	private TextView mSoundText;
	private AudioRecordMedia mediaRecorder;
	private AudioMedia media = new AudioMedia();
	private String filePath;
	private String tempPath;

	public interface PlayAudioListener {
		public void playCompleted();
	}

	private PlayAudioListener mAudioListener;
	/**
	 * 按住说话
	 */
	private Button pressVoice;

	private boolean isCancel;
	// 语音文件名
	private Long mAudioTime;
	private File audioDir = null;
	private float mYFirst;

	private enum STATE_RECORD {
		START, STOP, CANCEL, RESTART, IDLE
	};

	private STATE_RECORD mState = STATE_RECORD.IDLE;

	private void initSoundView() {
		// 播放模式
		mSoundLayout = (LinearLayout) findViewById(R.id.sound_layout);
		mSoundView = (SoundView) findViewById(R.id.sound_view);
		mSoundImage = (ImageView) findViewById(R.id.sound_image);
		mSoundText = (TextView) findViewById(R.id.sound_text);
		pressVoice = (Button) findViewById(R.id.btn_yuyin);

		pressVoice.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				// Logger.e("event.getY()==" + event.getY());
				// Logger.e("event.getX()==" + event.getX());
				int maxMove = getMaxMove();// 按住说话按钮高度的两倍
				if (mYFirst - event.getY() > maxMove) {
					// 取消发送
					showSoundView(STATE_RECORD.CANCEL);
					mYFirst = 0;
				} else {
					showSoundView(STATE_RECORD.RESTART);
				}
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							com.ngc.corelib.utils.Logger.e("event.getAction()==MotionEvent.ACTION_UP");
							recordButtonEventUp();
						}
					}, 500);
					break;
				case MotionEvent.ACTION_DOWN:
					Logger.e("event.getAction()==MotionEvent.ACTION_DOWN");
					stopPlaying();// 开始录音时如果有正在播放的语言，则停止
					startRecording();
					break;
				case MotionEvent.ACTION_CANCEL:
					Logger.e("event.getAction()==MotionEvent.ACTION_CANCEL");
					break;
				default:
					Logger.e("event.getAction()==" + MotionEvent.ACTION_CANCEL);
					break;
				}
				return false;
			}
		});
	}

	private int getMaxMove() {
		int height = pressVoice.getHeight();
		return (int) (1.5 * height);
	}

	private boolean isVoice = false;

	@SuppressWarnings("deprecation")
	private void showSoundView(STATE_RECORD state) {
		if (mState != state) {
			mState = state;
			switch (state) {
			case START:
				Logger.e("START");
				pressVoice.setBackgroundResource(R.drawable.new_start_audio_record_pressed);
				mSoundLayout.setVisibility(View.VISIBLE);
				mSoundImage.setImageResource(R.drawable.talk);
				mSoundText.setText("手指上滑，取消发送");
				mSoundText.setBackgroundDrawable(null);
				mSoundView.setVisibility(View.VISIBLE);
				mSoundView.playStatus = true;
				mSoundView.setRecorder(mediaRecorder);
				mSoundView.invalidate();
				isCancel = false;
				break;
			case STOP:
				Logger.e("STOP");
				if (pressVoice != null) {
					pressVoice.setBackgroundResource(R.drawable.new_start_audio_record_normal);
				}
				mSoundView.playStatus = false;
				mSoundImage.setImageResource(R.drawable.talk);
				mSoundView.setVisibility(View.VISIBLE);
				mSoundText.setText("手指上滑，取消发送");
				mSoundText.setBackgroundDrawable(null);
				mSoundLayout.setVisibility(View.GONE);
				tempPath = filePath;
				sendSoundMessage();
				break;
			case CANCEL:
				Logger.e("CANCEL");
				mSoundLayout.setVisibility(View.VISIBLE);
				mSoundView.playStatus = false;
				mSoundView.setVisibility(View.VISIBLE);
				isVoice = false;
				mSoundImage.setImageResource(R.drawable.speaker_cancel);
				mSoundText.setText("松开手指，取消发送");
				mSoundText.setBackgroundResource(R.drawable.speaker_cancel_text_bg);
				isCancel = true;
				filePath = tempPath;
				break;
			case RESTART:
				Logger.e("RESTART");
				mSoundImage.setImageResource(R.drawable.talk);
				mSoundView.setVisibility(View.VISIBLE);
				mSoundView.playStatus = true;
				mSoundText.setText("手指上滑，取消发送");
				mSoundText.setBackgroundDrawable(null);
				isCancel = false;
				break;
			default:
				break;
			}
		}
	}

	boolean isLongClick = false;

	private void recordButtonEventUp() {
		if (isLongClick) {
			stopRecording();
		}
	}

	private void stopPlaying() {
		if (null != media && media.isPlay()) {
			media.pause();
		}
		if (mAudioListener != null) {
			mAudioListener.playCompleted();
		}
	}

	private void destroySound() {
		if (null != mAudioListener) {
			mAudioListener.playCompleted();
		}
	}

	private void startRecording() {
		// 开始录音
		if (mState == STATE_RECORD.START && null != mediaRecorder) {
			mediaRecorder.stop();
		}
		if (mediaRecorder == null) {
			mediaRecorder = new AudioRecordMedia();
		}
		if (audioDir == null) {
			File cacheDir = Environment.getExternalStorageDirectory();
			audioDir = new File(cacheDir, Contacts.PATH_AUDIO);
			if (!audioDir.exists()) {
				audioDir.mkdirs();
			}
		}
		// 注意: 发送语音消息的时间，即为语音文件名
		mAudioTime = System.currentTimeMillis();
		if (audioDir != null && mediaRecorder != null) {
			filePath = audioDir.getAbsolutePath() + "/" + mAudioTime// 名字的拼装问题
					+ ".mp3";
			// + ".amr";
			mediaRecorder.setOutFileName(filePath);
			mediaRecorder.start();
			showSoundView(STATE_RECORD.START);
			isLongClick = true;
		}
	}

	private void stopRecording() {
		showSoundView(STATE_RECORD.STOP);
		mState = STATE_RECORD.IDLE;
		mYFirst = 0;
		if (null != mediaRecorder) {
			mediaRecorder.stop();
		}
	}

	protected void sendSoundMessage() {
		// 刷新界面
		if (!isCancel) {
			requestVoiceView.setVisibility(View.VISIBLE);
			requestVoiceView.setOnClickListener(clickListener);
		}
	}

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			startPlayingAnimation(requestVoiceIV);
			Logger.e("filePath:" + filePath);
			PlaySound.playSound(null, filePath, new PlayCompleteI() {
				@Override
				public void onPlayComplete() {
					if (animDrawable != null) {
						animDrawable.stop();
					}
					if (requestVoiceIV != null) {
						requestVoiceIV.setImageResource(R.drawable.chatto_voice_playing_f3_);
					}
				}
			}, null);
		}
	};

	/**
	 * 开始播放动画
	 * 
	 * @param sound
	 */
	private AnimationDrawable animDrawable;
	private ImageView preSoundView;

	private void startPlayingAnimation(ImageView sound) {
		if (animDrawable == null) {
			animDrawable = (AnimationDrawable) getApplicationContext().getResources().getDrawable(
					R.drawable.voice_to_anim);
		}
		if (preSoundView != null) {
			if (preSoundView != null && animDrawable.isRunning()) {
				animDrawable.stop();
			}
			preSoundView.setImageResource(R.drawable.sound_right);
		}

		preSoundView = sound;
		sound.setImageDrawable(animDrawable);
		animDrawable.setCallback(sound);
		animDrawable.setVisible(true, true);
		animDrawable.start();
	}

	private void upload() {
		if (bookItem == null) {
			Tips.tipShort(this, "请选择图书~");
			return;
		}
		if (bookItem.isAdd() && TextUtils.isEmpty(path)) {
			Tips.tipLong(this, "请拍照书的封面");
			return;
		}

		CopyOfBaseFileRequest request = new CopyOfBaseFileRequest();
		List<BasicNameValuePair> paramsList = new ArrayList<>();
		BasicNameValuePair pair01 = new BasicNameValuePair("u", SaveUtils.getUserId());
		BasicNameValuePair pair02 = new BasicNameValuePair("source", bookItem.isAdd() ? "2" : "1");
		BasicNameValuePair pair03 = new BasicNameValuePair("type", "2");
		BasicNameValuePair pair04 = new BasicNameValuePair("bookName", bookItem.getName());
		BasicNameValuePair pair05 = new BasicNameValuePair("author", bookItem.getAuthor());
		BasicNameValuePair pair06 = new BasicNameValuePair("press", bookItem.getPress());
		BasicNameValuePair pair08 = new BasicNameValuePair("isbn", bookItem.getIsbn());
		paramsList.add(pair01);
		paramsList.add(pair02);
		paramsList.add(pair03);
		paramsList.add(pair04);
		paramsList.add(pair05);
		paramsList.add(pair06);
		paramsList.add(pair08);
		String sign = SignUtil.signNameValue(paramsList, "fdf4da319ea90b3cdb861887c77a75ec");
		BasicNameValuePair signPair = new BasicNameValuePair("sign", sign);
		paramsList.add(signPair);
		String[] names = null;
		if (bookItem.isAdd()) {
			File[] files = { new File(filePath), new File(path) };
			names = new String[2];
			names[0] = "file";
			names[1] = "pic";
			request.setFile(files);
		} else {
			File[] files = { new File(filePath) };
			names = new String[1];
			names[0] = "file";
			request.setFile(files);
		}
		request.setParamsList(paramsList);
		CopyOfUploadFileAsyncTask asyncTask = new CopyOfUploadFileAsyncTask(UserUploadImgResult.class,
				new AsyncTaskListener() {
					@Override
					public void onTaskSuccess(int requestId, BaseResult result) {
						dismissProgressDialog();
						if (result != null) {
							dismissProgressDialog();
							if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
								Tips.tipLong(NoteVoiceActivity.this, "添加成功");
								ReadAgent.geUshareAgent().notifyUpdateNoteObserver(true);
							} else {
								Tips.tipLong(NoteVoiceActivity.this, result.getHead().getMessage());
							}
						} else {
							Tips.tipShort(NoteVoiceActivity.this, "网络请求失败");
						}
					}

					@Override
					public void onTaskStart(int requestId) {
						showProgressDialog("正在上传~");
					}

					@Override
					public void onTaskFail(int requestId, Exception e) {
						dismissProgressDialog();
						Tips.tipShort(NoteVoiceActivity.this, e.getMessage());
					}
				}, request, names);
		asyncTask.execute(Contacts.URL_BOOK_WRITE_NOTES);
	}

	private String path = "";
	private static final int TAKE_PICTURE = 0x000000;

	public void takePhoto() {
		String filePath = Environment.getExternalStorageDirectory() +
				File.separator + Environment.DIRECTORY_DCIM + File.separator;
		File vFile = new File(filePath + "readImage" + File.separator , String.valueOf(System
				.currentTimeMillis()) + ".jpg");

		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			if (!vFile.exists()) {
				File vDirPath = vFile.getParentFile();
				vDirPath.mkdirs();
			} else {
				if (vFile.exists()) {
					vFile.delete();
				}
			}
			path = vFile.getAbsolutePath();

			Intent openCameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			if (Build.VERSION.SDK_INT<24){
				Uri imageUri = Uri.fromFile(vFile);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			}else{
				//兼容android7.0 使用共享文件的形式
				ContentValues contentValues = new ContentValues(1);
				contentValues.put(MediaStore.Images.Media.DATA, path);
				Uri uri = getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			}
			startActivityForResult(openCameraIntent, TAKE_PICTURE);
		}
	}
}
