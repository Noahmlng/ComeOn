package com.comeon.android.util;

import android.app.Application;
import android.content.Context;

/**
 * 获取全局Context的工具类
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
