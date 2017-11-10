package com.read.mobile.beans;

import com.ngc.corelib.http.bean.BaseResult;

public class ReadBaseResult<T> extends BaseResult {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3730736641788340181L;
	protected T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
