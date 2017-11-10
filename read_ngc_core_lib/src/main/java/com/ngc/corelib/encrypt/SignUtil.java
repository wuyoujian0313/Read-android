package com.ngc.corelib.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.ngc.corelib.encrypt.md5.MD5Util;
import com.ngc.corelib.utils.Logger;

/**
 * 
 * @Title: SignUtil.java
 * @Package: com.silupay.sdk.utils.encrypt
 * @Description: 获取签名信息的工具类
 * @author:luoyunlong@silupay.com
 * @date: 2014年8月4日 下午4:15:30
 * @version: V1.0
 */
public class SignUtil {

	/**
	 * 根据键值对签名
	 * 
	 * @param params
	 * @param screatKey
	 *            密钥
	 * @return
	 */

	public static String signNameValue(List<BasicNameValuePair> params, String screatKey) {
		if (params == null)
			throw new NullPointerException("代签名键值对为空！( 详情见hkrt.icardpay.Utils.Sign.signNameValue() )");
		String str = nameValuePairsToStr(params);

		Logger.e("签名字符串===" + (str + "\n" + screatKey));
		// SHA-1
		try {
			String signStr = str + "_" + screatKey;
			Logger.e("签名前字符串：" + signStr);
			String sign = bytes2Hex(digestEncrypt((str + "_" + screatKey).getBytes("utf-8"), "SHA-1"));
			Logger.e("签名后字符串：" + sign);
			return sign;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
		// return MD5Util.getMD5String(str + screatKey);
	}

	/**
	 * 根据BasicNameValuePair的Key对Value进行排序拼接
	 * 
	 * @param params
	 * @return
	 */
	private static String nameValuePairsToStr(List<BasicNameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		if (params == null)
			return null;
		Collections.sort(params, new Comparator<NameValuePair>() {
			@Override
			public int compare(NameValuePair lhs, NameValuePair rhs) {
				return lhs.getName().compareTo(rhs.getName());
			}
		});
		for (NameValuePair nameValuePair : params) {
			sb.append(nameValuePair.getName() + "=" + nameValuePair.getValue());
			Logger.e(nameValuePair.getName() + "=" + nameValuePair.getValue());
		}
		return sb.toString();
	}

	/**
	 * 功能：摘要加密 参数：digest 要加密的摘要原文 encName
	 * 摘要加密算法，包括"MD5"、"SHA-1"、"SHA-256"，默认使用MD5 返回值：加密后的结果
	 */

	public static byte[] digestEncrypt(byte[] digest, String encName) {
		byte[] result = null;
		try {
			if (encName == null || encName.equals("")) {
				encName = "MD5";
			}
			MessageDigest md = MessageDigest.getInstance(encName);
			md.update(digest);
			result = md.digest();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Invalid algorithm.");
			return null;
		}
		return result;
	}

	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		if (null != bts) {
			for (int i = 0; i < bts.length; i++) {
				tmp = (Integer.toHexString(bts[i] & 0xFF));
				if (tmp.length() == 1) {
					des += "0";
				}
				des += tmp;
			}
		}
		return des;
	}
}
