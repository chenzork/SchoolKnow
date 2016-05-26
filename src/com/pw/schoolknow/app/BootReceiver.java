package com.pw.schoolknow.app;

import com.pw.schoolknow.service.BootService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
	
	public void onReceive(Context context, Intent intent) {
		
		//开机启动重要服务
		Intent service = new Intent(context,BootService.class);
		context.startService(service);
		
		//启动应用
		Intent it = context.getPackageManager().getLaunchIntentForPackage("com.pw.schoolknow"); 
		context.startActivity(it); 
		
	}

}
