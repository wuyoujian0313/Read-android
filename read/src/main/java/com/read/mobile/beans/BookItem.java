package com.read.mobile.beans;

import java.io.Serializable;

public class BookItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6104661480821983452L;

	private String id = "";// ": "18",
	private String name = "";// ": "武则天",
	private String pic_big = "";// ": "http://dfdfadfdf",
	private String pic_small = "";// ": "",
	private String author = "";// ": "小强",
	private String range = "";// ": "",
	private String type = "";// ": "",
	private String f_age = "";// ": "",
	private String press = "";// ": "",
	private String isbn = "";// ": "998877",
	private String recommend = "";// ": "0",
	private String link = "";// ": "",
	private String comment = "";// ": "",
	private String status = "";// ": "yes",
	private String created_date = "";// ": "0000-00-00",
	private String created = "";// ": "0",
	private String isOnline = "";// ": "1",
	private String isFavor = "";// ": "no"
	private String price = "0";// ": "no"
	private String statement = "";// ": "",
	private String brief = "";// ": "",
	private String introduction = "";// ": "",
	private String pic_intr = "";// ": ""
	private String pic_jj = "";// ": ""
	private String pic = "";// ": ""我的收藏用
	private String bookName = "";// ": ""我的收藏用
	private String bookname = "";// ": ""我的收藏用

	public String getBookname() {
		return bookname;
	}

	public void setBookname(String bookname) {
		this.bookname = bookname;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	private boolean isAdd;

	public boolean isAdd() {
		return isAdd;
	}

	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPic_jj() {
		return pic_jj;
	}

	public void setPic_jj(String pic_jj) {
		this.pic_jj = pic_jj;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getPic_intr() {
		return pic_intr;
	}

	public void setPic_intr(String pic_intr) {
		this.pic_intr = pic_intr;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
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

	public String getPic_big() {
		return pic_big;
	}

	public void setPic_big(String pic_big) {
		this.pic_big = pic_big;
	}

	public String getPic_small() {
		return pic_small;
	}

	public void setPic_small(String pic_small) {
		this.pic_small = pic_small;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getF_age() {
		return f_age;
	}

	public void setF_age(String f_age) {
		this.f_age = f_age;
	}

	public String getPress() {
		return press;
	}

	public void setPress(String press) {
		this.press = press;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	public String getIsFavor() {
		return isFavor;
	}

	public void setIsFavor(String isFavor) {
		this.isFavor = isFavor;
	}

}
