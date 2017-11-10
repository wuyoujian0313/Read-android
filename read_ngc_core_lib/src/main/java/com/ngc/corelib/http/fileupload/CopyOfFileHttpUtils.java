package com.ngc.corelib.http.fileupload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

import android.text.TextUtils;

import com.ngc.corelib.http.bean.CopyOfBaseFileRequest;
import com.ngc.corelib.utils.Logger;

public class CopyOfFileHttpUtils {
	/**
	 * 提交FILE并带表单数据
	 * 
	 * @param file
	 * @param url
	 * @param formparams
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String postFile(File[] files, String url, List<BasicNameValuePair> formparams, String[] fileName) {
		String result = null;
		try {
			FileBody[] bin = new FileBody[fileName.length];
			HttpClient httpclient = CustomerHttpClient.getHttpClient();
			HttpPost httppost = new HttpPost(url);
			for (int i = 0; i < bin.length; i++) {
				if (files[i] != null) {
					bin[i] = new FileBody(files[i]);
				}
			}

			MultipartEntity reqEntity = new MultipartEntity();
			if (formparams != null) {
				// new StringBody("汉字",Charset.forName("UTF-8")));
				for (NameValuePair nameValuePair : formparams) {
					reqEntity.addPart(nameValuePair.getName(),
							new StringBody(nameValuePair.getValue(), Charset.forName("UTF-8")));
				}
			}

			for (int i = 0; i < bin.length; i++) {
				reqEntity.addPart(fileName[i], bin[i]);
			}

			httppost.setEntity(reqEntity);
			Logger.e("执行: " + httppost.getRequestLine());

			HttpResponse response = httpclient.execute(httppost);
			Logger.e("statusCode is " + response.getStatusLine().getStatusCode());
			HttpEntity resEntity = response.getEntity();
			Logger.e("----------------------------------------");
			Logger.e("---" + response.getStatusLine());
			if (resEntity != null) {
				Logger.e("返回长度: " + resEntity.getContentLength());
				Logger.e("返回类型: " + resEntity.getContentType());
				InputStream in = resEntity.getContent();
				Logger.e("in is " + in);
				result = StringUtils.readStream(in);
				Logger.e("result--->" + result);
			}
			if (resEntity != null) {
				resEntity.consumeContent();
			}
		} catch (Exception e) {
			Logger.e(e.getMessage());
		}
		return result;
	}
}
