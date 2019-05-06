package com.comeon.android;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.comeon.android.fragment.GroupFragment;
import com.comeon.android.fragment.MapAppointmentFragment;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.ViewUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    ImageButton btn_open_map_appointment;
    ImageButton btn_open_group;
    ImageButton btn_launch_appointment;
    ImageButton btn_open_friends;
    ImageButton btn_open_mine;

    public FragmentManager fragmentManager=this.getSupportFragmentManager();

    MapAppointmentFragment mapAppointmentFragment;
    GroupFragment groupFragment;

    private Fragment current_Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtil.setStatusBarColor(this, Color.rgb(255,255,255),false);
        initControls();
        //注册碎片返回栈监听器
        fragmentManager.addOnBackStackChangedListener(new fragmentBackStackListener());
        replaceFragment(mapAppointmentFragment);
    }

    /**
     * 初始化控件
     */
    private void initControls() {
        btn_open_map_appointment = (ImageButton) findViewById(R.id.open_map_appointment_activity);
        btn_open_group = (ImageButton) findViewById(R.id.open_group_activity);
        btn_launch_appointment = (ImageButton) findViewById(R.id.open_launch_appointment_activity);
        btn_open_friends = (ImageButton) findViewById(R.id.open_friends_activity);
        btn_open_mine = (ImageButton) findViewById(R.id.open_mine_activity);
        mapAppointmentFragment=new MapAppointmentFragment();
        groupFragment=new GroupFragment();

        btn_open_map_appointment.setOnClickListener(this);
        btn_open_group.setOnClickListener(this);
        btn_launch_appointment.setOnClickListener(this);
        btn_open_friends.setOnClickListener(this);
        btn_open_mine.setOnClickListener(this);
    }

    /**
     * 动态替换碎片的方法
     * @param fragment
     */
    private void replaceFragment(Fragment fragment){
        //1、创建一个碎片的事务
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        //2、替换碎片的操作
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        //3、如果需要模拟返回栈的模式（替换的操作相当于让新的碎片处于栈顶，返回键就是出栈）
        fragmentTransaction.addToBackStack(null);
        //4、提交事务
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.open_map_appointment_activity:
                replaceFragment(mapAppointmentFragment);
                current_Fragment=mapAppointmentFragment;
                break;
            case R.id.open_group_activity:
                replaceFragment(groupFragment);
                current_Fragment=groupFragment;
                break;
            case R.id.open_launch_appointment_activity:
                break;
            case R.id.open_friends_activity:
                break;
            case R.id.open_mine_activity:
                break;
        }
    }


    /**
     * 内部类监听自定义碎片返回栈
     */
    public class fragmentBackStackListener implements FragmentManager.OnBackStackChangedListener{
        @Override
        public void onBackStackChanged() {
            /*
            当返回栈中没有碎片时，则关闭活动
             */
            if(fragmentManager.getBackStackEntryCount()==0){
                finish();
            }
        }
    }
}
