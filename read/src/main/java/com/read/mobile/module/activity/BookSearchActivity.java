package com.read.mobile.module.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.NetAsyncTask;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.utils.Tips;
import com.read.mobile.R;
import com.read.mobile.beans.BookItem;
import com.read.mobile.beans.BookSearchRequest;
import com.read.mobile.beans.BookSearchResult;
import com.read.mobile.constants.Contacts;
import com.read.mobile.env.BaseActivity;
import com.read.mobile.module.adapter.BookListAdapter;

public class BookSearchActivity extends BaseActivity {

	private EditText searchET;
	private View searchV;
	private View emptyView;
	private ListView bookListView;
	private View addBookView;
	private EditText bookName;
	private EditText bookMore;
	private EditText bookPress;
	private Button addBookBen;

	private List<BookItem> info = new ArrayList<>();
	private BookListAdapter adapter;
	private boolean isShow = false;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_book_search;
	}

	@Override
	protected void initViwes() {
		showLeftBack(null);
		showRightTV("新添", new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (isShow) {
					addBookView.setVisibility(View.GONE);
				} else {
					addBookView.setVisibility(View.VISIBLE);
				}
				isShow = !isShow;
			}
		});

		searchET = (EditText) findViewById(R.id.search_et);
		searchET.setHint("请输入书目名称");
		searchV = findViewById(R.id.search_ib);
		searchV.setOnClickListener(this);

		emptyView = findViewById(R.id.book_add_empty);
		bookListView = (ListView) findViewById(R.id.book_add_list);
		addBookView = findViewById(R.id.book_add_view);

		bookName = (EditText) findViewById(R.id.book_add_name_tv);
		bookMore = (EditText) findViewById(R.id.book_add_more_tv);
		bookPress = (EditText) findViewById(R.id.book_add_press_tv);
		addBookBen = (Button) findViewById(R.id.book_add_btn);
		addBookBen.setOnClickListener(this);

		adapter = new BookListAdapter(this, info);
		bookListView.setAdapter(adapter);
		bookListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				// TODO 图书详情
				Intent data = new Intent();
				data.putExtra("BookItem", info.get(position));
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		// 搜索
		case R.id.search_ib:
			String keyStr = searchET.getText().toString();
			if (TextUtils.isEmpty(keyStr)) {
				Tips.tipLong(this, "请输入搜索关键字~");
				return;
			}
			bookSearch(keyStr, Contacts.STYPE_KEYWORD);
			break;
		// 添加
		case R.id.book_add_btn: {
			addBook();
		}
			break;
		}
	}

	private void addBook() {
		String bookNameStr = bookName.getText().toString();
		String bookMoreStr = bookMore.getText().toString();
		String bookPressStr = bookPress.getText().toString();
		if (TextUtils.isEmpty(bookNameStr)) {
			Tips.tipLong(this, "请输入图书名称~");
			return;
		}
		if (TextUtils.isEmpty(bookMoreStr)) {
			Tips.tipLong(this, "请输入作者~");
			return;
		}
		if (TextUtils.isEmpty(bookPressStr)) {
			Tips.tipLong(this, "请输入出版社~");
			return;
		}
		BookItem bookItem = new BookItem();
		bookItem.setAuthor(bookMoreStr);
		bookItem.setName(bookNameStr);
		bookItem.setPress(bookPressStr);
		bookItem.setAdd(true);
		Intent data = new Intent();
		data.putExtra("BookItem", bookItem);
		setResult(RESULT_OK, data);
		finish();
	}

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
					BookSearchResult listResult = (BookSearchResult) result;
					if (listResult.getData().getInfo() == null || listResult.getData().getInfo().size() == 0) {
						emptyView.setVisibility(View.VISIBLE);
						bookListView.setVisibility(View.GONE);
						isShow = true;
					} else {
						info.addAll(listResult.getData().getInfo());
						emptyView.setVisibility(View.GONE);
						bookListView.setVisibility(View.VISIBLE);
						adapter.notifyDataSetChanged();
						isShow = false;
					}
				} else {
					Tips.tipLong(BookSearchActivity.this, result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				showProgressDialog("正在请求~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				Tips.tipLong(BookSearchActivity.this, e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_BOOK_SEARCH);
	}

}
