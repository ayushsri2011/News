    package com.nightcrawler.news.ContentProvider;

    import android.content.ContentProvider;
    import android.content.ContentUris;
    import android.content.ContentValues;
    import android.content.Context;
    import android.content.UriMatcher;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.net.Uri;
    import android.support.annotation.NonNull;
    import android.support.annotation.Nullable;

    import com.nightcrawler.news.Database.NewsContract;
    import com.nightcrawler.news.Database.NewsDbHelper;

    import java.util.Objects;

    public class NewsContentProvider extends ContentProvider {

        public static final int TASKS_FavTable = 100;
        public static final int TASK_WITH_ID_FavTable = 101;
        public static final int TASKS_LatestTable = 200;
        public static final int TASK_WITH_ID_LatestTable = 201;

        public static final String TABLE_NAME_FavTable = "FavNews";
        public static final String TABLE_NAME_LatestTable = "LatestNews";

        private NewsDbHelper newsDbHelper;

        private static final UriMatcher sUriMatcher = buildUriMatcher();

        public static UriMatcher buildUriMatcher() {

            // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
            UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
            content://com.nightcrawler.news/News/LatestNews
            uriMatcher.addURI(NewsContract.AUTHORITY, NewsContract.PATH_TASKS+ "/FavNews", TASKS_FavTable);
            uriMatcher.addURI(NewsContract.AUTHORITY, NewsContract.PATH_TASKS + "/#", TASK_WITH_ID_FavTable);

            uriMatcher.addURI(NewsContract.AUTHORITY, NewsContract.PATH_TASKS+ "/LatestNews", TASKS_LatestTable);
            uriMatcher.addURI(NewsContract.AUTHORITY, NewsContract.PATH_TASKS + "/#", TASK_WITH_ID_LatestTable);


            return uriMatcher;
        }

        @Override
        public boolean onCreate() {

            Context context = getContext();
            newsDbHelper = new NewsDbHelper(context);
            return true;
        }


        public Uri insert(@NonNull Uri uri, ContentValues values) {

            final SQLiteDatabase Newsdb = newsDbHelper.getWritableDatabase();

            int match = sUriMatcher.match(uri);
            Uri returnUri;

            switch (match) {
                case TASKS_FavTable:
                    long id1 = Newsdb.insert(TABLE_NAME_FavTable, null, values);
                    if (id1 > 0) {
                        returnUri = ContentUris.withAppendedId(NewsContract.NewsContractEntry.CONTENT_URI1, id1);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                    break;
                case TASKS_LatestTable:
                    long id2 = Newsdb.insert(TABLE_NAME_LatestTable, null, values);
                    if (id2 > 0) {
                        returnUri = ContentUris.withAppendedId(NewsContract.NewsContractEntry.CONTENT_URI2, id2);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);

            return returnUri;
        }

        @Nullable
        @Override
        public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

            final SQLiteDatabase Newsdb = newsDbHelper.getReadableDatabase();
            int match = sUriMatcher.match(uri);
            Cursor retCursor;

            switch (match) {
                case TASKS_FavTable:
                    retCursor = Newsdb.query(TABLE_NAME_FavTable, projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);
                    break;
                case TASKS_LatestTable:
                    retCursor = Newsdb.query(TABLE_NAME_LatestTable, projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            retCursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);

            return retCursor;
        }

        @Nullable
        @Override
        public String getType(@NonNull Uri uri) {
            return null;
        }

        @Override
        public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

            final SQLiteDatabase Newsdb = newsDbHelper.getWritableDatabase();
            int match = sUriMatcher.match(uri);
            int tasksDeleted; // starts as 0

            switch (match) {
                case TASK_WITH_ID_FavTable:
    //                String url = uri.getPathSegments().get(1);
                    tasksDeleted = Newsdb.delete(TABLE_NAME_FavTable, "url=?", selectionArgs);
                    break;
                case TASK_WITH_ID_LatestTable:
    //                String url = uri.getPathSegments().get(1);
                    tasksDeleted = Newsdb.delete(TABLE_NAME_LatestTable, "url=?", selectionArgs);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            if (tasksDeleted != 0)
                Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);

            return tasksDeleted;

        }

        @Override
        public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
            return 0;
        }

    }
