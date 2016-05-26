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
	
	//初始化
	public void install(){
		if(!hasInit()){
			this.InitCountDown();
			setInit(true);
		}
		
	}
	
	//初始化倒数日
	public void InitCountDown(){
		CountdownItem[] data=new CountdownItem[]{
				new CountdownItem("建党节",TimeUtil.createtimesamp(2014,7,1,0,0),""),
				new CountdownItem("暑假",TimeUtil.createtimesamp(2014,7,7,9,0),""),
				new CountdownItem("建军节",TimeUtil.createtimesamp(2014,8,1,0,0),""),
				new CountdownItem("七夕",TimeUtil.createtimesamp(2014,8,2,0,0),""),
				new CountdownItem("中元节",TimeUtil.createtimesamp(2014,8,10,0,0),""),
				new CountdownItem("中秋节",TimeUtil.createtimesamp(2014,9,6,0,0),""),
				new CountdownItem("教师节",TimeUtil.createtimesamp(2014,9,10,0,0),""),
				new CountdownItem("计算机等级考试",TimeUtil.createtimesamp(2014,9,20,9,0),""),
				new CountdownItem("国庆节",TimeUtil.createtimesamp(2014,10,1,0,0),""),
				new CountdownItem("重阳节",TimeUtil.createtimesamp(2014,10,2,0,0),"")
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
	 * 更新之前的版本号
	 * @return
	 */
	public int getLastVersion(){
		return share.getInt("version", 0);
	}
	
	/**
	 * 设置更新后的版本号
	 */
	public void setLastVersion(){
		edit.putInt("version", VersionHelper.getVerCode(context));
		edit.commit();
	}
	
	/**
	 * 覆盖安装出现引导页面,不重置本机数据
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
