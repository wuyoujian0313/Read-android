package com.read.mobile.module.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.NetAsyncTask;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.utils.Tips;
import com.ngc.corelib.views.xlist.XListView;
import com.ngc.corelib.views.xlist.XListView.IXListViewListener;
import com.read.mobile.R;
import com.read.mobile.beans.BookFavorListRequest;
import com.read.mobile.beans.BookFavorListResult;
import com.read.mobile.beans.BookInfoRequest;
import com.read.mobile.beans.BookInfoResult;
import com.read.mobile.beans.BookItem;
import com.read.mobile.constants.Contacts;
import com.read.mobile.env.BaseActivity;
import com.read.mobile.module.adapter.BookFaverListAdapter;

public class FavorListActivity extends BaseActivity implements IXListViewListener {

	private XListView helpLV;
	private List<BookItem> beans = new ArrayList<>();
	private BookFaverListAdapter adapter;

	private BookFavorListResult favorListResult;

	@Override
	protected void initData() {
		super.initData();
		favorListResult = (BookFavorListResult) getIntent().getSerializableExtra("BookFavorListResult");
		beans = favorListResult.getData().getInfo();
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_book_favor_list;
	}

	@Override
	protected void initViwes() {

		showLeftBack(null);

		helpLV = (XListView) findViewById(R.id.book_list);
		if (favorListResult.getData().getHasNext().equals(Contacts.RESULT_HAS_NEXT_YES)) {
			helpLV.setPullLoadEnable(true);
		} else {
			helpLV.setPullLoadEnable(false);
		}
		helpLV.setPullRefreshEnable(true);
		helpLV.setXListViewListener(this);

		adapter = new BookFaverListAdapter(this, beans);
		helpLV.setAdapter(adapter);

		helpLV.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				bookInfo(((BookItem) parent.getAdapter().getItem(position)).getIsbn());
			}
		});
	}

	@Override
	public void onRefresh() {
		bookList(true);
	}

	@Override
	public void onLoadMore() {
		bookList(false);
	}

	/**
	 * 图书列表
	 * 
	 * @param tid
	 */
	private void bookList(final boolean isRefresh) {
		BookFavorListRequest request = new BookFavorListRequest();
		if (isRefresh) {
			request.setOffset(String.valueOf(0));
		} else {
			request.setOffset(String.valueOf(beans.size()));
		}
		request.setLength(Contacts.REQUEST_PAGE_LENGTH);

		NetAsyncTask asyncTask = new NetAsyncTask(BookFavorListResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				helpLV.stopLoadMore();
				helpLV.stopRefresh();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					// TODO
					BookFavorListResult listResult = (BookFavorListResult) result;
					if (listResult.getData().getHasNext().equals(Contacts.RESULT_HAS_NEXT_YES)) {
						helpLV.setPullLoadEnable(true);
					} else {
						helpLV.setPullLoadEnable(false);
					}
					if (isRefresh) {
						beans.clear();
					}
					beans.addAll(listResult.getData().getInfo());
					adapter.notifyDataSetChanged();
				} else {
					Tips.tipLong(FavorListActivity.this, result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				showProgressDialog("正在请求~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				helpLV.stopLoadMore();
				helpLV.stopRefresh();
				Tips.tipLong(FavorListActivity.this, e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_BOOK_FAVORLIST);
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
					Intent intent = new Intent(FavorListActivity.this, BookDetailActivity.class);
					intent.putExtra("BookInfoResult", result);
					startActivity(intent);
				} else {
					Tips.tipLong(FavorListActivity.this, result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				showProgressDialog("正在请求~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				Tips.tipLong(FavorListActivity.this, e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_BOOK_INFO);
	}
}
