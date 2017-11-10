package com.read.mobile.module.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngc.corelib.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.read.mobile.R;
import com.read.mobile.beans.BookItem;

public class BookNoteListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<BookItem> beans;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public BookNoteListAdapter(Context context, List<BookItem> beans) {
		inflater = LayoutInflater.from(context);
		this.beans = beans;
		imageLoader = ImageLoader.getInstance();
		options = Options.getListOptions();

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
			arg1 = inflater.inflate(R.layout.item_book_list, null);
			holder.imgIV = (ImageView) arg1.findViewById(R.id.item_book_list_img);
			holder.heartIV = (ImageView) arg1.findViewById(R.id.item_book_list_heart);

			holder.nameTV = (TextView) arg1.findViewById(R.id.item_book_list_name);
			holder.authorTV = (TextView) arg1.findViewById(R.id.item_book_list_author);
			holder.publicTV = (TextView) arg1.findViewById(R.id.item_book_list_public);
			holder.priceYuanTV = (TextView) arg1.findViewById(R.id.item_book_list_price_yuan);
			holder.priceFenTV = (TextView) arg1.findViewById(R.id.item_book_list_price_fen);

			arg1.setTag(holder);
		}
		holder = (ViewHolder) arg1.getTag();
		BookItem bean = beans.get(arg0);
		if (!TextUtils.isEmpty(bean.getPic())) {
			imageLoader.displayImage(bean.getPic(), holder.imgIV, options);
		}
		holder.nameTV.setText(bean.getBookname());
		holder.authorTV.setText("作者：" + bean.getAuthor());
		holder.publicTV.setText("出版社：" + bean.getPress());

		return arg1;
	}

	class ViewHolder {
		ImageView imgIV;
		TextView nameTV;
		TextView authorTV;
		TextView publicTV;
		TextView priceYuanTV;
		TextView priceFenTV;
		ImageView heartIV;
	}

}
