package com.nightcrawler.news.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nightcrawler.news.DataObjects.Article;
import com.nightcrawler.news.Database.NewsContract;

public class Utility {


    public static boolean checkConnectivity(Context context){
        try{
            ConnectivityManager conMgr =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            return netInfo != null;
        }catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static void insertLatestNewsDb(Article article,Context context) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(NewsContract.NewsContractEntry.urlToImage, article.getUrlToImage());
        contentValues.put(NewsContract.NewsContractEntry.author, article.getAuthor());
        contentValues.put(NewsContract.NewsContractEntry.url, article.getUrl());
        contentValues.put(NewsContract.NewsContractEntry.title, article.getTitle());
        contentValues.put(NewsContract.NewsContractEntry.publishedAt, article.getPublishedAt());


        String[] args={article.getUrl()};
        Cursor cursor=context.getContentResolver().query(NewsContract
                .NewsContractEntry.CONTENT_URI2,null,"url=?",args,"timestamp desc");
        if(cursor.getCount()==0)
        {
            context.getContentResolver().insert(NewsContract
                    .NewsContractEntry.CONTENT_URI2, contentValues);
        }


    }
}
