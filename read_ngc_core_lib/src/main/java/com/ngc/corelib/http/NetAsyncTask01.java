package com.ngc.corelib.http;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.utils.Logger;

public class NetAsyncTask01 extends AsyncTask<String, Integer, BaseResult> {

	private AsyncTaskListener taskListener;
	private int requestId;
	private int overTime;
	private String method = "POST";

	private Exception exception;
	private Class<? extends BaseResult> clazz;

	private List<BasicNameValuePair> paramsList;

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public NetAsyncTask01(Class<? extends BaseResult> clazz, AsyncTaskListener taskListener,
			List<BasicNameValuePair> paramsList) {
		this.taskListener = taskListener;
		this.paramsList = paramsList;
		this.clazz = clazz;
	}

	public NetAsyncTask01(Class<? extends BaseResult> clazz, AsyncTaskListener taskListener,
			List<BasicNameValuePair> paramsList, String method, int overTime) {
		this.taskListener = taskListener;
		this.paramsList = paramsList;
		this.method = method;
		this.overTime = overTime;
		this.clazz = clazz;
	}

	@Override
	protected BaseResult doInBackground(String... params) {
		publishProgress(0);
		try {
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
