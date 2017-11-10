package com.read.mobile.module.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.NetAsyncTask;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.utils.Tips;
import com.ngc.corelib.views.FlowLayout;
import com.read.mobile.R;
import com.read.mobile.beans.BookInfoRequest;
import com.read.mobile.beans.BookInfoResult;
import com.read.mobile.beans.BookListRequest;
import com.read.mobile.beans.BookListResult;
import com.read.mobile.beans.BookSearchRequest;
import com.read.mobile.beans.BookSearchResult;
import com.read.mobile.constants.Contacts;
import com.read.mobile.env.BaseFragment;
import com.read.mobile.module.activity.BookDetailActivity;
import com.read.mobile.module.activity.BookListActivity;

public class BooksFragmentcopy extends BaseFragment {

	private EditText searchET;
	private View searchView;

	private LayoutInflater mInflater;

	private FlowLayout nlFL;
	private FlowLayout lbFL;

	private String[] nlStr = { "0-3岁", "3-6岁", "6-9岁", "9-12岁", "12岁以上" };
	private String[] lbStr = { "科学", "文化", "艺术", "常识", "益智", "品德", "故事", "教育" };

	// ***************************儿童读物******************************
	private View[] etdwViews = new View[5];
	private ImageView[] etdwIVs = new ImageView[5];
	private TextView[] etdwTVs = new TextView[5];
	private int[] etdwViewIds = { R.id.book_etdw_01, R.id.book_etdw_02, R.id.book_etdw_03, R.id.book_etdw_04,
			R.id.book_etdw_05 };
	private int[] etdwIVIds = { R.id.book_etdw_01_img, R.id.book_etdw_02_img, R.id.book_etdw_03_img,
			R.id.book_etdw_04_img, R.id.book_etdw_05_img };
	private int[] etdwTVIds = { R.id.book_etdw_01_name, R.id.book_etdw_02_name, R.id.book_etdw_03_name,
			R.id.book_etdw_04_name, R.id.book_etdw_05_name };

	// ***************************少儿精选******************************
	private View[] sejxViews = new View[5];
	private ImageView[] sejxIVs = new ImageView[5];
	private TextView[] sejxTVs = new TextView[5];
	private int[] sejxViewIds = { R.id.book_sejx_01, R.id.book_sejx_02, R.id.book_sejx_03, R.id.book_sejx_04,
			R.id.book_sejx_05 };
	private int[] sejxIVIds = { R.id.book_sejx_01_img, R.id.book_sejx_02_img, R.id.book_sejx_03_img,
			R.id.book_sejx_04_img, R.id.book_sejx_05_img };
	private int[] sejxTVIds = { R.id.book_sejx_01_name, R.id.book_sejx_02_name, R.id.book_sejx_03_name,
			R.id.book_sejx_04_name, R.id.book_sejx_05_name };

