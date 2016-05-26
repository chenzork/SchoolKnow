package com.pw.schoolknow.view;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.pw.schoolknow.R;
import com.pw.schoolknow.app.MyApplication;
import com.pw.schoolknow.bean.InformBase;
import com.pw.schoolknow.bean.UserBean;
import com.pw.schoolknow.config.InformConfig;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.db.FriendDB;
import com.pw.schoolknow.db.UserDB;
import com.pw.schoolknow.helper.BitmapHelper;
import com.pw.schoolknow.helper.CollegeHelper;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.push.AsyncSend;
import com.pw.schoolknow.push.AsyncSend.OnSendScuessListener;
import com.pw.schoolknow.utils.ImageUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.utils.TimeUtil;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.MyProgressBar;

public class FriendZone extends BaseActivity {
	
	@ViewInject(R.id.friend_zone_head)
	private ImageView head;
	@ViewInject(R.id.friend_zone_nick)
	private TextView  nick;
	@ViewInject(R.id.friend_zone_college)
	private TextView  college;
	@ViewInject(R.id.friend_zone_sex)
	private ImageView  sex;
	
	@ViewInject(R.id.friend_zone_add)
	private Button add;
	@ViewInject(R.id.friend_zone_chat)
	private Button chat;
	
	public String uid;
	public MyProgressBar mpb;
	public String PushId;
	
	public HttpUtils http;
	public HttpHandler<String> handler;
	private AsyncSend sendUtil;
	
	public BitmapUtils bmUtils;
	public LoginHelper lh;
	public Context mcontext;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_zone);
		setTitle("��������");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		ViewUtils.inject(this);
		lh=new LoginHelper(this);
		mcontext=this;
		
		add.setClickable(false);
		chat.setClickable(false);
		
		
		Intent it=getIntent();
		uid=it.getStringExtra("uid");
		if(uid==null||uid.trim().length()==0){
			return;
		}
		
		//����Ǻ��ѣ���ת�����ѽ���ҳ��
		if(FriendDB.getInstance(this, lh.getUid()).exist(uid)){
			Intent it2=new Intent(FriendZone.this,MyFriendZone.class);
			it2.putExtra("uid",uid);
			startActivity(it2);
			finish();
			return;
		}
		
		mpb=new MyProgressBar(this);
		mpb.setMessage("���ڼ�����...");
		
		//��ȡ�û���Ϣ
		http=new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid",uid);
		handler=http.send(HttpMethod.POST, ServerConfig.HOST+"/schoolknow/Friend/api/getUserInfo.php",
				params,new RequestCallBack<String>() {
			public void onSuccess(ResponseInfo<String> info) {
				try {
					JSONObject JsonData = new JSONObject(info.result);
					nick.setText(JsonData.getString("nickname"));
					college.setText(CollegeHelper.getCollegeName(JsonData.getString("college")));
				    if(!JsonData.getString("sex").equals("Ů")){
				    	sex.setImageResource(R.drawable.ic_knock_gender_male);	
				    }else{
				    	sex.setImageResource(R.drawable.ic_knock_gender_famale);
				    }
				    PushId=JsonData.getString("client");
				    
				    UserBean base=new UserBean(uid, JsonData.getString("sex"),
				    		"", JsonData.getString("nickname"), PushId, JsonData.getString("college"));
				    UserDB.getInstance(mcontext).save(base);
				    add.setClickable(true);
					chat.setClickable(true);
					mpb.dismiss();
				} catch (Exception e) {
					nick.setText("������Ϣ�����쳣");
					mpb.dismiss();
				}
			}
			public void onFailure(HttpException arg0, String arg1) {
				nick.setText("������Ϣ�����쳣");
				mpb.dismiss();
			}
		});
		
		//����ͷ��
		bmUtils=BitmapHelper.getHeadBitMap(mcontext);
		BitmapHelper.setHead(bmUtils, head, uid,headCallback);
	}
	
	public BitmapLoadCallBack<ImageView> headCallback=new BitmapLoadCallBack<ImageView>() {
		public void onLoadCompleted(ImageView head, String arg1, Bitmap bm,
				BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
			head.setImageBitmap(ImageUtil.toRoundCorner(bm, 20));
		}
		public void onLoadFailed(ImageView head, String arg1, Drawable arg2) {
			head.setImageResource(R.drawable.default_head);
		}
	};
	
	@OnClick({ R.id.friend_zone_add, R.id.friend_zone_chat })
	 public void clickMethod(View v) {
		if(!lh.hasLogin()){
			T.showShort(this, "���¼����ִ�иò���");
		}else{
			 if(v==add){
				 if(uid.equals(lh.getUid())){
					 T.showShort(this, "��������Լ�Ϊ����");
				 }else if(FriendDB.getInstance(mcontext, lh.getUid()).exist(uid)){
					 T.showShort(this, "���û��Ѿ�����ĺ���");
				 }else{
					 if(PushId!=null&&PushId.trim().length()!=0){
						 add.setClickable(false);
						 InformBase formItem=new InformBase(
									InformConfig.PUSH_ADDREQUEST,lh.getUid(), lh.getNickname(),"",
									TimeUtil.getCurrentTime()+"","",uid,PushId.trim());
						 sendUtil=new AsyncSend(MyApplication.getInstance().getGson().toJson(formItem), PushId.trim());
						 sendUtil.send();
						 sendUtil.setOnSendScuessListener(new OnSendScuessListener() {
							public void sendScuess() {
								T.showShort(mcontext, "�Ѿ���ú��ѷ�����Ӻ�������");
								new Handler().postDelayed(new Runnable() {
									public void run() {
										add.setClickable(true);
									}
								}, 10*1000);
							}

							@Override
							public void sendFailure() {
								// TODO Auto-generated method stub
								
							}
						});
					 }else{
						 T.showShort(this, "���û��˺��쳣���ݲ�����Ӻ���");
					 }
				 }
				 
			 }else if(v==chat){
				 if(uid.length()!=0){
					 Intent it=new Intent(FriendZone.this,Chat.class);
					 it.putExtra("uid", uid);
					 startActivity(it);
				 }else{
					 T.showShort(this, "���û��˺��쳣���ݲ��ܷ�����Ϣ");
				 }
			 }
		}
	 }
	
	
	//���ؼ�ֹͣ����
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
			handler.stop();
			mpb.dismiss();
		}
		return super.onKeyDown(keyCode, event);
	}
	

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		default:
			break;
		}
	}
	

}
