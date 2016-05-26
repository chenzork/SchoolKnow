package com.pw.schoolknow.view;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pw.schoolknow.view.base.BaseActivity;

public class KaoyanTool extends BaseActivity {
	
	@ViewInject(R.id.kaoyan_list_lv)
	private ListView lv;
	
	private ArrayAdapter<String> adapter;
	
	private final String[] kyName={"天勤考研","沪江考研","我要当学霸","新浪考研站","腾讯考研站"};
	private final String[] kyUrl={"http://www.wechatpark.com/wxcore/business/website/index.php?"
			+ "sign=otZh5jv40cHqBZSOskrKJ3KDjee0&uid=867#mp.weixin.qq.com",
			"http://m.hujiang.com/ky",
			"http://m.iamxueba.com/article/home/1300000000",
			"http://edu.sina.cn/?sa=t551d48v1377&pos=45&vt=4",
			"http://info.3g.qq.com/g/s?sid=AY2MhLnO65vFOTYQJbGT1o12&icfa=edu_h&aid=template&icfa=edu_kaoshi&tid=edu_kylm"};
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kaoyan_list);
		setTitle("考研助手");
		ViewUtils.inject(this);
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		adapter=new ArrayAdapter<String>(this,R.layout.kaoyan_list_item,R.id.kaoyan_list_item_title,kyName);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(onItemClick);
	}
	
	private OnItemClickListener onItemClick=new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent it=new Intent(KaoyanTool.this,MyBrowser.class);
			it.putExtra("url", kyUrl[position]);
			startActivity(it);
		}
	};

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		default:
			break;
		}
	}

}
