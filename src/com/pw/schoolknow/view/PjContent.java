package com.pw.schoolknow.view;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.pw.schoolknow.R;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.MyPj;
import com.pw.schoolknow.widgets.MyProgressBar;

public class PjContent extends BaseActivity {
	
	public String[] pj_lv_tip={
			"�̷��Ͻ���Ϊ��ʦ��",
			"�ϸ�Ҫ��ѧ����������ҵ�������������渺��",
			"ע��ʦ��֮�乵ͨ��������ȡѧ�����",
			"�������棬��������",
			"�ڿβ�η���������׼ȷ�����ݳ�ʵ���ص�ͻ��",
			"����ͨ����ѧ����������������������Ȥ",
			"���ձ����ƣ���������ϵʵ��",
			"������Ч�������ִ���ѧ�����ֶ��ϿΣ����鹤��",
			"���ʩ�̣�ע������������ѧ�������Լ��Ŀ���",
			"ͨ����ѧ�����տγ����ݣ������ѧϰ����"
	};
	
	@ViewInject(R.id.pj_tip)
	private TextView tip;
	@ViewInject(R.id.pj_content_score)
	private  TextView mark;
	
	private LinearLayout pj_layout;

	private SeekBar seekbar;
	@ViewInject(R.id.pj_content_editor)
	private EditText et;
	@ViewInject(R.id.pj_content_sure)
	private Button submit;
	@ViewInject(R.id.pj_content_cancel)
	private Button cancel;
	
	private MyPj[] pjArray;
	
	private String tid;
	private String uid;
	private int scoreMark=90;
	private Intent it;
	
	private String position;
	
	private static final String BaseUrl="http://schoolknow.sturgeon.mopaas.com/pj"
			+ "/pj.php";
	
	private MyProgressBar mpb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_access_content);
		
		it=getIntent();
		setTitle(it.getStringExtra("teacher"));
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		ViewUtils.inject(this);
		
		tid=it.getStringExtra("tid");
		uid=it.getStringExtra("uid");
		position=it.getStringExtra("position");
		LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		param.setMargins(0, 10, 0, 10);
		pj_layout=(LinearLayout) super.findViewById(R.id.access_content_selector);
		pjArray=new MyPj[pj_lv_tip.length];
		for(int i=0;i<pj_lv_tip.length;i++){
			pjArray[i]=new MyPj(this,(i+1)+"��"+pj_lv_tip[i]);
			pj_layout.addView(pjArray[i],param);
		}
		seekbar=(SeekBar) super.findViewById(R.id.pj_sBar);
		seekbar.setMax(39);
		seekbar.setProgress(scoreMark-60);
		seekbar.setOnSeekBarChangeListener(new seekbarlistener());
		
		tip.setText("�������棬����һ�����ڿ�ˮƽ���������ʦ��");
		
	}
	@OnClick({R.id.pj_content_cancel,R.id.pj_content_sure})
	public void onclick(View v){
		if(v==cancel){
			finish();
		}else if(v==submit){
			for(MyPj pj:pjArray){
				if(pj.getSelectItem()==0){
					T.showLong(this, "�㻹��δ�������,�ݲ����ύ");
					return;
				}
			}
			String etVal=et.getText().toString().trim();
			if(etVal.length()>140){
				T.showLong(this, "���������ۣ�����140��֮��");
			}else{
				mpb=new MyProgressBar(this);
				mpb.setMessage("�����ύ��...");
				
				submit.setEnabled(false);
				cancel.setEnabled(false);
				String selectVal="";
				for(MyPj pj:pjArray){
					selectVal+=pj.getSelectItem()+"|";
				}
				
				if(scoreMark<60||scoreMark>=100){
					scoreMark=87;
				}
				HttpUtils http=new HttpUtils();
				RequestParams param=new RequestParams();
				param.addBodyParameter("action", "pj");
				param.addBodyParameter("uid", uid);
				param.addBodyParameter("pwd", it.getStringExtra("pwd"));
				param.addBodyParameter("tid", tid);
				param.addBodyParameter("score",String.valueOf(scoreMark));
				param.addBodyParameter("note", etVal);
				param.addBodyParameter("select", selectVal);
				http.send(HttpMethod.POST,BaseUrl,param,new RequestCallBack<String>() {
					public void onSuccess(ResponseInfo<String> info) {
						String result=info.result;
						T.showLong(PjContent.this, result);
						cancel.setEnabled(true);
						mpb.dismiss();
						if(result.indexOf("���̳ɹ�")!=-1){
							Intent it = new Intent();
							it.putExtra("score",String.valueOf(scoreMark));
							it.putExtra("position", position);
							setResult(RESULT_OK, it);
						}
						finish();
						
					}
					public void onFailure(HttpException arg0, String arg1) {
						T.showLong(PjContent.this, "���ӷ������쳣");
						submit.setEnabled(true);
						cancel.setEnabled(true);
						mpb.dismiss();
					}
				});
			}
		}
	}
	
	private class seekbarlistener implements OnSeekBarChangeListener{
		public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
			scoreMark=progress+60;
			mark.setText(String.valueOf(scoreMark));
			if(scoreMark>=90){
				tip.setText("�����ǳ����棬���нϸߵ��ڿ�ˮƽ��������ʦ��");
			}else if(scoreMark>=80){
				tip.setText("�������棬����һ�����ڿ�ˮƽ���������ʦ��");
			}else if(scoreMark>=70){
				tip.setText("����̬��һ�㣬�ڿ�ˮƽһ�㣨������ʦ��");
			}else{
				tip.setText("�����������棬�ڿ�ˮƽ�ϲ������ʦ��");
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			this.finish();
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
