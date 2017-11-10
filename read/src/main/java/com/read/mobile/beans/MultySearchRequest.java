package com.read.mobile.beans;

import com.ngc.corelib.http.bean.BaseRequest;

public class MultySearchRequest extends BaseRequest {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7052993329468263369L;
	private String age;// 年龄范围
	private String type;// 图书类型
	private String offset;//
	private String length;//

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

}
