package com.read.mobile.module.adapter;

import java.util.List;

import android.content.Context;
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
import com.read.mobile.constants.Contacts;
import com.read.mobile.env.BaseApp;
import com.read.mobile.utils.SaveUtils;
import com.read.mobile.utils.Utils;
import com.read.mobile.utils.media.PlaySound;
import com.read.mobile.utils.media.RecorderUtils;
import com.read.mobile.utils.media.RecorderUtils.PlayCompleteI;

public class NoteListAdapter extends BaseAdapter {

	private List<NoteItem> items;
	private LayoutInflater inflater;

	public NoteListAdapter(Context context, List<NoteItem> items) {
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

	public int getItemViewType(int position) {
		if (items.get(position).getType().equals(Contacts.NOTE_TYPE_CHART)) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder;
		if (arg1 == null) {
			holder = new ViewHolder();
			if (getItemViewType(arg0) == 1) {
				arg1 = inflater.inflate(R.layout.item_note_voice_list, null);
				holder.timeTV = (TextView) arg1.findViewById(R.id.item_note_time_tv);
				holder.bookTV = (TextView) arg1.findViewById(R.id.item_note_book_tv);
				holder.progressBar = (ProgressBar) arg1.findViewById(R.id.item_note_progress_tv);
				holder.playBtn = (ImageView) arg1.findViewById(R.id.item_note_btn);
			} else {
				arg1 = inflater.inflate(R.layout.item_note_list_write, null);
				holder.timeTV = (TextView) arg1.findViewById(R.id.item_note_time_tv);
				holder.bookTV = (TextView) arg1.findViewById(R.id.item_note_book_tv);
				holder.noteTV = (TextView) arg1.findViewById(R.id.item_note_tv);
				holder.bookTimeTV = (TextView) arg1.findViewById(R.id.item_note_book_time_tv);
			}

			arg1.setTag(holder);
		}
		holder = (ViewHolder) arg1.getTag();
		NoteItem item = items.get(arg0);
		if (getItemViewType(arg0) == 1) {
			holder.timeTV.setText(item.getCreated());
			holder.bookTV.setText(item.getBookname());
			if (holder.progressBar.getTag() != null && holder.progressBar.getTag() instanceof Integer) {
				holder.progressBar.setProgress((int) holder.progressBar.getTag());
			} else {
				holder.progressBar.setProgress(0);
			}
			setAction(holder, item);
		} else {
			holder.timeTV.setText(item.getCreated());
			holder.bookTimeTV.setText("");
			holder.noteTV.setText(item.getWord());
			holder.bookTV.setText(item.getBookname());
		}
		return arg1;
	}

	class ViewHolder {
		TextView timeTV;
		TextView bookTV;
		ProgressBar progressBar;
		ImageView playBtn;
		TextView noteTV;
		TextView bookTimeTV;

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
		});
	}
}
