package com.comeon.android.util;

import android.util.Log;

/**
 * 日志信息打印类
 * 利用LogUtil修改level的方法实现对日志信息打印的控制（开发时为VERBOSE，上线时使用NOTHING）
 */
public class LogUtil {

    public static final int VERBOSE=1;
    public static final int DEBUG=2;
    public static final int INFO=3;
    public static final int WARN=4;
    public static final int ERROR=5;
    public static final int NOTING=6;

    public static int level=VERBOSE;

    public static void v(String tag, String msg){
        if(level<=VERBOSE){
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg){
        if(level<=DEBUG){
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg){
        if(level<=INFO){
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg){
        if(level<=WARN){
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg){
        if(level<=ERROR){
            Log.e(tag, msg);
        }
    }
}