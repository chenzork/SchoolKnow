package com.pw.schoolknow.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.pw.schoolknow.R;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.helper.NetHelper;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.MyProgressBar;

public class Suggest extends BaseActivity {
	
	private EditText content;
	private LoginHelper lh;
	private MyProgressBar mpb;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_suggest);
		setTitle("������Ϣ");
		setTitleBar(R.drawable.btn_titlebar_back,"",0,"����");
		
		content=(EditText) super.findViewById(R.id.suggest_content);
		lh=new LoginHelper(this);
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			String SuggestVal=content.getText().toString();
			if(SuggestVal.length()<=0){
				T.showShort(this,"��������Ҫ����������");
			}else if(SuggestVal.length()>256){
				T.showShort(this,"��������������256�ַ���");
			}else{
				if(NetHelper.isNetConnected(this)){
					mpb=new MyProgressBar(this);
					mpb.setMessage("�����ϴ�������Ϣ...");
					new AsyncUpload().execute(SuggestVal);
				}else{
					T.showShort(this,R.string.net_error_tip);
				}
			}
			break;
		default:
			break;
		}
	}
	
	public class AsyncUpload extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... str) {
			List<NameValuePair> params=new ArrayList<NameValuePair>(); 
			params.add(new BasicNameValuePair("uid",lh.getUid()));
			params.add(new BasicNameValuePair("token",lh.getToken()));
			params.add(new BasicNameValuePair("suggest",str[0]));
			return GetUtil.sendPost(ServerConfig.HOST+"/schoolknow/manage/suggest.php", params);
		}

		protected void onPostExecute(String result) {
			mpb.dismiss();
			if(result.equals("success")){
				T.showLong(Suggest.this,"��л���ķ���,����֧��������ǰ���������!");
				Intent it=new Intent(Suggest.this,Setting.class);
				startActivity(it);
				finish();
			}else{
				T.showLong(Suggest.this,"����ʧ��,�����³���");
			}
			super.onPostExecute(result);
		}
		
		
		
	}

}
