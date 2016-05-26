package com.pw.schoolknow.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.pw.schoolknow.R;
import com.pw.schoolknow.config.DecodeConfig;
import com.pw.schoolknow.config.PathConfig;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.CollegeHelper;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.helper.NetHelper;
import com.pw.schoolknow.utils.BitmapUtil;
import com.pw.schoolknow.utils.FileUtils;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.ImageUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.utils.TimeUtil;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.MyAlertDialog;
import com.pw.schoolknow.widgets.MyAlertMenu;
import com.pw.schoolknow.widgets.MyAlertMenu.MyDialogMenuInt;

public class UserInfo extends BaseActivity {
	
	private MyAlertMenu mam;
	
	private RelativeLayout userHead,collegeLayout,sexLayout,nicklayout,bindstuidLayout;
	private ImageView head;
	
	
	private TextView uid;
	private TextView nickname;
	private TextView sex;
	private TextView college,stuid;
	
	private LoginHelper lh; 
	
	private MyAlertDialog collegeMad,sexMad;
	
	public String picName;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_user_info);
		setTitle("������Ϣ");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		lh=new LoginHelper(this);
		
		userHead=(RelativeLayout) super.findViewById(R.id.user_info_head_layout);
		head=(ImageView) userHead.findViewById(R.id.user_info_head);
		String path=PathConfig.HEADPATH+DecodeConfig.decodeHeadImg(lh.getUid())+".pw";
		Bitmap dBitmap=null;
		if(new File(path).exists()){
			dBitmap = BitmapFactory.decodeFile(path);
		}else{
			dBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.default_head);
		}
		head.setImageBitmap(ImageUtil.toRoundCorner(dBitmap, 10));
		userHead.setOnClickListener(new ItemClickListener());
		
		uid=(TextView) super.findViewById(R.id.user_info_uid);
		uid.setText(lh.getUid());
		
		nickname=(TextView) super.findViewById(R.id.user_info_nickname);
		nickname.setText(lh.getNickname());
		
		sex=(TextView) super.findViewById(R.id.user_info_sex);
		sex.setText(lh.getSex());
		
		collegeLayout=(RelativeLayout) super.findViewById(R.id.user_info_college_layout);
		collegeLayout.setOnClickListener(new ItemClickListener());
		college=(TextView) super.findViewById(R.id.user_info_college);
		college.setText(lh.getCollege());
		
		sexLayout=(RelativeLayout) super.findViewById(R.id.user_info_sex_layout);
		sexLayout.setOnClickListener(new ItemClickListener());
		
		nicklayout=(RelativeLayout) super.findViewById(R.id.user_info_nick_layout);
		nicklayout.setOnClickListener(new ItemClickListener());
		
		bindstuidLayout=(RelativeLayout) super.findViewById(R.id.user_info_bindstuid_layout);
		bindstuidLayout.setOnClickListener(new ItemClickListener());
		stuid=(TextView) super.findViewById(R.id.user_info_bindstuid);
		if(!lh.getStuId().trim().equals("")){
			stuid.setText(lh.getStuId());
		}
	}
	
	

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	



	@Override
	protected void onRestart() {
		super.onRestart();
		//�����ǳ�
		String temp_name=lh.getNickname().trim();
		if(temp_name.length()==0){
			nickname.setText("�����û�");
		}else{
			nickname.setText(lh.getNickname());
		}
		if(!lh.getStuId().trim().equals("")){
			stuid.setText(lh.getStuId());
		}
	}



	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		default:
			break;
		}
	}
	
	
	public class ItemClickListener implements OnClickListener{
		public void onClick(View v) {
			if(v==userHead){
				mam=new MyAlertMenu(UserInfo.this,new String[]{"�����ѡ��","�������ѡ��","ȡ��"});
				mam.setOnItemClickListener(new MyDialogMenuInt() {
					@Override
					public void onItemClick(int position) {
						switch(position){
						case 0:
							//ѡ��ͼ��
							Intent it = new Intent(Intent.ACTION_PICK, null);
							it.setDataAndType(
									MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
									"image/*");
							startActivityForResult(it, 1);
							break;
						case 1:
							//ѡ�����
							picName=String.valueOf(TimeUtil.getCurrentTime())+".jpg";
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							FileUtils.createPath(PathConfig.SavePATH);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PathConfig.SavePATH,picName)));
							startActivityForResult(intent, 2);
							break;
						case 2:
							break;
						default:
							break;
						}
					}
				});
				//ѡ��ѧԺ
			}else if(v==collegeLayout){
				collegeMad=new MyAlertDialog(UserInfo.this);
				collegeMad.setTitle("��ѡ��ѧԺ");
				final ListView collegeLv=new ListView(UserInfo.this);
				collegeLv.setDivider(getResources().getDrawable(R.color.dedede));
				collegeLv.setDividerHeight(2);
				ArrayAdapter<CharSequence> colleageAda=ArrayAdapter.createFromResource(UserInfo.this,
		        		R.array.college,android.R.layout.simple_spinner_dropdown_item);
				colleageAda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				collegeLv.setAdapter(colleageAda);
				collegeMad.setContentView(collegeLv);
				collegeLv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						collegeMad.dismiss();
						if(!NetHelper.isNetConnected(UserInfo.this)){
							T.showShort(UserInfo.this,R.string.net_error_tip);
							return;
						}
						new AsyncUpdateInfo(college).execute("college",CollegeHelper.collegeName[position]);
					}
				});
			}else if(v==sexLayout){
				sexMad=new MyAlertDialog(UserInfo.this);
				sexMad.setTitle("��ѡ���Ա�");
				final ListView sexLv=new ListView(UserInfo.this);
				sexLv.setDivider(getResources().getDrawable(R.color.dedede));
				sexLv.setDividerHeight(2);
				String[] sexArr={"��","Ů"};
				ArrayAdapter<CharSequence> colleageAda=new ArrayAdapter<CharSequence>(UserInfo.this,
						android.R.layout.simple_spinner_dropdown_item,sexArr);
				sexLv.setAdapter(colleageAda);
				sexMad.setContentView(sexLv);
				sexLv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						sexMad.dismiss();
						String str="";
						if(position==0){
							str="��";
						}else{
							str="Ů";
						}
						if(!NetHelper.isNetConnected(UserInfo.this)){
							T.showShort(UserInfo.this,R.string.net_error_tip);
							return;
						}
						new AsyncUpdateInfo(sex).execute("sex",str);
					}
				});
			}else if(v==nicklayout){
				Intent it=new Intent(UserInfo.this,UpdateUserName.class);
				startActivity(it);
			}else if(v==bindstuidLayout){
				Intent it=new Intent(UserInfo.this,BindStuid.class);
				startActivity(it);
			}
		}
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		//������ȡ
		case 1:
			if(data!=null){
				startPhotoZoom(data.getData());
			}
			break;
		// �������
		case 2:
			File temp = new File(PathConfig.SavePATH,picName);
			if(temp.exists()){
				startPhotoZoom(Uri.fromFile(temp));
			}
			break;
		//ȡ�òü����ͼƬ
		case 3:
			if(data != null){
				setPicToView(data);
			}
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//�������crop=true�������ڿ�����Intent��������ʾ��VIEW�ɲü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 180);
		intent.putExtra("outputY", 180);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
	
	/**
	 * ����ü�֮���ͼƬ����
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			head.setImageBitmap(ImageUtil.toRoundCorner(
					photo, 10));
			
			//��ͷ�񱣴���sd��head�ļ���
			final LoginHelper lh=new LoginHelper(UserInfo.this);
			final String path=PathConfig.HEADPATH;
			final String name=DecodeConfig.decodeHeadImg(lh.getUid())+".pw";
			try {
				BitmapUtil.saveImg(photo,path,name);
			} catch (Exception e) {
				e.printStackTrace();
			}
			File f=new File(path+name);
			RequestParams params = new RequestParams();
			params.addBodyParameter("uploadedfile",f);
			HttpUtils http = new HttpUtils();
			http.send(HttpMethod.POST, ServerConfig.HOST+"/schoolknow/manage/updateHead.php?"+
					"uid="+lh.getUid()+"&token="+lh.getToken(),
					params,new RequestCallBack<String>() {
						public void onFailure(HttpException arg0, String arg1) {
							T.showLong(UserInfo.this,"ͷ���ϴ�ʧ�ܣ������³���");
						}
						public void onSuccess(ResponseInfo<String> arg0) {
							T.showLong(UserInfo.this,"ͷ���ϴ��ɹ�");
						}
				
			});
			
			//ByteArrayOutputStream stream = new ByteArrayOutputStream();
			//photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			//byte[] b = stream.toByteArray();
			// ��ͼƬ�����ַ�����ʽ�洢����
			//tp = new String(Base64Coder.encodeLines(b));
			
		}
	}
	
	public class AsyncUpdateInfo extends AsyncTask<String,Void,String>{
		
		public TextView tv;
		public String val;
		public String param;
		public AsyncUpdateInfo(TextView tv){
			this.tv=tv;
		}
		
		protected String doInBackground(String... str) {
			val=str[1];
			param=str[0];
			String collegeId="";
			for(int i=0;i<CollegeHelper.collegeName.length;i++){
				if(CollegeHelper.collegeName[i].equals(val)){
					collegeId=CollegeHelper.collegeId[i];
				}
			}
			List<NameValuePair> params=new ArrayList<NameValuePair>(); 
			params.add(new BasicNameValuePair("uid",lh.getUid()));
			params.add(new BasicNameValuePair("token",lh.getToken()));
			params.add(new BasicNameValuePair(str[0],param.equals("college")?collegeId:val));
			return GetUtil.sendPost(ServerConfig.HOST+"/schoolknow/manage/updateInfo.php", params);
		}
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result.equals("success")){
				tv.setText(val);
				T.showShort(UserInfo.this, "�޸ĳɹ�");
				if(param.equals("sex")){
					lh.setSex(val);
				}else if(param.equals("college")){
					lh.setCollege(val);
				}
			}else{
				T.showShort(UserInfo.this, "�޸�ʧ��,�����³���");
			}
		}
		
	}
	
	
	

}
