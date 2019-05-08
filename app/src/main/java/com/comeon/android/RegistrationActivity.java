package com.comeon.android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.comeon.android.fragment.SettingDistanceFragment;
import com.comeon.android.fragment.SettingHeadIconFragment;
import com.comeon.android.fragment.SettingPersonalInfoFragment;
import com.comeon.android.fragment.SettingPhoneFragment;
import com.comeon.android.util.Activity_Parent;
import com.comeon.android.util.Utilities;
import com.comeon.android.util.ViewUtil;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.internal.Util;

public class RegistrationActivity extends Activity_Parent {

    public FragmentManager fragmentManager = this.getSupportFragmentManager();

    SettingPersonalInfoFragment settingPersonalInfoFragment = new SettingPersonalInfoFragment();
    SettingPhoneFragment settingPhoneFragment = new SettingPhoneFragment();
    SettingHeadIconFragment settingHeadIconFragment = new SettingHeadIconFragment();
    SettingDistanceFragment settingDistanceFragment=new SettingDistanceFragment();

    Button btn_next_step;
    TextView txt_tip;

    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //修改状态栏颜色和字体颜色：浅色模式
        ViewUtil.setStatusBarColor(this, Color.rgb(255, 255, 255), false);
        initControls();
        //注册碎片返回栈监听器
        fragmentManager.addOnBackStackChangedListener(new RegistrationActivity.fragmentBackStackListener());
        Utilities.replaceFragment(fragmentManager,settingPhoneFragment,R.id.fragment_layout);
    }

    /**
     * 初始化控件
     */
    private void initControls() {
        btn_next_step = (Button) findViewById(R.id.btn_next_step);
        txt_tip = (TextView) findViewById(R.id.txt_tip);

        //为按钮绑定点击事件
        btn_next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                    切换到下一个碎片，如果是头像碎片了就跳转到MainActivity
                    改变按钮的文字
                    同时改变上方的文字
                 */
                if (currentFragment instanceof SettingPhoneFragment) {
                    txt_tip.setText("请设置个人信息");
                    btn_next_step.setText("下一步");
                    Utilities.replaceFragment(fragmentManager,settingPersonalInfoFragment,R.id.fragment_layout);
                } else if (currentFragment instanceof SettingPersonalInfoFragment) {
                    txt_tip.setText("请输入可接受的约球距离");
                    btn_next_step.setText("下一步");
                    Utilities.replaceFragment(fragmentManager,settingDistanceFragment,R.id.fragment_layout);
                } else if (currentFragment instanceof SettingDistanceFragment) {
                    txt_tip.setText("请设置头像");
                    btn_next_step.setText("开始吧！");
                    Utilities.replaceFragment(fragmentManager,settingHeadIconFragment,R.id.fragment_layout);
                } else if (currentFragment instanceof SettingHeadIconFragment) {
                    Intent intent=new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 返回键的操作事件
     */
    @Override
    public void onBackPressed() {
        if (currentFragment instanceof SettingPhoneFragment) {
            finish();
        } else if (currentFragment instanceof SettingPersonalInfoFragment) {
            txt_tip.setText("请输入手机号");
            btn_next_step.setText("下一步");
            Utilities.replaceFragment(fragmentManager,settingPhoneFragment,R.id.fragment_layout);
        } else if (currentFragment instanceof SettingDistanceFragment) {
            txt_tip.setText("请设置个人信息");
            btn_next_step.setText("下一步");
            Utilities.replaceFragment(fragmentManager,settingPersonalInfoFragment,R.id.fragment_layout);
        } else if (currentFragment instanceof SettingHeadIconFragment) {
            txt_tip.setText("请输入可接受的约球距离");
            btn_next_step.setText("下一步");
            Utilities.replaceFragment(fragmentManager,settingDistanceFragment,R.id.fragment_layout);
        }
    }

    /**
     * 内部类监听自定义碎片返回栈
     */
    public class fragmentBackStackListener implements FragmentManager.OnBackStackChangedListener {
        @Override
        public void onBackStackChanged() {
            /*
            当返回栈中没有碎片时，则关闭活动
             */
            if (fragmentManager.getBackStackEntryCount() == 0) {
                finish();
            }
        }
    }

}
