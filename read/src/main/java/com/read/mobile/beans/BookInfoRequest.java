package com.read.mobile.beans;

public class BookInfoRequest extends ReadBaseRequest {
	/**
	 * 
	 */
	private static final long serialVersionUID = 768191675534580843L;
	private String isbn;

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

}
