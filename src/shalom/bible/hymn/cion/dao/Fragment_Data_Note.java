/*
 * com.ziofront.android.contacts
 * Contact.java
 * Jiho Park    2009. 11. 27.
 *
 * Copyright (c) 2009 ziofront.com. All Rights Reserved.
 */
package shalom.bible.hymn.cion.dao;


public class Fragment_Data_Note {
	 int _id;
	 String kwon;
	 String jang;
	 String jul;
	 String content;
	 public Fragment_Data_Note(int _id,String kwon,String jang, String jul, String content) {
		this._id = _id;
		this.kwon = kwon;
		this.jang = jang;
		this.jul = jul;
		this.content = content;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getKwon() {
		return kwon;
	}
	public void setKwon(String kwon) {
		this.kwon = kwon;
	}
	public String getJang() {
		return jang;
	}
	public void setJang(String jang) {
		this.jang = jang;
	}
	public String getJul() {
		return jul;
	}
	public void setJul(String jul) {
		this.jul = jul;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	 
	 
 }
