package com.pw.schoolknow.app;

import com.pw.schoolknow.service.BootService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
	
	public void onReceive(Context context, Intent intent) {
		
		//����������Ҫ����
		Intent service = new Intent(context,BootService.class);
		context.startService(service);
		
		//����Ӧ��
		Intent it = context.getPackageManager().getLaunchIntentForPackage("com.pw.schoolknow"); 
		context.startActivity(it); 
		
	}

}
