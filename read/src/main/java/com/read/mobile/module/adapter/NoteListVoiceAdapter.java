package com.read.mobile.module.adapter;

import java.util.List;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ngc.corelib.utils.Logger;
import com.read.mobile.R;
import com.read.mobile.beans.NoteItem;
import com.read.mobile.env.BaseApp;
import com.read.mobile.module.activity.NoteVoiceActivity;
import com.read.mobile.utils.PermissionUitls;
import com.read.mobile.utils.SaveUtils;
import com.read.mobile.utils.Utils;
import com.read.mobile.utils.media.PlaySound;
import com.read.mobile.utils.media.RecorderUtils;
import com.read.mobile.utils.media.RecorderUtils.PlayCompleteI;

public class NoteListVoiceAdapter extends BaseAdapter {

	private List<NoteItem> items;
	private LayoutInflater inflater;

	public NoteListVoiceAdapter(Context context, List<NoteItem> items) {
		this.items = items;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.item_note_voice_list, null);
			holder = new ViewHolder();
			holder.timeTV = (TextView) arg1.findViewById(R.id.item_note_time_tv);
			holder.bookTV = (TextView) arg1.findViewById(R.id.item_note_book_tv);
			holder.progressBar = (ProgressBar) arg1.findViewById(R.id.item_note_progress_tv);
			holder.playBtn = (ImageView) arg1.findViewById(R.id.item_note_btn);
			arg1.setTag(holder);
		}
		holder = (ViewHolder) arg1.getTag();
		NoteItem item = items.get(arg0);
		holder.timeTV.setText(item.getCreated());
		holder.bookTV.setText(item.getBookname());
		if (holder.progressBar.getTag() != null && holder.progressBar.getTag() instanceof Integer) {
			holder.progressBar.setProgress((int) holder.progressBar.getTag());
		} else {
			holder.progressBar.setProgress(0);
		}
		setAction(holder, item);
		return arg1;
	}

	class ViewHolder {
		TextView timeTV;
		TextView bookTV;
		ProgressBar progressBar;
		ImageView playBtn;

	}

	private void checkPermission(final String functionName, int permissionCode, String[] permissions) {
		PermissionUitls.PermissionListener permissionListener = new PermissionUitls.PermissionListener() {
			@Override
			public void permissionAgree() {
			}

			@Override
			public void permissionReject() {

			}
		};
		PermissionUitls permissionUitls = PermissionUitls.getInstance(null, permissionListener);
		permissionUitls.permssionCheck(permissionCode,permissions);
	}

	private void setAction(final ViewHolder holder, final NoteItem item) {
		holder.playBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.progressBar.setProgress(0);
				holder.playBtn.setImageResource(R.drawable.icon_pause);
				if (RecorderUtils.getMediaPlayer() != null && RecorderUtils.getMediaPlayer().isPlaying()) {
					holder.playBtn.setImageResource(R.drawable.icon_play);
					RecorderUtils.stopPlay();
					if (holder.progressBar.getTag() != null && holder.progressBar.getTag() instanceof Integer) {
						holder.progressBar.setProgress((int) holder.progressBar.getTag());
					} else {
						holder.progressBar.setProgress(0);
					}
				} else {
					final String checkPermissinos[] = {
							Manifest.permission.WRITE_EXTERNAL_STORAGE};
					PermissionUitls.mContext = inflater.getContext();
					if(!PermissionUitls.isGetAllPermissionsByList(checkPermissinos) ) {
						new AlertDialog
								.Builder(inflater.getContext())
								.setTitle("提示信息")
								.setMessage("该功能需要您接受应用对一些关键权限（麦克）的申请，如之前拒绝过，可到手机系统的应用管理授权设置界面再次设置。")
								.setPositiveButton("确认", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {

										checkPermission("takephoto",PermissionUitls.PERMISSION_STORAGE_CODE,checkPermissinos);
									}
								}).show();
					} else {
						PlaySound.playSound(item.getSound(),
								BaseApp.getSharedPreferences().getString(SaveUtils.getUserId() + item.getSound(), ""),
								new PlayCompleteI() {
									@Override
									public void onPlayComplete() {
										Logger.e("我播放完了~");
										holder.playBtn.setImageResource(R.drawable.icon_play);
										if (holder.progressBar.getTag() != null
												&& holder.progressBar.getTag() instanceof Integer) {
											holder.progressBar.setProgress((int) holder.progressBar.getTag());
										} else {
											holder.progressBar.setProgress(0);
										}
									}
								}, holder.progressBar);
					}



				}
			}
		});
	}
}
