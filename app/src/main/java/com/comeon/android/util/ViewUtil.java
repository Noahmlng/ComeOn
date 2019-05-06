package com.comeon.android.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 界面工具类
 */
public class ViewUtil {

    /**
     * 设置状态栏透明，并启用全屏模式
     * @param activity
     */
    @TargetApi(19)
    public static void transparentBar(Activity activity){
        Window window=activity.getWindow();
        //因为4.4版本到5.0版本这个区间以及5.0版本以上两个区间支持，但处理方式不同，所以进行判断
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
