package com.read.mobile.beans;

import java.io.Serializable;

public class ChildInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8013925476163500100L;
	private String id;// ": "1",
	private String name;// ": "小蕾", //姓名
	private String sex;// ": "0", //性别
	private String birthday;// ": "1990-01-02", //生日
	private String intrest;// ": "唱歌,跳舞", //兴趣爱好
	private String created;// ": "1437112891",
	private String user_id;// ": "166" //所属用户
	private String tag;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
