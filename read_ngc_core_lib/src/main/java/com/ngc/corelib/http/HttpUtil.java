package com.ngc.corelib.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import com.ngc.corelib.utils.Logger;

/**
 * 
 * @Title: HttpUtil.java
 * @Package: com.silupay.sdk.utils.net
 * @Description: 网络连接工具
 * @author:luoyunlong@silupay.com
 * @date: 2014年8月4日 下午4:17:21
 * @version: V1.0
 */
public class HttpUtil {

	/**
	 * 超时时间
	 */
	private static final int TIMEOUT = 60000;

	/**
	 * 网络请求工具
	 * 
	 * @param url
	 * @param list
	 * @param method
	 * @param overTime
	 * @return
	 * @throws IOException
	 * @throws ConnectTimeoutException
	 */
	public String getStringData(String url, List<BasicNameValuePair> list, String method, int overTime)
			throws IOException, ConnectTimeoutException {
		String mStrData = null;
		HttpClient mHttpClient = new DefaultHttpClient();
		// 设置连接超时时间
		if (overTime == 0) {
			mHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
			mHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT);
		} else {
			mHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, overTime);
			mHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, overTime);
		}
		HttpResponse mHttpResponse = null;
		if (method == null || method.equalsIgnoreCase("GET")) {
			StringBuffer sb = new StringBuffer(url);
			if (list != null && !list.isEmpty()) {
				sb.append("?");
				for (BasicNameValuePair mList : list) {
					sb.append(mList.getName()).append("=");
					if (mList.getValue() != null) {
						sb.append(mList.getValue());
					}
					sb.append("&");
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			HttpGet mHttpGet = new HttpGet(sb.toString());
			mHttpResponse = mHttpClient.execute(mHttpGet);
		} else if (method.equalsIgnoreCase("POST")) {
			HttpPost mHttpPost = new HttpPost(url);
			if (list != null && !list.isEmpty()) {
				UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(list, "UTF-8");
				mHttpPost.setEntity(reqEntity);
				mHttpResponse = mHttpClient.execute(mHttpPost);
			}
		}
		if (mHttpResponse != null && mHttpResponse.getStatusLine().getStatusCode() == 200) {
			HttpEntity mHttpEntity = mHttpResponse.getEntity();
			InputStream mInputStream = mHttpEntity.getContent();
			BufferedReader mBufferedReader = new BufferedReader(new InputStreamReader(mInputStream, "UTF-8"));
			StringBuilder mStringBuilder = new StringBuilder();
			String line = null;
			while ((line = mBufferedReader.readLine()) != null) {
				mStringBuilder.append(line);
			}
			mInputStream.close();
			mStrData = mStringBuilder.toString();
		}

		return mStrData;
	}
}
