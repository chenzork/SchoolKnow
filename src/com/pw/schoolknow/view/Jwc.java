package com.pw.schoolknow.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

import com.pw.schoolknow.R;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.XListView;

public class Jwc extends BaseActivity {
	
	private XListView lv=null;
	private List<Map<String,Object>> list=null;
	
	private Object[][] fun={{"�ɼ���ѯ",R.drawable.demo},{"���԰���",R.drawable.demo},{"��������",R.drawable.demo},
	{"����ѡ��",R.drawable.demo},{"�ս��Ҳ�ѯ",R.drawable.demo},{"��������ѯ",R.drawable.demo},
	{"��������",R.drawable.demo},{"�ڶ�����ѧ�ֲ�ѯ",R.drawable.demo},
	{"�༶����",R.drawable.demo},{"ͼ��ݲ�ѯ",R.drawable.demo},{"У�Ѳ�ѯ",R.drawable.demo}};
	
	public Context mcontext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_jwc);
		setTitle("�����ѯ");
		setTitleBar(0,0);
		
		mcontext=this;
		
		lv=(XListView) super.findViewById(R.id.jwc_lv);
		lv.setPullLoadEnable(false);
		lv.setPullRefreshEnable(false);
		this.list=new ArrayList<Map<String,Object>>();
		for(int i=0;i<fun.length;i++){
			Map<String, Object> map=new HashMap<String,Object>();
			//map.put("img", String.valueOf(fun[i][1]));
			String tempStr=fun[i][0].toString();
			map.put("img", tempStr.substring(0,1));
			map.put("title",tempStr);
			this.list.add(map);
		}
		this.lv.setAdapter(new SimpleAdapter(this, list, R.layout.item_list_style2,
				new String[]{"img","title"}, new int[]{R.id.list_item_img,R.id.list_item_title}));
		
		
		
		this.lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent it=null;
				switch(position-1){
				case 0:
					it=new Intent(Jwc.this,Score.class);
					break;
				case 9:
					it=new Intent(Jwc.this,Library.class);
					break;
				case 5:
					it=new Intent(Jwc.this,Cet.class);
					break;
				case 3:
					it=new Intent(Jwc.this,OnlienCourseLogin.class);
					break;
				case 1:
					it=new Intent(Jwc.this,ExamArrange.class);
					break;
				case 6:
					it=new Intent(Jwc.this,BukaoInput.class);
					break;
				case 4:
					it=new Intent(Jwc.this,FreeClassroom.class);
					break;
				case 8:
					it=new Intent(Jwc.this,ClassInfo.class);
					break;
				case 10:
					it=new Intent(Jwc.this,Who.class);
					break;
				case 7:
					it=new Intent(Jwc.this,SecondClass.class);
					break;
				case 2:
					it=new Intent(Jwc.this,PjLogin.class);
					//T.showShort(mcontext, "�������̹��ܽ����Ժ�汾�Ƴ�");
					break;
				default:
					it=new Intent(Jwc.this,Score.class);
				}
				if(it!=null){
					startActivity(it);
				}
			}
		});
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub

	}

}
