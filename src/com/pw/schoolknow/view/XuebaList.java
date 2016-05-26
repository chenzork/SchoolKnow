package com.pw.schoolknow.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.MyProgressBar;

public class XuebaList extends BaseActivity {
	
	@ViewInject(R.id.score_data_listview)
	private ListView lv;
	
	private List<Map<String,Object>> list;
	private SimpleAdapter adapter;
	
	private MyProgressBar mpb;
	
	private String classId="";
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_score_data);
		setTitle("成绩排名");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		ViewUtils.inject(this);
		list=new ArrayList<Map<String,Object>>();
		
		mpb=new MyProgressBar(this);
		mpb.setMessage("正在加载中...");
		
		classId=getIntent().getStringExtra("classid");
		
		HttpUtils http=new HttpUtils();
		http.send(HttpMethod.GET, ServerConfig.HOST+"/schoolknow/api/xuebaList.php?classid="+classId,
				null, new RequestCallBack<String>() {
			public void onFailure(HttpException arg0, String arg1) {
				mpb.dismiss();
			}
			public void onSuccess(ResponseInfo<String> info) {
				mpb.dismiss();
				try {
					List<Map<String,Object>> tempList=new JsonHelper(info.result).
							parseJson(new String[]{"rank","name","score","stuid","grade","class","college"});
					list.addAll(tempList);
					adapter=new SimpleAdapter(XuebaList.this, list, R.layout.score_xueba_lv_item,
							new String[]{"rank","name","score","stuid","grade","class","college"},
							new int[]{R.id.xueba_lv_item_rank,R.id.xueba_lv_item_stuid,
							R.id.xueba_lv_item_name,R.id.xueba_lv_item_score,
							R.id.xueba_lv_item_grade,R.id.xueba_lv_item_class,
							R.id.xueba_lv_item_college});
					lv.setAdapter(adapter);
				} catch (Exception e) {
					
				}
			}
			
		});
		
		
		
		
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
