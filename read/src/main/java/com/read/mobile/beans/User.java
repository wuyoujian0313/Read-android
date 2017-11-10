package com.read.mobile.beans;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7838532451084731991L;
	private String nick;// ": "小巍", //昵称
	private String avatar;// ": "http://****/avatar/2015/07/1437380063166.jpg",
							// //头像地址
	private String favorCount;// ": 2, //收藏书目数量
	private String noteCount;// ": 6 //笔记数量

	private String user_id;// "": "166",
	private String phone;// "": "15001232845"

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getFavorCount() {
		return favorCount;
	}

	public void setFavorCount(String favorCount) {
		this.favorCount = favorCount;
	}

	public String getNoteCount() {
		return noteCount;
	}

	public void setNoteCount(String noteCount) {
		this.noteCount = noteCount;
	}

}
