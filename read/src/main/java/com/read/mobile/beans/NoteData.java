package com.read.mobile.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NoteData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5618429032199898879L;

	private List<NoteItem> info = new ArrayList<>();
	private String hasNext;

	public List<NoteItem> getInfo() {
		return info;
	}

	public void setInfo(List<NoteItem> info) {
		this.info = info;
	}

	public String getHasNext() {
		return hasNext;
	}

	public void setHasNext(String hasNext) {
		this.hasNext = hasNext;
	}

}
