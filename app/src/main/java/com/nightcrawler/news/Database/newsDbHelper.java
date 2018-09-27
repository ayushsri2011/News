package com.nightcrawler.news.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class newsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favNews.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public newsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold waitlist data
        final String SQL_CREATE_MOVIELIST_TABLE = "CREATE TABLE "
              + newsContract.newsContractEntry.TABLE_NAME + " (" +
                newsContract.newsContractEntry.publishedAt + " TEXT , " +
                newsContract.newsContractEntry.url + " TEXT  , " +
                newsContract.newsContractEntry.title + " TEXT , " +
                newsContract.newsContractEntry.author + " TEXT , " +
                newsContract.newsContractEntry.urlToImage  + " TEXT , "+
                newsContract.newsContractEntry.MOVIE_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                +"); ";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIELIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + newsContract.newsContractEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}