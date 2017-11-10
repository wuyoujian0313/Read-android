package com.read.mobile.module.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ngc.corelib.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.read.mobile.R;
import com.read.mobile.beans.BookItem;

public class BookCityListAdapter extends BaseAdapter {

	private List<List<BookItem>> beans;
	private Context context;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private LayoutInflater inflater;

	private ItemClickListener clickListener;

	public BookCityListAdapter(List<List<BookItem>> beans, Context context, ItemClickListener clickListener) {
		this.beans = beans;
		this.context = context;
		this.clickListener = clickListener;
		imageLoader = ImageLoader.getInstance();
		options = Options.getListOptions();
		inflater = LayoutInflater.from(context);
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
			arg1 = inflater.inflate(R.layout.item_book_city, null);
			holder.bookIV01 = (ImageView) arg1.findViewById(R.id.home_item_01);
			holder.bookIV02 = (ImageView) arg1.findViewById(R.id.home_item_02);
			holder.bookIV03 = (ImageView) arg1.findViewById(R.id.home_item_03);
			holder.bookIV04 = (ImageView) arg1.findViewById(R.id.home_item_04);
			arg1.setTag(holder);
		}
		holder = (ViewHolder) arg1.getTag();
		List<BookItem> bookItems = beans.get(arg0);
		if (bookItems.size() == 1) {
			holder.bookIV01.setVisibility(View.VISIBLE);
			holder.bookIV01.setTag(bookItems.get(0));
			imageLoader.displayImage(bookItems.get(0).getPic_big(), holder.bookIV01, options);
			holder.bookIV02.setVisibility(View.INVISIBLE);
			holder.bookIV03.setVisibility(View.INVISIBLE);
			holder.bookIV04.setVisibility(View.INVISIBLE);
		} else if (bookItems.size() == 2) {
			holder.bookIV01.setVisibility(View.VISIBLE);
			holder.bookIV02.setVisibility(View.VISIBLE);
			holder.bookIV01.setTag(bookItems.get(0));
			holder.bookIV02.setTag(bookItems.get(1));
			imageLoader.displayImage(bookItems.get(0).getPic_big(), holder.bookIV01, options);
			imageLoader.displayImage(bookItems.get(1).getPic_big(), holder.bookIV02, options);
			holder.bookIV03.setVisibility(View.INVISIBLE);
			holder.bookIV04.setVisibility(View.INVISIBLE);
		} else if (bookItems.size() == 3) {
			holder.bookIV01.setVisibility(View.VISIBLE);
			holder.bookIV02.setVisibility(View.VISIBLE);
			holder.bookIV03.setVisibility(View.VISIBLE);
			holder.bookIV01.setTag(bookItems.get(0));
			holder.bookIV02.setTag(bookItems.get(1));
			holder.bookIV03.setTag(bookItems.get(2));
			imageLoader.displayImage(bookItems.get(0).getPic_big(), holder.bookIV01, options);
			imageLoader.displayImage(bookItems.get(1).getPic_big(), holder.bookIV02, options);
			imageLoader.displayImage(bookItems.get(2).getPic_big(), holder.bookIV03, options);
			holder.bookIV04.setVisibility(View.INVISIBLE);
		} else if (bookItems.size() == 4) {
			holder.bookIV01.setVisibility(View.VISIBLE);
			holder.bookIV02.setVisibility(View.VISIBLE);
			holder.bookIV03.setVisibility(View.VISIBLE);
			holder.bookIV04.setVisibility(View.VISIBLE);
			holder.bookIV01.setTag(bookItems.get(0));
			holder.bookIV02.setTag(bookItems.get(1));
			holder.bookIV03.setTag(bookItems.get(2));
			holder.bookIV04.setTag(bookItems.get(3));
			imageLoader.displayImage(bookItems.get(0).getPic_big(), holder.bookIV01, options);
			imageLoader.displayImage(bookItems.get(1).getPic_big(), holder.bookIV02, options);
			imageLoader.displayImage(bookItems.get(2).getPic_big(), holder.bookIV03, options);
			imageLoader.displayImage(bookItems.get(3).getPic_big(), holder.bookIV04, options);
		}
		holder.bookIV01.setOnClickListener(listener);
		holder.bookIV02.setOnClickListener(listener);
		holder.bookIV03.setOnClickListener(listener);
		holder.bookIV04.setOnClickListener(listener);

		return arg1;
	}

	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if (clickListener != null) {
				clickListener.onItemClickListener(arg0);
			}
		}
	};

	class ViewHolder {
		ImageView bookIV01, bookIV02, bookIV03, bookIV04;
	}

	public static interface ItemClickListener {
		public void onItemClickListener(View arg0);
	}

}
