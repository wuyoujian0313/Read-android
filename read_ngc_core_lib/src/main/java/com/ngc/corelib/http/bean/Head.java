package com.ngc.corelib.http.bean;

import java.io.Serializable;

public class Head implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4375088804333181893L;

	private String status;// ": 1,
	private String code;// ": 20000,
	private String message;// ": "请求成功"

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
