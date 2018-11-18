package com.nightcrawler.news.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "News.db";
    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold waitlist data
        final String SQL_CREATE_TABLE_FavNews = "CREATE TABLE "
                + NewsContract.NewsContractEntry.TABLE_NAME1 + " (" +
                NewsContract.NewsContractEntry.publishedAt + " TEXT , " +
                NewsContract.NewsContractEntry.url + " TEXT  , " +
                NewsContract.NewsContractEntry.title + " TEXT , " +
                NewsContract.NewsContractEntry.author + " TEXT , " +
                NewsContract.NewsContractEntry.urlToImage + " TEXT , " +
                NewsContract.NewsContractEntry.NEWS_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + "); ";

        final String SQL_CREATE_TABLE_LatestNews = "CREATE TABLE "
                + NewsContract.NewsContractEntry.TABLE_NAME2 + " (" +
                NewsContract.NewsContractEntry.publishedAt + " TEXT , " +
                NewsContract.NewsContractEntry.url + " TEXT  , " +
                NewsContract.NewsContractEntry.title + " TEXT , " +
                NewsContract.NewsContractEntry.author + " TEXT , " +
                NewsContract.NewsContractEntry.urlToImage + " TEXT , " +
                NewsContract.NewsContractEntry.NEWS_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_FavNews);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_LatestNews);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.NewsContractEntry.TABLE_NAME1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.NewsContractEntry.TABLE_NAME2);
        onCreate(sqLiteDatabase);
    }
}