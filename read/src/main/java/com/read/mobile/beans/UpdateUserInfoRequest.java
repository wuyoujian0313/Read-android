package com.read.mobile.beans;

public class UpdateUserInfoRequest extends ReadBaseRequest {
	private String u;
	private String nick;
	private String mood;

	public String getU() {
		return u;
	}

	public void setU(String u) {
		this.u = u;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}

}
