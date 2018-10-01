package com.nightcrawler.news.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
}
