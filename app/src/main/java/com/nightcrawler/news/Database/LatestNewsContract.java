package com.nightcrawler.news.Database;

import android.net.Uri;
import android.provider.BaseColumns;

public class LatestNewsContract {

    public static final String AUTHORITY = "com.nightcrawler.news";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASKS = "LatestNews";


    public static final class LatestNewsContractEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();


        public static final String TABLE_NAME = "latestNews";
        public static final String publishedAt = "publishedAt";
        public static final String url = "url";
        public static final String title = "title";
        public static final String author = "author";
        public static final String urlToImage = "urlToImage";
        public static final String NEWS_TIMESTAMP = "timestamp";


    }
}