package com.pw.schoolknow.view;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.InformAdapter;
import com.pw.schoolknow.app.MyApplication;
import com.pw.schoolknow.bean.InformBase;
import com.pw.schoolknow.bean.SchoolFellowBean;
import com.pw.schoolknow.config.InformConfig;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.db.InformDB;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.MyProgressBar;
import com.pw.schoolknow.push.PushEventHandler;
import com.pw.schoolknow.push.PushMessageReceiver;


@SuppressLint("HandlerLeak")
public class Inform extends BaseActivity implements PushEventHandler {
	
	private ListView lv=null;
	private ImageView img;
	private List<InformBase> list;
	private InformAdapter adapter;
	private LoginHelper lh;
	
	public MyProgressBar mpb;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_inform);
		setTitle("�ҵ���Ϣ");
		
		lh=new LoginHelper(this);
		
		img=(ImageView) super.findViewById(R.id.inform_empty_failed);
		this.lv=(ListView) super.findViewById(R.id.inform_lv);
		this.list=new InformDB(this, lh.getUid()).getList();
		img.setVisibility(View.GONE);
		
		
		adapter=new InformAdapter(this, list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new onItemClickLmp());
		img.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
			}
		});
		
	}
	
	protected void onRestart() {
		super.onRestart();
		this.list.clear();
		this.list.addAll(new InformDB(this, lh.getUid()).getList());
		adapter.notifyDataSetChanged();
	}




	class onItemClickLmp implements OnItemClickListener{
		public void onItemClick(AdapterView<?> parent, View arg1, int position,
				long arg3) {
			if(position>=InformAdapter.defaultList.length){
				try{
					InformBase item=(InformBase) lv.getItemAtPosition(position);
					switch(item.getType()){
					case InformConfig.PUSH_SFComment:
						mpb=new MyProgressBar(Inform.this);
						mpb.setMessage("���ڼ�����...");
						new InformDB(Inform.this, lh.getUid()).setRead(item.getId());
						final String targetId=item.getTargetId();
						new Thread(new Runnable() {
							public void run() {
								String result=GetUtil.getRes(ServerConfig.HOST+"/schoolknow/Square/getNew.php"
										+"?action=getContent&num="+targetId);
								Message msg=new Message();
								msg.what=102;
								msg.obj=result;
								handler.sendMessage(msg);
							}
						}).start();
						break;
					case InformConfig.PUSH_ADDREQUEST:
						Intent it=new Intent(Inform.this,FriendAddVerify.class);
						startActivity(it);
						break;
					default:
						break;
					}
				}catch(Exception e){}
			}else{
				switch(position){
				case 0:
					startActivity(new Intent(Inform.this,FriendAddVerify.class));
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				default:
					break;
				}
			}
			
		}
		
	}
	
	//��ȡ����
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==102){
				try {
					List<Map<String,Object>> dataList=new JsonHelper(msg.obj.toString()).parseJson(
							new String[]{"id","uid","nn","ct","tm","num"});
					Map<String,Object> map=dataList.get(0);
					SchoolFellowBean base=new SchoolFellowBean(Integer.parseInt(map.get("id").toString()),
							map.get("uid").toString(),map.get("nn").toString(),map.get("tm").toString(),
							map.get("ct").toString(),map.get("num").toString());
					Intent it=new Intent(Inform.this,SchoolFellowContent.class);
					Bundle mBundle = new Bundle();  
				    mBundle.putSerializable(Index.SER_KEY,base);  
				    it.putExtras(mBundle); 
					startActivity(it);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mpb.dismiss();
		}
		
	};
	
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		default:
			break;
		}
	}
	
	protected void onPause() {
		PushMessageReceiver.ehList.remove(this);// �Ƴ�����
		super.onPause();
	}


	@Override
	protected void onResume() {
		PushMessageReceiver.ehList.add(this);// �������͵���Ϣ
		super.onResume();
	}

	//ˢ����Ϣ����
	public void onMessage(String tm) {
		 InformBase item=MyApplication.getInstance().getGson().fromJson(tm,InformBase.class);
		 if(item.getType()==InformConfig.PUSH_SFComment){
			 
		 }
	}
	@Override
	public void onBind(Context context, String channelId) {
		// TODO Auto-generated method stub
		
	}

}
