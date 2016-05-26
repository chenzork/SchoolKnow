package com.pw.schoolknow.push;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import com.pw.schoolknow.app.MyApplication;
import com.pw.schoolknow.helper.NetHelper;
import com.pw.schoolknow.utils.MD5Util;
import com.pw.schoolknow.utils.T;

import android.os.AsyncTask;
import android.os.Handler;

public class AsyncSend {
	
	private String mMessage;
	public Handler mHandler;
	private MyAsyncTask mTask;
	private String mUserId;
	private OnSendScuessListener mListener;
	
	public AsyncSend(String jsonMsg,String useId) {
		mMessage = jsonMsg;
		mUserId = useId;
		mHandler = new Handler();
	}
	
	public interface OnSendScuessListener {
		void sendScuess();
		void sendFailure();
	}
	
	public void setOnSendScuessListener(OnSendScuessListener listener) {
		this.mListener = listener;
	}
	
	Runnable reSend = new Runnable() {
		public void run() {
			send();//�ط�
		}
	};
	
	// ����
	public void send() {
		if (NetHelper.isNetConnected(MyApplication.getInstance())) {//����������
			mTask = new MyAsyncTask();
			mTask.execute(mMessage,mUserId);
			
		} else {
			T.showShort(MyApplication.getInstance(),"�뱣��������������");
		}
	}

	// ֹͣ
	public void stop() {
		if (mTask != null)
			mTask.cancel(true);
	}
	
	
	class MyAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... message) {
			String result = "";
			if(mUserId.trim().length()!=0){
				
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("action", "pushmessage");
				
				/*---���´��������趨�ӿ���Ӧ����---*/
				param.put("appkey", GetuiApi.APP_KEY);
				param.put("appid", GetuiApi.APP_ID);
				
				param.put("data",message[0]);
				param.put("clientid",message[1]); //����ȡ��ClientID
				param.put("expire", 3600); // ��Ϣ��ʱʱ�䣬��λΪ�룬��ѡ

				// ����Signֵ�����ڼ�Ȩ
				param.put("sign", makeSign(GetuiApi.MASTER_SECRET, param));

				result = GetuiHttpPost.httpPost(param);
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//T.showLong(MyApplication.getInstance(), result);
			try {
				JSONObject JsonData = new JSONObject(result);
				String reback=JsonData.getString("result");
				if(reback.equals("ok")){
					if (mListener != null){
						mListener.sendScuess();
					}
				}else{
					if (mListener != null){
						mListener.sendFailure();
					}
					if(reback.equals("TokenMD5NoUsers")){
						T.showLong(MyApplication.getInstance(), "�û��˺��쳣,�ݲ��ܷ�����Ϣ");
					}
				}
			} catch (JSONException e) {
				
			}
			
			
		}
	}
	
	/**
	 * ����Sign����
	 */
	public static String makeSign(String masterSecret, Map<String, Object> params) throws IllegalArgumentException {
		if (masterSecret == null || params == null) {
			throw new IllegalArgumentException("masterSecret and params can not be null.");
		}

		if (!(params instanceof SortedMap)) {
			params = new TreeMap<String, Object>(params);
		}

		StringBuilder input = new StringBuilder(masterSecret);
		for (Entry<String, Object> entry : params.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof String || value instanceof Integer || value instanceof Long) {
				input.append(entry.getKey());
				input.append(entry.getValue());
			}
		}
		return MD5Util.Md5(input.toString());
	}
	

}
