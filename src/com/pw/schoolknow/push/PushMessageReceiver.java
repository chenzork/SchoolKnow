package com.pw.schoolknow.push;

import java.util.ArrayList;

import com.igexin.sdk.Consts;
import com.pw.schoolknow.app.MyApplication;
import com.pw.schoolknow.bean.InformBase;
import com.pw.schoolknow.bean.SendChatMessageBean;
import com.pw.schoolknow.config.InformConfig;
import com.pw.schoolknow.db.ChatMessageDB;
import com.pw.schoolknow.db.FriendDB;
import com.pw.schoolknow.db.InformDB;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.utils.NotifyUtils;
import com.pw.schoolknow.view.Chat;
import com.pw.schoolknow.view.ChatRecords;
import com.pw.schoolknow.view.Inform;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class PushMessageReceiver extends BroadcastReceiver {
	
	
	public static ArrayList<PushEventHandler> ehList = new ArrayList<PushEventHandler>();
	public LoginHelper lh;
	
	
	

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		switch (bundle.getInt(Consts.CMD_ACTION)) {
		//透传数据
		case Consts.GET_MSG_DATA:
			byte[] payload = bundle.getByteArray("payload");
			if (payload != null) {
				
				String data = new String(payload);
				for (int i = 0; i < ehList.size(); i++)
					ehList.get(i).onMessage(data);	
				
				lh=new LoginHelper(context);
				if(lh.hasLogin()){
		        	try{
		        		 InformBase item=MyApplication.getInstance().getGson().fromJson(data,InformBase.class);
		        		 switch(item.getType()){
		        		 //花椒社区回复
		        		 case InformConfig.PUSH_SFComment:
		        			 if(item.getReceiveUid().equals(lh.getUid())){
		            			 new InformDB(context, lh.getUid()).saveInform(item);
		                         NotifyUtils.tip("您花椒社区有了新回复","消息通知", item.getSendName()+" 在花椒社区回复了你的动态",
			                    		 Inform.class);
		                	 }
		        			 break;
		        	     //添加好友请求
		        		 case InformConfig.PUSH_ADDREQUEST:
		        			 if(item.getReceiveUid().equals(lh.getUid())){
		        				 new InformDB(context, lh.getUid()).saveInform(item);
		                         
		                         NotifyUtils.tip("添加好友请求","添加好友通知", item.getSendName()+" 请求添加您为好友",
			                    		 Inform.class);
		                         
		        			 }
		        			 break;
		        		 //添加好友反馈信息
		        		 case InformConfig.PUSH_ADDCALLBACK:
		        			 int back=Integer.parseInt(item.getContent());
		        			 String backinfo="";
		        			 if(back==1){
		        				 backinfo=" 同意了你的好友请求";
		        				 FriendDB.getInstance(context,lh.getUid()).
		        				 add(item.getSendUid(), item.getSendName());
		        				 new InformDB(context, lh.getUid()).saveInform(item,"1");
		        			 }else{
		        				 backinfo=" 拒绝了你的好友请求";
		        				 new InformDB(context, lh.getUid()).saveInform(item,"3");
		        			 }
		                     NotifyUtils.tip("好友请求","好友请求反馈信息", item.getSendName()+backinfo,
		                    		 Inform.class);
		                     
		        			 break;
		        		 case InformConfig.PUSH_CHAT:
		        			 ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE); 
		        		     RunningTaskInfo info = manager.getRunningTasks(1).get(0); 
		        		     String shortClassName = info.topActivity.getClassName();    //类名
		        		     
		        		     //不是Chat界面
		        		     if(Chat.class!=Class.forName(shortClassName)){
		                         SendChatMessageBean messageItem=
		    							 MyApplication.getInstance().getGson().fromJson(item.getContent(),SendChatMessageBean.class);
		                         ChatMessageDB.getInstance(context, lh.getUid())
		 						.insert(messageItem.getType(),item.getSendUid(),messageItem.getContent(),
		 								messageItem.getTime(),"1","0");
		                         
		                         NotifyUtils.tip("聊天信息","聊天消息通知", item.getSendName()+" 给您发送了消息",
		                        		 ChatRecords.class);
		        		     }
		        			 
		        			 break;
		        		 default:
		        			 break;
		        		 }
		            	 
		        	}catch(Exception ex){
		        		
		        	}
		        	
		             
		        }
			}
			break;
			
		//获取ClientId
		case Consts.GET_CLIENTID:
			String cid = bundle.getString("clientid");
			for (int i = 0; i < ehList.size(); i++)
				ehList.get(i).onBind(context,cid);	
			break;
		
		//绑定状态
		case Consts.BIND_CELL_STATUS:
			break;
			
		//sendMessage接口调用回执
		case Consts.THIRDPART_FEEDBACK:
			break;
		default:
			break;
		}
	}

}
