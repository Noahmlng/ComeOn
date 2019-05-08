package com.comeon.android.util;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * 获取全局Context的工具类
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
