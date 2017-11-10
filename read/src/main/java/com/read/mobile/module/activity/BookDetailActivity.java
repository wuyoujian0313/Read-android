package com.read.mobile.module.activity;

import java.io.ByteArrayOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.NetAsyncTask;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.utils.Logger;
import com.ngc.corelib.utils.Options;
import com.ngc.corelib.utils.Tips;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.read.mobile.R;
import com.read.mobile.beans.BookInfoResult;
import com.read.mobile.beans.BookItem;
import com.read.mobile.beans.BookStoreBookRequest;
import com.read.mobile.beans.BookStoreBookResult;
import com.read.mobile.beans.NoteListRequest;
import com.read.mobile.beans.NoteListResult;
import com.read.mobile.constants.Contacts;
import com.read.mobile.constants.ShareContacts;
import com.read.mobile.env.BaseActivity;
import com.read.mobile.module.adapter.BookDetailViewPagerAdapter;
import com.read.mobile.utils.AccessTokenKeeper;
import com.read.mobile.views.ShareDialog;
import com.read.mobile.views.ShareDialog.ShareClickListener;
import com.read.mobile.views.popup.ActionItem;
import com.read.mobile.views.popup.TitlePopup;
import com.read.mobile.views.popup.TitlePopup.OnItemOnClickListener;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class BookDetailActivity extends BaseActivity implements ShareClickListener {

	private BookInfoResult bookInfoResult;

	private ImageView imgIV;
	private TextView nameTV;
	private TextView type01;
	private TextView type02;
	private TextView authorTV;
	private TextView descTV;
	private TextView pressTV;
	private ImageView heartIV;
	private TextView faverNumTV;

	private TextView fenTV;
	private TextView yuanTV;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	private ViewPager detailViewPager;
	private BookDetailViewPagerAdapter adapter;
	private TextView jianjieTV;
	private TextView bijiTV;
	private TextView daoduTV;

	private TitlePopup titlePopup;
	private String shareContent;

	@Override
	protected void initData() {
		super.initData();
		bookInfoResult = (BookInfoResult) getIntent().getSerializableExtra("BookInfoResult");
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_book_detail;
	}

	@Override
	protected void initViwes() {

		showLeftBack(null);
		mTencent = Tencent.createInstance(ShareContacts.SHARE_QQ_APPID, this.getApplicationContext());
		imageLoader = ImageLoader.getInstance();
		options = Options.getListOptions();

		imgIV = (ImageView) findViewById(R.id.book_detail_img);
		nameTV = (TextView) findViewById(R.id.book_detail_name);
		type01 = (TextView) findViewById(R.id.book_detail_type_01);
		type02 = (TextView) findViewById(R.id.book_detail_type_02);
		authorTV = (TextView) findViewById(R.id.book_detail_author);
		descTV = (TextView) findViewById(R.id.book_detail_desc);
		pressTV = (TextView) findViewById(R.id.book_detail_press);
		heartIV = (ImageView) findViewById(R.id.book_detail_faverate);
		faverNumTV = (TextView) findViewById(R.id.book_detail_faverate_num);
		fenTV = (TextView) findViewById(R.id.book_detail_price_fen);
		yuanTV = (TextView) findViewById(R.id.book_detail_price_yuan);

		if (!TextUtils.isEmpty(bookInfoResult.getData().getPic_big())) {
			imageLoader.displayImage(bookInfoResult.getData().getPic_big(), imgIV, options);
		}
		nameTV.setText(bookInfoResult.getData().getName());
		if (TextUtils.isEmpty(bookInfoResult.getData().getType())) {
			type01.setVisibility(View.GONE);
		} else {
			type01.setText(bookInfoResult.getData().getType());
			type01.setVisibility(View.VISIBLE);
		}
		if (TextUtils.isEmpty(bookInfoResult.getData().getF_age())) {
			type02.setVisibility(View.GONE);
		} else {
			if (bookInfoResult.getData().getF_age().contains("岁")) {
				type02.setText(bookInfoResult.getData().getF_age());
			} else {
				type02.setText(bookInfoResult.getData().getF_age() + "岁");
			}
			type02.setVisibility(View.VISIBLE);
		}
		authorTV.setText("作者：" + bookInfoResult.getData().getAuthor());
		descTV.setText(bookInfoResult.getData().getStatement());
		pressTV.setText("出版社：" + bookInfoResult.getData().getPress());
		if (bookInfoResult.getData().getIsFavor().equals("yes")) {
			heartIV.setImageResource(R.drawable.heart_read);
		} else {
			heartIV.setImageResource(R.drawable.icon_heart_white);
		}
		if (!TextUtils.isEmpty(bookInfoResult.getData().getPrice())) {
			int price = Integer.valueOf(bookInfoResult.getData().getPrice());
			int yuan = price / 100;
			int fen = price % 100;
			yuanTV.setText(String.valueOf(yuan) + ".");
			fenTV.setText(((fen >= 10) ? String.valueOf(fen) : ("0" + fen)) + "元");
		} else {
			yuanTV.setText("暂无报价");
			fenTV.setText("");
		}
		shareContent = bookInfoResult.getData().getBrief();
		jianjieTV = (TextView) findViewById(R.id.book_detail_jianjie);
		bijiTV = (TextView) findViewById(R.id.book_detail_biji);
		daoduTV = (TextView) findViewById(R.id.book_detail_daodu);
		jianjieTV.setOnClickListener(this);
		bijiTV.setOnClickListener(this);
		daoduTV.setOnClickListener(this);

		detailViewPager = (ViewPager) findViewById(R.id.book_detail_vp);
		adapter = new BookDetailViewPagerAdapter(this, imageLoader, options);
		adapter.setViewData(bookInfoResult);
		detailViewPager.setAdapter(adapter);
		detailViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					jianjieTV.performClick();
					break;
				case 1:
					bijiTV.performClick();
					break;
				case 2:
					daoduTV.performClick();
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		jianjieTV.setEnabled(false);

		heartIV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setOnClick();
			}
		});
		noteList();

		showRight01Back(R.drawable.share, new OnClickListener() {
			@Override
			public void onClick(View view) {
				// titlePopup.show(view);
				ShareDialog dialog = new ShareDialog(BookDetailActivity.this, BookDetailActivity.this);
				dialog.shows();
			}
		});

		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(this, "分享到朋友圈", R.drawable.share_quan));
		titlePopup.addAction(new ActionItem(this, "分享到微信", R.drawable.share_wechat));

		showRight01Back(R.drawable.share, new OnClickListener() {
			@Override
			public void onClick(View view) {
				// titlePopup.show(view);
				ShareDialog dialog = new ShareDialog(BookDetailActivity.this, BookDetailActivity.this);
				dialog.shows();
			}
		});
		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {
			@Override
			public void onItemClick(ActionItem item, int position) {
				switch (position) {
				case 0:
					sendToWX(true, "http://123.56.104.127/book/sharePage?isbn=" + bookInfoResult.getData().getIsbn(),
							"我最喜欢的一本书\n" + bookInfoResult.getData().getName());
					break;
				case 1:
					sendToWX(false, "http://123.56.104.127/book/sharePage?isbn=" + bookInfoResult.getData().getIsbn(),
							bookInfoResult.getData().getName());
					break;
				}
			}
		});
		uiListener = new BaseUiListener();
	}

	private int currentIndex = 0;

	private void enableViews() {
		jianjieTV.setEnabled(true);
		bijiTV.setEnabled(true);
		daoduTV.setEnabled(true);
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.book_detail_jianjie:
			if (currentIndex != 0) {
				enableViews();
				jianjieTV.setEnabled(false);
				currentIndex = 0;
			}
			break;
		case R.id.book_detail_biji:
			if (currentIndex != 1) {
				enableViews();
				bijiTV.setEnabled(false);
				currentIndex = 1;
			}
			break;
		case R.id.book_detail_daodu:
			if (currentIndex != 2) {
				enableViews();
				daoduTV.setEnabled(false);
				currentIndex = 2;
			}
			break;
		}
		detailViewPager.setCurrentItem(currentIndex);
	}

	private void setOnClick() {
		final BookItem bean = bookInfoResult.getData();
		BookStoreBookRequest request = new BookStoreBookRequest();
		request.setAuthor(bean.getAuthor());
		request.setBookName(bean.getName());
		request.setIsbn(bean.getIsbn());
		request.setPic(bean.getPic_big());
		request.setPress(bean.getPress());
		request.setType((bean.getIsFavor().equals("yes")) ? "0" : "1");

		NetAsyncTask asyncTask = new NetAsyncTask(BookStoreBookResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					if (bean.getIsFavor().equals("yes")) {
						bookInfoResult.getData().setIsFavor("no");
						heartIV.setImageResource(R.drawable.icon_heart_white);
					} else {
						bookInfoResult.getData().setIsFavor("yes");
						heartIV.setImageResource(R.drawable.heart_read);
					}
				} else {
					Tips.tipLong(BookDetailActivity.this, result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				showProgressDialog("正在请求~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				Tips.tipLong(BookDetailActivity.this, e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_BOOK_STOREBOOK);
	}

	private void noteList() {
		NoteListRequest request = new NoteListRequest();
		request.setIsbn(bookInfoResult.getData().getIsbn());
		request.setU("");
		NetAsyncTask asyncTask = new NetAsyncTask(NoteListResult.class, new AsyncTaskListener() {
			@Override
			public void onTaskSuccess(int requestId, BaseResult result) {
				dismissProgressDialog();
				if (result.getHead().getCode().equals(Contacts.RESULT_CODE_OK)) {
					NoteListResult listResult = (NoteListResult) result;
					if (adapter != null) {
						adapter.setNoteData(listResult.getData().getInfo());
					}
				} else {
					// Tips.tipLong(BookDetailActivity.this,
					// result.getHead().getMessage());
				}
			}

			@Override
			public void onTaskStart(int requestId) {
				showProgressDialog("正在请求~");
			}

			@Override
			public void onTaskFail(int requestId, Exception e) {
				dismissProgressDialog();
				Tips.tipLong(BookDetailActivity.this, e.getLocalizedMessage());
			}
		}, request);
		asyncTask.execute(Contacts.URL_BOOK_NOTE_LIST);
	}

	// ************************************微信分享*************************************************
	// ************************************微信分享*************************************************
	private IWXAPI api;

	public void sendToWX(boolean isFriendCircle, String shareUrl, String shareTitle) {
		api = WXAPIFactory.createWXAPI(this, ShareContacts.SHARE_WECHAT_APPID);
		api.registerApp(ShareContacts.SHARE_WECHAT_APPID);
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = shareUrl;
		Logger.e(!isFriendCircle ? "我是微信分享" : "我是朋友圈分享" + "-----" + shareUrl);
		WXMediaMessage msg = new WXMediaMessage(webpage);
		Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		msg.thumbData = bmpToByteArray(thumb, true);
		msg.title = shareTitle;
		if (shareContent != null) {
			if (shareContent.length() > 24) {
				msg.description = shareContent.substring(0, 24);
			} else {
				msg.description = shareContent;
			}
		}
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = isFriendCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}

	private byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	// ************************************微博分享*************************************************
	/** 微博微博分享接口实例 */
	private IWeiboShareAPI mWeiboShareAPI = null;

	private void init() {
		// 创建微博 SDK 接口实例
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(getApplicationContext(), ShareContacts.SHARE_SINA_APPID);
		// 获取微博客户端相关信息，如是否安装、支持 SDK 的版本
		mWeiboShareAPI.registerApp();
	}

	private void shareToSina(String shareUrl, String shareTitle) {
		init();
		// 1. 初始化微博的分享消息
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		weiboMessage.imageObject = getImageObj(bmp);
		weiboMessage.textObject = getTextObj(shareTitle, shareUrl);
		// 2. 初始化从第三方到微博的消息请求
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		// 用transaction唯一标识一个请求
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;

		// 3. 发送请求消息到微博，唤起微博分享界面
		AuthInfo authInfo = new AuthInfo(this, ShareContacts.SHARE_SINA_APPID, ShareContacts.SHARE_SINA_APPSECRET,
				ShareContacts.SHARE_SINA_SCOPE);
		Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
		String token = "";
		if (accessToken != null) {
			token = accessToken.getToken();
		}

		mWeiboShareAPI.sendRequest(this, request, authInfo, token, new WeiboAuthListener() {
			@Override
			public void onWeiboException(WeiboException arg0) {
			}

			@Override
			public void onComplete(Bundle bundle) {
				Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
				AccessTokenKeeper.writeAccessToken(getApplicationContext(), newToken);
			}

			@Override
			public void onCancel() {
			}
		});

	}

	/**
	 * 创建文本消息对象。
	 * 
	 * @return 文本消息对象。
	 */
	private TextObject getTextObj(String shareUrl, String shareTitle) {
		TextObject textObject = new TextObject();
		textObject.text = shareTitle + "\n" + shareUrl;
		return textObject;
	}

	/**
	 * 创建图片消息对象。
	 * 
	 * @return 图片消息对象。
	 */
	private ImageObject getImageObj(Bitmap bitmap) {
		ImageObject imageObject = new ImageObject();
		imageObject.setImageObject(bitmap);
		return imageObject;
	}

	// ************************************QQ分享*************************************************
	Tencent mTencent;
	private BaseUiListener uiListener;

	private void share2QQ(String shareUrl, String shareTitle, String shareSummary) {
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle);
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareSummary);
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, bookInfoResult.getData().getPic_big());
		// params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
		// "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, getString(R.string.app_name));
		mTencent.shareToQQ(this, params, uiListener);
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onError(UiError e) {
			// showResult("onError:", "code:" + e.errorCode + ", msg:" +
			// e.errorMessage + ", detail:" + e.errorDetail);
			showResult("onError", "分享失败");
		}

		@Override
		public void onCancel() {
			// showResult("onCancel", "");
			showResult("onError", "分享取消");
		}

		@Override
		public void onComplete(Object arg0) {
			// showResult("onComplete", arg0.toString());
			// JSONObject jsonObject = new JSONObject(arg0.toString());
			// String tokenStr = jsonObject.optString("access_token");
			// UserInfo info = new UserInfo(this,
			// MainActivity.mQQAuth.getQQToken());
			// info.getUserInfo(new BaseUIListener(this,
			// "get_simple_userinfo"));
			showResult("onError", "分享成功");
		}
	}

	private void showResult(String tag, String message) {
		// Logger.e(tag + "---" + message);
		// Tips.tipLong(this, tag + "---" + message);
		Tips.tipShort(this, message);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Tencent.onActivityResultData(requestCode, resultCode, data, uiListener);
	}

	@Override
	public void onWechatClick() {
		sendToWX(false, "http://123.56.104.127/book/sharePage?isbn=" + bookInfoResult.getData().getIsbn(), "我最喜欢的一本书\n"
				+ bookInfoResult.getData().getName());
	}

	@Override
	public void onCiecleClick() {
		sendToWX(true, "http://123.56.104.127/book/sharePage?isbn=" + bookInfoResult.getData().getIsbn(), "我最喜欢的一本书\n"
				+ bookInfoResult.getData().getName());
	}

	@Override
	public void onSinaClick() {
		// shareToSina("http://123.56.104.127/book/sharePage?isbn=" +
		// bookInfoResult.getData().getIsbn(), "我最喜欢的一本书\n"
		// + bookInfoResult.getData().getName());
	}

	@Override
	public void onQQClick() {
		share2QQ("http://123.56.104.127/book/sharePage?isbn=" + bookInfoResult.getData().getIsbn(), "我最喜欢的一本书\n"
				+ bookInfoResult.getData().getName(), bookInfoResult.getData().getBrief());
	}

}