	// ***************************青年书籍******************************
	private View[] qnsjViews = new View[5];
	private ImageView[] qnsjIVs = new ImageView[5];
	private TextView[] qnsjTVs = new TextView[5];
	private int[] qnsjViewIds = { R.id.book_qnsj_01, R.id.book_qnsj_02, R.id.book_qnsj_03, R.id.book_qnsj_04,
			R.id.book_qnsj_05 };
	private int[] qnsjIVIds = { R.id.book_qnsj_01_img, R.id.book_qnsj_02_img, R.id.book_qnsj_03_img,
			R.id.book_qnsj_04_img, R.id.book_qnsj_05_img };
	private int[] qnsjTVIds = { R.id.book_qnsj_01_name, R.id.book_qnsj_02_name, R.id.book_qnsj_03_name,
			R.id.book_qnsj_04_name, R.id.book_qnsj_05_name };

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_books;
	}

	@Override
	protected void initViews() {
		searchET = (EditText) findViewById(R.id.search_et);
		searchView = findViewById(R.id.search_ib);
		searchView.setOnClickListener(this);

		nlFL = (FlowLayout) findViewById(R.id.nl_fl);
		lbFL = (FlowLayout) findViewById(R.id.lb_fl);

		initNLList();
		initLBList();

		for (int i = 0; i < etdwViews.length; i++) {
			etdwViews[i] = findViewById(etdwViewIds[i]);
			etdwIVs[i] = (ImageView) findViewById(etdwIVIds[i]);
			etdwTVs[i] = (TextView) findViewById(etdwTVIds[i]);
		}
		for (int i = 0; i < sejxViews.length; i++) {
			sejxViews[i] = findViewById(sejxViewIds[i]);
			sejxIVs[i] = (ImageView) findViewById(sejxIVIds[i]);
			sejxTVs[i] = (TextView) findViewById(sejxTVIds[i]);
		}
		for (int i = 0; i < qnsjViews.length; i++) {
			qnsjViews[i] = findViewById(qnsjViewIds[i]);
			qnsjIVs[i] = (ImageView) findViewById(qnsjIVIds[i]);
			qnsjTVs[i] = (TextView) findViewById(qnsjTVIds[i]);
		}

		findViewById(R.id.book_etdw_more).setOnClickListener(this);
		findViewById(R.id.book_sejx_more).setOnClickListener(this);
		findViewById(R.id.book_qnsj_more).setOnClickListener(this);

		bookList("");
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.search_ib: {
			String keyStr = searchET.getText().toString();
			if (TextUtils.isEmpty(keyStr)) {
				Tips.tipLong(getActivity(), "请输入搜索关键字~");
				return;
			}
			bookSearch(keyStr, Contacts.STYPE_KEYWORD);
			break;
		}
		case R.id.book_etdw_more:
			bookSearch(Contacts.SEARCH_KEYSTR_ETDW, Contacts.STYPE_RANGE);
			break;
		case R.id.book_sejx_more:
			bookSearch(Contacts.SEARCH_KEYSTR_SEJX, Contacts.STYPE_RANGE);
			break;
		case R.id.book_qnsj_more:
			bookSearch(Contacts.SEARCH_KEYSTR_QNSJ, Contacts.STYPE_RANGE);
			break;
		}
	}

	private void initNLList() {
		if (mInflater == null) {
			mInflater = LayoutInflater.from(getActivity());
		}
		for (int i = 0; i < nlStr.length; i++) {
			TextView tv = (TextView) mInflater.inflate(R.layout.item_flowlayout, nlFL, false);
			tv.setText(nlStr[i]);
			tv.setOnClickListener(clickListener);
			nlFL.addView(tv);
		}
	}

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {

		}
	};

	private void initLBList() {
		if (mInflater == null) {
			mInflater = LayoutInflater.from(getActivity());
		}
		for (int i = 0; i < lbStr.length; i++) {
			TextView tv = (TextView) mInflater.inflate(R.layout.item_flowlayout, lbFL, false);
			tv.setText(lbStr[i]);
			tv.setOnClickListener(lbClickListener);
			lbFL.addView(tv);
		}
	}

	private OnClickListener lbClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {

		}
	};

	/**
	 * 图书搜索
	 * 
	 * @param tid
	 */
	private void bookSearch(final String keyStr, final String stype) {
		BookSearchRequest request = new BookSearchRequest();
		request.setOffset(String.valueOf(1));
		request.setLength(Contacts.REQUEST_PAGE_LENGTH);
		request.setKeyStr(keyStr);
		request.setStype(stype);

		NetAsyncTask asyncTask = new NetAsyncTask(BookSearchResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					Intent intent = new Intent(getActivity(), BookListActivity.class);
					intent.putExtra("BookSearchResult", result);
					intent.putExtra("keyStr", keyStr);
					intent.putExtra("stype", stype);
					startActivity(intent);
				} else {
					Tips.tipLong(getActivity(), result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				showProgressDialog("正在请求~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				Tips.tipLong(getActivity(), e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_BOOK_SEARCH);
	}

	/**
	 * 图书详情
	 * 
	 * @param tid
	 */
	private void bookInfo(String isbn) {
		BookInfoRequest request = new BookInfoRequest();
		request.setIsbn(isbn);

		NetAsyncTask asyncTask = new NetAsyncTask(BookInfoResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					Intent intent = new Intent(getActivity(), BookDetailActivity.class);
					intent.putExtra("BookInfoResult", result);
					startActivity(intent);
				} else {
					Tips.tipLong(getActivity(), result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				showProgressDialog("正在请求~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				Tips.tipLong(getActivity(), e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_BOOK_INFO);
	}

	private void bookList(String type) {
		BookListRequest request = new BookListRequest();
		request.setType(type);

		NetAsyncTask asyncTask = new NetAsyncTask(BookListResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
				} else {
					Tips.tipLong(getActivity(), result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				showProgressDialog("正在请求~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				Tips.tipLong(getActivity(), e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_BOOK_BOOK_LIST);

	}
}
