package com.comeon.android;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.comeon.android.controls.TabEntity;
import com.comeon.android.fragment.FriendsFragment;
import com.comeon.android.fragment.GroupFragment;
import com.comeon.android.fragment.MapAppointmentFragment;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.ViewUtil;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.flyco.tablayout.widget.MsgView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    public FragmentManager fragmentManager = this.getSupportFragmentManager();

    MapAppointmentFragment mapAppointmentFragment = new MapAppointmentFragment();
    GroupFragment groupFragment = new GroupFragment();
    private Fragment current_Fragment;

    CommonTabLayout tlCommen;
    CoordinatorLayout flContent;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String[] mTitles = {"约球", "组团","发布组团", "朋友", "我的"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtil.setStatusBarColor(this, Color.rgb(255, 255, 255), false);
        initControls();
        //注册碎片返回栈监听器
        fragmentManager.addOnBackStackChangedListener(new fragmentBackStackListener());
        replaceFragment(mapAppointmentFragment);
    }

    /**
     * 初始化控件
     */
    private void initControls() {
        tlCommen=(CommonTabLayout)findViewById(R.id.tl_commen);
        //加载Tab
        initTab();
        /*
            监听tab选项，切换到相应的碎片
         */
        tlCommen.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position){
                    case 0:
                        replaceFragment(mapAppointmentFragment);
                        break;
                    case 1:
                        replaceFragment(groupFragment);
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {
                switch (position){
                    case 0:
                        replaceFragment(mapAppointmentFragment);
                        break;
                    case 1:
                        replaceFragment(groupFragment);
                        break;
                    case 2:
                        break;
                }
            }
        });
        flContent=(CoordinatorLayout)findViewById(R.id.fragment_layout);
    }

    /**
     * 加载Tab选项
     */
    private void initTab() {
        for (String mTitle : mTitles) {
            if("约球".equals(mTitle)){
                //后面两个值是选中图标和未选中(R.drawable.xxx)不要图标就填0
                mTabEntities.add(new TabEntity(mTitle, R.drawable.nav_btn_appoint_checked, R.drawable.nav_btn_appoint));
                mFragments.add(mapAppointmentFragment);
            }else if("组团".equals(mTitle)){
                mTabEntities.add(new TabEntity(mTitle, R.drawable.nav_btn_group_checked, R.drawable.nav_btn_group));
                mFragments.add(groupFragment);
            }else if("发布组团".equals(mTitle)){
                mTabEntities.add(new TabEntity(mTitle, R.drawable.nav_btn_launch_appointment, R.drawable.nav_btn_launch_appointment));
                mFragments.add(new FriendsFragment());
            }else if("朋友".equals(mTitle)){
                mTabEntities.add(new TabEntity(mTitle, R.drawable.nav_btn_friends, R.drawable.nav_btn_friends));
                mFragments.add(new FriendsFragment());
            }else if("我的".equals(mTitle)){
                mTabEntities.add(new TabEntity(mTitle, R.drawable.nav_btn_mine, R.drawable.nav_btn_mine));
                mFragments.add(new FriendsFragment());
            }
        }
        tlCommen.setTabData(mTabEntities, this, R.id.fragment_layout, mFragments);
        // 未读消息2位数
//        tlCommen.showMsg(0,32);
//        tlCommen.setMsgMargin(0, -5, 5);

        // 未读消息3位数
//        tlCommen.showMsg(1, 100);
//        tlCommen.setMsgMargin(1, -5, 5);

        // 设置未读消息红点
//        tlCommen.showDot(2);
//        MsgView rtv_2 = tlCommen.getMsgView(2);
//        if (rtv_2 != null) {
//            UnreadMsgUtils.setSize(rtv_2, 7);
//        }

        // 设置未读消息背景
//        tlCommen.showMsg(3, 5);
//        tlCommen.setMsgMargin(3, 0, 5);
//        MsgView rtv_3 = tlCommen.getMsgView(3);
//        if (rtv_3 != null) {
//            rtv_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
//        }

        //隐藏指定位置未读红点或消息
//        tlCommen.hideMsg(2);
    }

    /**
     * 动态替换碎片的方法
     *
     * @param fragment
     */
    private void replaceFragment(Fragment fragment) {
        //1、创建一个碎片的事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //2、替换碎片的操作
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        //3、如果需要模拟返回栈的模式（替换的操作相当于让新的碎片处于栈顶，返回键就是出栈）
        fragmentTransaction.addToBackStack(null);
        //4、提交事务
        fragmentTransaction.commit();

        current_Fragment = fragment;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onBackPressed() {
        Intent home=new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
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
