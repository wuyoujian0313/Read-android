package com.ngc.corelib.http.bean;

import java.io.Serializable;

public class BaseResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3075931093197737607L;
	protected Head head;

	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

}
