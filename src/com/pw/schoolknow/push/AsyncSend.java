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
			send();//重发
		}
	};
	
	// 发送
	public void send() {
		if (NetHelper.isNetConnected(MyApplication.getInstance())) {//如果网络可用
			mTask = new MyAsyncTask();
			mTask.execute(mMessage,mUserId);
			
		} else {
			T.showShort(MyApplication.getInstance(),"请保持网络连接正常");
		}
	}

	// 停止
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
				
				/*---以下代码用于设定接口相应参数---*/
				param.put("appkey", GetuiApi.APP_KEY);
				param.put("appid", GetuiApi.APP_ID);
				
				param.put("data",message[0]);
				param.put("clientid",message[1]); //您获取的ClientID
				param.put("expire", 3600); // 消息超时时间，单位为秒，可选

				// 生成Sign值，用于鉴权
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
						T.showLong(MyApplication.getInstance(), "用户账号异常,暂不能发送消息");
					}
				}
			} catch (JSONException e) {
				
			}
			
			
		}
	}
	
	/**
	 * 生成Sign方法
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
