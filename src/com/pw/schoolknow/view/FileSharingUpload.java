package com.pw.schoolknow.view;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.utils.UploadFileUtil;
import com.pw.schoolknow.view.base.BaseActivity;

public class FileSharingUpload extends BaseActivity {
	
	private Button select;
	private Button submit;
	private TextView tip;
	private EditText edit;
	private String FileUrl=null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_file_sharing_upload);
		setTitle("�����ļ��ϴ�");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		this.select=(Button) super.findViewById(R.id.sharing_upload_select);
		this.edit=(EditText) super.findViewById(R.id.sharing_upload_updata);
		this.tip=(TextView) super.findViewById(R.id.sharing_upload_tip);
		this.submit=(Button) super.findViewById(R.id.sharing_upload_submit);
		
		//��ת���ļ�ѡ�����
		this.select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent it=new Intent(FileSharingUpload.this,FileSelect.class);
				startActivity(it);				
			}
		});
		
		submit.setOnClickListener(new submitListener());
		
		String action=getIntent().getStringExtra("action");
		//���Ǵ�ѡ���ļ�������ת������
		if(action.equals("false")){
			return;
		}else{
			FileUrl=getIntent().getStringExtra("path");
			try{
				String fileName = FileUrl.substring(FileUrl.lastIndexOf("/")+1,
						FileUrl.lastIndexOf("."));
				tip.setText("��ѡ����ļ���:"+FileUrl+",�����޸��ϴ��ļ���");
				edit.setVisibility(View.VISIBLE);
				edit.setText(fileName);
			}catch(Exception e){
				T.showLong(FileSharingUpload.this,"��ѡ����ļ���ʽ����ȷ��������ѡ��");
			}
			
		}
		
		
	}
	
	//�ϴ��ļ�
	public class submitListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			File f=new File(FileUrl);
			if(FileUrl==null){
				T.showShort(FileSharingUpload.this, "����ѡ���ļ����ϴ�");
			}else if(!f.isFile()){
				T.showShort(FileSharingUpload.this, "��ѡ����ļ�����");
			}else if(f.length()/(1024*1024)>20){
				T.showShort(FileSharingUpload.this, "�ϴ����ļ����ܴ���20M");
			}else if(edit.getText().toString().equals("")){
				T.showShort(FileSharingUpload.this, "�ϴ����ļ�������Ϊ��");
			}else if(edit.getText().length()>30){
				T.showShort(FileSharingUpload.this, "�ϴ����ļ������ܳ���30���ַ�");
			}else{
				new UploadFileUtil(FileSharingUpload.this,FileUrl).execute();
				tip.setText("��ѡ����Ҫ������ļ�");
				edit.setText("");
				edit.setVisibility(View.GONE);
			}
		}
		
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub

	}

}
