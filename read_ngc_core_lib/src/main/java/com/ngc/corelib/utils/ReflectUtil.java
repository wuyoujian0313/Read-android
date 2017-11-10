package com.ngc.corelib.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.text.TextUtils;

public class ReflectUtil {

	private static Field[] fields;
	private static Field[] superfields;

	public static List<BasicNameValuePair> getHttpList(Object o) throws IllegalAccessException,
			IllegalArgumentException {
		Class<? extends Object> _clazz;

		// _clazz = Class.forName(classPath);
		_clazz = o.getClass();
		fields = _clazz.getDeclaredFields();
		Class<? extends Object> _superclzz = _clazz.getSuperclass();
		superfields = _superclzz.getDeclaredFields();

		List<BasicNameValuePair> paramsList = new ArrayList<BasicNameValuePair>();

		Field.setAccessible(fields, true);
		for (Field field : fields) {
			if (field.getName().equals("sign") || field.getName().equals("serialVersionUID")) {
				continue;
			} else {
				if (!TextUtils.isEmpty(field.get(o) == null ? "" : field.get(o).toString())) {
					paramsList.add(new BasicNameValuePair(field.getName(), field.get(o) == null ? "" : field.get(o)
							.toString()));
				}
			}
		}
		Field.setAccessible(superfields, true);
		for (Field field : superfields) {
			if (field.getName().equals("sign") || field.getName().equals("serialVersionUID")) {
				continue;
			} else {
				paramsList.add(new BasicNameValuePair(field.getName().trim(), field.get(o).toString().trim()));
			}
		}

		return paramsList;
	}

	public static Map<String, String> getHttpMap(Object o) throws IllegalAccessException, IllegalArgumentException {
		Class<? extends Object> _clazz;

		// _clazz = Class.forName(classPath);
		_clazz = o.getClass();
		fields = _clazz.getDeclaredFields();
		Class<? extends Object> _superclzz = _clazz.getSuperclass();
		superfields = _superclzz.getDeclaredFields();

		Map<String, String> map = new HashMap<String, String>();

		Field.setAccessible(fields, true);
		for (Field field : fields) {
			if (field.getName().equals("sign") || field.getName().equals("serialVersionUID")) {
				continue;
			} else {
				map.put(field.getName(), field.get(o) == null ? "" : field.get(o).toString());
			}
		}
		Field.setAccessible(superfields, true);
		for (Field field : superfields) {
			if (field.getName().equals("sign") || field.getName().equals("serialVersionUID")) {
				continue;
			} else {
				map.put(field.getName(), field.get(o) == null ? "" : field.get(o).toString());
			}
		}

		return map;
	}
}
