package com.pw.schoolknow.view;

import com.pw.schoolknow.R;
import com.pw.schoolknow.app.MyApplication;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.utils.SmartBarUtils;
import com.pw.schoolknow.utils.T;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.RadioButton;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class Main extends TabActivity implements OnCheckedChangeListener {
	
	private RadioGroup group;
	private TabHost tabHost;
	
	
	public static final String[] TAB_ITEM = {"tabItem1","tabItem2","tabItem3","tabItem4","tabItem5"};
	@SuppressWarnings("rawtypes")
	public static final Class[] activity={Index.class,Jwc.class,User.class,Schedule.class,More.class};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_main);
		
		if(SmartBarUtils.hasSmartBar()){
			View decorView = getWindow().getDecorView();
			SmartBarUtils.hide(decorView);
		}
		
		group = (RadioGroup) findViewById(R.id.main_radiogp);
		group.setOnCheckedChangeListener(this);
		
		tabHost = this.getTabHost();
		
		TabSpec[] tab=new TabSpec[TAB_ITEM.length];
		for(int i=0;i<TAB_ITEM.length;i++){
			tab[i]=tabHost.newTabSpec(TAB_ITEM[i]);
			////����Ѿ���¼��ת���û���ҳ��û��½��ת����¼ҳ��
			if(i==2&&!new LoginHelper(MyApplication.getInstance()).hasLogin()){
				tab[i].setIndicator(TAB_ITEM[i]).setContent(new Intent(this,Login.class));
			}else{
				tab[i].setIndicator(TAB_ITEM[i]).setContent(new Intent(this, activity[i]));
			}
			tabHost.addTab(tab[i]);
		}
		
		//ѡ��Ĭ��ҳ��
		int position=0;
		try{
			Intent it=getIntent();
			String param=it.getStringExtra("param");
			position=Integer.parseInt(param);
		}catch(Exception e){
			position=0;
		}finally{
			tabHost.setCurrentTabByTag(TAB_ITEM[position]);
			RadioButton CurrentBtn=(RadioButton) group.getChildAt(position);
			CurrentBtn.setChecked(true);
		}
		
		
	}
	
	


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) 
		{
		case R.id.radio_button1:
			tabHost.setCurrentTabByTag(TAB_ITEM[0]);
			break;
		case R.id.radio_button2:
			tabHost.setCurrentTabByTag(TAB_ITEM[1]);
			break;
		case R.id.radio_button3:
			tabHost.setCurrentTabByTag(TAB_ITEM[2]);
			break;
		case R.id.radio_button4:
			tabHost.setCurrentTabByTag(TAB_ITEM[3]);
			break;
		case R.id.radio_button5:
			tabHost.setCurrentTabByTag(TAB_ITEM[4]);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	 * ������2�η��ؼ��˳�ϵͳ
	 */
	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				T.showShort(Main.this,"�ٰ�һ�η��ؼ��˳�У԰ͨ");
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}
	

}
