package com.pw.schoolknow.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.pw.schoolknow.R;
import com.pw.schoolknow.app.MyApplication;
import com.pw.schoolknow.bean.CountdownItem;
import com.pw.schoolknow.db.CountdownDB;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.utils.TimeUtil;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.MyAlertDialog;
import com.pw.schoolknow.widgets.MyAlertDialog.MyDialogInt;

public class CountDownUpdate extends BaseActivity {
	
	private EditText title,address;
	private TextView date,time;
	private CountdownItem item=null;
	private MyAlertDialog mad;
	private MyAlertDialog timePicker;
	private MyAlertDialog datePicker;
	
	private Button btn;
	
	public int year,month,day,hour,min=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_count_down_update);
		setTitle("������");
		
		item=(CountdownItem) getIntent().getSerializableExtra(CountDown.SER_KEY);
		
		title=(EditText) super.findViewById(R.id.count_down_update_title);
		date=(TextView) super.findViewById(R.id.count_down_update_dat);
		time=(TextView) super.findViewById(R.id.count_down_update_time);
		address=(EditText) super.findViewById(R.id.count_down_update_address);
		
		btn=(Button) super.findViewById(R.id.count_down_update_btn);
		
		date.setOnClickListener(new EditOnclick());
		time.setOnClickListener(new EditOnclick());
		btn.setOnClickListener(new EditOnclick());
		
		if(item==null){
			setTitleBar(R.drawable.btn_titlebar_back,0);
		}else{
			setTitleBar(R.drawable.btn_titlebar_back,"",0,"ɾ��");
			
			title.setText(item.getTitle());
			address.setText(item.getAddress());
			Long time_s=item.getTime_samp();
			date.setText(TimeUtil.getDayTime(time_s));
			time.setText(TimeUtil.getHourAndMin(time_s));
			
			year=TimeUtil.getYear(time_s);
			month=TimeUtil.getMonth(time_s);
			day=TimeUtil.getDay(time_s);
			hour=TimeUtil.getHour(time_s);
			min=TimeUtil.getMin(time_s);
		}

	}
	
	public class EditOnclick implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(v==time){
				timePicker=new MyAlertDialog(CountDownUpdate.this);
				timePicker.setTitle("ѡ��ʱ��");
				final TimePicker tp=new TimePicker(CountDownUpdate.this);
				tp.setIs24HourView(true);
				timePicker.setContentView(tp);
				timePicker.setLeftButton("ȷ��", new MyDialogInt() {
					@Override
					public void onClick(View view) {
						hour=tp.getCurrentHour();
						min=tp.getCurrentMinute();
						time.setText(hour+":"+min);
						timePicker.dismiss();
					}
				});
				timePicker.setRightButton("ȡ��", new MyDialogInt() {
					@Override
					public void onClick(View view) {
						timePicker.dismiss();
					}
				});
			}else if(v==date){
				datePicker=new MyAlertDialog(CountDownUpdate.this);
				datePicker.setTitle("ѡ������");
				final DatePicker dp=new DatePicker(CountDownUpdate.this);
				datePicker.setContentView(dp);
				datePicker.setLeftButton("ȷ��", new MyDialogInt() {
					@Override
					public void onClick(View view) {
						year=dp.getYear();
						month=dp.getMonth()+1;
						day=dp.getDayOfMonth();
						date.setText(year+"-"+month+"-"+day);
						datePicker.dismiss();
					}
				});
				datePicker.setRightButton("ȡ��", new MyDialogInt() {
					@Override
					public void onClick(View view) {
						datePicker.dismiss();
					}
				});
			}else if(v==btn){
				if(title.getText().length()==0){
					T.showShort(MyApplication.getInstance(), "���ⲻ��Ϊ��");
				}else if(title.getText().length()>30){
					T.showShort(MyApplication.getInstance(), "���ⲻ�ܳ���30����");
				}else if(date.getText().length()==0){
					T.showShort(MyApplication.getInstance(), "��ѡ������");
				}else if(time.getText().length()==0){
					T.showShort(MyApplication.getInstance(), "��ѡ��ʱ��");
				}else if(address.getText().length()>30){
					T.showShort(MyApplication.getInstance(), "��ַ���ܳ���30����");
				}else{
					//����
					if(item==null){
						long time_samp=TimeUtil.createtimesamp(year,month,day,hour,min);
						CountdownItem cItem=new CountdownItem(
								title.getText().toString(), time_samp,address.getText().toString());
						new CountdownDB(CountDownUpdate.this).insert(cItem);
						T.showShort(MyApplication.getInstance(), "��ӳɹ�");
					//�޸�
					}else{
						long time_samp=TimeUtil.createtimesamp(year,month,day,hour,min);
						CountdownItem cItem=new CountdownItem(
								title.getText().toString(), time_samp,address.getText().toString());
						new CountdownDB(CountDownUpdate.this).update(cItem, item.getId());
						T.showShort(MyApplication.getInstance(), "�޸ĳɹ�");
					}
					Intent it=new Intent(CountDownUpdate.this,CountDown.class);
					startActivity(it);
					finish();
				}
				
			}
		}
		
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			mad=new MyAlertDialog(CountDownUpdate.this);
			mad.setTitle("��Ϣ��ʾ");
			mad.setMessage("��ȷ��Ҫɾ��������¼��?");
			mad.setLeftButton("ȷ��", new MyDialogInt() {
				@Override
				public void onClick(View view) {
					mad.dismiss();
					new CountdownDB(CountDownUpdate.this).delete(item.getId());
					T.showShort(MyApplication.getInstance(), "ɾ���ɹ�");
					Intent it=new Intent(CountDownUpdate.this,CountDown.class);
					startActivity(it);
					finish();
				}
			});
			mad.setRightButton("ȡ��", new MyDialogInt() {
				@Override
				public void onClick(View view) {
					mad.dismiss();
				}
			});
			break;
		default:
			break;
		}
	}

}
