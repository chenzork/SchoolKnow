package com.pw.schoolknow.view;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.EmailAutoTipAdapter;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.service.Init;
import com.pw.schoolknow.service.UpdateFriendsService;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.PatternUtils;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.widgets.MyProgressBar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class Login extends  Activity implements OnClickListener {
	
	private AutoCompleteTextView uid=null;
	private TextView pwd=null;
	private Button login=null;
	private Button reg=null;
	private MyProgressBar mpb;
	
	private static final int Login_State=1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.act_login);
		
		uid=(AutoCompleteTextView) super.findViewById(R.id.login_edit_uid);
		pwd=(TextView) super.findViewById(R.id.login_edit_pwd);
		login=(Button) super.findViewById(R.id.login_btn_login);
		reg=(Button) super.findViewById(R.id.login_btn_reg);
		
		login.setOnClickListener(this);
		reg.setOnClickListener(this);
		
		//���������Զ���ʾ
		 final EmailAutoTipAdapter adapter = new EmailAutoTipAdapter(this);  
		this.uid.setAdapter(adapter);
		this.uid.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {
				String input = s.toString();  
                adapter.mList.clear();  
                if (input.length() > 0) {  
                    for (int i = 0; i < EmailAutoTipAdapter.emailArray.length; ++i) {
                    	int pos=input.indexOf("@");
                    	if(pos!=-1){
                    		input=input.substring(0,pos);
                    	}
                        adapter.mList.add(input + EmailAutoTipAdapter.emailArray[i]);  
                    }  
                }  
                adapter.notifyDataSetChanged();  
                uid.showDropDown();  
			}
		});
	}

	@Override
	public void onClick(View view) {
		if(view==login){
			final String stuid=uid.getText().toString();
			final String password=pwd.getText().toString();
			if(!PatternUtils.CheckEmail(stuid)){
				T.showShort(this,"������������");
				return;
			}
			mpb=new MyProgressBar(Login.this);
			mpb.setMessage("���ڵ�¼��...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					Message msg=new Message();
					msg.what=Login_State;
					String param="uid="+stuid+"&pwd="+password;					
					msg.obj=GetUtil.getRes(ServerConfig.HOST+"/schoolknow/login.php?"+param);
					handler.sendMessage(msg);
				}
			}).start();
		}else if(view==reg){
			//��ת��ע��ҳ��
			Intent it=new Intent(Login.this,Register.class);
			startActivity(it);
		}
	}
	
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case Login_State:
				if(!msg.obj.toString().equals("false")){
					if(new LoginHelper(Login.this).login(msg.obj.toString())){
						
						//���������·���
						Intent updateIntent = new Intent(Login.this, 
				                Init.class);
				       startService(updateIntent); 
				       
				       //���غ�����Ϣ
				       startService(new Intent(Login.this, 
				                UpdateFriendsService.class)); 
						
						T.showLong(Login.this,"��½�ɹ�");
						Intent it=new Intent(Login.this,Main.class);
						it.putExtra("param","2");
						startActivity(it);
						finish();
						
					}
				}else{
					T.showLong(Login.this,"�˺Ż���������������");
				}
				break;
			default:
				break;
			}
			mpb.dismiss();
		}
		
	};

	
	

}
