package com.pw.schoolknow.bean;


/**
 * 用户基本属性对象
 * @author peng
 *
 */
public class UserBean {
	
	private int id;
	
	private String email;
	private String sex;
	private String stuid;
	private String nick;
	private String client;
	private String college;
	
	public UserBean(String email, String sex, String stuid, String nick,
			String client, String college) {
		this.email = email;
		this.sex = sex;
		this.stuid = stuid;
		this.nick = nick;
		this.client = client;
		this.college = college;
	}
	
	public UserBean(int id,String email, String sex, String stuid, String nick,
			String client, String college) {
		this.id=id;
		this.email = email;
		this.sex = sex;
		this.stuid = stuid;
		this.nick = nick;
		this.client = client;
		this.college = college;
	}
	
	

	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getStuid() {
		return stuid;
	}

	public void setStuid(String stuid) {
		this.stuid = stuid;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}
	
	
	

}
