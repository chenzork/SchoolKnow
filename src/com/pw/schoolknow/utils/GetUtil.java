package com.pw.schoolknow.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class GetUtil {
	
	/**
	 * �������л�ȡ��Դ
	 * @param url
	 * @return
	 */
	public static String getRes(String url){
		try {
			URL u=new URL(url);
			HttpURLConnection conn=(HttpURLConnection) u.openConnection();
			
			//���ó�ʱ
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(30000);
			 StringBuffer buffer = new StringBuffer();
			 InputStreamReader r = new InputStreamReader(conn.getInputStream());
			 BufferedReader rd = new BufferedReader(r);
			 String line;
			 while ((line = rd.readLine()) != null) {
				    buffer.append(line);
			 }
			 rd.close();
			 conn.getInputStream().close();
			 return buffer.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	
	/**
	 * ��ȡ����ͼƬ
	 * @return
	 */
	
	public static Bitmap getBitMap(String picUrl){
		URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
        	myFileUrl = new URL(picUrl);
	        HttpURLConnection conn = (HttpURLConnection) myFileUrl
	                        .openConnection();
	        conn.setDoInput(true);
	        conn.connect();
	        InputStream is = conn.getInputStream();
	        bitmap = BitmapFactory.decodeStream(is);
	        is.close();
        } catch (Exception e) {
        }

        return bitmap;
	}
	
	
	/**
	* ��ָ��URL����GET����������
	* @param url ���������URL
	* @param param �������
	* List<NameValuePair> params=new ArrayList<NameValuePair>(); 
	* params.add(new BasicNameValuePair("key","value")
	* @return URL������Զ����Դ����Ӧ
	*/
	public static String sendPost(String url , List<NameValuePair> params){
		String result = "";
		HttpPost httpRequest=null; 
	    HttpResponse httpResponse; 
	    httpRequest=new HttpPost(url); 
	    try { 
            //����HTTP request 
            httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8)); 
            //ȡ��HTTP response 
            httpResponse=new DefaultHttpClient().execute(httpRequest); 
            //��״̬��Ϊ200 
            if(httpResponse.getStatusLine().getStatusCode()==200){ 
                //ȡ����Ӧ�ִ� 
                String strResult=EntityUtils.toString(httpResponse.getEntity()); 
                result=strResult; 
            }else{ 
            	result="Error Response"+httpResponse.getStatusLine().toString(); 
            } 
        } catch (Exception e) { 
           
        }
		return result;		
	}
	
	
	/**
	* ��ָ��URL����POST����������
	* @param url ���������URL
	* @param param ����������������Ӧ����name1=value1&name2=value2����ʽ��
	* @return URL������Զ����Դ����Ӧ
	*/
	public static String sendPost(String url,String param){
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try{
			URL realUrl = new URL(url);
			//�򿪺�URL֮�������
			URLConnection conn = realUrl.openConnection();
			//����ͨ�õ���������
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			//����POST�������������������
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// ��ȡURLConnection�����Ӧ�������
			out = new PrintWriter(conn.getOutputStream());
			//�����������
			out.print(param);
			//flush ������Ļ���
			out.flush();
			//����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine())!= null){
				result += line;
			}
		}catch(Exception e){
			//System.out.println("����POST ��������쳣��" + e);
			e.printStackTrace();
		}finally{
			try{
				if (out != null){
					out.close();
				}
				if (in != null){
					in.close();
				}
			}catch (IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
		
	}
	
	
	/**
	* ��ָ��URL����GET����������
	* @param url ���������URL
	* @param param ����������������Ӧ����name1=value1&name2=value2����ʽ��
	* @return URL������Զ����Դ����Ӧ
	*/
	public static String sendGet(String url , String param){
		String result = "";
		BufferedReader in = null;
		try{
			String urlName = url + "?" + param;
			URL realUrl = new URL(urlName);
			//�򿪺�URL֮�������
			URLConnection conn = realUrl.openConnection();
			//����ͨ�õ���������
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			//����ʵ�ʵ�����
			conn.connect();
			//��ȡ������Ӧͷ�ֶ�
			//Map<String, List<String>> map = conn.getHeaderFields();
			//�������е���Ӧͷ�ֶ�
			//for (String key : map.keySet()){
				//System.out.println(key + "--->" + map.get(key));
			//}
			//����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine())!= null){
				result +=line;
			}
		}catch(Exception e){
		 //System.out.println("����GET��������쳣��" + e);
		 e.printStackTrace();
		}finally{
			try{
				if (in != null){
					in.close();
				}
			}catch (IOException ex){
				ex.printStackTrace();
			}
		}
		return result;		
	}

}
