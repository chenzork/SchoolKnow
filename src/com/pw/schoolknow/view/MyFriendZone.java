package com.pw.schoolknow.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.pw.schoolknow.R;
import com.pw.schoolknow.bean.UserBean;
import com.pw.schoolknow.db.UserDB;
import com.pw.schoolknow.db.UserDB.getUserCallBack;
import com.pw.schoolknow.helper.BitmapHelper;
import com.pw.schoolknow.helper.CollegeHelper;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.view.base.BaseActivity;

public class MyFriendZone extends BaseActivity {
	
	@ViewInject(R.id.my_friendzone_headimg)
	private ImageView head;
	
	@ViewInject(R.id.my_friendzone_nickname)
	private TextView nickname;
	
	@ViewInject(R.id.my_friendzone_uid)
	private TextView userid;
	
	@ViewInject(R.id.my_friendzone_btn)
	private Button btn;
	
	@ViewInject(R.id.my_friendzone_remark)
	private TextView reamrk;
	@ViewInject(R.id.my_friendzone_address)
	private TextView address;
	@ViewInject(R.id.my_friendzone_mood)
	private TextView mood;
	@ViewInject(R.id.my_friendzone_college)
	private TextView college;
	
	private String uid;
	private String pushId;

	
	private BitmapUtils bmUtil;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_friendzone);
		setTitle("��ϸ��Ϣ");
		setTitleBar(R.drawable.btn_titlebar_back, 0);
		
		ViewUtils.inject(this);
		
		uid=getIntent().getStringExtra("uid");
		
		if(uid==null||uid.trim().length()==0){
			finish();
			return;
		}
		
		bmUtil=BitmapHelper.getHeadBitMap(this);
		BitmapHelper.setHead(bmUtil, head, uid);
		
		UserDB.getInstance(this).getUser(uid, new getUserCallBack() {
			public void getUserInfo(UserBean base) {
				nickname.setText(base.getNick());
				userid.setText(base.getEmail());
				reamrk.setText(base.getNick());
				address.setText("���� �ϲ�");
				
				Drawable drawable=null;
			    if(base.getSex().equals("��")){
			    	drawable = getResources().getDrawable(R.drawable.img_male); 	
			    }else{
			    	drawable = getResources().getDrawable(R.drawable.img_female);
			    }
			    drawable.setBounds(0, 0, 32, 32); 
			    nickname.setCompoundDrawablePadding(5);
			    nickname.setCompoundDrawables(null, null, drawable, null);
			    college.setText(CollegeHelper.getCollegeName(base.getCollege()));
			}

			@Override
			public void getPushId(String PushId) {
				pushId=PushId;
			}
		});
		
	}
	
	 @OnClick({R.id.my_friendzone_btn}) 
	 public void clickMethod(View v) {  
		 if(pushId==null||pushId.trim().length()==0){
			 T.showShort(this, "�ú����˻������쳣���ݲ�������");
		 }else{
			 Intent it=new Intent(this,Chat.class);
		      it.putExtra("uid", uid);
		      startActivity(it);
		 }
	      
	 }  
	

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			break;
		default:
			break;
		}
	}

}
