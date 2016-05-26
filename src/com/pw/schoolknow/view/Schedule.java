package com.pw.schoolknow.view;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.pw.schoolknow.R;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.db.ScheduleDB;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.helper.ScheduleHelper;
import com.pw.schoolknow.helper.TermHelper;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.Rotate3dAnimation;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.view.base.BaseGroupActivity;
import com.pw.schoolknow.widgets.ClassSelect;
import com.pw.schoolknow.widgets.MyAlertDialog;
import com.pw.schoolknow.widgets.MyPopMenu;
import com.pw.schoolknow.widgets.MyProgressBar;
import com.pw.schoolknow.widgets.MyAlertDialog.MyDialogInt;
import com.pw.schoolknow.widgets.MyPopMenu.MyPopMenuImp;

/**
 * �γ̱�
 * @author wei8888go
 *
 */
public class Schedule extends BaseGroupActivity {
	
	private LinearLayout content;
	private Intent intent = null;
	
	private MyPopMenu popmenu;
	private MyProgressBar mpb;
	private MyAlertDialog loadMad;
	private MyProgressBar update_mpb;
	
	private ScheduleHelper sh;
	private LoginHelper lh;
	
	private MyAlertDialog mad;
	public ClassSelect newClass=null;
	
	public  HttpUtils http;
	public Context mcontext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_schedule);
		setTitle("�γ̱�");
		setTitleBar(0,"����",R.drawable.btn_titlebar_select,"");	
		
		mcontext=this;
		
		content=(LinearLayout) super.findViewById(R.id.schedule_content);
		sh=new ScheduleHelper(this);
		lh=new LoginHelper(this);
		
		if(sh.getWeekView()){
			switchActivity(1);
		}else{
			switchActivity(0);
		}
		
	}
	
	@SuppressWarnings("deprecation")
	private void switchActivity(int id) {
		this.content.removeAllViews();	
		switch (id) {	
		case 0: 													
			this.intent = new Intent(Schedule.this,
					ScheduleDay.class);
			break;
		case 1:
			this.intent = new Intent(Schedule.this,
					ScheduleWeek.class);
			break;
		}
		this.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		// ���ӱ�� 
		Window subActivity = this.getLocalActivityManager().startActivity(
				"subActivity", this.intent);						// Activity תΪ View
		this.content.addView(subActivity.getDecorView(),
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT); // �������View
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			if(sh.getWeekView()){
				applyRotation(0,10);
			}else{
				applyRotation(0,-10);
			}
			break;
		case 2:
			popmenu=new MyPopMenu(Schedule.this);
			popmenu.addItems(new String[]{"��ӿα�","����α�","���¿α�","������ѡ��"});
			popmenu.showAsDropDown(v);
			popmenu.setOnItemClickListener(new MyPopMenuImp() {
				@Override
				public void onItemClick(int index) {
					switch(index){
					case 0:
						Intent it=new Intent(Schedule.this,ScheduleAdd.class);
						startActivity(it);
						break;
					case 1:
						startActivity(new Intent(Schedule.this,ScheduleManage.class));
						break;
					case 2:
						loadMad=new MyAlertDialog(mcontext);
						loadMad.setTitle("��Ϣ��ʾ");
						loadMad.setMessage("���µ�ǰ�α���ͬ���������¿α�ѡ�޿κ��Զ���Ŀγ̽��ᶪʧ��"
								+"��ȷ��Ҫ���µ�ǰ�α���?");
						loadMad.setLeftButton("ȷ��", new MyDialogInt() {
							public void onClick(View view) {
								loadMad.dismiss();
								update_mpb=new MyProgressBar(Schedule.this);
								update_mpb.setScaleProgress();
								update_mpb.setMax(100);
								update_mpb.setMessage("���ڼ�������...");
								update_mpb.setProgress(0);
								if(http==null){
									http = new HttpUtils();
									http.configCurrentHttpCacheExpiry(1000 * 10);
								}
								RequestParams params = new RequestParams();  
							    params.addQueryStringParameter("term", TermHelper.
							    		getNumTerm(TermHelper.getStringTerm3(sh.getCurrentScheduleTerm())));
							    params.addQueryStringParameter("classid", sh.getCurrentScheduleClassId());
								http.send(HttpMethod.GET, ServerConfig.HOST+"/schoolknow/api/update_schedule.php",
										params, new RequestCallBack<String>() {
									public void onLoading(long total, long current,
											boolean isUploading) {
										super.onLoading(total, current, isUploading);
										int precent=(int)(current*100/total);
										if(precent>=0&&precent<=100){
											update_mpb.setProgress(precent);
										}
									}
									public void onFailure(HttpException arg0, String arg1) {
										T.showLong(mcontext,"����ʧ��,��ȷ��������ͨ������");
										update_mpb.dismiss();
									}
									public void onSuccess(ResponseInfo<String> Info) {
										update_mpb.setProgress(100);
										update_mpb.setMessage("���ڸ������ݿ�...");
										
										//�������ݿ�
										new ScheduleDB(Schedule.this,ScheduleDB.TB_SYLLABUS).
										updateSchedule(sh.getCurrentScheduleClassId(), sh.getCurrentScheduleTerm(),
												Info.result);
										update_mpb.dismiss();
										
										//���½���
										if(sh.getWeekView()){
											switchActivity(1);
										}else{
											switchActivity(0);
										}
										update_mpb=null;
										T.showLong(mcontext,"�������");
									}
								});
							}
						});
						loadMad.setRightButton("ȡ��", new MyDialogInt() {
							public void onClick(View view) {
								loadMad.dismiss();
							}
						});
						break;
					case 3:
						if(!lh.hasLogin()){
							T.showShort(mcontext,"���½���ٳ��Ե��빫��ѡ�޿�");
						}else if(!lh.hasBindStuid()){
							T.showShort(mcontext,"�����ڸ��������а�ѧ��");
						}else{
							String stuid=lh.getStuId();
							String term=TermHelper.getNumTerm(TermHelper.getStringTerm3(sh.getCurrentScheduleTerm()));
							mpb=new MyProgressBar(Schedule.this);
							mpb.setMessage("���ڼ�����...");
							new AsyncLoadOptionalCourse().execute(term,stuid);
						}
						break;
					default:
						break;
					}
					
				}
			});
			break;
		case 3:
			break;
		default:
			break;
		}
	}
	
	private void applyRotation(float start, float end) {

		final float centerX = content.getWidth() / 2.0f;
		final float centerY = content.getHeight() / 2.0f;

		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
				centerX, centerY);
		rotation.setDuration(500);
		rotation.setFillAfter(true);

		rotation.setAnimationListener(new DisplayNextView());

		content.startAnimation(rotation);
	}
	
	private final class DisplayNextView implements Animation.AnimationListener {
		public void onAnimationStart(Animation animation) {
		}
		public void onAnimationEnd(Animation animation) {
			content.post(new SwapViews());
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}
	}
	
	@SuppressLint("HandlerLeak")
	private final class SwapViews implements Runnable {
		public void run() {
			final float centerX = content.getWidth() / 2.0f;
			final float centerY = content.getHeight() / 2.0f;
			Rotate3dAnimation rotation;
			rotation = new Rotate3dAnimation(270, 360, centerX, centerY);
			rotation.setDuration(500);
			rotation.setFillAfter(true);
			content.startAnimation(rotation);
			Message msg=new Message();
			msg.what=102;
			handler.sendMessage(msg);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		
		super.onResume();
		
		//�ر�ѡ��༶���� 
		if(newClass!=null){
			newClass.dismiss();
		}
		
		//����û�����ݿ�
		if(new ScheduleDB(this,ScheduleDB.TB_SYMANAGE).getSyManage().size()==0){
			 mad=new MyAlertDialog(this);
			 mad.setTitle("��Ϣ��ʾ");
			 mad.setMessage("���ػ�û�пγ̱���Ϣ���Ƿ�ӽ��񴦵��룿");
			 mad.setLeftButton("ȷ��",new MyDialogInt() {
				@Override
				public void onClick(View view) {
					mad.dismiss();
					Intent it=new Intent(Schedule.this,ScheduleAdd.class);
					startActivity(it);
					return;
				}
			});
			mad.setRightButton("ȡ��",new MyDialogInt() {
				@Override
				public void onClick(View view) {
					mad.dismiss();					
				}
			});
		}
	}







	@SuppressLint("HandlerLeak")
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==102){
				if(sh.getWeekView()){
					switchActivity(0);
					sh.setWeekView(false);
					setTitleBar(0,"����",R.drawable.btn_titlebar_select,"");	
				}else{
					switchActivity(1);
					sh.setWeekView(true);
					setTitleBar(0,"����",R.drawable.btn_titlebar_select,"");	
				}
			}
		}
		
	};
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
	
	//���빫��ѡ�޿�
	public class AsyncLoadOptionalCourse extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... source) {
			String data=GetUtil.getRes(ServerConfig.HOST+"/schoolknow/api/MyoptionalCourse.php"+
		"?term="+source[0]+"&stuid="+source[1]);
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				mpb.dismiss();
				final List<Map<String,Object>> list=new JsonHelper(result).
						parseJson(new String[]{"sub","week","s"});
				if(list.size()!=0){
					String OptionScheduleInfo="";
					for(int i=0;i<list.size();i++){
						String sub=list.get(i).get("sub").toString();
						String[] temp_param=sub.split("\\|");
						OptionScheduleInfo+=(i+1)+"��"+temp_param[0]+"\n";
					}
					loadMad=new MyAlertDialog(Schedule.this);
					loadMad.setTitle("��Ϣ��ʾ");
					loadMad.setMessage("��⵽���µĹ���ѡ�޿�:\n\n"+OptionScheduleInfo+"\n�Ƿ��뵽��ǰ�γ̱�?");
					loadMad.setLeftButton("ȷ��",new MyDialogInt() {
						public void onClick(View view) {
							loadMad.dismiss();
							for(Map<String,Object> map:list){
								String subject=map.get("sub").toString();
								String week=map.get("week").toString();
								String s=map.get("s").toString();
								
								//д��γ̱�
								new ScheduleDB(Schedule.this,ScheduleDB.TB_SYLLABUS).
								updateDaySchedule(sh.getCurrentScheduleClassId(), 
										sh.getCurrentScheduleTerm(), subject, week, s);
							}
							//���½���
							if(sh.getWeekView()){
								switchActivity(1);
							}else{
								switchActivity(0);
							}
							T.showShort(Schedule.this,"�ɹ����빫��ѡ�޿�");
						}
					});
					loadMad.setRightButton("ȡ��",new MyDialogInt() {
						public void onClick(View view) {
							loadMad.dismiss();
						}
					});
				}else{
					T.showShort(Schedule.this,"û�м�⵽���Ĺ���ѡ�޿���Ϣ");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
	}

}
