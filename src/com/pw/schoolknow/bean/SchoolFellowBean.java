package com.pw.schoolknow.bean;

import java.io.Serializable;

public class SchoolFellowBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String uid;   //�û�id
	private String nickname;   //�ǳ�
	private String time;     //����ʱ��
	private String content;  //˵˵����
	private String commentNum;
	public String likeNum;
	public boolean hasLike;
	
	public SchoolFellowBean(int id, String uid, String nickname,
			String time, String content, String commentNum) {
		this.id = id;
		this.uid = uid;
		this.nickname = nickname;
		this.time = time;
		this.content = content;
		this.commentNum = commentNum;
		this.likeNum="0";
		this.hasLike=false;
	}
	
	public SchoolFellowBean(int id, String uid, String nickname,
			String time, String content, String commentNum,String like) {
		this.id = id;
		this.uid = uid;
		this.nickname = nickname;
		this.time = time;
		this.content = content;
		this.commentNum = commentNum;
		this.likeNum=like;
		this.hasLike=false;
	}
	
	public SchoolFellowBean(int id, String uid, String nickname,
			String time, String content, String commentNum,String like,boolean haslike) {
		this.id = id;
		this.uid = uid;
		this.nickname = nickname;
		this.time = time;
		this.content = content;
		this.commentNum = commentNum;
		this.likeNum=like;
		this.hasLike=haslike;
	}
	
	public String getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(String likeNum) {
		this.likeNum = likeNum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}

	public boolean isHasLike() {
		return hasLike;
	}

	public void setHasLike(boolean hasLike) {
		this.hasLike = hasLike;
	}
	
	
	
	
	

}
