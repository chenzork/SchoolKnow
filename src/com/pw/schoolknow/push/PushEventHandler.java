package com.pw.schoolknow.push;

import android.content.Context;


public interface PushEventHandler {
	
	public  void onMessage(String message);
	public  void onBind(Context context,String channelId);

}
