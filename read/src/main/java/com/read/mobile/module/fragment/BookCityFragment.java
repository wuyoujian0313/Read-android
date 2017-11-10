package com.read.mobile.module.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;

import com.google.gson.Gson;
import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.NetAsyncTask;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.utils.Tips;
import com.ngc.corelib.views.xlist.XListView;
import com.ngc.corelib.views.xlist.XListView.IXListViewListener;
import com.read.mobile.R;
import com.read.mobile.beans.BookInfoRequest;
import com.read.mobile.beans.BookInfoResult;
import com.read.mobile.beans.BookItem;
import com.read.mobile.beans.BookRecListRequest;
import com.read.mobile.beans.BookRecListResult;
import com.read.mobile.constants.Contacts;
import com.read.mobile.env.BaseFragment;
import com.read.mobile.module.activity.BookDetailActivity;
import com.read.mobile.module.adapter.BookCityListAdapter;
import com.read.mobile.module.adapter.BookCityListAdapter.ItemClickListener;
import com.read.mobile.utils.SaveUtils;

public class BookCityFragment extends BaseFragment implements IXListViewListener, ItemClickListener {

	private XListView helpLV;
	private List<List<BookItem>> beans = new ArrayList<>();
	private BookCityListAdapter adapter;
	private List<BookItem> items = new ArrayList<>();

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_book_city_02;
	}

	@Override
	protected void initViews() {
		helpLV = (XListView) findViewById(R.id.book_city_list);
		helpLV.setPullLoadEnable(false);
		helpLV.setPullRefreshEnable(true);
		helpLV.setXListViewListener(this);

		adapter = new BookCityListAdapter(beans, getActivity(), this);
		helpLV.setAdapter(adapter);

		handleList(SaveUtils.getRecBookList());

		bookRecList(beans.size() > 0 ? false : true, true);

	}

	private void handleList(List<BookItem> bookItems) {
		items.addAll(bookItems);
		if (bookItems != null && bookItems.size() > 0) {
			int size = (bookItems.size() % 4 == 0) ? (bookItems.size() / 4) : (bookItems.size() / 4 + 1);
			for (int i = 0; i < size; i++) {
				List<BookItem> items = new ArrayList<>();
				for (int j = 0; j < 4; j++) {
					if (i * 4 + j < bookItems.size()) {
						items.add(bookItems.get(i * 4 + j));
					}
				}
				beans.add(items);
			}
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		bookRecList(false, true);
	}

	@Override
	public void onLoadMore() {
		bookRecList(false, false);
	}

	private void bookRecList(final boolean hasDialog, final boolean isRefresh) {
		BookRecListRequest request = new BookRecListRequest();
		if (isRefresh) {
			request.setOffset("0");
		} else {
			request.setOffset(String.valueOf(items.size()));
		}
		request.setLength(Contacts.REQUEST_PAGE_LENGTH);

		NetAsyncTask asyncTask = new NetAsyncTask(BookRecListResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				helpLV.stopLoadMore();
				helpLV.stopRefresh();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					BookRecListResult listResult = (BookRecListResult) result;
					if (listResult.getData().getHasNext().equals(Contacts.RESULT_HAS_NEXT_YES)) {
						helpLV.setPullLoadEnable(true);
					} else {
						helpLV.setPullLoadEnable(false);
					}
					if (isRefresh) {
						beans.clear();
						items.clear();
						String json = new Gson().toJson(listResult.getData().getInfo());
						SaveUtils.saveRecBookList(json);
					}
					handleList(listResult.getData().getInfo());
				} else {
					Tips.tipLong(getActivity(), result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				if (hasDialog)
					showProgressDialog("正在请求~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				helpLV.stopLoadMore();
				helpLV.stopRefresh();
				Tips.tipLong(getActivity(), e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_BOOK_REC_BOOKLIST);
	}

	@Override
	public void onItemClickListener(View arg0) {
		BookItem bookItem = (BookItem) arg0.getTag();
		if (bookItem != null) {
			bookInfo(bookItem.getIsbn());
		}
	}

	/**
	 * 图书搜索
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
}
