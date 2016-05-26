package com.pw.schoolknow.utils;

import java.util.Map;

import com.pw.schoolknow.app.MyApplication;
import com.pw.schoolknow.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


public class NotifyUtils {
	
	private static final int NORMAL_NOTIFY_ID = 1;  
	
	public static void tip(String barTitle,String contentTitle,String contentText,Class<? extends Activity> klass){
		
		Context context=MyApplication.getInstance();
		
		NotificationManager notificationManager=(NotificationManager) 
				context.getSystemService(Activity.NOTIFICATION_SERVICE);
		Notification notification=
			new Notification(R.drawable.ic_launcher,barTitle,
						System.currentTimeMillis());
		
		 // 设定声音  
        notification.defaults |= Notification.DEFAULT_SOUND;  
          
        //设定震动(需加VIBRATE权限)  
        notification.defaults |= Notification.DEFAULT_VIBRATE;  
        
        //设定LED灯提醒  
        notification.defaults |= Notification.DEFAULT_LIGHTS;  
          
        //设置点击此通知后自动清除  
        notification.flags |= Notification.FLAG_AUTO_CANCEL;  
		
        Intent it=new Intent(context,klass);
        
		PendingIntent contentIntent=PendingIntent.getActivity(context,0,
				it, PendingIntent.FLAG_UPDATE_CURRENT);
		
		notification.setLatestEventInfo(context,contentTitle,contentText, contentIntent);
		
		notificationManager.notify(NORMAL_NOTIFY_ID,notification);
	}
	
	
	public static void tip(String barTitle,String contentTitle,String contentText,
			Class<? extends Activity> klass,Map<String,String> map){
			Context context=MyApplication.getInstance();
		
		NotificationManager notificationManager=(NotificationManager) 
				context.getSystemService(Activity.NOTIFICATION_SERVICE);
		Notification notification=
			new Notification(R.drawable.ic_launcher,barTitle,
						System.currentTimeMillis());
		
		 // 设定声音  
        notification.defaults |= Notification.DEFAULT_SOUND;  
          
        //设定震动(需加VIBRATE权限)  
        notification.defaults |= Notification.DEFAULT_VIBRATE;  
        
        //设定LED灯提醒  
        notification.defaults |= Notification.DEFAULT_LIGHTS;  
          
        //设置点击此通知后自动清除  
        notification.flags |= Notification.FLAG_AUTO_CANCEL;  
		
        Intent it=new Intent(context,klass);
        for (String key : map.keySet()){
        	it.putExtra(key, map.get(key));
        }
        
		PendingIntent contentIntent=PendingIntent.getActivity(context,0,
				it, PendingIntent.FLAG_UPDATE_CURRENT);
		
		notification.setLatestEventInfo(context,contentTitle,contentText, contentIntent);
		
		notificationManager.notify(NORMAL_NOTIFY_ID,notification);
	}

}
