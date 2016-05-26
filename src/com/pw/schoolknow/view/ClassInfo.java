package com.pw.schoolknow.view;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.pw.schoolknow.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.ClassSelect;
import com.pw.schoolknow.widgets.ClassSelect.ClassSelectInterface;

public class ClassInfo extends BaseActivity {
	
	@ViewInject(R.id.second_class_select_btn)
	private Button mSelectBtn;
	@ViewInject(R.id.second_class_select_sumbit)
	private Button mSubmit;
	
	private ClassSelect newClass;
	private String classId;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_second_class);
		setTitle("班级名单查询");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		ViewUtils.inject(this);
	}
	
	@OnClick({R.id.second_class_select_btn,R.id.second_class_select_sumbit})
	public void OnClickImpl(View v){
		if(v==mSelectBtn){
			newClass=new ClassSelect(this);
			newClass.setOnclickListener(new ClassSelectInterface() {
				@Override
				public void onClick(View view) {
					newClass.dismiss();
					classId=newClass.getClassId();
					mSelectBtn.setText(newClass.getClassN());
				}
			});
		}else if(v==mSubmit){
			if(classId==null||classId.trim().length()==0){
				T.showShort(this,"请先选择班级");
			}else{
				Intent it=new Intent(this,ClassContent.class);
				it.putExtra("classid", classId);
				startActivity(it);
			}
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
