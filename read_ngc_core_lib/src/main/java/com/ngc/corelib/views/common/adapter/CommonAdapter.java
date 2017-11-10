package com.ngc.corelib.views.common.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context context;
	protected List<T> list;
	protected int layoutId;

	public CommonAdapter(Context context, List<T> list, int layoutId) {
		this.context = context;
		this.list = list;
		this.layoutId = layoutId;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public T getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.getViewHolder(context, position, convertView, parent, layoutId);
		convert(holder, getItem(position));
		return holder.getConventView();
	}

	protected abstract void convert(ViewHolder holder, T t);

}
