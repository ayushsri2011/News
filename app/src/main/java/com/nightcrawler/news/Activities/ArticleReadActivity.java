package com.nightcrawler.news.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nightcrawler.news.R;

import com.nightcrawler.news.DataObjects.Article;

import java.util.Objects;

public class ArticleReadActivity extends AppCompatActivity {
    WebView article_webView;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_read);

        Intent intent = getIntent();
        String url=intent.getStringExtra("url");
        String author=intent.getStringExtra("author");


        article_webView=(WebView)findViewById(R.id.article_webView);
        pb=(ProgressBar)findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);


        article_webView.loadUrl(url);
        article_webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                pb.setVisibility(View.INVISIBLE);
            }
        });

        WebSettings webSettings = article_webView.getSettings();






//        webSettings.supportZoom();
//        webSettings.getBuiltInZoomControls();
//        webSettings.setJavaScriptEnabled(true);

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
