package com.read.mobile.beans;

public class BookListRequest extends ReadBaseRequest {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4771503220419181248L;

	private String type;// 是 每页长度，默认10

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
