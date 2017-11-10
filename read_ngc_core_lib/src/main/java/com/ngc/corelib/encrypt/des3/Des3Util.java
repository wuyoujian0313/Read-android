package com.ngc.corelib.encrypt.des3;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.annotation.SuppressLint;

/**
 * 
 * @Title: Des3Util.java
 * @Package: com.silupay.sdk.utils.encrypt.des3
 * @author:luoyunlong@silupay.com
 * @version: V1.0
 */
@SuppressLint("DefaultLocale")
public class Des3Util {
	public static final String CHAR_ENCODING = "UTF-8";

	public static byte[] encode(byte[] key, byte[] data) throws Exception {
		return MsgAuthCode.des3Encryption(key, data);
	}

	public static byte[] decode(byte[] key, byte[] value) throws Exception {
		return MsgAuthCode.des3Decryption(key, value);
	}

	public static String encode(String key, String data) {
		try {
			byte[] keyByte = key.getBytes(CHAR_ENCODING);
			byte[] dataByte = data.getBytes(CHAR_ENCODING);
			byte[] valueByte = MsgAuthCode.des3Encryption(keyByte, dataByte);
			String value = new String(Base64.encode(valueByte), CHAR_ENCODING);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String decode(String key, String value) {
		try {
			byte[] keyByte = key.getBytes(CHAR_ENCODING);
			byte[] valueByte = Base64.decode(value.getBytes(CHAR_ENCODING));
			byte[] dataByte = MsgAuthCode.des3Decryption(keyByte, valueByte);
			String data = new String(dataByte, CHAR_ENCODING);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String encryptToHex(String key, String data) {
		try {
			byte[] keyByte = key.getBytes(CHAR_ENCODING);
			byte[] dataByte = data.getBytes(CHAR_ENCODING);
			byte[] valueByte = MsgAuthCode.des3Encryption(keyByte, dataByte);
			String value = ConvertUtils.toHex(valueByte);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String decryptFromHex(String key, String value) {
		try {
			byte[] keyByte = key.getBytes(CHAR_ENCODING);
			byte[] valueByte = ConvertUtils.fromHex(value);
			byte[] dataByte = MsgAuthCode.des3Decryption(keyByte, valueByte);
			String data = new String(dataByte, CHAR_ENCODING);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressLint("TrulyRandom")
	public static String udpEncrypt(String key, String data) {
		try {
			Key k = updGenerateKey(key);
			IvParameterSpec IVSpec = new IvParameterSpec(new byte[8]);
			Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			c.init(1, k, ((IVSpec)));
			byte output[] = c.doFinal(data.getBytes("UTF-8"));
			return new String(Base64.encode(output), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Key updGenerateKey(String key) {
		try {
			DESedeKeySpec KeySpec = new DESedeKeySpec(UdpHexDecode(key));
			SecretKeyFactory KeyFactory = SecretKeyFactory.getInstance("DESede");
			Key k = ((KeyFactory.generateSecret(((KeySpec)))));
			return k;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String udpDecrypt(String key, String data) {
		try {
			byte[] input = Base64.decode(data.getBytes("UTF-8"));
			Key k = updGenerateKey(key);
			IvParameterSpec IVSpec = new IvParameterSpec(new byte[8]);
			Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			c.init(2, k, ((IVSpec)));
			byte output[] = c.doFinal(input);
			return new String(output, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressLint("DefaultLocale")
	public static byte[] UdpHexDecode(String s) {
		byte abyte0[] = new byte[s.length() / 2];
		String s1 = s.toLowerCase();
		for (int i = 0; i < s1.length(); i += 2) {
			char c = s1.charAt(i);
			char c1 = s1.charAt(i + 1);
			int j = i / 2;
			if (c < 'a')
				abyte0[j] = (byte) (c - 48 << 4);
			else
				abyte0[j] = (byte) ((c - 97) + 10 << 4);
			if (c1 < 'a')
				abyte0[j] += (byte) (c1 - 48);
			else
				abyte0[j] += (byte) ((c1 - 97) + 10);
		}
		return abyte0;
	}
}
