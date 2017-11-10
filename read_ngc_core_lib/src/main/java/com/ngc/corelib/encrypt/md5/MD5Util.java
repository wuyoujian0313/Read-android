package com.ngc.corelib.encrypt.md5;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @Title: MD5Util.java
 * @Package: com.silupay.sdk.utils.encrypt.md5
 * @author:luoyunlong@silupay.com
 * @version: V1.0
 */
public class MD5Util {

	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static MessageDigest messagedigest = null;

	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex) {
			nsaex.printStackTrace();
		}
	}

	/**
	 * ��String����MD5����
	 * 
	 * @param s
	 * @return
	 */
	public static String getMD5String(String s) {
		String str = "";
		try {
			str = getMD5String(new String(s.getBytes(System.getProperty("file.encoding")), "UTF-8").getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * ��byte�������Md5����
	 * 
	 * @param bytes
	 * @return
	 */
	public static String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

}
