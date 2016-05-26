package com.pw.schoolknow.view;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.pw.schoolknow.R;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.view.base.BaseActivity;

public class WhoContent extends BaseActivity {
	
	private ListView lv;
	private List<Map<String,Object>> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_who_content);
		setTitle("У�Ѳ�ѯ");
		setTitleBar(0,0);
		
		try {
			list=new JsonHelper(getIntent().getStringExtra("data")).parseJson(
				new String[]{"stuId","classId","name","sex","birth","major",
						"nation","politics","address","status"},
				new String[]{"ѧ��:","�ڰ���:","����:","�Ա�:","��������:",
						"�༶����:","����:","������ò:","����:","ѧϰ״̬:"});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		lv=(ListView) super.findViewById(R.id.who_content_lv);
		lv.setAdapter(new SimpleAdapter(this, list, R.layout.item_who_content,
				new String[]{"stuId","classId","name","sex","birth","major",
				"nation","politics","address","status"},
				new int[]{R.id.find_person_list_stuId,R.id.find_person_list_classId,
				R.id.find_person_list_name,R.id.find_person_list_sex,
				R.id.find_person_list_birth,R.id.find_person_list_major,
				R.id.find_person_list_nation,R.id.find_person_list_politics,
				R.id.find_person_list_address,R.id.find_person_list_status}));
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub

	}

}
