package com.pw.schoolknow.view;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.pw.schoolknow.R;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pw.schoolknow.bean.FilmItemBean;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.BitmapHelper;
import com.pw.schoolknow.utils.VideoUtil;
import com.pw.schoolknow.view.base.BaseActivity;

public class FilmDescribe extends BaseActivity {

	@ViewInject(R.id.film_descoribe_playbtn)
	private TextView  btnPlay;
	
	@ViewInject(R.id.film_descoribe_content)
	private TextView content;
	
	@ViewInject(R.id.film_descoribe_pic)
	private ImageView pic;
	
	private BitmapUtils bitmapUtils;
	
	private String filmUrl="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.film_describe);
		
		FilmItemBean item=(FilmItemBean) getIntent().getSerializableExtra("filmItem");
		
		setTitle(item.getTitle());
		
		setTitleBar(R.drawable.btn_titlebar_back, 0);
		bitmapUtils=BitmapHelper.getBitmapUtils(this);
		
		ViewUtils.inject(this);
		
		bitmapUtils.display(pic,item.getImgUrl());
		
		HttpUtils http=new HttpUtils();
		http.send(HttpMethod.GET, "http://hj.ecjtu.org/admin/api/getFilmContent.php?id="+item.getId(),
				null,new RequestCallBack<String>() {
					public void onFailure(HttpException arg0, String arg1) {
						
					}
					public void onSuccess(ResponseInfo<String> info) {
						String result=info.result;			
						try {
							JSONObject JsonData = new JSONObject(result);
							filmUrl=JsonData.getString("downurl");
							content.setText(JsonData.getString("descoribe"));
						} catch (JSONException e) {

						}
					}
				});
	}
	
	
	@OnClick(R.id.film_descoribe_playbtn)
	public void play(View v){
		//Demo Url: http://wangjian.ecjtu.org/hf.mp4
		if(filmUrl.trim().length()==0){
			VideoUtil.play(getApplicationContext(), "http://wangjian.ecjtu.org/Video/qifengle.mp4");
		}else{
			VideoUtil.play(getApplicationContext(), filmUrl);
		}
		
	}
	
	@Override
	protected void onResume() {
	 /**
	  * 强制设置为竖屏
	  */
	 if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
	  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	 }
	 super.onResume();
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			break;
		default:
			break;
		}
	}

}
