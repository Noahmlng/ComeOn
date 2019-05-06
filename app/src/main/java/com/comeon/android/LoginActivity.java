package com.comeon.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.comeon.android.util.Activity_Parent;
import com.comeon.android.util.ViewUtil;

public class LoginActivity extends Activity_Parent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //隐藏当前活动的状态栏（全屏模式）
        ViewUtil.transparentBar(this);
    }
}
