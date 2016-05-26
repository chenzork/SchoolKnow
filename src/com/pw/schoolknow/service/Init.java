package com.pw.schoolknow.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.igexin.slavesdk.MessageManager;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.helper.NetHelper;
import com.pw.schoolknow.helper.VersionHelper;
import com.pw.schoolknow.utils.GetUtil;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.pw.schoolknow.push.PushMessageReceiver;
import com.pw.schoolknow.push.PushEventHandler;


/**
 * ����Ƿ����
 * @author wei8888go
 *
 */
public class Init  extends Service implements PushEventHandler {
	
	public LoginHelper lh;
	public VersionHelper vh;
	@Override
	public void onCreate() {
		super.onCreate();
		
		lh=new LoginHelper(this);
		vh=new VersionHelper(this);
		
		
		MessageManager.getInstance().initialize(this.getApplicationContext());
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg=new Message();
				msg.what=102;
				msg.obj=GetUtil.getRes(ServerConfig.HOST+"/schoolknow/versionManage.php?current="
				+VersionHelper.getVerCode(Init.this));
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==102){
				String temp=msg.obj.toString().trim();
				if(!temp.equals("")&&temp.length()!=0){
					try {
						int version=VersionHelper.getVerCode(Init.this);
						JSONObject JsonData = new JSONObject(temp);
						
						String serverCode=JsonData.getString("code").trim();
						
						int serverVer=Integer.parseInt(serverCode);
						
						if(serverVer>version){
							if(serverVer>vh.getUpdateVersion()){
								if(serverVer>vh.getUpdateVersion()){
									vh.update(temp,true);
								}
							}
						}
					}catch (JSONException e) {
					}catch (Exception e) {
					}
				}
				stopSelf();
			}
		}
		
	};
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onStart(Intent intent, int startId) {
		PushMessageReceiver.ehList.add(this);// �������͵���Ϣ
		super.onStart(intent, startId);
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		PushMessageReceiver.ehList.remove(this);// �Ƴ�����
		super.onTaskRemoved(rootIntent);
	}

	@Override
	public void onMessage(String tm) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onBind(Context context, String channelId) {
		final String userid=channelId;
		if(userid.trim().length()!=0){
			new LoginHelper(this).setUserid(userid);
			if(NetHelper.isNetConnected(this)){
				if(lh.hasLogin()){
					new Thread(new Runnable() {
						public void run() {
							GetUtil.getRes(ServerConfig.HOST+"/schoolknow/manage/updatePush.php"
									+"?user="+lh.getUid()+"&userid="+userid+"&ver="+VersionHelper.getVerCode(Init.this));
						}
					}).start();
				}
			}
			
		}
	}

}
