package com.nightcrawler.news.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nightcrawler.news.Database.newsContract;
import com.nightcrawler.news.Database.newsDbHelper;
import com.nightcrawler.news.R;

import java.util.Objects;

public class ArticleReadActivity extends AppCompatActivity {
    WebView article_webView;    ProgressBar pb;
    ImageButton share;    ImageButton bookmarkArticle;
    String url, urlToImage, publishedAt, title, author;
    boolean fav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_read);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
//        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);




        share = (ImageButton) findViewById(R.id.share);
        bookmarkArticle = (ImageButton) findViewById(R.id.bookmarkArticle);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        urlToImage = intent.getStringExtra("urlToImage");
        publishedAt = intent.getStringExtra("publishedAt");
        title = intent.getStringExtra("title");
        author = intent.getStringExtra("author");

        article_webView = (WebView) findViewById(R.id.article_webView);
        pb = (ProgressBar) findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);

        article_webView.loadUrl(url);
        article_webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                pb.setVisibility(View.INVISIBLE);
            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Shared using NewsApp  " + url);
                startActivity(Intent.createChooser(shareIntent, "Share link using"));
            }
        });

        bookmarkArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = newsContract.newsContractEntry.CONTENT_URI;
                String[] selectionArgs = {url};

                Cursor mCount = getContentResolver().query(uri, null, "url=?", selectionArgs, null, null);
                Toast.makeText(getBaseContext(), "mcount="+mCount.getCount(), Toast.LENGTH_SHORT).show();
                if (!fav) {
                    insertFavDb();
                    bookmarkArticle.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    fav = true;
                } else {
                    bookmarkArticle.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    fav = false;
                    getContentResolver().delete(newsContract.newsContractEntry.CONTENT_URI.buildUpon().appendPath("101").build(), "url=?", selectionArgs);
                    Toast.makeText(getBaseContext(), "Removed from favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Uri uri = newsContract.newsContractEntry.CONTENT_URI;
        String[] selectionArgs = {url};
        Cursor mCount = getContentResolver().query(uri, null, "url=?", selectionArgs, null, null);

        if (Objects.requireNonNull(mCount).getCount()==1) {
            bookmarkArticle.setImageResource(R.drawable.ic_bookmark_black_24dp);
            fav = true;
        } else {
            bookmarkArticle.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
            fav = false;
        }

    }

    public void insertFavDb() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(newsContract.newsContractEntry.urlToImage, urlToImage);
        contentValues.put(newsContract.newsContractEntry.author, author);
        contentValues.put(newsContract.newsContractEntry.url, url);
        contentValues.put(newsContract.newsContractEntry.title, title);
        contentValues.put(newsContract.newsContractEntry.publishedAt, publishedAt);

        getContentResolver().insert(newsContract.newsContractEntry.CONTENT_URI, contentValues);

        Toast.makeText(this, "Added to Bookmarks", Toast.LENGTH_SHORT).show();
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
