package com.pw.schoolknow.receiver;

import java.util.HashMap;
import java.util.Map;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.utils.NotifyUtils;
import com.pw.schoolknow.view.ScoreData;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScoreSearchReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		HttpUtils http=new HttpUtils();
		http.send(HttpMethod.GET, ServerConfig.HOST+"/schoolknow/api/getscore.php?stuid=20112110010521"+
		"&term=2013.2",null,new RequestCallBack<String>() {
			public void onSuccess(ResponseInfo<String> info) {
				String result=info.result;
				if(result.trim().length()==0){
					NotifyUtils.tip("查询成绩结果", "查询成绩结果", "查询出现异常",null);
				}else if(result.equals("{\"name\":\"\",\"stuid\":null,\"score\":[]}")){
					NotifyUtils.tip("查询成绩结果", "查询成绩结果", "暂时没有查询到您的成绩",null);
				}else{
					Map<String,String> map=new HashMap<String, String>();
					map.put("jsondata",result);
					NotifyUtils.tip("查询成绩结果", "查询成绩结果", "点击查看成绩", ScoreData.class,map);
				}
			}
			public void onFailure(HttpException arg0, String arg1) {
				NotifyUtils.tip("查询成绩结果", "查询成绩结果", "连接服务器异常",null);
			}
		});
	}

}
