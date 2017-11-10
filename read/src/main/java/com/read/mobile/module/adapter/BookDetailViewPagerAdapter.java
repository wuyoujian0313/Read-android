package com.read.mobile.module.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ngc.corelib.utils.Logger;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.read.mobile.R;
import com.read.mobile.beans.BookInfoResult;
import com.read.mobile.beans.NoteItem;
import com.read.mobile.constants.Contacts;

public class BookDetailViewPagerAdapter extends PagerAdapter {

	private BookInfoResult infoResult;
	private List<NoteItem> items;
	private NoteListAdapter adapter;

	// ***********************简介******************************
	private View jianjieView;
	private TextView jianjieTV;
	private ImageView jianjieIV;

	// ***********************笔记***********************************
	private View bijiView;
	private ListView noteLV;
	// ***********************导读***********************************
	private View daoduView;
	private TextView daoduTV;
	private ImageView daoduIV;

	private Activity context;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public BookDetailViewPagerAdapter(Context context, ImageLoader imageLoader, DisplayImageOptions options) {
		this.context = (Activity) context;
		this.imageLoader = imageLoader;
		this.options = options;
	}

	@Override
	public Object instantiateItem(View container, int position) {
		View view = null;
		LayoutInflater inflater = context.getLayoutInflater();
		switch (position) {
		case 0:
			if (jianjieView == null) {
				jianjieView = inflater.inflate(R.layout.item_book_detail_viewpager_01, null);
				initJianjieView(jianjieView);
			}
			view = jianjieView;
			break;
		case 1:
			if (bijiView == null) {
				bijiView = inflater.inflate(R.layout.item_book_detail_viewpager_02, null);
				initNoteView(bijiView);
			}
			view = bijiView;
			break;
		case 2:
			if (daoduView == null) {
				daoduView = inflater.inflate(R.layout.item_book_detail_viewpager_03, null);
				initDaoduView(daoduView);
			}
			view = daoduView;
			break;
		}
		((ViewPager) container).addView(view, 0);
		return view;
	}

	/**
	 * 简介
	 * 
	 * @param view
	 */
	private void initJianjieView(View view) {
		jianjieTV = (TextView) view.findViewById(R.id.book_jianjie_tv);
		jianjieIV = (ImageView) view.findViewById(R.id.book_jianjie_iv);
		if (jianjieView != null && infoResult != null) {
			if (!TextUtils.isEmpty(infoResult.getData().getBrief())) {
				jianjieTV.setText(infoResult.getData().getBrief());
			} else {
				jianjieTV.setText("暂无简介");
			}
			if (TextUtils.isEmpty(infoResult.getData().getPic_jj())) {
				jianjieIV.setVisibility(View.GONE);
			} else {
				jianjieIV.setVisibility(View.VISIBLE);
				imageLoader.displayImage(infoResult.getData().getPic_jj(), jianjieIV, options);
			}

		}
	}

	/**
	 * 笔记
	 * 
	 * @param view
	 */
	private void initNoteView(View view) {
		noteLV = (ListView) view.findViewById(R.id.book_biji_lv);
		if (bijiView != null && items != null) {
			adapter = new NoteListAdapter(context, items);
			noteLV.setAdapter(adapter);
		}
	}

	// TODO
	public void setNoteData(List<NoteItem> item) {
		items = new ArrayList<>();
		for (int i = 0; i < item.size(); i++) {
			if (item.get(i).getType().equals(Contacts.NOTE_TYPE_CHART)) {
				items.add(item.get(i));
			}
		}
		adapter = new NoteListAdapter(context, items);
		if (noteLV != null) {
			noteLV.setAdapter(adapter);
		}
	}

	/**
	 * 导读
	 */
	private void initDaoduView(View view) {
		daoduTV = (TextView) view.findViewById(R.id.book_daodu_tv);
		daoduIV = (ImageView) view.findViewById(R.id.book_daodu_iv);
		if (daoduView != null && infoResult != null) {
			if (!TextUtils.isEmpty(infoResult.getData().getIntroduction())) {
				daoduTV.setText(infoResult.getData().getIntroduction());
			} else {
				daoduTV.setText("暂无导读");
			}
			if (TextUtils.isEmpty(infoResult.getData().getPic_intr())) {
				daoduIV.setVisibility(View.GONE);
			} else {
				daoduIV.setVisibility(View.VISIBLE);
				imageLoader.displayImage(infoResult.getData().getPic_intr(), daoduIV, options);
			}
		}
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	public void setViewData(BookInfoResult infoResult) {

		Logger.e("infoResult--->" + infoResult.getData().getBrief());

		this.infoResult = infoResult;
		if (jianjieView != null && infoResult != null) {
			if (!TextUtils.isEmpty(infoResult.getData().getBrief())) {
				jianjieTV.setText(infoResult.getData().getBrief());
			} else {
				jianjieTV.setText("暂无简介");
			}
		}
		if (bijiView != null && infoResult != null && infoResult.getData() != null) {
		}
		if (daoduView != null && infoResult != null) {
			if (!TextUtils.isEmpty(infoResult.getData().getIntroduction())) {
				daoduTV.setText(infoResult.getData().getIntroduction());
			} else {
				daoduTV.setText("暂无导读");
			}
		}
	}
}
