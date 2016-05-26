package com.pw.schoolknow.view;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;
import com.pw.schoolknow.app.MyApplication;
import com.pw.schoolknow.utils.BMapUtil;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.MyProgressBar;



public class MapEcjtu extends BaseActivity {
	
	public BMapManager mBMapMan = null;  
	public static MapView mMapView = null;
	private MapController mMapController = null;
	
	@ViewInject(R.id.map_location_btn)  
	private ImageButton locationBtn;
	
	// ��λ���
	LocationClient mLocClient;
	LocationData locData = null;
	public MyLocationListenner myListener = new MyLocationListenner();
		
	//��λͼ��
	locationOverlay myLocationOverlay = null;
	
	public static MyProgressBar mpb;
	
	//��������ͼ��
	private PopupOverlay   pop  = null;//��������ͼ�㣬����ڵ�ʱʹ��
	private TextView  popupText = null;//����view
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	 
		mBMapMan=new BMapManager(getApplication());  
		mBMapMan.init(MyApplication.BaiduMapApi, null); 
		
		setContentView(R.layout.act_map_location);
		setTitle("�����ͼ");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		ViewUtils.inject(this);
		
		locationBtn.setOnClickListener(new BtnClick());
		
		
		mMapView=(MapView)findViewById(R.id.mapView);  
		mMapController = mMapView.getController();
		mMapView.setBuiltInZoomControls(true); 
		mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
		
		mpb=new MyProgressBar(this);
		mpb.setMessage("���ڶ�λ��...");
		
		//��λ��ʼ��
        mLocClient = new LocationClient( this );
        locData = new LocationData();
        mLocClient.registerLocationListener( myListener );
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//��gps
        option.setCoorType("bd09ll");     //������������
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
       
        //��λͼ���ʼ��
		myLocationOverlay = new locationOverlay(mMapView);
		//���ö�λ����
	    myLocationOverlay.setData(locData);
	    //��Ӷ�λͼ��
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		//�޸Ķ�λ���ݺ�ˢ��ͼ����Ч
		mMapView.refresh();
	}
	
	protected void onDestroy(){  
        mMapView.destroy();  
        if(mBMapMan!=null){  
                mBMapMan.destroy();  
                mBMapMan=null;  
        }  
        super.onDestroy();  
	}  
	protected void onPause(){  
	        mMapView.onPause();  
	        if(mBMapMan!=null){  
	               mBMapMan.stop();  
	        }  
	        super.onPause();  
	}   
	protected void onResume(){  
	        mMapView.onResume();  
	        if(mBMapMan!=null){  
	                mBMapMan.start();  
	        }  
	       super.onResume();  
	}  
	
	public class BtnClick implements OnClickListener{
		public void onClick(View view) {
			if(view==locationBtn){
				mLocClient.requestLocation();
				mpb.show();
			}
		}
		
	}
	
	public class MyLocationListenner implements BDLocationListener {
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
                return ;
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            //�������ʾ��λ����Ȧ����accuracy��ֵΪ0����
            locData.accuracy = location.getRadius();
            // �˴��������� locData�ķ�����Ϣ, �����λ SDK δ���ط�����Ϣ���û������Լ�ʵ�����̹�����ӷ�����Ϣ��
            locData.direction = location.getDerect();
            //���¶�λ����
            myLocationOverlay.setData(locData);
            //����ͼ������ִ��ˢ�º���Ч
            mMapView.refresh();
            mpb.dismiss();
            mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
            myLocationOverlay.setLocationMode(LocationMode.NORMAL);
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	 //�̳�MyLocationOverlay��дdispatchTapʵ�ֵ������
  	public class locationOverlay extends MyLocationOverlay{

  		public locationOverlay(MapView mapView) {
  			super(mapView);
  			// TODO Auto-generated constructor stub
  		}
  		@Override
  		protected boolean dispatchTap() {
  			// TODO Auto-generated method stub
  			//�������¼�,��������
  			popupText.setBackgroundResource(R.drawable.popup);
			popupText.setText("�ҵ�λ��");
			pop.showPopup(BMapUtil.getBitmapFromView(popupText),
					new GeoPoint((int)(locData.latitude*1e6), (int)(locData.longitude*1e6)),
					8);
  			return true;
  		}
  		
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
