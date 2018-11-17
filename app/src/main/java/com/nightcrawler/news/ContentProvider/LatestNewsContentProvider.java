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

import com.nightcrawler.news.Database.LatestNewsContract;
import com.nightcrawler.news.Database.LatestNewsDbHelper;

import java.util.Objects;

import static com.nightcrawler.news.Database.LatestNewsContract.LatestNewsContractEntry.TABLE_NAME;

public class LatestNewsContentProvider extends ContentProvider {

    public static final int TASKS = 200;
    public static final int TASK_WITH_ID = 201;
    private com.nightcrawler.news.Database.LatestNewsDbHelper LatestNewsDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(LatestNewsContract.AUTHORITY, LatestNewsContract.PATH_TASKS, TASKS);
        uriMatcher.addURI(LatestNewsContract.AUTHORITY, LatestNewsContract.PATH_TASKS + "/#", TASK_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        LatestNewsDbHelper = new LatestNewsDbHelper(context);
        return true;
    }


    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = LatestNewsDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TASKS:
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(LatestNewsContract.LatestNewsContractEntry.CONTENT_URI, id);
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

        final SQLiteDatabase db = LatestNewsDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case TASKS:
                retCursor = db.query(TABLE_NAME, projection,
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


        final SQLiteDatabase db = LatestNewsDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int tasksDeleted; // starts as 0

        switch (match) {
            case TASK_WITH_ID:
//                String url = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(TABLE_NAME, "url=?", selectionArgs);
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
