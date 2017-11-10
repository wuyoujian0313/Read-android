package com.read.mobile.module.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngc.corelib.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.read.mobile.R;
import com.read.mobile.beans.BookItem;

public class BookRangeListAdapter extends BaseAdapter {

	private List<BookItem> beans;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private LayoutInflater inflater;
	private Context context;

	public BookRangeListAdapter(List<BookItem> fenlei, Context context) {
		this.beans = fenlei;
		imageLoader = ImageLoader.getInstance();
		options = Options.getListOptions();
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		return beans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return beans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.item_book_fenlei, null);
			arg1.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, context.getResources()
					.getDimensionPixelSize(R.dimen.book_heigh)));// 重点行
			holder.bookImg = (ImageView) arg1.findViewById(R.id.book_etdw_01_img);
			holder.bookName = (TextView) arg1.findViewById(R.id.book_etdw_01_name);
			arg1.setTag(holder);
		}
		holder = (ViewHolder) arg1.getTag();
		imageLoader.displayImage(beans.get(arg0).getPic_big(), holder.bookImg, options);
		holder.bookName.setText(beans.get(arg0).getName());
		return arg1;
	}

	class ViewHolder {
		ImageView bookImg;
		TextView bookName;
	}

}
