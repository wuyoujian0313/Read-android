package com.read.mobile.utils;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ngc.corelib.utils.SharedPreferencesAccess;
import com.read.mobile.beans.BookItem;
import com.read.mobile.beans.BookListResult;
import com.read.mobile.beans.MultySearchResult;
import com.read.mobile.beans.User;
import com.read.mobile.constants.ShareKeys;

public class SaveUtils {

	/**
	 * 获取用户手机号
	 * 
	 * @return
	 */
	public static String getUserMobile() {
		return SharedPreferencesAccess.getInstance().getString(ShareKeys.USER_PHONE, "");
	}

	/**
	 * 获取用户头像
	 * 
	 * @return
	 */
	public static String getUserAvatar() {
		return SharedPreferencesAccess.getInstance().getString(ShareKeys.USER_AVATAR, "");
	}

	/**
	 * 保存用户头像
	 * 
	 * @return
	 */
	public static void setUserAvatar(String avatar) {
		SharedPreferencesAccess.getInstance().putString(ShareKeys.USER_AVATAR, avatar);
	}

	/**
	 * 获取用户Name
	 * 
	 * @return
	 */
	public static String getUserNick() {
		return SharedPreferencesAccess.getInstance().getString(ShareKeys.USER_NICK, "");
	}

	/**
	 * 获取用户ID
	 * 
	 * @return
	 */
	public static String getUserId() {
		return SharedPreferencesAccess.getInstance().getString(ShareKeys.USER_ID, "");
	}

	/**
	 * 获取用户签名
	 * 
	 * @return
	 */
	public static String getUserSign() {
		return SharedPreferencesAccess.getInstance().getString(ShareKeys.USER_SIGN, "");
	}

	/**
	 * 设置用户签名
	 * 
	 * @return
	 */
	public static void setUserSign(String moodStr) {
		SharedPreferencesAccess.getInstance().putString(ShareKeys.USER_SIGN, moodStr);
	}

	/**
	 * 设置用户姓名
	 * 
	 * @return
	 */
	public static void setUserName(String nameStr) {
		SharedPreferencesAccess.getInstance().putString(ShareKeys.USER_NICK, nameStr);
	}

	/**
	 * 登录
	 * 
	 * @param user
	 */
	public static void login(User user) {
		if (!TextUtils.isEmpty(user.getUser_id())) {
			SharedPreferencesAccess.getInstance().putString(ShareKeys.USER_ID, user.getUser_id());
		}
		SharedPreferencesAccess.getInstance().putString(ShareKeys.USER_AVATAR, user.getAvatar());
		SharedPreferencesAccess.getInstance().putString(ShareKeys.USER_NICK, user.getNick());
		SharedPreferencesAccess.getInstance().putString(ShareKeys.USER_PHONE, user.getPhone());
	}

	/**
	 * 登录
	 * 
	 * @param user
	 */
	public static void logout() {
		SharedPreferencesAccess.getInstance().putString(ShareKeys.USER_ID, "");
		SharedPreferencesAccess.getInstance().putString(ShareKeys.USER_AVATAR, "");
		SharedPreferencesAccess.getInstance().putString(ShareKeys.USER_NICK, "");
		SharedPreferencesAccess.getInstance().putString(ShareKeys.USER_PHONE, "");
		SharedPreferencesAccess.getInstance().putString(ShareKeys.USER_SIGN, "");
	}

	/**
	 * 图书列表
	 * 
	 * @param bookListJson
	 */
	public static void saveBookList(String bookListJson) {
		SharedPreferencesAccess.getInstance().putString(ShareKeys.BOOK_LIST_JSON, bookListJson);
	}

	/**
	 * 
	 * @return
	 */
	public static MultySearchResult getBookList() {
		MultySearchResult datas = null;
		if (!TextUtils.isEmpty(SharedPreferencesAccess.getInstance().getString(ShareKeys.BOOK_LIST_JSON, ""))) {
			datas = new Gson().fromJson(SharedPreferencesAccess.getInstance().getString(ShareKeys.BOOK_LIST_JSON, ""),
					MultySearchResult.class);
		}
		return datas;
	}

	/**
	 * 推荐图书列表
	 * 
	 * @param bookListJson
	 */
	public static void saveRecBookList(String bookListJson) {
		SharedPreferencesAccess.getInstance().putString(ShareKeys.BOOK_REC_LIST_JSON, bookListJson);
	}

	/**
	 * 
	 * @return
	 */
	public static List<BookItem> getRecBookList() {
		List<BookItem> datas = new ArrayList<BookItem>();
		if (!TextUtils.isEmpty(SharedPreferencesAccess.getInstance().getString(ShareKeys.BOOK_REC_LIST_JSON, ""))) {
			datas = new Gson().fromJson(
					SharedPreferencesAccess.getInstance().getString(ShareKeys.BOOK_REC_LIST_JSON, ""),
					new TypeToken<List<BookItem>>() {
					}.getType());
		}
		return datas;
	}

}
