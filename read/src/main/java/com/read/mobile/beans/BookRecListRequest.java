package com.read.mobile.beans;

public class BookRecListRequest extends ReadBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7733177496643395116L;

	private String offset;
	private String length = "12";

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

}
