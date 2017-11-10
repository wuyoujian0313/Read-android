package com.read.mobile.module.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.NetAsyncTask;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.utils.Tips;
import com.ngc.corelib.views.FlowLayout;
import com.read.mobile.R;
import com.read.mobile.beans.BookBean;
import com.read.mobile.beans.BookFenleiBean;
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
import com.read.mobile.module.adapter.CopyOfBookRangeListAdapter;
import com.read.mobile.module.adapter.CopyOfBookRangeListAdapter.BookClickListener;
import com.read.mobile.utils.SaveUtils;
import com.read.mobile.views.MyGridView;

public class Copy_2_of_BooksFragment extends BaseFragment implements BookClickListener {

	private EditText searchET;
	private View searchView;

	private LayoutInflater mInflater;

	private FlowLayout nlFL;
	private FlowLayout lbFL;

	private String[] nlStr = { "0-3岁", "3-6岁", "6-9岁", "9-12岁", "12岁以上" };
	private String[] lbStr = { "科学", "文化", "艺术", "常识", "益智", "品德", "故事", "教育" };

	private MyGridView listView;
	private CopyOfBookRangeListAdapter adapter;
	private List<BookFenleiBean> beans = new ArrayList<>();

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

		listView = (MyGridView) findViewById(R.id.book_fenlei_list);
		adapter = new CopyOfBookRangeListAdapter(beans, getActivity(), this);
		listView.setAdapter(adapter);

		// setData(SaveUtils.getBookList());

		bookList("");
	}

	private void setData(BookListResult bookListResult) {
		if (bookListResult != null) {
			initNLList();

			if (lbFL.getChildCount() == 0) {
				initLBList(bookListResult.getData().getLeibie());
			}
			beans.clear();
			beans.addAll(bookListResult.getData().getFenlei());
			adapter.notifyDataSetChanged();
		}
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

	private void initLBList(List<String> types) {
		if (mInflater == null) {
			mInflater = LayoutInflater.from(getActivity());
		}
		for (int i = 0; i < types.size(); i++) {
			TextView tv = (TextView) mInflater.inflate(R.layout.item_flowlayout, lbFL, false);
			tv.setText(types.get(i));
			tv.setOnClickListener(lbClickListener);
			lbFL.addView(tv);
		}
	}

	private OnClickListener lbClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			for (int i = 0; i < lbFL.getChildCount(); i++) {
				if (arg0 == lbFL.getChildAt(i)) {
					lbFL.getChildAt(i).setEnabled(false);
					bookList(((TextView) lbFL.getChildAt(i)).getText().toString());
				} else {
					lbFL.getChildAt(i).setEnabled(true);
				}
			}
		}
	};

	/**
	 * 图书搜索
	 * 
	 * @param tid
	 */
	private void bookSearch(final String keyStr, final String stype) {
		BookSearchRequest request = new BookSearchRequest();
		request.setOffset(String.valueOf(0));
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

	/**
	 * 
	 * @param type
	 */
	private void bookList(final String type) {
		BookListRequest request = new BookListRequest();
		request.setType(type);

		NetAsyncTask asyncTask = new NetAsyncTask(BookListResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					if (TextUtils.isEmpty(type)) {
						SaveUtils.saveBookList(new Gson().toJson(result));
					}
					setData((BookListResult) result);
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

	@Override
	public void onBookClickListener(View arg0) {
		bookInfo(((BookBean) arg0.getTag()).getIsbn());
	}

	@Override
	public void onMoreClickListener(View arg0) {
		bookSearch(arg0.getTag().toString(), Contacts.STYPE_RANGE);
	}
}
