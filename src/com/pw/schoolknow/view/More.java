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
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.MoreAdapter;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.MoreStyle2Item;

public class More extends BaseActivity {
	
	private GridView gv;
	private List<Map<String,String>> list;
	//private static String data[]=new String[]{"��������","�����ʴ�","�����г�","����ʱ","���Ϲ���","�����ͼ"};
	private static String data[]=new String[]{"��������","�����г�","����ʱ","�����ͼ","��ݲ�ѯ","����ӰԺ","��������"};
	private static String color[]=new String[]{"#00ABA9","#099FB8","#C77C29","#85B114","#1775B3","#88487C","#00ABA9"};
	private static int imgRs[]=new int[]{R.drawable.main_business_icon,R.drawable.main_business_icon,
		R.drawable.main_business_icon,R.drawable.main_business_icon,R.drawable.main_business_icon,
		R.drawable.main_business_icon,R.drawable.main_business_icon,R.drawable.main_business_icon};
	
	private Context mcontext;
	
	public MoreStyle2Item newsItem,shopItem,lifeItem;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_more);
		setTitle("����");
		setTitleBar(0,0);
		mcontext=this;
		
		gv=(GridView) super.findViewById(R.id.more_grid);
		list=new ArrayList<Map<String,String>>();
		for(int i=0;i<data.length;i++){
			Map<String,String> map=new HashMap<String, String>();
			map.put("text",data[i]);
			map.put("img",String.valueOf(imgRs[i]));
			map.put("color",color[i]);
			list.add(map);
		}
		
		gv.setAdapter(new MoreAdapter(list,this));
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent it=null;
				switch(position){
				case 0:
					it=new Intent(More.this,SchoolFellowSquare.class);
					startActivity(it);
					break;
				case 1:
					it=new Intent(More.this,Market.class);
					startActivity(it);
					break;
				case 2:
					it=new Intent(More.this,CountDown.class);
					startActivity(it);
					break;
				case 3:
					it=new Intent(More.this,MapEcjtu.class);
					startActivity(it);
					break;
				case 4:
					it=new Intent(More.this,SudiForm.class);
					startActivity(it);
					break;
				case 5:
					it=new Intent(More.this,FilmList.class);
					startActivity(it);
					break;
				case 6:
					it=new Intent(More.this,KaoyanTool.class);
					startActivity(it);
					break;
				default:
					it=new Intent(More.this,SchoolFellowSquare.class);
					startActivity(it);
					break;
				}
			}
		});
		
	}
	
	/**
	 * Ϊ�Զ������MoreStyle2Item����ֵ
	 * @param item
	 * @param title
	 * @param obj
	 */
	public void setValues(MoreStyle2Item item,String title,Object[][] obj,MoreStyle2Item.onItemClickImp imp){
		List<Map<String,Object>> mList=new ArrayList<Map<String,Object>>();
		for(int i=0;i<obj.length;i++){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("img", obj[i][0]);
			map.put("title", obj[i][1]);
			mList.add(map);
		}
		item.setTitle(title);
		item.setAdapter(new SimpleAdapter(mcontext,mList, R.layout.base_item_style2,
				new String[]{"img","title"}, new int[]{R.id.base_item_style2_img,R.id.base_item_style2_text}));
		item.setOnItemClick(imp);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		
	}

}
