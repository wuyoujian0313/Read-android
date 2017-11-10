package com.read.mobile.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultySearchData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5681304949041757872L;

	private List<String> leibie = new ArrayList<>();
	private List<String> age = new ArrayList<String>();

	private List<BookItem> books = new ArrayList<>();

	public List<String> getFenlei() {
		return leibie;
	}

	public void setFenlei(List<String> fenlei) {
		this.leibie = leibie;
	}

	public List<String> getAge() {
		return age;
	}

	public void setAge(List<String> age) {
		this.age = age;
	}

	public List<BookItem> getBooks() {
		return books;
	}

	public void setBooks(List<BookItem> books) {
		this.books = books;
	}

}
