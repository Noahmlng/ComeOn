package com.comeon.android.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.comeon.android.db.UserInfo;

/**
 * 全部的活动继承自AllActivities类：统一设置
 */
public class Activity_Parent extends AppCompatActivity {

    protected static UserInfo loginUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
