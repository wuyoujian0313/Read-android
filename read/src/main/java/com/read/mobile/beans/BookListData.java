package com.read.mobile.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookListData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7882870899178831809L;

	private List<String> leibie = new ArrayList<>();

	private List<BookFenleiBean> fenlei = new ArrayList<>();

	public List<String> getLeibie() {
		return leibie;
	}

	public void setLeibie(List<String> leibie) {
		this.leibie = leibie;
	}

	public List<BookFenleiBean> getFenlei() {
		return fenlei;
	}

	public void setFenlei(List<BookFenleiBean> fenlei) {
		this.fenlei = fenlei;
	}

}
