package com.read.mobile.module.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.read.mobile.R;
import com.read.mobile.beans.NoteItem;
import com.read.mobile.utils.Utils;

public class NoteListWriteAdapter extends BaseAdapter {

	private List<NoteItem> items;
	private LayoutInflater inflater;

	public NoteListWriteAdapter(Context context, List<NoteItem> items) {
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
			arg1 = inflater.inflate(R.layout.item_note_list_write, null);
			holder = new ViewHolder();
			holder.timeTV = (TextView) arg1.findViewById(R.id.item_note_time_tv);
			holder.bookTV = (TextView) arg1.findViewById(R.id.item_note_book_tv);
			holder.noteTV = (TextView) arg1.findViewById(R.id.item_note_tv);
			holder.bookTimeTV = (TextView) arg1.findViewById(R.id.item_note_book_time_tv);
			arg1.setTag(holder);
		}
		holder = (ViewHolder) arg1.getTag();
		NoteItem item = items.get(arg0);
		holder.timeTV.setText(item.getCreated());
		holder.bookTimeTV.setText("");
		holder.noteTV.setText(item.getWord());
		holder.bookTV.setText(item.getBookname());
		return arg1;
	}

	class ViewHolder {
		TextView timeTV;
		TextView bookTV;
		TextView noteTV;
		TextView bookTimeTV;
	}

}
