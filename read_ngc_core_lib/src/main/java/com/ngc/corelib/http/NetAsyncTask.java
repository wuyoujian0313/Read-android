package com.ngc.corelib.http;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.ngc.corelib.encrypt.SignUtil;
import com.ngc.corelib.http.bean.BaseRequest;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.utils.Logger;
import com.ngc.corelib.utils.ReflectUtil;

public class NetAsyncTask extends AsyncTask<String, Integer, BaseResult> {

	private AsyncTaskListener taskListener;
	private int requestId;
	private int overTime;
	private String method = "POST";
	private BaseRequest request;

	private Exception exception;
	private Class<? extends BaseResult> clazz;

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public NetAsyncTask(Class<? extends BaseResult> clazz, AsyncTaskListener taskListener, BaseRequest request) {
		this.taskListener = taskListener;
		this.request = request;
		this.clazz = clazz;
	}

	public NetAsyncTask(Class<? extends BaseResult> clazz, AsyncTaskListener taskListener, BaseRequest request,
			String method, int overTime) {
		this.taskListener = taskListener;
		this.request = request;
		this.method = method;
		this.overTime = overTime;
		this.clazz = clazz;
	}

	@Override
	protected BaseResult doInBackground(String... params) {
		publishProgress(0);
		try {
			List<BasicNameValuePair> paramsList;
			paramsList = ReflectUtil.getHttpList(request);
			String sign = SignUtil.signNameValue(paramsList, "fdf4da319ea90b3cdb861887c77a75ec");
			paramsList.add(new BasicNameValuePair("sign", sign));
			Logger.e("请求地址：" + params[0]);
			for (int i = 0; i < paramsList.size(); i++) {
				Logger.e(paramsList.get(i).getName() + "-------" + i + "------->" + paramsList.get(i).getValue());
			}

			String result = new HttpUtil().getStringData(params[0], paramsList, method, overTime);
			BaseResult baseResult = new Gson().fromJson(result, clazz);
			return baseResult;
		} catch (Exception e) {
			Logger.e(e.getMessage());
			exception = e;
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		if (values[0] == 0)
			taskListener.onTaskStart(requestId);
	}

	@Override
	protected void onPostExecute(BaseResult result) {
		if (result != null)
			taskListener.onTaskSuccess(requestId, result);
		else {
			if (exception == null) {
				exception = new Exception("数据返回失败~");
			}
			taskListener.onTaskFail(requestId, exception);
		}
	}

}
