package com.nightcrawler.news.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.nightcrawler.news.R;

import com.nightcrawler.news.DataObjects.Article;

import java.util.Objects;

public class ArticleReadActivity extends AppCompatActivity {
    WebView article_webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_read);

        Intent intent = getIntent();
        Bundle args = intent.getExtras();
        assert args != null;
        Article article=args.getParcelable("ARTICLE");
        Log.d("TEST",""+article.getUrl());
        article_webView=(WebView)findViewById(R.id.article_webView);
        article_webView.loadUrl(Objects.requireNonNull(article).getUrl());

//        setContentView(article_webView);

//        TextView title_article=(TextView)findViewById(R.id.title_article);
//        title_article.setText(article.getTitle());

    }
}
