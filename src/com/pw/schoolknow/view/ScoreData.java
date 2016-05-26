package com.pw.schoolknow.view;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.ScoreAdapter;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.helper.ScoreHelper;
import com.pw.schoolknow.receiver.ScoreSearchReceiver;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.L;
import com.pw.schoolknow.utils.NotifyUtils;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.utils.TimeUtil;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.MyAlertDialog;
import com.pw.schoolknow.widgets.MyAlertDialog.MyDialogInt;
public class ScoreData extends BaseActivity {
	
	private ListView listview;
	private List<Map<String,Object>> list;
	
	private float allScore=0;
	private float allCredit=0;
	private float Score=0;
	
	String stuName,studId,stuScore;
	
	private TextView rank;
	private TextView majorRank;
	private ProgressBar pb;
	
	private AlarmManager alarm=null;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_score_data);
		setTitle("�ɼ���ѯ");
		setTitleBar(R.drawable.btn_titlebar_back,"",0,"ԤԼ");
		
		listview=(ListView) super.findViewById(R.id.score_data_listview);
		String json=getIntent().getStringExtra("jsondata");
		
		mContext=this;
		
		try {
			JSONObject JsonData = new JSONObject(json);
			stuName=JsonData.getString("name").toString();
			studId=JsonData.getString("stuid").toString();
			stuScore=JsonData.getString("score").toString();
			
			list=new JsonHelper(stuScore).parseJson(
					new String[]{"subject","score","extra_1","extra_2","demand","credit"});
			
			for(Map<String,Object> map:list){
				if(map.get("demand").toString().trim().equals("���޿�")&&
						!map.get("score").toString().trim().equals("�ϸ�")){
					float credit=Float.parseFloat(map.get("credit").toString());
					allScore+=ScoreHelper.getScore(map.get("score").toString())*credit;
					allCredit+=credit;
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Score=(float)(Math.round(allScore/allCredit*10))/10;
		
		View lauout=LayoutInflater.from(this)
		          .inflate(R.layout.part_score_data_head, null, false);
		
		lauout.setOnClickListener(layoutClick);
		
		TextView tvName=(TextView) lauout.findViewById(R.id.score_data_head_name);
		tvName.setText("����:"+stuName);
		
		TextView tvStuid=(TextView) lauout.findViewById(R.id.score_data_head_stuid);
		tvStuid.setText("ѧ��:"+studId);
		
		TextView aveScore=(TextView) lauout.findViewById(R.id.score_data_head_avescore);
		aveScore.setText("��ѧ�ּ�:"+Score);
		
		rank=(TextView) lauout.findViewById(R.id.score_data_head_classrank);
		majorRank=(TextView) lauout.findViewById(R.id.score_data_head_majorank);
		pb=(ProgressBar) lauout.findViewById(R.id.score_data_head_pb);
		
		listview.addHeaderView(lauout);
		listview.setAdapter(new ScoreAdapter(list, ScoreData.this));
		
		new AsyncLoadRank().execute(studId);
		
	}
	
	private OnClickListener layoutClick=new OnClickListener() {
		public void onClick(View v) {
			Intent it=new Intent(ScoreData.this,XuebaList.class);
			it.putExtra("classid", studId.substring(0,studId.length()-3));
			startActivity(it);
		}
	};
	
	
	//��ȡ�༶����
	class AsyncLoadRank extends AsyncTask<String, Void, String>{
		protected String doInBackground(String... params) {
			String result=GetUtil.getRes(ServerConfig.HOST+"/schoolknow/api/getRank.php?stuid="+params[0]);
			return result;
		}

		protected void onPostExecute(String result) {
			try{
				String[] param=result.split("\\|");
				rank.setText(param[1]);
				majorRank.setText("רҵ����:"+param[0]);
				super.onPostExecute(result);
				pb.setVisibility(View.GONE);
			}catch(Exception e){
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
			final MyAlertDialog mad=new MyAlertDialog(this);
			mad.setTitle("��Ϣ��ʾ");
			mad.setMessage("ԤԼ��ѯ�����賿3����ѯ�ɼ�����ʾ��ѯ������ڼ�Ҫ��������������������ȷ��Ҫ����ԤԼ��ѯ�ɼ���");
			mad.setLeftButton("ȷ��", new MyDialogInt() {
				public void onClick(View view) {
					Intent it=new Intent(ScoreData.this,ScoreSearchReceiver.class);
					it.setAction("com.pw.schoolknow.receiver.score");
					PendingIntent sender=PendingIntent.
							getBroadcast(ScoreData.this,0,it, PendingIntent.FLAG_UPDATE_CURRENT);
					alarm=(AlarmManager)ScoreData.this.getSystemService(Context.ALARM_SERVICE);
					Calendar c=Calendar.getInstance();
					long currentTime=TimeUtil.getCurrentTime();
					c.setTimeInMillis(currentTime);
					int hour=TimeUtil.getHour(currentTime);
					int waitHour=hour<4?4:24-hour+4;
					c.add(Calendar.HOUR,waitHour);
					alarm.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
					mad.dismiss();
					T.showLong(mContext, "ԤԼ�ɹ�");
				}
			});
			mad.setRightButton("ȡ��", new MyDialogInt() {
				public void onClick(View view) {
					mad.dismiss();
				}
			});
			break;
		case 3:
			break;
		default:
			break;
		}
		
	}

}
