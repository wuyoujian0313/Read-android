package com.read.mobile.module.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.NetAsyncTask;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.utils.Tips;
import com.ngc.corelib.views.xlist.XListView;
import com.ngc.corelib.views.xlist.XListView.IXListViewListener;
import com.read.mobile.R;
import com.read.mobile.beans.NoteItem;
import com.read.mobile.beans.NoteListRequest;
import com.read.mobile.beans.NoteListResult;
import com.read.mobile.constants.Contacts;
import com.read.mobile.env.BaseFragment;
import com.read.mobile.module.activity.NoteVoiceActivity;
import com.read.mobile.module.activity.NoteWriteActivity;
import com.read.mobile.module.adapter.NoteListWriteAdapter;
import com.read.mobile.views.segmentcontrol.SegmentControl;
import com.read.mobile.views.segmentcontrol.SegmentControl.OnSegmentControlClickListener;

public class CopyOfNoteFragment extends BaseFragment implements IXListViewListener {

	private SegmentControl control;
	private XListView charXListView;
	private XListView voiceXListView;

	private NoteListWriteAdapter writeAdapter;
	private List<NoteItem> writeList = new ArrayList<>();

	private boolean isVoice = false;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_note;
	}

	@Override
	protected void initViews() {
		showRight01Back(R.drawable.icon_edit, new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO 添加笔记
				if (isVoice) {
					// 声音
					Intent intent = new Intent(getActivity(), NoteVoiceActivity.class);
					startActivity(intent);
				} else {
					// 文字
					Intent intent = new Intent(getActivity(), NoteWriteActivity.class);
					startActivity(intent);
				}
			}
		});
		charXListView = (XListView) findViewById(R.id.note_wenzi_list);
		voiceXListView = (XListView) findViewById(R.id.note_voice_list);

		charXListView.setPullLoadEnable(false);
		charXListView.setPullRefreshEnable(false);
		charXListView.setXListViewListener(this);
		// adapter = new BookListAdapter(this, beans);
		// helpLV.setAdapter(adapter);
		charXListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			}
		});

		voiceXListView.setPullLoadEnable(false);
		voiceXListView.setPullRefreshEnable(false);
		voiceXListView.setXListViewListener(this);
		// adapter = new BookListAdapter(this, beans);
		// helpLV.setAdapter(adapter);
		voiceXListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			}
		});

		control = (SegmentControl) findViewById(R.id.segment_control);
		control.setOnSegmentControlClickListener(new OnSegmentControlClickListener() {
			@Override
			public void onSegmentControlClick(int index) {
				switch (index) {
				case 0:
					// TODO 文字
					isVoice = false;
					break;
				case 1:
					// TODO 语音
					isVoice = true;
					break;
				}
			}
		});

		writeAdapter = new NoteListWriteAdapter(getActivity(), writeList);
		charXListView.setAdapter(writeAdapter);
		noteList(true);
	}

	private int charPageNo = 1;
	private int voicePageNo = 1;

	private void noteList(final boolean hasDialog) {
		NoteListRequest request = new NoteListRequest();
		if (isVoice) {
			request.setType(Contacts.NOTE_TYPE_VOICE);
		} else {
			request.setType(Contacts.NOTE_TYPE_CHART);
		}
		NetAsyncTask asyncTask = new NetAsyncTask(NoteListResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				charXListView.stopLoadMore();
				voiceXListView.stopLoadMore();
				charXListView.stopRefresh();
				voiceXListView.stopRefresh();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					NoteListResult listResult = (NoteListResult) result;
					if (isVoice) {
						voicePageNo++;
					} else {
						charPageNo++;
						writeList.addAll(listResult.getData().getInfo());
						writeAdapter.notifyDataSetChanged();
					}
					if (listResult.getData().getHasNext().equals(Contacts.RESULT_HAS_NEXT_YES)) {
						if (isVoice) {
							voiceXListView.setPullLoadEnable(true);
						} else {
							charXListView.setPullLoadEnable(true);
						}
					} else {
						if (isVoice) {
							voiceXListView.setPullLoadEnable(false);
						} else {
							charXListView.setPullLoadEnable(false);
						}
					}
					// TODO
					// beans.addAll(listResult.getData().getInfo());
					// adapter.notifyDataSetChanged();
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
				voiceXListView.stopLoadMore();
				charXListView.stopRefresh();
				voiceXListView.stopRefresh();
				Tips.tipLong(getActivity(), e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_BOOK_NOTE_LIST);
	}

	@Override
	public void onRefresh() {
		if (isVoice) {
			voicePageNo = 1;
			noteList(false);
		} else {
			charPageNo = 1;
			noteList(false);
		}
	}

	@Override
	public void onLoadMore() {
		noteList(true);
	}

}
