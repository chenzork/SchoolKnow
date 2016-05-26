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

public class FreeClassroomContent extends BaseActivity {
	
	private ListView lv;
	private List<Map<String,Object>> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_free_classroom_content);
		setTitle("�ս��Ҳ�ѯ");
		setTitleBar(0,0);
		
		String json=getIntent().getStringExtra("data");
		try {
			list=new JsonHelper(json).parseJson(
					new String[]{"1","2","3","4"},
					new String[]{"����:","��λ��:","��������","��ע��"});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		lv=(ListView) super.findViewById(R.id.free_classroom_content_lv);
		lv.setAdapter(new SimpleAdapter(this, list, R.layout.item_free_classroom_content,
				new String[]{"1","2","3","4"},
				new int[]{R.id.free_classroom_content_item_name,R.id.free_classroom_content_item_num,
				R.id.free_classroom_content_item_type,R.id.free_classroom_content_item_param}));
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub
		
	}

}
