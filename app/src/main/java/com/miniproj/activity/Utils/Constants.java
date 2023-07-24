package com.miniproj.activity.Utils;


import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.miniproj.activity.realm.Latilng;
import com.miniproj.activity.realm.LatilngClone;

import java.util.ArrayList;


public class Constants extends MultiDexApplication {

    public Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setContext(this);
    }

    public void setContext(Context con) {
        context = con;
    }

    public Context getContext() {
        return context;
    }


    public static Boolean broadcastResStatus = false;
    public static Boolean BroadcastFirstConst = false;
    public static Boolean StartScrVisibility = false;
    public static int bg_stop= 0;
    public static ArrayList<Latilng> latilngsStatic = new ArrayList<>();




}