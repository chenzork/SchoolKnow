package com.pw.schoolknow.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.helper.TermHelper;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.ClassSelect;
import com.pw.schoolknow.widgets.ClassSelect.ClassSelectInterface;
import com.pw.schoolknow.widgets.MyProgressBar;

public class ExamArrange extends BaseActivity{
	
	@ViewInject(R.id.exam_search_class)
	private Spinner Mclass;
	
	@ViewInject(R.id.exam_search_btn)
	private Button btn;
	
	@ViewInject(R.id.exam_arrange_class_select)
	private Button classSelect;
	
	private MyProgressBar mpb=null;

	private String classIdString="";
	private String englishIdStrng="";
	
	public ClassSelect newClass=null;
	private  Context mContext;
	
	private List<Map<String,Object>> list;
	
	private HttpUtils http;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_exam_arrange);
		setTitle("���԰���");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		mContext=this;
		ViewUtils.inject(this);
		http=new HttpUtils();
		list=new ArrayList<Map<String,Object>>();
		
		//ѡ��༶
		this.Mclass.setPrompt("��ѡ�����Ӣ��༶");

		btn.setOnClickListener(mClickImlp);
		classSelect.setOnClickListener(mClickImlp);
		Mclass.setOnItemSelectedListener(selectImpl);
		
	}
	
	private OnItemSelectedListener selectImpl=new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			try{
				englishIdStrng=list.get(position).get("id").toString();
			}catch(Exception e){
				englishIdStrng="";
			}
			
		}
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	};

	/**
	 * ��ѯ�Ͱ༶ѡ��ť�¼�����
	 */
	private OnClickListener mClickImlp=new OnClickListener() {
		public void onClick(View v) {
			if(v==btn){
				Intent it=new Intent(mContext,ExamArrangeContent.class);
				it.putExtra("classid",classIdString);
				it.putExtra("englishid",englishIdStrng);
				startActivity(it);
			}else if(v==classSelect){
				newClass=new ClassSelect(mContext);
				newClass.setOnclickListener(new ClassSelectInterface() {
					@Override
					public void onClick(View view) {
						newClass.dismiss();
						mpb=new MyProgressBar(mContext);
						mpb.setMessage("����Ӣ��༶��Ϣ...");
						classIdString=newClass.getClassId();
						classSelect.setText(newClass.getClassN());
						if(TermHelper.hasEnglishClass(classIdString)){
							http.send(HttpMethod.GET, ServerConfig.HOST+
									"/schoolknow/api/ExamarrangeApi.php?action=getEnglish&classid="+classIdString,
											null, new RequestCallBack<String>() {
										public void onSuccess(ResponseInfo<String> info) {
											try {
												list.clear();
												list.addAll(new JsonHelper(info.result).parseJson(
														new String[]{"id","name"}));
												List<String> className=new ArrayList<String>();
												for(Map<String,Object> map:list){
													className.add(map.get("name").toString());
												}
												ArrayAdapter<String> classAdapter=new ArrayAdapter<String>(ExamArrange.this,
														android.R.layout.simple_spinner_item,className);
												classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
												Mclass.setAdapter(classAdapter);
												mpb.dismiss();
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
										public void onFailure(HttpException arg0, String arg1) {
											T.showLong(mContext, "���ӷ������쳣");
										}
									});
						}else{
							mpb.dismiss();
							englishIdStrng="";
							List<String> className=new ArrayList<String>();
							className.add("��ѡ��Ӣ��༶");
							ArrayAdapter<String> classAdapter=new ArrayAdapter<String>(ExamArrange.this,
									android.R.layout.simple_spinner_item,className);
							classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							Mclass.setAdapter(classAdapter);
						}
					}
				});
			}
		}
	};

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
