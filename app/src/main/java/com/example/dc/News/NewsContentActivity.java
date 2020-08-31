package com.example.dc.News;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.dc.R;

public class NewsContentActivity extends AppCompatActivity {
    String url;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);


        TextView news_title_content = findViewById(R.id.news_title_content);

        WebView news_content_content = findViewById(R.id.news_content_content);

        Intent intent = getIntent();

        if (intent!=null){
            url = intent.getStringExtra("url");
            title = intent.getStringExtra("title");
        }

        news_title_content.setText(title);
        news_content_content.setWebViewClient(new WebViewClient());
        news_content_content.getSettings().setJavaScriptEnabled(true);
        news_content_content.loadUrl(url);
    }
}
