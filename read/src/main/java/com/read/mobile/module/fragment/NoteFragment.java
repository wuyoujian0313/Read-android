package com.read.mobile.module.fragment;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import com.ngc.corelib.utils.Logger;
import com.read.mobile.R;
import com.read.mobile.env.BaseFragment;
import com.read.mobile.module.activity.NoteVoiceActivity;
import com.read.mobile.module.activity.NoteWriteActivity;
import com.read.mobile.views.segmentcontrol.SegmentControl;
import com.read.mobile.views.segmentcontrol.SegmentControl.OnSegmentControlClickListener;

@SuppressLint("NewApi")
public class NoteFragment extends BaseFragment {

	private SegmentControl control;
	private FragmentManager fm;

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
		control = (SegmentControl) findViewById(R.id.segment_control);
		initFragment();
		control.setOnSegmentControlClickListener(new OnSegmentControlClickListener() {
			@Override
			public void onSegmentControlClick(int index) {
				switch (index) {
				case 0:
					// TODO 文字
					isVoice = false;
					showWordFragment();
					break;
				case 1:
					// TODO 语音
					isVoice = true;
					showVoiceFragment();
					break;
				}
			}
		});
		showWordFragment();
	}

	private NoteWordFragment wordFragment;
	private NoteVoiceFragment voiceFragment;

	private void showWordFragment() {
		FragmentTransaction transaction = fm.beginTransaction();
		hideFragments(transaction);
		if (wordFragment == null) {
			wordFragment = new NoteWordFragment();
			transaction.add(R.id.fl_content, wordFragment);
		} else {
			transaction.show(wordFragment);
		}
		transaction.commit();
	}

	private void showVoiceFragment() {
		FragmentTransaction transaction = fm.beginTransaction();
		hideFragments(transaction);
		if (voiceFragment == null) {
			voiceFragment = new NoteVoiceFragment();
			transaction.add(R.id.fl_content, voiceFragment);
		} else {
			transaction.show(voiceFragment);
		}
		transaction.commit();
	}

	private void initFragment() {
		fm = getChildFragmentManager();
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (wordFragment != null) {
			Logger.e("我隐藏的是---wordFragment");
			transaction.hide(wordFragment);
		}
		if (voiceFragment != null) {
			Logger.e("我隐藏的是---voiceFragment");
			transaction.hide(voiceFragment);
		}
	}

	public NoteVoiceFragment getVoiceFragment() {
		return voiceFragment;
	}

	public NoteWordFragment getWordFragment() {
		return wordFragment;
	}

	public boolean isVoice() {
		return isVoice;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {

		Logger.e("hidden---->" + hidden);

		// if (hidden) {
		// hideFragments(fm.beginTransaction());
		// } else {
		// if (isVoice) {
		// showVoiceFragment();
		// } else {
		// showWordFragment();
		// }
		// }
	}

}
