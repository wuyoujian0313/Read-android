package com.read.mobile.beans;

import java.io.Serializable;

public class NoteItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5129963676049385560L;

	private String id;// ": "1",
	private String author;// ": "166",
	private String bookname;// ": "围城",
	private String pic;// ": "166",
	private String press;// ": "166",
	private String user_id;// ": "166",
	private String word;// ": "166",
	private String isbn;// ": "166",
	private String type;// ": "1",
	private String source;// ": "1",
	private String sound;// ": "",
	private String created;// ": "1436876518"

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBookname() {
		return bookname;
	}

	public void setBookname(String bookname) {
		this.bookname = bookname;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPress() {
		return press;
	}

	public void setPress(String press) {
		this.press = press;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

}
