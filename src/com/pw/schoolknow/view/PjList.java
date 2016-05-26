package com.pw.schoolknow.view;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.pw.schoolknow.adapter.PjListAdapter;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.view.base.BaseActivity;



/**
 * 评教列表
 * 需要参数: 密码和服务器登录成功返回参数
 * 点击：进入评教页面
 * @author wei8888go
 *
 */
public class PjList extends BaseActivity {
	
	@ViewInject(R.id.score_data_listview)
	private ListView lv;
	
	private List<Map<String,Object>> list;
	private PjListAdapter adapter;
	private String uid="";
	private String pwd;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_score_data);
		setTitle("评教列表");
		setTitleBar(R.drawable.btn_titlebar_back,"",0,"更多");
		
		ViewUtils.inject(this);
		
		String getString=getIntent().getStringExtra("data");
		pwd=getIntent().getStringExtra("pwd");
		JSONObject JsonData;
		try {
			JsonData = new JSONObject(getString);
			String courseInfo=JsonData.getString("content");
			String[] args=new String[]{"name","course","demand","info","mark","tid"};
			list=new JsonHelper(courseInfo).parseJson(args);
			
			adapter=new PjListAdapter(this, list);
			
			View head=LayoutInflater.from(this)
			          .inflate(R.layout.pj_list_head,null);
			String userInfo=JsonData.getString("user");
			//JSONObject userData=new JSONObject(userInfo);
			JsonData = new JSONObject(userInfo);
			((TextView) head.findViewById(R.id.pjlist_head_name)).
			setText("姓名："+JsonData.getString("name"));
			uid=JsonData.getString("uid");
			((TextView) head.findViewById(R.id.pjlist_head_stuid)).
			setText("学号："+uid);
			((TextView) head.findViewById(R.id.pjlist_head_class)).
			setText("班级："+JsonData.getString("class"));
			
			lv.addHeaderView(head);
			lv.setAdapter(adapter);
			
			lv.setOnItemClickListener(itemClick);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private OnItemClickListener itemClick=new OnItemClickListener() {
		@SuppressWarnings("unchecked")
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			try{
				Map<String,Object> map=(Map<String, Object>) lv.getItemAtPosition(position);
				//Map<String,Object> map=list.get(position);
				String markValue=map.get("mark").toString();
				String teacher=map.get("name").toString();
				if(markValue.trim().length()>=4&&!markValue.equals("null")){
					T.showLong(PjList.this, teacher+"老师已经评分，请勿重复操作");
				}else{
					Intent it=new Intent(PjList.this,PjContent.class);
					it.putExtra("teacher", teacher);
					it.putExtra("uid", uid);
					it.putExtra("tid", map.get("tid").toString());
					it.putExtra("pwd", pwd);
					it.putExtra("position", String.valueOf(position));
					startActivityForResult(it, 2);
				}
			}catch(Exception e){}
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 2){
			if(data!=null){
				if (resultCode == RESULT_OK){
					 String newScore=data.getStringExtra("score");
					 int newPosition=Integer.parseInt(data.getStringExtra("position"))-1; 
					 if(newPosition>=0&&newPosition<list.size()){
						 list.get(newPosition).put("mark",newScore+".00");
						 adapter.notifyDataSetChanged();
						// adapter=new PjListAdapter(this, list);
						 //lv.setAdapter(adapter);
					 }else{
						 T.showShort(PjList.this,"请重新登录刷新评教分数");
						 finish();
					 }
				 }
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
			String pjUrl="http://pw20140520.sturgeon.mopaas.com/m/main.php?"+
		"uid="+uid+"&pwd="+pwd;
			Intent it=new Intent(this,MyBrowser.class);
			it.putExtra("url",pjUrl);
			startActivity(it);
			break;
		case 3:
			break;
		default:
			break;
		}
	}

}
