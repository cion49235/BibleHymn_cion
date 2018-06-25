/*
 * com.ziofront.android.contacts
 * Contact.java
 * Jiho Park    2009. 11. 27.
 *
 * Copyright (c) 2009 ziofront.com. All Rights Reserved.
 */
package shalom.bible.hymn.cion.dao;


public class Fragment_Data_Prayer {
	 int _id;
	 String gidomun_main_description;
	 String gidomun_sub_description;
	 String gidomun_content;
	 public Fragment_Data_Prayer(int _id,String gidomun_main_description,String gidomun_sub_description, String gidomun_content) {
		this._id = _id;
		this.gidomun_main_description = gidomun_main_description;
		this.gidomun_sub_description = gidomun_sub_description;
		this.gidomun_content = gidomun_content;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getGidomun_main_description() {
		return gidomun_main_description;
	}
	public void setGidomun_main_description(String gidomun_main_description) {
		this.gidomun_main_description = gidomun_main_description;
	}
	public String getGidomun_sub_description() {
		return gidomun_sub_description;
	}
	public void setGidomun_sub_description(String gidomun_sub_description) {
		this.gidomun_sub_description = gidomun_sub_description;
	}
	public String getGidomun_content() {
		return gidomun_content;
	}
	public void setGidomun_content(String gidomun_content) {
		this.gidomun_content = gidomun_content;
	}
 }
