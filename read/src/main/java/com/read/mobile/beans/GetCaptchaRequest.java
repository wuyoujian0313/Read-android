package com.read.mobile.beans;

public class GetCaptchaRequest extends ReadBaseRequest {
	private String phone;// 是 用户手机号
	private String type;// 是 1: 注册 2 ：忘记密码

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
