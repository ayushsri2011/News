package com.nightcrawler.news.Activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nightcrawler.news.Database.NewsContract;
import com.nightcrawler.news.R;

import java.util.Objects;

public class ArticleReadActivity extends AppCompatActivity {
    WebView article_webView;
    ProgressBar pb;
    ImageButton share;
    ImageButton bookmarkArticle;
    String url, urlToImage, publishedAt, title, author;
    boolean fav = false;
    private AdView mAdView;
    InterstitialAd mInterstitialAd;
    public static final String TAG = MainActivity.class
            .getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_read);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
//        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(this);
        googleAnalytics.setLocalDispatchPeriod(3000);

        final Tracker tracker = googleAnalytics.newTracker("UA-129102573-1");
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
        tracker.setScreenName("ArticleReadActivity");
        Log.i(TAG, "Setting screen name: " + "");
        tracker.setScreenName("ArticleReadActivity");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_test));

        final AdRequest adRequest = new AdRequest.Builder().addTestDevice(getString(R.string.admob_test_device_id))
                .build();

        // Load ads into Interstitial Ads
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mInterstitialAd.loadAd(adRequest);
            }
        }, 20000);


        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });

        mAdView = findViewById(R.id.adView);
        createAd();

        share = findViewById(R.id.share);
        bookmarkArticle = findViewById(R.id.bookmarkArticle);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        urlToImage = intent.getStringExtra("urlToImage");
        publishedAt = intent.getStringExtra("publishedAt");
        title = intent.getStringExtra("title");
        author = intent.getStringExtra("author");

        article_webView = findViewById(R.id.article_webView);
        pb = findViewById(R.id.pb);
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
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("ButtonClick")
                        .setAction("ShareArticle")
                        .setLabel("Share Article Link")
                        .build());

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Shared using NewsApp  " + url);
                startActivity(Intent.createChooser(shareIntent, "Share link using"));
            }
        });

        bookmarkArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("ButtonClick")
                        .setAction("Bookmark")
                        .setLabel("Bookmark/Unbookmark an article")
                        .build());

                Uri uri = NewsContract.NewsContractEntry.CONTENT_URI1;
                String[] selectionArgs = {url};

                Cursor mCount = getContentResolver().query(uri, null, "url=?", selectionArgs, null, null);
//                Toast.makeText(getBaseContext(), "mcount=" + mCount.getCount(), Toast.LENGTH_SHORT).show();
                if (!fav) {
                    insertFavDb();
                    bookmarkArticle.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    fav = true;
                } else {
                    bookmarkArticle.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    fav = false;
                    getContentResolver().delete(NewsContract.NewsContractEntry.CONTENT_URI1.buildUpon().appendPath("101").build(), "url=?", selectionArgs);
                    Toast.makeText(getBaseContext(), "Removed from favourites", Toast.LENGTH_SHORT).show();
                }
                mCount.close();

            }
        });


        Uri uri = NewsContract.NewsContractEntry.CONTENT_URI1;
        String[] selectionArgs = {url};
        Cursor mCount = getContentResolver().query(uri, null, "url=?", selectionArgs, null, null);

        if (Objects.requireNonNull(mCount).getCount() == 1) {
            bookmarkArticle.setImageResource(R.drawable.ic_bookmark_black_24dp);
            fav = true;
        } else {
            bookmarkArticle.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
            fav = false;
        }

    }

    @Override
    public void onBackPressed() {

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
            animation.startNow();
            finish();
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void createAd() {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(getString(R.string.admob_test_device_id)).build();
//        .addTestDevice("TEST_DEVICE_ID")
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    public void insertFavDb() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsContract.NewsContractEntry.urlToImage, urlToImage);
        contentValues.put(NewsContract.NewsContractEntry.author, author);
        contentValues.put(NewsContract.NewsContractEntry.url, url);
        contentValues.put(NewsContract.NewsContractEntry.title, title);
        contentValues.put(NewsContract.NewsContractEntry.publishedAt, publishedAt);

        getContentResolver().insert(NewsContract.NewsContractEntry.CONTENT_URI1, contentValues);

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
