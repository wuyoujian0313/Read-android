package com.read.mobile.module.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.NetAsyncTask;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.utils.Options;
import com.ngc.corelib.utils.Tips;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.read.mobile.R;
import com.read.mobile.beans.BookFavorListRequest;
import com.read.mobile.beans.BookFavorListResult;
import com.read.mobile.beans.BookNoteListRequest;
import com.read.mobile.beans.BookNoteListResult;
import com.read.mobile.beans.ChildInfoRequest;
import com.read.mobile.beans.ChildInfoResult;
import com.read.mobile.beans.UserInfoRequest;
import com.read.mobile.beans.UserInfoResult;
import com.read.mobile.constants.Contacts;
import com.read.mobile.env.BaseFragment;
import com.read.mobile.module.activity.ChildInfoActivity;
import com.read.mobile.module.activity.FavorListActivity;
import com.read.mobile.module.activity.InfoEditActivity;
import com.read.mobile.module.activity.NoteBookListActivity;
import com.read.mobile.module.activity.SettingActivity;
import com.read.mobile.observer.ReadAgent;
import com.read.mobile.observer.ReadObserver.MondifyUserInfoSeccessObserver;
import com.read.mobile.observer.ReadObserver.UploadImgSeccessObserver;
import com.read.mobile.utils.SaveUtils;

public class MemberFragment extends BaseFragment implements MondifyUserInfoSeccessObserver, UploadImgSeccessObserver {

	private ImageView photoIV;
	private TextView nameTV;
	private TextView signTV;
	private TextView collectCountTV;
	private TextView noteCountTV;

	private ImageLoader loader;
	private DisplayImageOptions options;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_member;
	}

	@Override
	protected void initViews() {

		ReadAgent.geUshareAgent().addMondifyNameSeccessObservers(this);
		ReadAgent.geUshareAgent().addUploadImgSeccessObserverObserver(this);

		photoIV = (ImageView) findViewById(R.id.vip_photo_iv);
		nameTV = (TextView) findViewById(R.id.vip_name_tv);
		signTV = (TextView) findViewById(R.id.vip_sign_tv);
		collectCountTV = (TextView) findViewById(R.id.vip_collect_book_count_tv);
		noteCountTV = (TextView) findViewById(R.id.vip_note_book_count_tv);

		photoIV.setOnClickListener(this);
		findViewById(R.id.vip_child_info_ll).setOnClickListener(this);
		findViewById(R.id.vip_collect_book_ll).setOnClickListener(this);
		findViewById(R.id.vip_note_book_ll).setOnClickListener(this);
		findViewById(R.id.vip_setting_ll).setOnClickListener(this);

		loader = ImageLoader.getInstance();
		options = Options.getIconOptions();
		setData();

		userInfo();
	}

	private void setData() {
		if (!TextUtils.isEmpty(SaveUtils.getUserAvatar())) {
			loader.displayImage(SaveUtils.getUserAvatar(), photoIV, options);
		}
		nameTV.setText(SaveUtils.getUserNick());
		signTV.setText(SaveUtils.getUserSign());
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.vip_photo_iv: {
			// 修改个人信息
			Intent intent = new Intent(getActivity(), InfoEditActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.vip_child_info_ll: {
			// 孩子信息
			childInfo();
		}
			break;
		case R.id.vip_collect_book_ll: {
			favorList();
		}
			break;
		case R.id.vip_note_book_ll: {
			noteList();
		}
			break;
		case R.id.vip_setting_ll: {
			Intent intent = new Intent(getActivity(), SettingActivity.class);
			startActivity(intent);
		}
			break;
		}
	}

	@Override
	public void handleUserInfo(String avatar) {
		if (!TextUtils.isEmpty(SaveUtils.getUserAvatar())) {
			loader.displayImage(SaveUtils.getUserAvatar(), photoIV, options);
		}
	}

	@Override
	public void handleUserInfo(String name, String signStr) {
		nameTV.setText(SaveUtils.getUserNick());
		signTV.setText(SaveUtils.getUserSign());
	}

	/**
	 * 我收藏的
	 */
	private void favorList() {
		BookFavorListRequest request = new BookFavorListRequest();
		request.setOffset(String.valueOf(0));
		request.setLength(Contacts.REQUEST_PAGE_LENGTH);
		request.setU(SaveUtils.getUserId());
		NetAsyncTask asyncTask = new NetAsyncTask(BookFavorListResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					Intent intent = new Intent(getActivity(), FavorListActivity.class);
					intent.putExtra("BookFavorListResult", result);
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
		asyncTask.execute(Contacts.URL_BOOK_FAVORLIST);
	}

	/**
	 * 我收藏的
	 */
	private void noteList() {
		BookNoteListRequest request = new BookNoteListRequest();
		request.setOffset(String.valueOf(0));
		request.setU(SaveUtils.getUserId());
		request.setLength(Contacts.REQUEST_PAGE_LENGTH);
		NetAsyncTask asyncTask = new NetAsyncTask(BookNoteListResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					Intent intent = new Intent(getActivity(), NoteBookListActivity.class);
					intent.putExtra("BookNoteListResult", result);
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
		asyncTask.execute(Contacts.URL_BOOK_GETNOTEBOOKLIST);
	}

	/**
	 * 我的信息
	 */
	private void userInfo() {
		UserInfoRequest request = new UserInfoRequest();
		NetAsyncTask asyncTask = new NetAsyncTask(UserInfoResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					UserInfoResult infoResult = (UserInfoResult) result;
					SaveUtils.login(infoResult.getData());
					setData();
				} else {
					Tips.tipLong(getActivity(), result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				Tips.tipLong(getActivity(), e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_USER_INFO);
	}

	/**
	 * 小孩信息
	 */
	private void childInfo() {
		ChildInfoRequest request = new ChildInfoRequest();
		request.setU(SaveUtils.getUserId());
		NetAsyncTask asyncTask = new NetAsyncTask(ChildInfoResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					Intent intent = new Intent(getActivity(), ChildInfoActivity.class);
					intent.putExtra("ChildInfoResult", result);
					startActivity(intent);
				} else {
					Tips.tipLong(getActivity(), result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				Tips.tipLong(getActivity(), e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_CHILD_INFO);
	}
}
