/*
 * com.ziofront.android.contacts
 * Contact.java
 * Jiho Park    2009. 11. 27.
 *
 * Copyright (c) 2009 ziofront.com. All Rights Reserved.
 */
package shalom.bible.hymn.cion.dao;


public class Activity_Data_Podcast_Favorite {
	int _id;
	String id; 
	String num;
	String title;
	String provider;
	String imageurl;
	String rssurl;
	String udate;
	public Activity_Data_Podcast_Favorite(int _id, String id, String num, String title, String provider, String imageurl, String rssurl, String udate){
		this._id = _id;
		this.id = id;
		this.num = num;
		this.title = title;
		this.provider = provider;
		this.imageurl = imageurl;
		this.rssurl = rssurl;
		this.udate = udate;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getRssurl() {
		return rssurl;
	}
	public void setRssurl(String rssurl) {
		this.rssurl = rssurl;
	}
	public String getUdate() {
		return udate;
	}
	public void setUdate(String udate) {
		this.udate = udate;
	}
	
	
}