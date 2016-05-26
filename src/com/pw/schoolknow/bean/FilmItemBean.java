package com.pw.schoolknow.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class FilmItemBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	
	@Expose
	private String id;
	@Expose
	private String title;
	@Expose
	private String time;
	@Expose
	private String imgUrl;

	public FilmItemBean() {
	
	}

	public FilmItemBean(String id, String title, String uploadTime, String imgUrl) {
		this.id = id;
		this.title = title;
		this.time = uploadTime;
		this.imgUrl = imgUrl;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUploadTime() {
		return time;
	}
	public void setUploadTime(String uploadTime) {
		this.time = uploadTime;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	

	
	
	
	

}
