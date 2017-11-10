package com.ngc.corelib.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.ngc.corelib.encrypt.des3.Base64;
import com.ngc.corelib.env.NGCApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class SharedPreferencesAccess {

	public static String PREF_NAME = "jrb_pref";

	private static SharedPreferencesAccess mInstance;

	private static SharedPreferences preferences;

	private SharedPreferencesAccess() {
		synchronized (this) {
			final Context context = NGCApp.appContext;
			preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		}
	}

	public static SharedPreferencesAccess getInstance() {
		synchronized (SharedPreferencesAccess.class) {
			if (mInstance == null) {
				mInstance = new SharedPreferencesAccess();
			}
		}
		return mInstance;
	}

	public void putBoolean(String key, boolean value) {
		Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void putFloat(String key, float value) {
		Editor editor = preferences.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public void putInt(String key, int value) {
		Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public void putLong(String key, long value) {
		Editor editor = preferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public void putString(String key, String value) {
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void putObject(String key, Serializable seri) {
		Editor editor = preferences.edit();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(seri);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String baseStr = new String(Base64.encode(baos.toByteArray()));
		editor.putString(key, baseStr);
		editor.commit();
	}

	public Object getObject(String key) {
		String encodeStr = preferences.getString(key, "");
		if (TextUtils.isEmpty(encodeStr)) {
			return null;
		}
		byte[] base64Bytes = Base64.decodeBase64(encodeStr.getBytes());
		ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
		Object obj = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
			obj = ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public boolean getBoolean(String key, boolean defValue) {
		return preferences.getBoolean(key, defValue);
	}

	public int getInt(String key, int defValue) {
		return preferences.getInt(key, defValue);
	}

	public float getFloat(String key, float defValue) {
		return preferences.getFloat(key, defValue);
	}

	public long getLong(String key, long defValue) {
		return preferences.getLong(key, defValue);
	}

	public String getString(String key, String defValue) {
		return preferences.getString(key, defValue);
	}

}
