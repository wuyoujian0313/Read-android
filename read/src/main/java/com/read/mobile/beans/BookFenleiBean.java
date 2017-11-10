package com.read.mobile.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookFenleiBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3452644168014891478L;
	private String range;
	private List<BookBean> books = new ArrayList<>();

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public List<BookBean> getBooks() {
		return books;
	}

	public void setBooks(List<BookBean> books) {
		this.books = books;
	}

}
