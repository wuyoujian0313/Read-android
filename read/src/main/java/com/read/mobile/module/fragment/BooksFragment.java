package com.read.mobile.module.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.NetAsyncTask;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.utils.Logger;
import com.ngc.corelib.utils.Tips;
import com.ngc.corelib.views.FlowLayout;
import com.read.mobile.R;
import com.read.mobile.beans.BookInfoRequest;
import com.read.mobile.beans.BookInfoResult;
import com.read.mobile.beans.BookItem;
import com.read.mobile.beans.BookSearchRequest;
import com.read.mobile.beans.BookSearchResult;
import com.read.mobile.beans.MultySearchRequest;
import com.read.mobile.beans.MultySearchResult;
import com.read.mobile.constants.Contacts;
import com.read.mobile.env.BaseFragment;
import com.read.mobile.module.activity.BookDetailActivity;
import com.read.mobile.module.activity.BookListActivity;
import com.read.mobile.module.activity.MulBookListActivity;
import com.read.mobile.module.adapter.BookRangeListAdapter;
import com.read.mobile.utils.SaveUtils;
import com.read.mobile.views.GridViewWithHeaderAndFooter;

public class BooksFragment extends BaseFragment {

	private EditText searchET;
	private View searchView;

	private View more;

	private LayoutInflater mInflater;

	private FlowLayout nlFL;
	private FlowLayout lbFL;

	private String searchType = "";
	private String searchAge = "";

	private GridViewWithHeaderAndFooter listView;
	private BookRangeListAdapter adapter;
	private List<BookItem> beans = new ArrayList<>();

	private View headView;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_books;
	}

	@Override
	protected void initViews() {
		if (headView == null) {
			initHeadView();
		}
		listView = (GridViewWithHeaderAndFooter) findViewById(R.id.book_fenlei_list);
		listView.addHeaderView(headView);

		adapter = new BookRangeListAdapter(beans, getActivity());

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				bookInfo(beans.get(arg2).getIsbn());
			}
		});
		setData(SaveUtils.getBookList());
		multySearch(searchAge, searchType);

	}

	private void initHeadView() {
		headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_gridview_head, null);
		searchET = (EditText) headView.findViewById(R.id.search_et);
		searchView = headView.findViewById(R.id.search_ib);
		more = headView.findViewById(R.id.more);
		searchView.setOnClickListener(this);
		more.setOnClickListener(this);

		nlFL = (FlowLayout) headView.findViewById(R.id.nl_fl);
		lbFL = (FlowLayout) headView.findViewById(R.id.lb_fl);
	}

	private void setData(MultySearchResult bookListResult) {
		if (bookListResult != null) {
			if (nlFL.getChildCount() == 0) {
				initNLList(bookListResult.getData().getAge());
			}
			if (lbFL.getChildCount() == 0) {
				initLBList(bookListResult.getData().getFenlei());
			}
			beans.clear();
			adapter.notifyDataSetChanged();
			Logger.e("bookListResult.getData().getBooks()===========" + bookListResult.getData().getBooks().size());
			beans.addAll(bookListResult.getData().getBooks());
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
		case R.id.more: {
			multySearchMore(searchAge, searchType);
			break;
		}
		}
	}

	private void initNLList(List<String> ages) {
		if (mInflater == null) {
			mInflater = LayoutInflater.from(getActivity());
		}
		for (int i = 0; i < ages.size(); i++) {
			TextView tv = (TextView) mInflater.inflate(R.layout.item_flowlayout, nlFL, false);
			tv.setText(ages.get(i));
			tv.setOnClickListener(clickListener);
			if (!TextUtils.isEmpty(tv.getText())) {
				nlFL.addView(tv);
			}
		}
	}

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			for (int i = 0; i < nlFL.getChildCount(); i++) {
				if (arg0 == nlFL.getChildAt(i)) {
					nlFL.getChildAt(i).setEnabled(false);
					searchAge = ((TextView) nlFL.getChildAt(i)).getText().toString();
					searchAge = searchAge.endsWith("岁") ? searchAge.replace("岁", "") : searchAge;
					Logger.e("searchAge--------->" + searchAge);
					multySearch(searchAge, searchType);
				} else {
					nlFL.getChildAt(i).setEnabled(true);
				}
			}
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
			if (!TextUtils.isEmpty(tv.getText()))
				lbFL.addView(tv);
		}
	}

	private OnClickListener lbClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			for (int i = 0; i < lbFL.getChildCount(); i++) {
				if (arg0 == lbFL.getChildAt(i)) {
					lbFL.getChildAt(i).setEnabled(false);
					searchType = ((TextView) lbFL.getChildAt(i)).getText().toString();
					multySearch(searchAge, searchType);
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
	 * 图书搜索
	 * 
	 * @param tid
	 */
	private void multySearch(final String age, final String stype) {
		MultySearchRequest request = new MultySearchRequest();
		request.setOffset(String.valueOf(0));
		request.setLength(String.valueOf(15));
		request.setAge(age);
		request.setType(stype);

		NetAsyncTask asyncTask = new NetAsyncTask(MultySearchResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					if (TextUtils.isEmpty(age) && TextUtils.isEmpty(stype)) {
						SaveUtils.saveBookList(new Gson().toJson(result));
					}
					setData((MultySearchResult) result);
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
		asyncTask.execute(Contacts.URL_BOOK_MULTYSEARCH);
	}

	private void multySearchMore(final String age, final String stype) {
		MultySearchRequest request = new MultySearchRequest();
		request.setOffset(String.valueOf(15));
		request.setLength(String.valueOf(15));
		request.setAge(age);
		request.setType(stype);

		NetAsyncTask asyncTask = new NetAsyncTask(MultySearchResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					Intent intent = new Intent(getActivity(), MulBookListActivity.class);
					intent.putExtra("MultySearchResult", result);
					intent.putExtra("age", age);
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
		asyncTask.execute(Contacts.URL_BOOK_MULTYSEARCH);
	}
}
