package com.pw.schoolknow.view;

import android.os.Bundle;
import android.view.View;

import com.pw.schoolknow.R;
import com.pw.schoolknow.view.base.BaseActivity;

/**
 * 一键评教的入口
 * @author wei8888go
 *
 */
public class PjEasyStart extends BaseActivity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pj_easy_entry);
		setTitle("神秘功能");
		setTitleBar(R.drawable.btn_titlebar_back,0);
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			this.finish();
			break;
		case 2:
			break;
		default:
			break;
		}
	}

}
