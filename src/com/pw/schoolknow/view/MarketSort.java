package com.pw.schoolknow.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleAdapter;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.NoScrollGridView;


/**
 * �����г�����
 * @author wei8888go
 *
 */

public class MarketSort extends BaseActivity {
	
	
	public Context mcontext;
	
	@ViewInject(R.id.market_sort_nsgridview)
	private NoScrollGridView gv;
	
	public final static String[] sortInfo={"���Ӳ�Ʒ","������Ʒ","������Ʒ","ѧϰ��Ʒ","��������","�������","��ͨ����",
		"��������","�ⷿ��Ϣ","��ְ��Ϣ","������Ʒ"};
	public int[] sortIcon={R.drawable.demo,R.drawable.demo,R.drawable.demo,R.drawable.demo,R.drawable.demo,
			R.drawable.demo,R.drawable.demo,R.drawable.demo,R.drawable.demo,R.drawable.demo,R.drawable.demo,
			R.drawable.demo};
	public List<Map<String,Object>> list;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_market_sort);
		setTitle("�������");
		setTitleBar(0,0);
		mcontext=this;
		ViewUtils.inject(this);
		
		list=new ArrayList<Map<String,Object>>();
		
		for(int i=0;i<sortInfo.length;i++){
			if(sortInfo[i]!=null){
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("img", String.valueOf(sortIcon[i]));
				map.put("text",sortInfo[i]);
				list.add(map);
			}
		}
		
		gv.setAdapter(new SimpleAdapter(mcontext, list, R.layout.item_market_sort_gv, 
				new String[]{"img","text"}, new int[]{R.id.item_market_sort_gv_img,
				R.id.item_market_sort_gv_text}));
		
	}


	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub

	}

}
