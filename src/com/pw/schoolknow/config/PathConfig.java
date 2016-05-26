package com.pw.schoolknow.config;

import android.os.Environment;

public class PathConfig {
	
	//sd��·��
	public static final String BASEPATH=Environment.getExternalStorageDirectory()
			.getPath() + "/schoolknow";
	
	//����ͷ��
	public static final String HEADPATH=BASEPATH+"/data/head/";
	
	//�������ص�ͼƬ
   public static final String SavePATH=PathConfig.BASEPATH + "/" + "photo" + "/";
   
   //ͼƬ�����ַ
   public static final String CacheImgPATH=PathConfig.BASEPATH + "/data/imgCache";
   
   //��ʱ�ļ���
   public static final String TempPATH=PathConfig.BASEPATH + "/data/temp";
   
   //�����ļ���
   public static final String DownPATH=PathConfig.BASEPATH + "/DownLoad";
   
   // ���sd�Ƿ����
   public static boolean checkSD(){
	   String sdStatus = Environment.getExternalStorageState();
       if (sdStatus.equals(Environment.MEDIA_MOUNTED)) { 
             return true;
       }
	   return false;
   }
   
   
   
   
}
