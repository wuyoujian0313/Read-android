package com.ngc.corelib.http;

import com.ngc.corelib.http.bean.BaseResult;

public interface AsyncTaskListener {

	public void onTaskStart(int requestId);

	public void onTaskSuccess(int requestId, BaseResult result);

	/**
	 * 
	 * @param requestId
	 * @param e
	 *            ConnectTimeoutException/IOException
	 */
	public void onTaskFail(int requestId, Exception e);

}
