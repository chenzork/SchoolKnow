package com.pw.schoolknow.helper;

import com.pw.schoolknow.bean.CountdownItem;
import com.pw.schoolknow.db.CountdownDB;
import com.pw.schoolknow.utils.TimeUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class InitHelper {
	public SharedPreferences share;
	public SharedPreferences.Editor edit=null;
	private Context context;
	private static final String FILENAME="Init";
	
	@SuppressLint("CommitPrefEdits")
	public InitHelper(Context context){
		this.context=context;
		this.share=this.context.getSharedPreferences(FILENAME,
				Context.MODE_PRIVATE);
		this.edit=share.edit();
	}
	
	//��ʼ��
	public void install(){
		if(!hasInit()){
			this.InitCountDown();
			setInit(true);
		}
		
	}
	
	//��ʼ��������
	public void InitCountDown(){
		CountdownItem[] data=new CountdownItem[]{
				new CountdownItem("������",TimeUtil.createtimesamp(2014,7,1,0,0),""),
				new CountdownItem("���",TimeUtil.createtimesamp(2014,7,7,9,0),""),
				new CountdownItem("������",TimeUtil.createtimesamp(2014,8,1,0,0),""),
				new CountdownItem("��Ϧ",TimeUtil.createtimesamp(2014,8,2,0,0),""),
				new CountdownItem("��Ԫ��",TimeUtil.createtimesamp(2014,8,10,0,0),""),
				new CountdownItem("�����",TimeUtil.createtimesamp(2014,9,6,0,0),""),
				new CountdownItem("��ʦ��",TimeUtil.createtimesamp(2014,9,10,0,0),""),
				new CountdownItem("������ȼ�����",TimeUtil.createtimesamp(2014,9,20,9,0),""),
				new CountdownItem("�����",TimeUtil.createtimesamp(2014,10,1,0,0),""),
				new CountdownItem("������",TimeUtil.createtimesamp(2014,10,2,0,0),"")
		};
		for(int i=0;i<data.length;i++){
			new CountdownDB(context).insert(data[i]);
		}
	}
	
	public void setInit(boolean b){
		edit.putBoolean("init", b);
		edit.commit();
	}
	
	public boolean hasInit(){
		return share.getBoolean("init",false);
	}
	
	/**
	 * ����֮ǰ�İ汾��
	 * @return
	 */
	public int getLastVersion(){
		return share.getInt("version", 0);
	}
	
	/**
	 * ���ø��º�İ汾��
	 */
	public void setLastVersion(){
		edit.putInt("version", VersionHelper.getVerCode(context));
		edit.commit();
	}
	
	/**
	 * ���ǰ�װ��������ҳ��,�����ñ�������
	 * @return
	 */
	public boolean checkHasInit(){
		if(VersionHelper.getVerCode(context)!=getLastVersion()){
			install();
			setLastVersion();
			return false;
		}
		return true;
	}
	
	
}
