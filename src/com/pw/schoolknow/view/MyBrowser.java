package com.pw.schoolknow.view;

import java.util.Stack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pw.schoolknow.R;
import com.pw.schoolknow.view.base.BaseActivity;
import com.pw.schoolknow.widgets.MyProgressBar;

public class MyBrowser extends BaseActivity {
	
	private WebSettings webSettings=null;
	private WebView webView=null;
	private MyProgressBar mpb;
	
	private Intent it;

	@SuppressLint("SetJavaScriptEnabled") protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browser_act);
		setTitleBar(R.drawable.btn_titlebar_back,R.drawable.navigationbar_refresh);
		setTitle("加载中...");
		it=getIntent();
		
		webView=(WebView) super.findViewById(R.id.my_browser);
		webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setUseWideViewPort(false); 
        webSettings.setLoadWithOverviewMode(true); 
        webView.setWebViewClient(new MyWebViewClient()); 
        webView.setWebChromeClient(wvcc);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

		webView.loadUrl(it.getStringExtra("url"));
	}
	
	private WebChromeClient wvcc=new WebChromeClient(){
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			setTitle(title);
		}
		
	};
	
	private class MyWebViewClient extends WebViewClient {  
		@Override  
        public boolean shouldOverrideUrlLoading(WebView view, String url) {  
            //return super.shouldOverrideUrlLoading(view, url); 
			webView.loadUrl(url);
			return true;
        }  
  
        @Override  
        public void onPageFinished(WebView view, String url) {  
        	mpb.dismiss();
            super.onPageFinished(view, url);  
  
        }  
        @Override  
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        	mpb=new MyProgressBar(MyBrowser.this);
        	mpb.setMessage("正在加载中...");
            super.onPageStarted(view, url, favicon);  
        }  
  
        @Override  
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {  
            super.onReceivedError(view, errorCode, description, failingUrl);  
        }  
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			if(webView.canGoBack()){
				webView.goBack();
			}else{
				finish();
			}
			break;
		case 2:
			webView.reload();
			break;
		default:
			break;
		}
	}

}
