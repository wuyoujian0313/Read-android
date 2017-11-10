package com.read.mobile.beans;

public class ChangePasswdRequest extends ReadBaseRequest {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8144748036910847943L;
	private String u;// 是 用户id
	private String oldPasswd;// 是 旧密码
	private String newPasswd;// 是 新密码

	public String getU() {
		return u;
	}

	public void setU(String u) {
		this.u = u;
	}

	public String getOldPasswd() {
		return oldPasswd;
	}

	public void setOldPasswd(String oldPasswd) {
		this.oldPasswd = oldPasswd;
	}

	public String getNewPasswd() {
		return newPasswd;
	}

	public void setNewPasswd(String newPasswd) {
		this.newPasswd = newPasswd;
	}

}
