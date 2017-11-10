package com.ngc.corelib.utils;

/**
 * 把对象置为NULL的工具类
 * 
 * @author huangyuan
 * 
 */
public class NullUtils {
	/**
	 * 设置对象null
	 * 
	 * @param objects
	 */
	public static void setObjNull(Object... objects) {
		if (objects != null) {
			for (Object object : objects) {
				if (object != null) {
					object = null;
				}
			}
		}
	}
}
