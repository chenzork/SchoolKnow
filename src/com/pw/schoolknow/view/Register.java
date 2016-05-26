package com.pw.schoolknow.view;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.pw.schoolknow.R;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.SystemHelper;
import com.pw.schoolknow.utils.EncodeUtil;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.PatternUtils;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.MyProgressBar;


public class Register extends BaseActivity {
	
	private MyProgressBar mpb;
	private String params;
	private EditText email,pwd,pwd2,nickname,stuid;
	private Spinner sex;
	private LinearLayout step1,step2,step3;
	private Button btn1,btn2,btn3;
	private String sexVal="";
	
	List<CharSequence> sexSelect=new ArrayList<CharSequence>();
	
	private static final int CHECK_EMAIL=1;
	private static final int REGISTER_INFO=2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_register);
		setTitle("����ע��");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		this.nickname=(EditText) super.findViewById(R.id.register_nickname);
		this.sex=(Spinner) super.findViewById(R.id.register_sex);
		this.stuid=(EditText) super.findViewById(R.id.register_stuid);
		this.email=(EditText) super.findViewById(R.id.register_email);
		this.pwd=(EditText) super.findViewById(R.id.register_pwd);
		this.pwd2=(EditText) super.findViewById(R.id.register_pwd2);
		
		this.step1=(LinearLayout) super.findViewById(R.id.register_step1);
		this.step2=(LinearLayout) super.findViewById(R.id.register_step2);
		this.step3=(LinearLayout) super.findViewById(R.id.register_step3);
		
		this.btn1=(Button) super.findViewById(R.id.register_btn_reg1);
		this.btn2=(Button) super.findViewById(R.id.register_btn_reg2);
		this.btn3=(Button) super.findViewById(R.id.register_btn_reg3);		
		this.btn1.setOnClickListener(new onBtnClickListener());
		this.btn2.setOnClickListener(new onBtnClickListener());
		this.btn3.setOnClickListener(new onBtnClickListener());
		
		this.sexSelect.add("��");
		this.sexSelect.add("Ů");
		this.sex.setPrompt("��ѡ������Ա�");
		ArrayAdapter<CharSequence> adapter=new ArrayAdapter<CharSequence>(this,
        		android.R.layout.simple_spinner_item,this.sexSelect);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sex.setAdapter(adapter);
		sex.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				sexVal=sexSelect.get(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public class onBtnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			String emailVal=Editval(email);
			String pwdVal=Editval(pwd);
			String pwd2Val=Editval(pwd2);
			String nicknameVal=Editval(nickname);
			String stuidVal=Editval(stuid);
			if(v==btn1){
				//ע���һ��
				if(emailVal.equals("")){
					T.showShort(Register.this, "����������");
				}else if(!PatternUtils.CheckEmail(emailVal)){
					T.showShort(Register.this, "�����ʽ����");
				}else if(emailVal.length()>=50){
					T.showShort(Register.this, "���䳤������Ϊ50�ַ���");
				}else if(pwdVal.equals("")){
					T.showShort(Register.this, "���벻��Ϊ��");
				}else if(pwdVal.length()<8||pwdVal.length()>16){
					T.showShort(Register.this, "��������Ϊ8-16�ַ�");
				}else if(!PatternUtils.CheckPwd(pwdVal)){
					T.showShort(Register.this, "����ֻ�ܰ�����ĸ������");
				}else if(!pwdVal.equals(pwd2Val)){
					T.showShort(Register.this, "2����������벻һ��");
				}else{
					mpb=new MyProgressBar(Register.this);
					mpb.setMessage("������֤����...");
					new Thread(new Runnable() {
						@Override
						public void run() {
							Message msg=new Message();
							msg.what=CHECK_EMAIL;
							String p="action=ce&email="+Editval(email);
							String t=GetUtil.getRes(ServerConfig.HOST+"/schoolknow/register.php?"+p);					
							msg.obj=t;
							handler.sendMessage(msg);
						}
					}).start();
				}
			}else if(v==btn2){
				//ע��ڶ���
				if(nicknameVal.length()<2||nicknameVal.length()>10){
					T.showShort(Register.this, "�ǳ�����Ϊ2-10�ַ�");			
				}else{
					step2.setVisibility(View.GONE);
					step3.setVisibility(View.VISIBLE);
				}
			}else if(v==btn3){
				if(stuidVal.length()!=14&&!stuidVal.equals("")){
					T.showShort(Register.this, "ѧ����������");			
				}else{
					//ע�����
					mpb=new MyProgressBar(Register.this);
					mpb.setMessage("����ע����...");
					String clientId="";//new PushSharePreference(Register.this).getUserId();
					String IMEI=SystemHelper.getTelephoneIMEI(Register.this);
					
					params="email="+emailVal+"&pwd="+pwdVal+"&nick="+EncodeUtil.ToUtf8(nicknameVal)+
							"&clientId="+clientId+"&sex="+EncodeUtil.ToUtf8(sexVal)+"&stuid="
						+stuidVal+"&imei="+IMEI;
										
					new Thread(new Runnable() {
						@Override
						public void run() {
							Message msg=new Message();
							msg.what=REGISTER_INFO;
							String t=GetUtil.getRes(ServerConfig.HOST+"/schoolknow/register.php?"+params);					
							msg.obj=t;
							handler.sendMessage(msg);
						}
					}).start();
				}
			}
		}
		
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case REGISTER_INFO:
				String tip=msg.obj.toString();
				if(tip.equals("bind error")){
					T.showShort(Register.this, "��ѧ���Ѿ��������˰�");	
				}else if(tip.equals("reg success")){
					T.showShort(Register.this, "��ϲ��ע��ɹ�");	
					//2s����ת��¼ҳ��
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							Intent it=new Intent(Register.this,Main.class);
							it.putExtra("param","2");
							startActivity(it);
							finish();
						}
					},2000);
				}
				break;
			case CHECK_EMAIL:
				if(msg.obj.toString().equals("f")){   //����ע��
					step1.setVisibility(View.GONE);
					step2.setVisibility(View.VISIBLE);
				}else{
					T.showShort(Register.this,"�������Ѿ���ע��");
				}
			default:
				break;
			}
			mpb.dismiss();
			
		}
		
	};
	
	/**
	 * ��ȡ������ֵ
	 * @param et
	 * @return
	 */
	public String Editval(EditText et){
		return et.getText().toString();
		
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
