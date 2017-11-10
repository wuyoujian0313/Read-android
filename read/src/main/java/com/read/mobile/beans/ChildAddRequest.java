package com.read.mobile.beans;

public class ChildAddRequest extends ReadBaseRequest {
	private String name;
	private String sex;
	private String birthday;
	private String intrest;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getIntrest() {
		return intrest;
	}

	public void setIntrest(String intrest) {
		this.intrest = intrest;
	}

}
