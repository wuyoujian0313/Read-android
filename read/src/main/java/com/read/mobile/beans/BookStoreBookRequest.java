package com.read.mobile.beans;

public class BookStoreBookRequest extends ReadBaseRequest {
	private String type;// 是 1: 收藏 0: 取消收藏
	private String bookName;// 是 图书名称
	private String author;// 是 图书作者
	private String press;// 是 出版社
	private String pic;// 否 图书图片url
	private String isbn;// 是 书号，唯一标示

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPress() {
		return press;
	}

	public void setPress(String press) {
		this.press = press;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

}
