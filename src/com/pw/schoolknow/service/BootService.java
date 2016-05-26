package com.pw.schoolknow.service;

import com.igexin.slavesdk.MessageManager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BootService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId){
		
		//开启接收推送的消息
		MessageManager.getInstance().initialize(this.getApplicationContext());
		
		return START_STICKY;	
	}

}
