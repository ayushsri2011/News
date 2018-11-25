package com.nightcrawler.news.Database;

import android.net.Uri;
import android.provider.BaseColumns;

public class NewsContract {

    public static final String AUTHORITY = "com.nightcrawler.news";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASKS = "News";


    public static final class NewsContractEntry implements BaseColumns {

        public static final Uri CONTENT_URI1 =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).appendPath("FavNews").build();

        public static final Uri CONTENT_URI2 =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).appendPath("LatestNews").build();


        //    content://com.nightcrawler.news/News

        public static final String TABLE_NAME1 = "favNews";
        public static final String TABLE_NAME2 = "latestNews";
        public static final String publishedAt = "publishedAt";
        public static final String url = "url";
        public static final String title = "title";
        public static final String author = "author";
        public static final String urlToImage = "urlToImage";
        public static final String NEWS_TIMESTAMP = "timestamp";
    }
}
