package com.read.mobile.module.fragment;

import java.util.ArrayList;
import java.util.List;

import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.NetAsyncTask;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.utils.Logger;
import com.ngc.corelib.utils.Tips;
import com.ngc.corelib.views.xlist.XListView;
import com.ngc.corelib.views.xlist.XListView.IXListViewListener;
import com.read.mobile.R;
import com.read.mobile.beans.NoteItem;
import com.read.mobile.beans.NoteListRequest;
import com.read.mobile.beans.NoteListResult;
import com.read.mobile.constants.Contacts;
import com.read.mobile.module.activity.NoteDetailActivity;
import com.read.mobile.module.adapter.NoteListWriteAdapter;
import com.read.mobile.observer.ReadAgent;
import com.read.mobile.observer.ReadObserver.UpdateNote;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class NoteWordFragment extends Fragment implements IXListViewListener, UpdateNote {

	protected ProgressDialog mProgressDialog;
	private View view;

	private XListView charXListView;
	private NoteListWriteAdapter writeAdapter;
	private List<NoteItem> writeList = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_note_word, null);
		charXListView = (XListView) findViewById(R.id.note_wenzi_list);
		charXListView.setPullRefreshEnable(true);
		charXListView.setPullLoadEnable(false);
		charXListView.setXListViewListener(this);
		writeAdapter = new NoteListWriteAdapter(getActivity(), writeList);
		charXListView.setAdapter(writeAdapter);
		ReadAgent.geUshareAgent().addUpdateNoteObserver(this);
		noteList(true, true);
		charXListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
				Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
				intent.putExtra("NoteItem", (NoteItem) paramAdapterView.getAdapter().getItem(paramInt));
				startActivity(intent);
			}
		});
		return view;
	}

	private View findViewById(int id) {
		return view.findViewById(id);
	}

	protected synchronized void showProgressDialog(String message) {
		try {
			if (mProgressDialog == null) {
				mProgressDialog = new ProgressDialog(getActivity());
				mProgressDialog.setIndeterminate(true);
				mProgressDialog.setCanceledOnTouchOutside(false);
			}
			mProgressDialog.setMessage(message);
			mProgressDialog.show();
		} catch (Exception e) {
		}
	}

	protected synchronized void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	private void noteList(final boolean hasDialog, final boolean refresh) {
		NoteListRequest request = new NoteListRequest();
		request.setType(Contacts.NOTE_TYPE_CHART);
		request.setLength(Contacts.REQUEST_PAGE_LENGTH);
		if (refresh) {
			request.setOffset(String.valueOf(0));
		} else {
			request.setOffset(String.valueOf(writeList.size()));
		}
		NetAsyncTask asyncTask = new NetAsyncTask(NoteListResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				charXListView.stopLoadMore();
				charXListView.stopRefresh();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					NoteListResult listResult = (NoteListResult) result;
					if (refresh) {
						writeList.clear();
					}
					if (listResult.getData().getInfo().size() == 0) {
						charXListView.setPullLoadEnable(false);
					}
					writeList.addAll(listResult.getData().getInfo());
					writeAdapter.notifyDataSetChanged();
					if (listResult.getData().getHasNext().equals(Contacts.RESULT_HAS_NEXT_YES)) {
						charXListView.setPullLoadEnable(true);
					} else {
						charXListView.setPullLoadEnable(false);
					}
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
				charXListView.stopLoadMore();
				charXListView.stopRefresh();
				Tips.tipLong(getActivity(), e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_BOOK_NOTE_LIST);
	}

	@Override
	public void onRefresh() {
		noteList(false, true);
	}

	@Override
	public void onLoadMore() {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ReadAgent.geUshareAgent().removeUpdateNoteObserver(this);
	}

	@Override
	public void handle(boolean isVoice) {
		Logger.e("我是收到了吧 Word 的 -----------  " + isVoice);
		if (!isVoice) {
			noteList(false, true);
		}
	}
}
