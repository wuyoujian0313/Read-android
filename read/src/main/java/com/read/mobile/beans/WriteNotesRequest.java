package com.read.mobile.beans;

public class WriteNotesRequest extends ReadBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 962899601266563613L;
	private String u;// 是 用户id
	private String source;// 是 写笔记的图书来源 1: 书库里有的书 2: 用户新建
	private String type;// 是 笔记类型 1: 文字笔记 2: 语音笔记
	private String bookName;// 是 图书名称
	private String author;// 是 图书作者
	private String press;// 是 出版社
	private String pic;// 否 图书图片url
	private String isbn;// 是 书号，唯一标示
	private String content;// 是 笔记内容（文字或语音）语音则是二进制流

	public String getU() {
		return u;
	}

	public void setU(String u) {
		this.u = u;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
