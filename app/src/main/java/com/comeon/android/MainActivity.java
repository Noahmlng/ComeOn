package com.comeon.android;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.comeon.android.controls.TabEntity;
import com.comeon.android.db.UserInfo;
import com.comeon.android.fragment.FriendsFragment;
import com.comeon.android.fragment.GroupFragment;
import com.comeon.android.fragment.LaunchOrderFragment;
import com.comeon.android.fragment.MapAppointmentFragment;
import com.comeon.android.fragment.MineFragment;
import com.comeon.android.util.Activity_Parent;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.Utilities;
import com.comeon.android.util.ViewUtil;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.flyco.tablayout.widget.MsgView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity_Parent implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    public FragmentManager fragmentManager = this.getSupportFragmentManager();

    MapAppointmentFragment mapAppointmentFragment = new MapAppointmentFragment();
    GroupFragment groupFragment = new GroupFragment();
    LaunchOrderFragment launchOrderFragment=new LaunchOrderFragment();
    MineFragment mineFragment=new MineFragment();

    public UserInfo getLoginUser(){
        return loginUser;
    }

    CommonTabLayout tlCommen;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String[] mTitles = {"约球", "组团","发布组团", "朋友", "我的"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginUser= getIntent().getParcelableExtra("login_user");
        if(loginUser!=null){
            LogUtil.d(TAG, "当前登录的用户id为："+loginUser.getId());
        }

        setContentView(R.layout.activity_main);
        ViewUtil.setStatusBarColor(this, Color.rgb(255, 255, 255), false);
        requestPermission();
        initControls();
        //注册碎片返回栈监听器
        fragmentManager.addOnBackStackChangedListener(new fragmentBackStackListener());
        Utilities.replaceFragment(fragmentManager, mapAppointmentFragment, R.id.fragment_layout);
    }

    /**
     * 申请权限
     */
    private void requestPermission() {
        List<String> permissionList = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }

        //如果还有需要申请的权限，则开始发起申请
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    /**
     * 处理返回的权限结果
     * @param requestCode  请求码
     * @param permissions  权限列表
     * @param grantResults 申请权限的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (permissions.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            //当用户拒绝授予权限时的操作
                        }
                    }
                }
                break;
        }
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
                        Utilities.replaceFragment(fragmentManager, mapAppointmentFragment, R.id.fragment_layout);
                        break;
                    case 1:
                        Utilities.replaceFragment(fragmentManager, groupFragment, R.id.fragment_layout);
                        break;
                    case 2:
                        Utilities.replaceFragment(fragmentManager, launchOrderFragment, R.id.fragment_layout);
                        break;
                    case 3:
                        break;
                    case 4:
                        Utilities.replaceFragment(fragmentManager, mineFragment, R.id.fragment_layout);
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
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
                mFragments.add(launchOrderFragment);
            }else if("朋友".equals(mTitle)){
                mTabEntities.add(new TabEntity(mTitle, R.drawable.nav_friends_checked, R.drawable.nav_btn_friends));
                mFragments.add(new FriendsFragment());
            }else if("我的".equals(mTitle)){
                mTabEntities.add(new TabEntity(mTitle, R.drawable.nav_btn_ming_checked, R.drawable.nav_btn_mine));
                mFragments.add(mineFragment);
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
     * 处理界面中的点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    /**
     * 返回键返回桌面
     */
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
