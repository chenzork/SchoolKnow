package com.pw.schoolknow.view;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.pw.schoolknow.R;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.MyProgressBar;

public class ExamArrangeContent extends BaseActivity {
	
	private ListView lv;
	private TextView tip=null;
	private MyProgressBar mpb;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_exam_arrange_content);
		setTitle("考试安排");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		lv=(ListView) super.findViewById(R.id.exam_arrange_content_lv);
		this.tip=(TextView) super.findViewById(R.id.exam_arrange_tip);
		tip.setVisibility(View.GONE);
		context=this;
		
		Intent it=getIntent();
		String classId=it.getStringExtra("classid");
		String englishId=it.getStringExtra("englishid");
		mpb=new MyProgressBar(this);
		mpb.setMessage("正在加载中...");
		HttpUtils http=new HttpUtils();
		http.send(HttpMethod.GET,ServerConfig.HOST+
				"/schoolknow/api/ExamarrangeApi.php?action=getContent&classid="+classId+"&englishid="+englishId,
				null, new RequestCallBack<String>() {
			public void onFailure(HttpException arg0, String arg1) {
				mpb.dismiss();
				T.showLong(context,"连接服务器异常");
			}
			public void onSuccess(ResponseInfo<String> info) {
				try {
					List<Map<String,Object>> list=new JsonHelper(info.result).parseJson(
							new String[]{"subject","class","term","week","time",
									"classroom","o_t","i_t"},
							new String[]{"","班级:","学期:","考试周:","考试时间:","考试教室:","监考老师: ",","});
					SimpleAdapter adapter=new SimpleAdapter(ExamArrangeContent.this,list,
							R.layout.item_exam_arrange_content, 
							new String[]{"subject","class","term","week","time","classroom","o_t","i_t"},
							new int[]{R.id.exam_arrange_data_list_subject,R.id.exam_arrange_data_list_class,
							R.id.exam_arrange_data_list_term,R.id.exam_arrange_data_list_week,
							R.id.exam_arrange_data_list_time,R.id.exam_arrange_data_list_classroom,
							R.id.exam_arrange_data_list_o_t,R.id.exam_arrange_data_list_i_t});
					lv.setAdapter(adapter);
					if(list.size()==0){
						tip.setVisibility(View.VISIBLE);
						lv.setVisibility(View.GONE);
						tip.setText("你所查的班级目前没有考试安排,考试安排一般16周以后才会陆续发布,"+
					"本软件与教务处同步更新，敬请等待！");
					}
					mpb.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		
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
