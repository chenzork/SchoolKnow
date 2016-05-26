package com.pw.schoolknow.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.pw.schoolknow.R;
import com.pw.schoolknow.view.base.BaseLayout;
import com.pw.schoolknow.widgets.NewsTitleTextView;

@SuppressWarnings("deprecation")
public class Ask extends ActivityGroup {
	
	private ListView lv=null;
	private String[][] data=new String[][]{
			{"�����ǻ�","10-13 10:51:20","�����ʲô��������ڻ����ʴ�����Ӵ�����ʲô��������ڻ����ʴ�����Ӵ"},
			{"�����ǻ�","10-13 10:51:20","�����ʲô��������ڻ����ʴ�����Ӵ�����ʲô��������ڻ����ʴ�����Ӵ"},
			{"�����ǻ�","10-13 10:51:20","�����ʲô��������ڻ����ʴ�����Ӵ�����ʲô��������ڻ����ʴ�����Ӵ"},
			{"�����ǻ�","10-13 10:51:20","�����ʲô��������ڻ����ʴ�����Ӵ�����ʲô��������ڻ����ʴ�����Ӵ"},
			{"�����ǻ�","10-13 10:51:20","�����ʲô��������ڻ����ʴ�����Ӵ�����ʲô��������ڻ����ʴ�����Ӵ"},
			{"�����ǻ�","10-13 10:51:20","�����ʲô��������ڻ����ʴ�����Ӵ�����ʲô��������ڻ����ʴ�����Ӵ"},
			{"�����ǻ�","10-13 10:51:20","�����ʲô��������ڻ����ʴ�����Ӵ�����ʲô��������ڻ����ʴ�����Ӵ"},
			{"�����ǻ�","10-13 10:51:20","�����ʲô��������ڻ����ʴ�����Ӵ�����ʲô��������ڻ����ʴ�����Ӵ"}
	};
	private List<Map<String,Object>> list;
	private LinearLayout layoutBtn;
	public int clickColor=Color.parseColor("#ECAF12");
	public int defaultColor=Color.parseColor("#6F6F6F");
	
	private LinearLayout content;
	private Intent intent = null;
	
	private BaseLayout baseLy;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		baseLy=new BaseLayout(this,R.layout.act_ask);
		setContentView(baseLy);
		baseLy.TitleText.setText("�����ʴ�");
		baseLy.leftBtn.setImageResource(R.drawable.btn_titlebar_back);
		baseLy.rightBtn.setImageResource(R.drawable.navigationbar_compose);
		baseLy.leftBtn.setOnClickListener(new titleBarClick());
		baseLy.rightBtn.setOnClickListener(new titleBarClick());
		
		lv=(ListView) super.findViewById(R.id.ask_lv);
		content=(LinearLayout) super.findViewById(R.id.ask_layout_content);
		list=new ArrayList<Map<String,Object>>();
		for(int i=0;i<data.length;i++){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("name", data[i][0]);
			map.put("time", data[i][1]);
			map.put("content", data[i][2]);
			list.add(map);
		}
		lv.setAdapter(new SimpleAdapter(this, list,R.layout.item_ask,
				new String[]{"name","time","content"},
				new int[]{R.id.ask_item_user_name,R.id.ask_item_time,R.id.ask_item_content}));
		
		layoutBtn=(LinearLayout) super.findViewById(R.id.ask_select_btn);
		for(int i=0;i<layoutBtn.getChildCount();i++){
			NewsTitleTextView text= (NewsTitleTextView) layoutBtn.getChildAt(i);
			if(i==0){
				text.setIsHorizontaline(true);
				text.setTextColor(clickColor);
				text.setHorizontalineColor(clickColor);
			}
			text.setTag(i);
			text.setIsVerticalLine(true);
			text.setOnClickListener(new onClickListenerImp());
		}
		
		switchActivity(0);
	}
	
	private void switchActivity(int id) {
		this.content.removeAllViews();	
		switch (id) {	
		case 0: 													// ָ��������Intent
			this.intent = new Intent(Ask.this,
					AskRanking.class);
			break;
		case 1: 													// ָ��������Intent
			this.intent = new Intent(Ask.this,
					AskRanking.class);
			break;
		case 2: 													// ָ��������Intent
			this.intent = new Intent(Ask.this,
					AskRanking.class);
			break;
		case 3:														// ָ��������Intent
			this.intent = new Intent(Ask.this,
					AskRanking.class);
			break;
		}
		this.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		// ���ӱ�� 
		Window subActivity = this.getLocalActivityManager().startActivity(
				"subActivity", this.intent);						// Activity תΪ View
		this.content.addView(subActivity.getDecorView(),
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT); // �������View
	}
	
	class onClickListenerImp implements OnClickListener{
		@Override
		public void onClick(View v) {
			int position=Integer.parseInt(v.getTag().toString());
			for(int i=0;i<layoutBtn.getChildCount();i++){
				NewsTitleTextView text= (NewsTitleTextView) layoutBtn.getChildAt(i);
				text.setHorizontalineColor(clickColor);
				if(i==position){
					text.setIsHorizontaline(true);
					text.setTextColor(clickColor);
				}else{
					text.setIsHorizontaline(false);
					text.setTextColor(defaultColor);
				}
			}
			switchActivity(position);
		}
		
	}



	public class titleBarClick implements OnClickListener{
		public void onClick(View v) {
			Intent it=null;
			if(v==baseLy.leftBtn){
				it=new Intent(Ask.this,Main.class);
				it.putExtra("param","4");
				startActivity(it);
				finish();
			}else if(v==baseLy.rightBtn){
				it=new Intent(Ask.this,NewAsk.class);
				startActivity(it);
				finish();
			}
		}
		
	}

}
