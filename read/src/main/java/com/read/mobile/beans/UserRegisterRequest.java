package com.read.mobile.beans;

public class UserRegisterRequest extends ReadBaseRequest {
	/**
	 * 
	 */
	private static final long serialVersionUID = 968110541706216521L;
	private String phone;
	private String passwd;
	private String captcha;

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

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

}
