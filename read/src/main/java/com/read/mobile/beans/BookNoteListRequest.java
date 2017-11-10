package com.read.mobile.beans;

public class BookNoteListRequest extends ReadBaseRequest {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3836375970077418330L;
	private String offset;// 否 第几页
	private String length; // 否 每页条数 默认10

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
