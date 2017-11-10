package com.read.mobile.beans;

public class BookFavorListRequest extends ReadBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1049473269823810465L;
	private String offset;
	private String length;

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
