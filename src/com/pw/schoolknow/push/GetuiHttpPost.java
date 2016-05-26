package com.pw.schoolknow.push;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.json.simple.JSONObject;

public class GetuiHttpPost {

	public static final String SERVICEURL = "http://sdk.open.api.igexin.com/service";
	public static final int CONNECTION_TIMEOUT_INT = 8000;
	public static final int READ_TIMEOUT_INT = 5000;

	public static String httpPost(Map<String, Object> map) {

		String param = JSONObject.toJSONString(map);
		String result = ""; // ��ȡ��������������

		if (param != null) {

			URL url = null;

			try {
				url = new URL(SERVICEURL);
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				urlConn.setDoInput(true); // ���������������ֽ���
				urlConn.setDoOutput(true); // ��������������ֽ���
				urlConn.setRequestMethod("POST");
				urlConn.setUseCaches(false); // ���û���
				urlConn.setRequestProperty("Charset", "utf-8");
				urlConn.setConnectTimeout(CONNECTION_TIMEOUT_INT);
				urlConn.setReadTimeout(READ_TIMEOUT_INT);

				urlConn.connect(); // ���Ӽ�������˷�����Ϣ

				DataOutputStream dop = new DataOutputStream(urlConn.getOutputStream());
				dop.write(param.getBytes("utf-8")); // ���Ͳ���
				dop.flush(); // ���ͣ���ջ���
				dop.close(); // �ر�

				// ���濪ʼ�����չ���
				BufferedReader bufferReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
				
				String readLine = null;
				while ((readLine = bufferReader.readLine()) != null) {
					result += readLine;
				}
				bufferReader.close();
				urlConn.disconnect();

				return result;

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			
		}
		return result;
	}

}
