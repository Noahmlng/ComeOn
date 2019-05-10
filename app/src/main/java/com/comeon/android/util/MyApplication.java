package com.comeon.android.util;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

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

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(context);
    }

    public static Context getContext(){
        return context;
    }
}
