package com.pw.schoolknow.view;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pw.schoolknow.view.base.BaseActivity;

public final class UserHelper extends BaseActivity {
	
	@ViewInject(R.id.user_help_menu_lv)
	private ListView lv;
	
	private String[] menuData={"反馈信息","版本更新","版本介绍","用户协议","软件评分"};
	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_user_helper);
		
		ViewUtils.inject(this);
		
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menuData);
		lv.setAdapter(adapter);
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		
	}
	

}
