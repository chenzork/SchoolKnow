package com.pw.schoolknow.bean;

import java.io.Serializable;


/**
 * ����ʱ����
 * @author wei8888go
 *
 */

public class CountdownItem  implements Comparable<CountdownItem>,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String title;     //���ѱ���
	private long time_samp;    //ʱ���
	private String address;    //�ص�                                                                    
	
	public CountdownItem(int id,String title, long time_samp, String address) {
		this.id=id;
		this.title = title;
		this.time_samp = time_samp;
		this.address = address;
	}
	
	public CountdownItem(String title, long time_samp, String address) {
		super();
		this.title = title;
		this.time_samp = time_samp;
		this.address = address;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getTime_samp() {
		return time_samp;
	}
	public void setTime_samp(long time_samp) {
		this.time_samp = time_samp;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public int compareTo(CountdownItem item) {
		return this.getTime_samp()<=item.getTime_samp()?-1:1; 
	}
	
	
}
