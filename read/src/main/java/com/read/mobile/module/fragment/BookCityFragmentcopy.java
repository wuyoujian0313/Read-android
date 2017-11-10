package com.read.mobile.module.fragment;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.ngc.corelib.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.read.mobile.R;
import com.read.mobile.env.BaseFragment;

public class BookCityFragmentcopy extends BaseFragment {

	private LinearLayout contenterLL;
	private LinearLayout itemLL;
	private ImageView item01View;
	private ImageView item04View;

	private ViewGroup.LayoutParams itemLayoutParams;
	private ViewGroup.LayoutParams view01LayoutParams;
	private ViewGroup.LayoutParams view04LayoutParams;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_book_city;
	}

	@Override
	protected void initViews() {

		contenterLL = (LinearLayout) findViewById(R.id.home_contener_ll);
		itemLL = (LinearLayout) findViewById(R.id.home_item_ll);
		item01View = (ImageView) findViewById(R.id.home_item_01);
		item04View = (ImageView) findViewById(R.id.home_item_04);
		itemLayoutParams = itemLL.getLayoutParams();
		view01LayoutParams = item01View.getLayoutParams();
		view04LayoutParams = item04View.getLayoutParams();

		imageLoader = ImageLoader.getInstance();
		options = Options.getListOptions();

		addBooks();
		contenterLL.invalidate();

	}

	private void addBooks() {
		int totle = 50;
		int itemSize = (totle % 4 == 0) ? (totle / 4) : (totle / 4 + 1);
		for (int i = 0; i < itemSize; i++) {
			LinearLayout itemV = new LinearLayout(getActivity());
			itemV.setLayoutParams(itemLayoutParams);
			itemV.setPadding((int) getResources().getDimension(R.dimen.padding), 0,
					(int) getResources().getDimension(R.dimen.padding),
					(int) getResources().getDimension(R.dimen.padding_bottom));
			itemV.setBackgroundResource(R.drawable.bg_shucheng_);
			for (int j = 0; j < 3; j++) {
				ImageView imageView = new ImageView(getActivity());
				imageView.setLayoutParams(view01LayoutParams);
				imageView.setScaleType(ScaleType.CENTER_INSIDE);
				imageLoader.displayImage("http://img.taopic.com/uploads/allimg/140517/234768-14051F0012163.jpg",
						imageView, options);
				itemV.addView(imageView);
			}
			ImageView imageView02 = new ImageView(getActivity());
			imageView02.setLayoutParams(view04LayoutParams);
			imageView02.setScaleType(ScaleType.CENTER_INSIDE);
			imageLoader.displayImage("http://pic.58pic.com/58pic/11/47/09/33r58PIChIa.jpg", imageView02, options);
			itemV.addView(imageView02);
			contenterLL.addView(itemV);
		}

	}

}
