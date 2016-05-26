package com.pw.schoolknow.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.content.Context;

public class LocalFileSR {
	
	
	@SuppressLint("WorldReadableFiles")
	/**
	 * д�����ش洢
	 * @param context
	 * @param filename
	 * @param message
	 * @return
	 */
	public static Boolean writeLocalFileData(Context context,String filename,String message){
		 try {
	            FileOutputStream outStream=context.openFileOutput(filename,Context.MODE_WORLD_READABLE);
	            outStream.write(message.getBytes());
	            outStream.close();
	        } catch (FileNotFoundException e) {
	            return false;
	        }catch (IOException e){
	            return false;
	        }
		 return true;
	}
	
	/**
	 * �ӱ��ش洢��������
	 * @return
	 */
	 public static String readLocalFileData(Context context,String filename){
		 String result="";
        try {
               FileInputStream fin =context.openFileInput(filename);
               //��ȡ�ļ�����
               int lenght = fin.available();
               byte[] buffer = new byte[lenght];
               fin.read(buffer);
               result = EncodingUtils.getString(buffer,"UTF-8");
       } catch (Exception e) {
               e.printStackTrace();
       }
       return result;
	 }
	 /**
	  * ɾ�������ļ�
	  * @param context
	  * @param filename
	  */
	 
	 public void DeleteLocalFile(Context context,String filename){
		 context.deleteFile(filename);
	 }

}
