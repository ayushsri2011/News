package com.nightcrawler.news.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.nightcrawler.news.R;

import com.nightcrawler.news.DataObjects.Article;

import java.util.Objects;

public class ArticleReadActivity extends AppCompatActivity {
    WebView article_webView;
    TextView article_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_read);

        Intent intent = getIntent();
        String url=intent.getStringExtra("url");
//        Bundle args = intent.getExtras();
//        assert args != null;
//        Article article=args.getParcelable("ARTICLE");
//        Log.d("TEST2",""+article.getUrl());
        article_webView=(WebView)findViewById(R.id.article_webView);
        article_title=(TextView)findViewById(R.id.article_title);
        article_title.setText("TEST");
        article_webView.loadUrl(url);
        WebSettings webSettings = article_webView.getSettings();
        webSettings.supportZoom();
//        webSettings.getBuiltInZoomControls();
        webSettings.setJavaScriptEnabled(true);

//        String newUserAgent = article_webView.getSettings().getUserAgentString();
//        String ua = article_webView.getSettings().getUserAgentString();
//        String androidOSString = article_webView.getSettings().getUserAgentString().substring(ua.indexOf("("), ua.indexOf(")") + 1);
//        newUserAgent = article_webView.getSettings().getUserAgentString().replace(androidOSString, "(X11; Linux x86_64)");
//        article_webView.getSettings().setUserAgentString(newUserAgent);
//        article_webView.getSettings().setUseWideViewPort(true);
//        article_webView.getSettings().setLoadWithOverviewMode(true);
//        article_webView.reload();





//        Objects.requireNonNull(article).getUrl()

    }
}
