package com.pw.schoolknow.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;


/**
 * ����������Ϣ
 * @author wei8888go
 *
 */
public class ScheduleHelper {
	
	public SharedPreferences share;
	public SharedPreferences.Editor edit=null;
	private Context context;
	private static final String FILENAME="Schedule";
	
	@SuppressLint("CommitPrefEdits")
	public ScheduleHelper(Context context){
		this.context=context;
		this.share=this.context.getSharedPreferences(FILENAME,
				Context.MODE_PRIVATE);
		this.edit=share.edit();
	}
	
	/**
	 * ���õ�ǰ�γ̱�༶��
	 */
	public void setCurrentScheduleClassId(String str){
		edit.putString("classId",str);
		edit.commit();
	}
	
	/**
	 * ��ȡ��ǰ�γ̱�༶��
	 */
	
	public String getCurrentScheduleClassId(){
		return share.getString("classId","");
	}
	
	/**
	 * ���õ�ǰ�γ̱�ѧ��
	 */
	public void setCurrentScheduleTerm(String str){
		edit.putString("Term",str);
		edit.commit();
	}
	
	/**
	 * ��ȡ��ǰ�γ̱�ѧ��
	 */
	
	public String getCurrentScheduleTerm(){
		return share.getString("Term","");
	}
	
	//��������ͼ
	public void setWeekView(Boolean b){
		edit.putBoolean("WeekView", b);
		edit.commit();
	}
	
	//�Ƿ�Ϊ����ͼ
	public Boolean getWeekView(){
		return share.getBoolean("WeekView", false);
	}

}
