package com.pw.schoolknow.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pw.schoolknow.adapter.FilmListAdapter;
import com.pw.schoolknow.bean.FilmItemBean;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.MyProgressBar;

public class FilmList extends BaseActivity {
	
	@ViewInject(R.id.film_list_lv)
	private ListView lv;
	
	private List<FilmItemBean> list;
	private FilmListAdapter adapter;
	
	private MyProgressBar mpb;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.film_list);
		setTitle("花椒影院");
		
		ViewUtils.inject(this);
		
		list=new ArrayList<FilmItemBean>();
		
		mpb=new MyProgressBar(this);
		mpb.setMessage("正在加载中...");
		
		HttpUtils http=new HttpUtils();
		http.send(HttpMethod.GET, "http://hj.ecjtu.org/admin/api/getFilmList.php",
				null, new RequestCallBack<String>() {
			public void onFailure(HttpException arg0, String arg1) {
				mpb.dismiss();
				T.showLong(FilmList.this, "加载出现异常");
			}
			public void onSuccess(ResponseInfo<String> info) {
				String result=info.result;
				List<FilmItemBean> tempList=new ArrayList<FilmItemBean>();
				try {
					List<Map<String,Object>> jsonList=
							new JsonHelper(result).parseJson(new String[]{"id","title","time","imgUrl"});
					for(Map<String,Object> map:jsonList){
						FilmItemBean item=new FilmItemBean();
						item.setId(map.get("id").toString());
						item.setImgUrl(map.get("imgUrl").toString());
						item.setTitle(map.get("title").toString());
						item.setUploadTime(map.get("time").toString());
						tempList.add(item);
					}
					list.addAll(tempList);
					adapter.notifyDataSetInvalidated();
					mpb.dismiss();
				} catch (Exception e) {
					T.showLong(FilmList.this, e.toString());
				}
				
				
			}
			
		});
		adapter=new FilmListAdapter(this, list);
		lv.setAdapter(adapter);
		
	}
	
	@com.lidroid.xutils.view.annotation.event.OnItemClick(R.id.film_list_lv)
	public void OnItemClick(AdapterView<?> parent, View view, int position,long id){
		Intent it=new Intent();
		it.setClass(this, FilmDescribe.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("filmItem", list.get(position));
		it.putExtras(bundle);
		startActivity(it);
	}
	
	

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub

	}

}
