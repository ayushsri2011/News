package com.nightcrawler.news.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;


public class MyWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyWidgetRemoteViewsFactory(getApplicationContext(), intent);
    }
}