package com.read.mobile.beans;

import java.io.Serializable;

public class UserData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5851454445433101968L;
	private User data;

	public User getData() {
		return data;
	}

	public void setData(User data) {
		this.data = data;
	}

}
