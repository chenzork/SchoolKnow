package com.pw.schoolknow.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.pw.schoolknow.R;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.view.base.BaseActivity;


@SuppressLint("HandlerLeak")
public class SecondClassContent extends BaseActivity {
	
	private ListView lv=null;
	private List<Map<String,Object>> list;
	private String[] tip=new String[]{"ѧ��:","����:",
			"��һѧ��Ƽ�����ѧ��:","��һѧ�깫���Ͷ�ѧ��:","��һѧ������ѧ��:",
			"�ڶ�ѧ��Ƽ�����ѧ��:","�ڶ�ѧ�깫���Ͷ�ѧ��:","�ڶ�ѧ������ѧ��:",
			"����ѧ��Ƽ�����ѧ��:","����ѧ�깫���Ͷ�ѧ��:","����ѧ������ѧ��:",
			"����ѧ��Ƽ�����ѧ��:","����ѧ�깫���Ͷ�ѧ��:","����ѧ������ѧ��:",
			"����ѧ��Ƽ�����ѧ��:","����ѧ�깫���Ͷ�ѧ��:","����ѧ������ѧ��:",
			"�ϼƿƼ�����ѧ��:","�ϼƹ����Ͷ�ѧ��:","�ϼ�����ѧ��:",
			"��ѧ��:"};
	private int[] res=new int[]{
			R.id.second_class_content_item_uid,R.id.second_class_content_item_name,
			R.id.second_class_content_item_1p1,R.id.second_class_content_item_1p2,
			R.id.second_class_content_item_1p3,R.id.second_class_content_item_2p1,
			R.id.second_class_content_item_2p2,R.id.second_class_content_item_2p3,
			R.id.second_class_content_item_3p1,R.id.second_class_content_item_3p2,
			R.id.second_class_content_item_3p3,R.id.second_class_content_item_4p1,
			R.id.second_class_content_item_4p2,R.id.second_class_content_item_4p3,
			R.id.second_class_content_item_5p1,R.id.second_class_content_item_5p2,
			R.id.second_class_content_item_5p3,R.id.second_class_content_item_6p1,
			R.id.second_class_content_item_6p2,R.id.second_class_content_item_6p3,
			R.id.second_class_content_item_add};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_second_class_content);
		setTitle("�ڶ�����ѧ�ֲ�ѯ");
		setTitleBar(0,0);
		
		lv=(ListView) super.findViewById(R.id.second_class_content_lv);
		String jsonData=getIntent().getStringExtra("data");
		
		list=new ArrayList<Map<String,Object>>();
		
		try {
			list=new JsonHelper(jsonData).parseJson(create1To21(),tip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		lv.setAdapter(new SimpleAdapter(this, list,R.layout.item_second_class_content,
				create1To21(),res));
	}
	
	//���ɰ���1-21�ַ������飬���ڽ���json����
		public String[] create1To21(){
			String[] temp=new String[21];
			for(int i=0;i<temp.length;i++){
				temp[i]=String.valueOf(i+1);
			}
			return temp;		
		}
	
	

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub

	}

}
