package com.read.mobile.beans;

import java.io.Serializable;

public class BookBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3630884805504600783L;
	private String book_name;
	private String pic_big;
	private String isbn;

	public String getBook_name() {
		return book_name;
	}

	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}

	public String getPic_big() {
		return pic_big;
	}

	public void setPic_big(String pic_big) {
		this.pic_big = pic_big;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

}
