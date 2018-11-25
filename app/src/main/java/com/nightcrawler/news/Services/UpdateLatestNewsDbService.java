package com.nightcrawler.news.Services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nightcrawler.news.DataObjects.Article;
import com.nightcrawler.news.Database.NewsContract;
import com.nightcrawler.news.R;
import com.nightcrawler.news.Utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpdateLatestNewsDbService extends IntentService {


    public UpdateLatestNewsDbService(String name) {
        super(name);
    }
    public UpdateLatestNewsDbService( ) {
        super("");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences sharedPreferences = getSharedPreferences("country", 0);
        String country = sharedPreferences.getString("country", "us");
        Resources res = getResources();


        String URL = res.getString(R.string.request_LatestNews);
        URL = URL + country + "&apiKey=a631133308204b1ba583dc2ed43486b5";


        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().create();
                List<Article> articleList = new ArrayList<>();
                JSONObject responseJson = null;
                try {
                    responseJson = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray articlesList = responseJson.optJSONArray("articles");
                if (articlesList == null) {
                    return;
                }

                for (int i = 0; i < articlesList.length(); i++) {
                    Article article = gson.fromJson(articlesList.optJSONObject(i).toString(), Article.class);
                    articleList.add(article);
                }
                if (articleList.size() > 0) {
                    for (int i = 0; i < articleList.size(); i++) {
                        Utility.insertLatestNewsDb(articleList.get(i),getApplicationContext());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TEST", "FAILURE");
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void insertLatestNewsDb(Article article) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(NewsContract.NewsContractEntry.urlToImage, article.getUrlToImage());
        contentValues.put(NewsContract.NewsContractEntry.author, article.getAuthor());
        contentValues.put(NewsContract.NewsContractEntry.url, article.getUrl());
        contentValues.put(NewsContract.NewsContractEntry.title, article.getTitle());
        contentValues.put(NewsContract.NewsContractEntry.publishedAt, article.getPublishedAt());

        getContentResolver().insert(NewsContract
                .NewsContractEntry.CONTENT_URI2, contentValues);

    }
}