package com.read.mobile.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8215480107921073340L;

	private List<BookItem> info = new ArrayList<>();
	private String hasNext;

	public String getHasNext() {
		return hasNext;
	}

	public void setHasNext(String hasNext) {
		this.hasNext = hasNext;
	}

	public List<BookItem> getInfo() {
		return info;
	}

	public void setInfo(List<BookItem> info) {
		this.info = info;
	}

}
