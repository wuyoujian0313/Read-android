package com.ngc.corelib.views.common.adapter;

import com.ngc.corelib.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
	private SparseArray<View> mViews;
	private View mConventView;
	private int position;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public ViewHolder(Context context, int position, ViewGroup parent, int layoutId) {
		this.position = position;
		mViews = new SparseArray<View>();
		mConventView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConventView.setTag(this);
	}

	public static ViewHolder getViewHolder(Context context, int position, View convertView, ViewGroup parent,
			int layoutId) {
		if (convertView == null) {
			return new ViewHolder(context, position, parent, layoutId);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.position = position;
			return holder;
		}
	}

	public View getConventView() {
		return mConventView;
	}

	/**
	 * 通过ViewID获取View
	 * 
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConventView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 设置TextView的值
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		((TextView) getView(viewId)).setText(text);
		return this;
	}

	/**
	 * 设置图片
	 * 
	 * @param viewId
	 * @param url
	 * @return
	 */
	public ViewHolder setImage(int viewId, String url) {
		if (imageLoader == null) {
			imageLoader = ImageLoader.getInstance();
			options = Options.getListOptions();
		}
		imageLoader.displayImage(url, (ImageView) getView(viewId), options);
		return this;
	}

	/**
	 * 设置图片
	 * 
	 * @param viewId
	 * @param url
	 * @return
	 */
	public ViewHolder setImage(int viewId, int drawableId) {
		((ImageView) getView(viewId)).setImageResource(drawableId);
		return this;
	}
}
