/**
 * SharedPreferencesHelper.java
 * 2012-4-11
 */
package com.read.mobile.utils.media;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ngc.corelib.encrypt.des3.Base64;
import com.read.mobile.env.BaseApp;

public class UserSharedPreferencesHelper {

	public static final String APP_SHARD = "_user";
	private static SharedPreferences mSharedPreferences;
	private static Editor mEditor;

	// 当前用户UID
	private static final String CURRENT_UID = "current_uid";
	private static final String USER_NAME = "user_name";
	private static final String PASSWORD = "password";
	private static final String LOGINSTATE = "loginstate";
	private static final String ISSHOWALLFRIENDS = "isshowallfriends";

	// 当前用户的配置：在消息列表中是否显示通知消息
	private static final String IS_SHOW_TRANSMSG = "is_show_transmsg";

	// 用户金币数, save as object
	public static final String USER_MONEY = "user_money";

	// 服务器关系设置
	public static final String SNS_SERVER_SETTINGS = "sns_server_settings";

	// speaker mode
	private static final String SPEAKER_MODEL = "speaker_model";
	public static final int SPEAKER_IN_CALL = 0;
	public static final int SPEAKER_NORMAL = 1;

	public static SharedPreferences getSharedPreferences() {

		if (mSharedPreferences == null) {
			mSharedPreferences = BaseApp.getInstance().getSharedPreferences(APP_SHARD, Context.MODE_PRIVATE);
		}
		return mSharedPreferences;
	}

	public static Editor getEditor() {

		if (mEditor == null) {
			mEditor = getSharedPreferences().edit();
		}
		return mEditor;
	}

	public static void saveCurrentUid(String uid) {
		Editor editor = getEditor();
		editor.putString(CURRENT_UID, uid);
		editor.commit();
	}

	public static void saveCurrentUserName(String name) {
		Editor editor = getEditor();
		editor.putString(USER_NAME, name);
		editor.commit();
	}

	public static void saveCurrentPassword(String pwd) {
		Editor editor = getEditor();
		editor.putString(PASSWORD, pwd);
		editor.commit();
	}

	public static void saveCurrentLoginState(boolean state) {
		Editor editor = getEditor();
		editor.putBoolean(LOGINSTATE, state);
		editor.commit();
	}

	public static void setShowFriendState(boolean state) {
		Editor editor = getEditor();
		editor.putBoolean(ISSHOWALLFRIENDS, state);
		editor.commit();
	}

	public static void setSpeakerMode(int model) {
		Editor editor = getEditor();
		editor.putInt(SPEAKER_MODEL, model);
		editor.commit();
	}

	public static int getSpeakerMode() {
		return getSharedPreferences().getInt(SPEAKER_MODEL, 1);
	}

	public static boolean getShowFriendState() {
		return getSharedPreferences().getBoolean(ISSHOWALLFRIENDS, true);
	}

	public static String getCurrentUserName() {
		return getSharedPreferences().getString(USER_NAME, null);
	}

	public static String getCurrentPassword() {
		return getSharedPreferences().getString(PASSWORD, null);
	}

	public static boolean getCurrentLoginState() {
		return getSharedPreferences().getBoolean(LOGINSTATE, false);
	}

	public static String getCurrentUid() {
		return getSharedPreferences().getString(CURRENT_UID, "null");
	}

	public static void setShowTransMessage(boolean state) {
		Editor editor = getEditor();
		editor.putBoolean(IS_SHOW_TRANSMSG + "_" + getCurrentUid(), state);
		editor.commit();
	}

	public static boolean getShowTransMessage() {
		return getSharedPreferences().getBoolean(IS_SHOW_TRANSMSG + "_" + getCurrentUid(), true);
	}

	public static void clear() {
		mEditor = getEditor();
		mEditor.clear();
		mEditor.commit();
	}

	public static void saveObject(Object obj, String name) {
		mEditor = getEditor();
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 创建对象输出流，并封闭字节流
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(obj);
			// 将字节流编码成base64的字符串
			String rawContacts = new String(Base64.encodeBase64(baos.toByteArray()));
			mEditor.putString(name, rawContacts);
			mEditor.commit();
		} catch (IOException e) {
		}
	}

	public static Object readObject(String name) {
		mSharedPreferences = getSharedPreferences();
		String objStr = mSharedPreferences.getString(name, "");
		if (!"".equals(objStr)) {
			// 读取字节
			byte[] base64 = Base64.decodeBase64(objStr.getBytes());

			// 封装到字节流
			ByteArrayInputStream bais = new ByteArrayInputStream(base64);
			try {
				// 再次封装
				ObjectInputStream bis = new ObjectInputStream(bais);
				try {
					// 读取对象
					return bis.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new Object();
	}

}
