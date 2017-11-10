package com.read.mobile.beans;

public class BookSearchRequest extends ReadBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 268243055276634758L;

	private String stype;// 是 搜索类型 isbn：isbn搜索 keyword:关键字搜索 age：年龄范围搜索（2-4
							// 或4-6）type: 图书分类搜索（科技，艺术）
	private String keyStr;// 是 对应搜素条件内容
	private String length;// 否 长度 默认10
	private String offset;// 否 偏移量

	public String getStype() {
		return stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}

	public String getKeyStr() {
		return keyStr;
	}

	public void setKeyStr(String keyStr) {
		this.keyStr = keyStr;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

}
