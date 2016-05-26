package com.pw.schoolknow.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.pw.schoolknow.R;
import com.pw.schoolknow.db.ScheduleDB;
import com.pw.schoolknow.helper.ScheduleHelper;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.MyAlertDialog;
import com.pw.schoolknow.widgets.MyAlertDialog.MyDialogInt;
import com.pw.schoolknow.widgets.MyAlertMenu;
import com.pw.schoolknow.widgets.MyAlertMenu.MyDialogMenuInt;

/**
 * �γ̱����
 * @author wei8888go
 *
 */
public class ScheduleManage extends BaseActivity {
	
	private GridView gridview=null;
	private List<Map<String,Object>> list=null;
	private MyAlertMenu mam=null;
	private MyAlertDialog mad=null;
	private ScheduleHelper sh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_schedule_manage);
		setTitle("�γ̱����");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		gridview=(GridView) super.findViewById(R.id.schedule_manage_gallery);
		list=new ScheduleDB(this,ScheduleDB.TB_SYMANAGE).getSyManage();
		gridview.setAdapter(new SimpleAdapter(this, list, R.layout.item_schedule_manage,
				new String[]{"classname","term","classid","term_temp"},
				new int[]{R.id.schedule_manage_listview_item_className,
				R.id.schedule_manage_listview_item_term,R.id.schedule_manage_listview_item_classid,
				R.id.schedule_manage_listview_item_term_temp}));
		gridview.setOnItemClickListener(new OnItemClickListenerimp());
		
		sh=new ScheduleHelper(this);
		
		
	}
	
	public class OnItemClickListenerimp implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			@SuppressWarnings("unchecked")
			HashMap<String,Object> map=(HashMap<String,Object>)gridview.
					getItemAtPosition(position);
			final String classid=map.get("classid").toString();
			final String className=map.get("classname").toString();
			final String classTerm=map.get("term").toString().replace("\n", "");
			final String Term_temp=map.get("term_temp").toString();
			mam=new MyAlertMenu(ScheduleManage.this,new String[]{"����α�","ɾ���α�","ȡ��"});
			mam.setOnItemClickListener(new MyDialogMenuInt() {
				@Override
				public void onItemClick(int position) {
					switch(position){
					case 0:
						sh.setCurrentScheduleClassId(classid);
						sh.setCurrentScheduleTerm(Term_temp);
						Intent it=new Intent(ScheduleManage.this,Main.class);
						it.putExtra("param","3");
						startActivity(it);
						finish();
						break;
					case 1:
						mad=new MyAlertDialog(ScheduleManage.this);
						mad.setTitle("��Ϣ��ʾ");
						mad.setMessage("��ȷ��Ҫɾ��\n\n"+className+"��\n("+classTerm+")\n\n�γ̱���?");
						mad.setLeftButton("ȷ��",new MyDialogInt() {
							@Override
							public void onClick(View view) {
								mad.dismiss();
								String currClassId=sh.getCurrentScheduleClassId();
								String currTerm=sh.getCurrentScheduleTerm();
								if(currClassId.equals(classid)&&currTerm.equals(Term_temp)){
									T.showShort(ScheduleManage.this,"��ǰ����ʹ�õĿγ̱��ܱ�ɾ��!");
								}else{
									try{
										new ScheduleDB(ScheduleManage.this,ScheduleDB.TB_SYMANAGE).
											deleteManage(classid,Term_temp);
										new ScheduleDB(ScheduleManage.this,ScheduleDB.TB_SYLLABUS).
											deleteSchedule(classid,Term_temp);
										T.showShort(ScheduleManage.this,"�γ̱�ɾ���ɹ���");
									}catch(Exception e){
										T.showShort(ScheduleManage.this,"ɾ�������쳣��");
									}finally{
										onCreate(null);
									}
								}
							}
						});
						mad.setRightButton("ȡ��",new MyDialogInt() {
							@Override
							public void onClick(View view) {
								mad.dismiss();
							}
						});
						break;
					case 2:
						break;
					default:
						break;
					}
				}
			});
		}
		
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			this.finish();
			break;
		case 2:
			break;
		case 3:
			break;
		default:
			break;
		}

	}

}
