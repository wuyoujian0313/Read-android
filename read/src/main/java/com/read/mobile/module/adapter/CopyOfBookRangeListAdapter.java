package com.read.mobile.module.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngc.corelib.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.read.mobile.R;
import com.read.mobile.beans.BookFenleiBean;

public class CopyOfBookRangeListAdapter extends BaseAdapter {

	private List<BookFenleiBean> beans;
	private Context context;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private LayoutInflater inflater;

	private BookClickListener clickListener;

	public CopyOfBookRangeListAdapter(List<BookFenleiBean> fenlei, Context context, BookClickListener clickListener) {
		this.beans = fenlei;
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
			arg1 = inflater.inflate(R.layout.item_book_fenlei, null);
			int[] etdwViewIds = { R.id.book_etdw_01, R.id.book_etdw_02, R.id.book_etdw_03, R.id.book_etdw_04,
					R.id.book_etdw_05 };
			int[] etdwIVIds = { R.id.book_etdw_01_img, R.id.book_etdw_02_img, R.id.book_etdw_03_img,
					R.id.book_etdw_04_img, R.id.book_etdw_05_img };
			int[] etdwTVIds = { R.id.book_etdw_01_name, R.id.book_etdw_02_name, R.id.book_etdw_03_name,
					R.id.book_etdw_04_name, R.id.book_etdw_05_name };
			for (int i = 0; i < holder.etdwViews.length; i++) {
				holder.etdwViews[i] = arg1.findViewById(etdwViewIds[i]);
				holder.etdwIVs[i] = (ImageView) arg1.findViewById(etdwIVIds[i]);
				holder.etdwTVs[i] = (TextView) arg1.findViewById(etdwTVIds[i]);
			}
			holder.moreView = arg1.findViewById(R.id.book_etdw_more);
			holder.titleTV = (TextView) arg1.findViewById(R.id.book_title);
			arg1.setTag(holder);
		}
		holder = (ViewHolder) arg1.getTag();
		BookFenleiBean bookItems = beans.get(arg0);
		holder.titleTV.setText(bookItems.getRange());
		holder.moreView.setTag(bookItems.getRange());
		for (int i = bookItems.getBooks().size(); i < holder.etdwViews.length; i++) {
			holder.etdwViews[i].setVisibility(View.INVISIBLE);
		}
		for (int i = 0; i < bookItems.getBooks().size(); i++) {
			holder.etdwViews[i].setVisibility(View.VISIBLE);
			imageLoader.displayImage(bookItems.getBooks().get(i).getPic_big(), holder.etdwIVs[i], options);
			holder.etdwTVs[i].setText(bookItems.getBooks().get(i).getBook_name());
			holder.etdwViews[i].setTag(bookItems.getBooks().get(i));
		}
		for (int i = 0; i < holder.etdwViews.length; i++) {
			holder.etdwViews[i].setOnClickListener(listener);
		}
		holder.moreView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (clickListener != null) {
					clickListener.onMoreClickListener(arg0);
				}
			}
		});
		return arg1;
	}

	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if (clickListener != null) {
				clickListener.onBookClickListener(arg0);
			}
		}
	};

	class ViewHolder {
		View[] etdwViews = new View[5];
		ImageView[] etdwIVs = new ImageView[5];
		TextView[] etdwTVs = new TextView[5];
		View moreView;
		TextView titleTV;
	}

	public static interface BookClickListener {
		public void onBookClickListener(View arg0);

		public void onMoreClickListener(View arg0);
	}

}
