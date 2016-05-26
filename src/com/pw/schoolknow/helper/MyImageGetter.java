package com.pw.schoolknow.helper;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.pw.schoolknow.R;
import com.pw.schoolknow.config.PathConfig;
import com.pw.schoolknow.utils.BitmapUtil;
import com.pw.schoolknow.utils.SDCacheManager;
import com.pw.schoolknow.utils.Sha1Util;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.text.Html.ImageGetter;
import android.widget.TextView;

public class MyImageGetter implements ImageGetter {
	
	//SD�����С 100M
	private int sdMaxSize = 100 * 1024 * 1024;
	 //�ڴ滺���С 4M
	private int mMaxSize = 4 * 1024 * 1024;

	 //һ������洢���ڴ� ����LRUCache ����ͼƬ����ʱOOM
	public LruCache<String, Drawable> mCache;

	// ��������洢��SD�� ���Զ���cache������ʵ��,�� LRU����
	public SDCacheManager sdCache;

	 //SD������·��
	private String cachePath =PathConfig.BASEPATH + "/data/imgCache";
	
	private TextView tv;
	
	public static MyImageGetter getters;
	
	public static int a = 0, b = 0;
	public static boolean aa = false;
	
	public Context context;
	
	public MyImageGetter(Context context,TextView tv){
		mCache = new LruCache<String, Drawable>(mMaxSize);
		sdCache = new SDCacheManager(sdMaxSize);
		initSdCache();
		this.tv = tv;
		this.context=context;
	}
		
	public Drawable getDrawable(String source) {
		
		URLDrawable urlDrawable = new URLDrawable();
		ImageGetterAsyncTask asyncTask = null;
		//���ڴ����ȡ
		if (mCache.get(source) != null) {
			return mCache.get(source);
		}
		// ��SD��ȡͼƬ
		else if (sdCache.contains(urlToFileName(source))) {
			asyncTask = new ImageGetterAsyncTask(urlDrawable, "sdcard");
		}
		// ����������
		else {
			asyncTask = new ImageGetterAsyncTask(urlDrawable, "net");
		}
		asyncTask.execute(source);
		
		//return urlDrawable;
		 //urlDrawable.drawable=context.getResources().getDrawable(R.drawable.default_head);
		//urlDrawable.setBounds(0, 0, urlDrawable.getIntrinsicWidth(), urlDrawable.getIntrinsicHeight());
		//new AsyncLoadImageGetter(urlDrawable).execute(source);
		return urlDrawable;
			
	}
	
	public class AsyncLoadImageGetter extends AsyncTask<String, Void, Drawable>{
		
		private URLDrawable ud;
		
		public AsyncLoadImageGetter(URLDrawable ud){
			this.ud=ud;
		}
		
		protected Drawable doInBackground(String... urlString) {
			// TODO Auto-generated method stub
			return new BitmapDrawable(BitmapUtil.loadImageFromUrl(urlString[0],10));
		}
		
		protected void onPostExecute(Drawable result) {
			super.onPostExecute(result);
			ud.setBounds(0, 0, 0 + result.getIntrinsicWidth(),
					result.getIntrinsicHeight());
			ud.drawable=result;
			if (result != null) {
				tv.setText(tv.getText()); // ͨ��������������� TextView ������������UI
				//tv.invalidate();
				//MyImageGetter.this.tv.requestLayout();
			}
		}
		
		
		
	}
	
	
	public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
		
		URLDrawable urlDrawable;
		String tag;
		
		public ImageGetterAsyncTask(URLDrawable d, String tag) {
			this.tag = tag;
			this.urlDrawable = d;
		}
		
		protected void onPostExecute(Drawable result) {
			if (result != null) {
				urlDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(),
						result.getIntrinsicHeight());
				urlDrawable.drawable = result;
				if (result != null) {
					tv.setText(tv.getText()); // ͨ��������������� TextView ������������UI
				}
			}
		}
		
		protected Drawable doInBackground(String... params) {
			String source = params[0];// ͼƬURL
			return fetchDrawable(source);
		}
		
		// ��ȡURL��Drawable����
		public Drawable fetchDrawable(String urlString) {
			BitmapDrawable bitmap = null;
			Drawable drawable = null;
			try {
				InputStream is;
				if (tag.equals("net")){
					is = fetch(urlString);
					//Bitmap bm=BitmapUtil.getImageThumbnail(urlString, 100, 100);
					//bitmap=new BitmapDrawable(bm);
				}else{
					is = sdCache.getInputStream(cachePath,
							urlToFileName(urlString));
				}
				bitmap = (BitmapDrawable) BitmapDrawable.createFromStream(is,
						"src");
				drawable = bitmap;
				a = bitmap.getBitmap().getWidth();
				b = bitmap.getBitmap().getHeight();
				drawable.setBounds(0, 0, bitmap.getBitmap().getWidth(), bitmap
						.getBitmap().getHeight());
				// ���뻺��
				if (tag.equals("net")) {
					mCache.put(urlString, drawable);
					sdCache.saveToSdCard(cachePath, urlToFileName(urlString),
							drawable);
				}
				return drawable;
			} catch (Exception e) {
				Resources res = context.getResources();
				Drawable d=res.getDrawable(R.drawable.default_head);
				return d;
			}
		}

		private InputStream fetch(String urlString)
				throws MalformedURLException, IOException {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(urlString);
			HttpResponse response = httpClient.execute(request);
			return response.getEntity().getContent();
		}
		
	}
	
	
	
	public boolean flag() {
		return aa;
	}
	
	/**
	 * ����SD���л����ļ�Ŀ¼ ��ʼ��Sdcache
	 */
	private void initSdCache() {

		File path = new File(cachePath);
		if (path.exists()) {
			File[] f = path.listFiles();
			for (File ff : f) {
				sdCache.add(ff.getName(), ff);
			}
		}
	}
	
	/**
	 * ��ȡURL·���е��ļ�����
	 * 
	 * @param url
	 * @return �ļ�����
	 */
	private String urlToFileName(String url) {
//		String ftype = url.substring(url.lastIndexOf("."));
//		String fName = url.substring(url.lastIndexOf("/") + 1,
//				url.lastIndexOf(ftype));
//		return fName.replace(".", "") + ftype;
		return new Sha1Util().getDigestOfString(url.getBytes());
	}
	
	
	public class URLDrawable extends BitmapDrawable {

		protected Drawable drawable;
		public void draw(Canvas canvas) {
			
			if (drawable != null) {
				drawable.draw(canvas);
			}
		}
	}
	
}
