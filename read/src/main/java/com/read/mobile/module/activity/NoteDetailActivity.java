package com.read.mobile.module.activity;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngc.corelib.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.read.mobile.R;
import com.read.mobile.beans.NoteItem;
import com.read.mobile.env.BaseActivity;

public class NoteDetailActivity extends BaseActivity {

	private ImageView imgIV;
	private TextView nameTV;
	private TextView authorTV;
	private TextView pressTV;
	private TextView noteET;

	private NoteItem noteItem;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_note_detail;
	}

	@Override
	protected void initData() {
		super.initData();
		noteItem = (NoteItem) getIntent().getSerializableExtra("NoteItem");
	}

	@Override
	protected void initViwes() {
		showLeftBack(null);
		imageLoader = ImageLoader.getInstance();
		options = Options.getListOptions();
		imgIV = (ImageView) findViewById(R.id.note_write_img);
		nameTV = (TextView) findViewById(R.id.note_write_name);
		authorTV = (TextView) findViewById(R.id.note_write_author);
		pressTV = (TextView) findViewById(R.id.note_write_press);
		noteET = (TextView) findViewById(R.id.note_detail_content);
		setInfo(noteItem);
	}

	private void setInfo(NoteItem noteItem) {
		if (noteItem != null) {
			if (!TextUtils.isEmpty(noteItem.getPic())) {
				imageLoader.displayImage(noteItem.getPic(), imgIV, options);
			}
			if (!TextUtils.isEmpty(noteItem.getBookname())) {
				nameTV.setText(noteItem.getBookname());
			} else {
				nameTV.setText("");
			}
			if (!TextUtils.isEmpty(noteItem.getAuthor())) {
				authorTV.setText(noteItem.getAuthor());
			} else {
				authorTV.setText("");
			}
			if (!TextUtils.isEmpty(noteItem.getPress())) {
				pressTV.setText(noteItem.getPress());
			} else {
				pressTV.setText("");
			}
			if (!TextUtils.isEmpty(noteItem.getWord())) {
				noteET.setText(noteItem.getWord());
			} else {
				noteET.setText("");
			}

		}
	}

}
