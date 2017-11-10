package com.ngc.corelib.http.fileupload;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.ngc.corelib.http.AsyncTaskListener;
import com.ngc.corelib.http.bean.BaseResult;
import com.ngc.corelib.http.bean.CopyOfBaseFileRequest;
import com.ngc.corelib.utils.Logger;

public class CopyOfUploadFileAsyncTask extends AsyncTask<String, Integer, BaseResult> {

	private AsyncTaskListener taskListener;
	private int requestId;
	private CopyOfBaseFileRequest request;

	private Exception exception;
	private Class<? extends BaseResult> clazz;

	private String[] fileName;

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public CopyOfUploadFileAsyncTask(Class<? extends BaseResult> clazz, AsyncTaskListener taskListener,
			CopyOfBaseFileRequest request, String[] fileName) {
		this.taskListener = taskListener;
		this.request = request;
		this.clazz = clazz;
		this.fileName = fileName;
	}

	@Override
	protected BaseResult doInBackground(String... params) {
		publishProgress(0);
		try {
			String result = CopyOfFileHttpUtils.postFile(request.getFile(), params[0], request.getParamsList(),
					fileName);
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
