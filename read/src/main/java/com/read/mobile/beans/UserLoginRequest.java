package com.read.mobile.beans;

public class UserLoginRequest extends ReadBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -570267291002905845L;

	private String phone;
	private String passwd;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

}
