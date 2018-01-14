package com.groupproject.cs3.vca;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webViewActivity extends AppCompatActivity {

    WebView webView;
    static WebView sWebView;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        String url = getIntent().getExtras().getString("url");
        final ProgressDialog progDailog = ProgressDialog.show(webViewActivity.this, "Loading","Please wait...", true);
                progDailog.setCancelable(false);
                final WebView myWebView = (WebView) findViewById(R.id.webview);
                myWebView.getSettings().setJavaScriptEnabled(true);
                myWebView.getSettings().setLoadWithOverviewMode(true);
                myWebView.getSettings().setUseWideViewPort(true);
                myWebView.setWebViewClient(new WebViewClient(){

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        progDailog.show();
                        view.loadUrl(url);

                        return true;
                    }
                    @Override
                    public void onPageFinished(WebView view, final String url) {
                        progDailog.dismiss();
                    }
                });

                myWebView.loadUrl(url);

    }



}
