package com.pw.schoolknow.view;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.UserAdapter;
import com.pw.schoolknow.config.DecodeConfig;
import com.pw.schoolknow.config.PathConfig;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.helper.SystemHelper;
import com.pw.schoolknow.utils.BitmapUtil;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.CircleImageView;

public class User extends BaseActivity {
	
	private CircleImageView userImg;
	private TextView userName;
	private ListView lv;
	
	private UserAdapter adapter;    
	
	private PointF startPoint = new PointF();
	public View head;
	
	public LoginHelper lh;
	
	private Context mContext;
	
	private int startHeight;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_user);
		setTitle("我的主页");
		
		LayoutInflater inflater=LayoutInflater.from(this);
		head = inflater.inflate(R.layout.part_user_head, null,false);
		
		userImg=(CircleImageView) head.findViewById(R.id.myzone_user_img);
		userImg.setBorderColor(Color.WHITE);
		userImg.setBorderWidth(5);
		
		mContext=this;
		startHeight=head.getHeight();
		
		lh=new LoginHelper(User.this);
		String path=PathConfig.HEADPATH+DecodeConfig.decodeHeadImg(lh.getUid())+".pw";
		Bitmap dBitmap=null;
		if(new File(path).exists()){
			dBitmap = BitmapFactory.decodeFile(path);
		}else{
			dBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.default_head);
			new AsyncLoadHead().execute();
		}
	    userImg.setImageBitmap(dBitmap);
	    
	    //设置用户名和性别
	    userName=(TextView) head.findViewById(R.id.myzone_user_name);
	    userName.setText(lh.getNickname());
	    Drawable drawable=null;
	    if(lh.isBoy()){
	    	drawable = getResources().getDrawable(R.drawable.img_male); 	
	    }else{
	    	drawable = getResources().getDrawable(R.drawable.img_female);
	    }
	    drawable.setBounds(0, 0, 32, 32); 
    	userName.setCompoundDrawables(null, null, drawable, null);
	    
	    userImg.setOnClickListener(new userHeadClick());
	    
	    lv=(ListView) super.findViewById(R.id.user_lv);
		lv.addHeaderView(head);
		
		adapter=new UserAdapter(this, new String[]{"个人资料","我的消息","我的好友","聊天记录","设置"});
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new onItemClickImp());
		lv.requestDisallowInterceptTouchEvent(true);
		//head.setOnTouchListener(OnTouchImpl);
	}
	
	
	protected void onRestart() {
		adapter.notifyDataSetChanged();
		userName.setText(lh.getNickname());
		String path=PathConfig.HEADPATH+DecodeConfig.decodeHeadImg(lh.getUid())+".pw";
		Bitmap dBitmap=null;
		if(new File(path).exists()){
			dBitmap = BitmapFactory.decodeFile(path);
		}else{
			dBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.default_head);
			new AsyncLoadHead().execute();
		}
		userImg.setImageBitmap(dBitmap);
		super.onRestart();
	}



	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
	
	protected void onResume() {
		if(!lh.hasLogin()){
			finish();
		}
		super.onResume();
	}
	
	private OnTouchListener OnTouchImpl=new OnTouchListener() {
		public boolean onTouch(View arg0, MotionEvent event) {
			switch (event.getAction() & MotionEvent.ACTION_MASK){
			// 手指压下屏幕
			case MotionEvent.ACTION_DOWN:
				int[] location = new int[2];
				if (location[1] >= 0) {
					startPoint.set(event.getX(), event.getY());
				}
				break;
			// 手指在屏幕上移动，改事件会被不断触发
			case MotionEvent.ACTION_MOVE:
				float dy = event.getY() - startPoint.y;
				int currentHeight=(int) (head.getHeight()+dy);
				if(currentHeight<SystemHelper.getScreenHeight(mContext)/2+70&&currentHeight>=startHeight){
					ViewGroup.LayoutParams param=head.getLayoutParams();
					param.height=currentHeight;
					head.setLayoutParams(param);
				}
				break;
				// 手指离开屏幕
			case MotionEvent.ACTION_UP:
				ViewGroup.LayoutParams param=head.getLayoutParams();
				param.height=startHeight;
				head.setLayoutParams(param);
				break;
		}
			return false;
		}
	};
	

	public class AsyncLoadHead extends AsyncTask<Void, Void, Bitmap> {
		protected Bitmap doInBackground(Void... params) {
			return GetUtil.getBitMap(ServerConfig.HOST+"/schoolknow/manage/head/"+
					DecodeConfig.decodeHeadImg(lh.getUid())+".pw");
		}

		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if(result!=null){
				final String path=PathConfig.HEADPATH;
				final String name=DecodeConfig.decodeHeadImg(lh.getUid())+".pw";
				try {
					BitmapUtil.saveImg(result,path,name);
				} catch (Exception e) {
					e.printStackTrace();
				}
				onCreate(null);
			}
		}
		
		
	}
	
	protected void HandleTitleBarEvent(int buttonId, View v) {

	}
	
	//个性设置
	public class onItemClickImp implements OnItemClickListener{

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch(position){
			case 1:
				Intent it0=new Intent(User.this,UserInfo.class);
				startActivity(it0);
				break;
			case 2:
				Intent it=new Intent(User.this,Inform.class);
				startActivity(it);
				break;
			case 3:
				startActivity(new Intent(User.this,FriendsList.class));
				break;
			case 4:
				startActivity(new Intent(User.this,ChatRecords.class));
				break;
			case 5:
				Intent it2=new Intent(User.this,Setting.class);
				startActivity(it2);
				break;
			default:
				break;
			}
			
		}
		
	}
	
	public class userHeadClick implements OnClickListener{
		public void onClick(View v) {
			Intent it=new Intent(User.this,UserInfo.class);
			startActivity(it);
			finish();
		}
		
	}

	

}
