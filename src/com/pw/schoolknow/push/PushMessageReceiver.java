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
		//͸������
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
		        		 //���������ظ�
		        		 case InformConfig.PUSH_SFComment:
		        			 if(item.getReceiveUid().equals(lh.getUid())){
		            			 new InformDB(context, lh.getUid()).saveInform(item);
		                         NotifyUtils.tip("���������������»ظ�","��Ϣ֪ͨ", item.getSendName()+" �ڻ��������ظ�����Ķ�̬",
			                    		 Inform.class);
		                	 }
		        			 break;
		        	     //��Ӻ�������
		        		 case InformConfig.PUSH_ADDREQUEST:
		        			 if(item.getReceiveUid().equals(lh.getUid())){
		        				 new InformDB(context, lh.getUid()).saveInform(item);
		                         
		                         NotifyUtils.tip("��Ӻ�������","��Ӻ���֪ͨ", item.getSendName()+" ���������Ϊ����",
			                    		 Inform.class);
		                         
		        			 }
		        			 break;
		        		 //��Ӻ��ѷ�����Ϣ
		        		 case InformConfig.PUSH_ADDCALLBACK:
		        			 int back=Integer.parseInt(item.getContent());
		        			 String backinfo="";
		        			 if(back==1){
		        				 backinfo=" ͬ������ĺ�������";
		        				 FriendDB.getInstance(context,lh.getUid()).
		        				 add(item.getSendUid(), item.getSendName());
		        				 new InformDB(context, lh.getUid()).saveInform(item,"1");
		        			 }else{
		        				 backinfo=" �ܾ�����ĺ�������";
		        				 new InformDB(context, lh.getUid()).saveInform(item,"3");
		        			 }
		                     NotifyUtils.tip("��������","������������Ϣ", item.getSendName()+backinfo,
		                    		 Inform.class);
		                     
		        			 break;
		        		 case InformConfig.PUSH_CHAT:
		        			 ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE); 
		        		     RunningTaskInfo info = manager.getRunningTasks(1).get(0); 
		        		     String shortClassName = info.topActivity.getClassName();    //����
		        		     
		        		     //����Chat����
		        		     if(Chat.class!=Class.forName(shortClassName)){
		                         SendChatMessageBean messageItem=
		    							 MyApplication.getInstance().getGson().fromJson(item.getContent(),SendChatMessageBean.class);
		                         ChatMessageDB.getInstance(context, lh.getUid())
		 						.insert(messageItem.getType(),item.getSendUid(),messageItem.getContent(),
		 								messageItem.getTime(),"1","0");
		                         
		                         NotifyUtils.tip("������Ϣ","������Ϣ֪ͨ", item.getSendName()+" ������������Ϣ",
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
			
		//��ȡClientId
		case Consts.GET_CLIENTID:
			String cid = bundle.getString("clientid");
			for (int i = 0; i < ehList.size(); i++)
				ehList.get(i).onBind(context,cid);	
			break;
		
		//��״̬
		case Consts.BIND_CELL_STATUS:
			break;
			
		//sendMessage�ӿڵ��û�ִ
		case Consts.THIRDPART_FEEDBACK:
			break;
		default:
			break;
		}
	}

}
