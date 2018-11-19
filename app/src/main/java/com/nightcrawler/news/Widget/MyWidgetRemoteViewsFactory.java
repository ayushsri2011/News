package com.nightcrawler.news.Widget;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.nightcrawler.news.Database.NewsContract;
import com.nightcrawler.news.R;

import java.util.ArrayList;
import java.util.List;


public class MyWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<String> widgetList;

    public MyWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
        updateWidgetListView();
    }

    private void updateWidgetListView() {

        widgetList = new ArrayList<>();
        widgetList.add(" ");
        try {
            Cursor cursor = mContext.getContentResolver().query(NewsContract.NewsContractEntry.CONTENT_URI2, null, null,
                    null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() == 0) {
                widgetList.add("Cursor is not null but empty");
            } else {
                cursor.moveToFirst();
                    widgetList.add("News");
                for (int i = 0; i < cursor.getCount(); i++) {
                    widgetList.add(cursor.getString(3));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        else
                widgetList.add("Cursor is null");
        } catch (Exception e) {
            Log.v("Failed to fetch values", "Fail");
            e.printStackTrace();
        }


    }

    @Override
    public void onDataSetChanged() {

        updateWidgetListView();
    }

    @Override
    public void onDestroy() {
        widgetList.clear();
    }

    @Override
    public int getCount() {
        return widgetList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget_list_item);
        rv.setTextViewText(R.id.widgetItemTaskNameLabel, widgetList.get(position));

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("Extra", widgetList.get(1));
        rv.setOnClickFillInIntent(R.id.widgetItemContainer, fillInIntent);


        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}