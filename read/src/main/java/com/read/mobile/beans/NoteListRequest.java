package com.read.mobile.beans;

public class NoteListRequest extends ReadBaseRequest {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5324567840200425124L;

	private String type;// 是 笔记类型 1: 文字笔记 2: 语音笔记

	private String isbn;
	private String offset;
	private String length;

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

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

	public String getU() {
		return u;
	}

	public void setU(String u) {
		this.u = u;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "NoteListRequest [u=" + u + ", type=" + type + ", isbn=" + isbn + ", offset=" + offset + ", length="
				+ length + ", sign=" + sign + "]";
	}

}
