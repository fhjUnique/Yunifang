package com.bwie.hhww.yunifang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bwie.hhww.yunifang.R;

/**
 * Created by dell on 2017/3/16.
 */

public class CarouselDetails extends AppCompatActivity {

    private WebView carousel_web;
    private ProgressBar pb;
    private ImageView image_webView;
    private Button bt_back;
    private TextView text_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carousel_activity);
        initView();

        Intent it = getIntent();
        String url = it.getStringExtra("url");
        String title = it.getStringExtra("title");
        if(title == null){
            text_title.setText("御泥坊");
        }else{
            text_title.setText(title);
        }
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pb.setMax(100);
        carousel_web.getSettings().setJavaScriptEnabled(true);
        carousel_web.getSettings().setSupportZoom(true);
        carousel_web.getSettings().setBuiltInZoomControls(true);
        carousel_web.setWebChromeClient(new WebViewClient());
        carousel_web.loadUrl(url);
    }



    private class WebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            pb.setProgress(newProgress);
            if (newProgress == 100) {
                pb.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
    public void initView(){
        carousel_web = (WebView) findViewById(R.id.carousel_web);
        bt_back = (Button) findViewById(R.id.bt_back);
        text_title = (TextView) findViewById(R.id.text_title);
        pb = (ProgressBar) findViewById(R.id.pb);
    }
}