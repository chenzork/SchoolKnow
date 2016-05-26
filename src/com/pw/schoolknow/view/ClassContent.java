package com.pw.schoolknow.view;


import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
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
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.MyProgressBar;

public class ClassContent extends BaseActivity {
	
	@ViewInject(R.id.score_data_listview)
	private ListView lv;
	
	private MyProgressBar mpb;
	private Context mContext;
	private Intent intent;
	private SimpleAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_score_data);
		setTitle("班级名单查询");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		mContext=this;
		ViewUtils.inject(this);
		mpb=new MyProgressBar(this);
		mpb.setMessage("正在加载中...");
		
		intent=getIntent();
		
		HttpUtils http=new HttpUtils();
		http.send(HttpMethod.GET, ServerConfig.HOST+
				"/schoolknow/api/classInfo.php?classid="+intent.getStringExtra("classid"),
				null,new RequestCallBack<String>() {
			public void onFailure(HttpException arg0, String arg1) {
				T.showShort(mContext, "连接服务器异常");
				mpb.dismiss();
			}
			public void onSuccess(ResponseInfo<String> info) {
				try {
					List<Map<String,Object>> list=new JsonHelper(info.result).parseJson(
							new String[]{"name","stuid","classid","state"},
							new String[]{"","学号：","班级序号：","学籍状态："});
					adapter=new SimpleAdapter(mContext, list, R.layout.class_info_item,
							new String[]{"name","stuid","classid","state"},
							new int[]{R.id.class_info_name,R.id.class_info_stuid,
							R.id.class_info_classid,R.id.class_info_state});
					lv.setAdapter(adapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
				mpb.dismiss();
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
