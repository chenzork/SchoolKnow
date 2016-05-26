package com.pw.schoolknow.view;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.pw.schoolknow.R;
import com.pw.schoolknow.helper.NetHelper;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.MyProgressBar;

public class PjLogin extends BaseActivity {
	
	@ViewInject(R.id.login_online_course_id)
	private EditText uid;
	@ViewInject(R.id.login_online_course_pwd)
	private EditText pwd;
	@ViewInject(R.id.login_online_course_btn)
	private Button btn;
	
	private Context mContext;
	
	private String stuid=null;
	private String password=null;
	private MyProgressBar pb=null;
	
	private HttpUtils http;
	private static final String BaseUrl="http://schoolknow.sturgeon.mopaas.com/pj"
			+ "/getLoginState.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_online_course_login);
		setTitle("评教登录");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		mContext=this;
		ViewUtils.inject(this);
	}
	
	@OnClick(R.id.login_online_course_btn)
	public void onsubmit(View v){
		if(NetHelper.isNetConnected(mContext)){
			stuid=uid.getText().toString();
			password=pwd.getText().toString();
			if(stuid.trim().equals("")){
				T.showShort(PjLogin.this, "请输入你的学号");
			}else if(password.trim().equals("")){
				T.showShort(PjLogin.this, "请输入评教的密码");
			}else{
				pb=new MyProgressBar(mContext);
				pb.setMessage("正在登陆中...");
				http=new HttpUtils();
				http.send(HttpMethod.GET,BaseUrl+"?pj_uid="+stuid+"&pj_pwd="+password, 
						null,new RequestCallBack<String>() {
					public void onSuccess(ResponseInfo<String> info) {
						String result=info.result;
						if(result.trim().length()!=0){
							try {
								JSONObject JsonData = new JSONObject(result);
								String state=JsonData.getString("state");
								if(state.equals("success")){
									T.showLong(mContext,"登录成功");
									Intent it=new Intent(mContext,PjList.class);
									it.putExtra("data", result);
									it.putExtra("pwd", password);
									startActivity(it);
								}else if(state.equals("failure")){
									T.showLong(mContext,JsonData.getString("content"));
								}else{
									T.showLong(mContext,"连接服务器异常");
								}
							} catch (JSONException e) {
								T.showShort(PjLogin.this, "解析数据异常");
							}
						}else{
							T.showShort(PjLogin.this, "非法访问");
						}
						pb.dismiss();
					}
					public void onFailure(HttpException arg0, String arg1) {
						T.showShort(PjLogin.this, "连接服务器异常");
						pb.dismiss();
					}
				});
			}
		}else{
			T.showShort(mContext, R.string.net_error_tip);
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
