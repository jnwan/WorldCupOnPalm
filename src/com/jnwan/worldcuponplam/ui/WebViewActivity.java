package com.jnwan.worldcuponplam.ui;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jnwan.worldcuponplam.R;



public class WebViewActivity extends Activity{

    private WebView myWebView;
    private ProgressBar progressBar;   
    @SuppressLint({ "SetJavaScriptEnabled" })
    protected void onCreate( Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.webview);
        getActionBar().hide();
        String string = getIntent().getExtras().getString("link");
        myWebView = (WebView)findViewById(R.id.webView);
        //progressBar = (ProgressBar)findViewById(R.id.web_bar);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setSupportZoom(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.setWebChromeClient(new MyWebChromeClient());  
        myWebView.loadUrl(string);
    }
    
    private class MyWebViewClient extends WebViewClient{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			WebViewActivity newInstance = new WebViewActivity();
			Intent intent = new Intent(WebViewActivity.this,newInstance.getClass());
			intent.putExtra("link", url);
			startActivity(intent);
			return true;
		} 	
    	
    }
    private class MyWebChromeClient extends WebChromeClient {  
        @Override  
        public void onProgressChanged(WebView view, int newProgress) {  
            progressBar.setProgress(newProgress);  
            if(newProgress==100){  
                progressBar.setVisibility(View.GONE);  
            }  
            super.onProgressChanged(view, newProgress);  
        }            
    } 
    
    protected void onDestroy() {
        super.onDestroy();
        myWebView.destroy();
    }
}