package com.comeon.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.fragment.GroupDetailsFragment;
import com.comeon.android.fragment.StadiumDetailsFragment;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class InfoDisplayActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "InfoDisplayActivity";

    FragmentManager fragmentManager = getSupportFragmentManager();

    //控件
    ViewPager viewPager;
    ImageButton btn_goBack;
    ImageButton btn_share;
    ImageButton btn_favor;
    private int btn_favorClickCount=0;

    //设置适配器
    private MyPageAdapter mAdapter;
    private ArrayList<Fragment> mFragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_display);
        ViewUtil.setStatusBarColor(this, Color.rgb(117, 135, 183), true);
        //加载控件
        initControls();
        //加载内容
        loadInfo();
    }

    /**
     * 判断并加载信息
     */
    private void loadInfo() {
        //接收传入的InfoActivity的对象
        ArrayList<AppointmentOrder> orders = getIntent().getParcelableArrayListExtra("orderInfo");
        if (orders != null) {
            LogUtil.d(TAG, "传入的订单数：" + orders.size());
            mFragments = new ArrayList<Fragment>();
            for (int i = 0; i < orders.size(); i++) {
                mFragments.add(new GroupDetailsFragment(orders.get(i)));
            }
            mAdapter = new MyPageAdapter(fragmentManager);
        }

        //接收传入的InfoActivity的对象
        ArrayList<StadiumInfo> stadiums = getIntent().getParcelableArrayListExtra("stadiumInfo");
        if (stadiums != null) {
            LogUtil.d(TAG, "传入的场馆数：" + stadiums.size());
            mFragments = new ArrayList<Fragment>();
            for (int i = 0; i < stadiums.size(); i++) {
                mFragments.add(new StadiumDetailsFragment(stadiums.get(i)));
            }
            mAdapter = new MyPageAdapter(fragmentManager);
        }
        //设置ViewPager与适配器关联
        viewPager.setAdapter(mAdapter);
    }

    private void initControls() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        btn_goBack = (ImageButton) findViewById(R.id.btn_goback);
        btn_share = (ImageButton) findViewById(R.id.btn_share);
        btn_favor = (ImageButton) findViewById(R.id.btn_favor);

        //绑定点击事件
        btn_goBack.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        btn_favor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_goback:
                finish();
                Intent intent=new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_share:
                Toast.makeText(this, "暂不支持分享功能", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_favor:
                if (btn_favorClickCount==0){
                    Toast.makeText(this, "已加入收藏", Toast.LENGTH_SHORT).show();
                    btn_favorClickCount++;
                }else if(btn_favorClickCount==1){
                    btn_favor.setImageResource(R.mipmap.ic_favor);
                    Toast.makeText(this, "已取消收藏", Toast.LENGTH_SHORT).show();
                    btn_favorClickCount=0;
                }
                break;
        }
    }

    /**
     * 适配器帮助切换viewPager中的值
     */
    private class MyPageAdapter extends FragmentPagerAdapter {

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }


    /**
     * 查看组团信息的详情
     *
     * @param context
     * @param orders  要查看的组团信息组（第一个为点击进入的组团信息）
     */
    public static void checkOrderInfo(Context context, ArrayList<AppointmentOrder> orders) {
        Intent intent = new Intent(context, InfoDisplayActivity.class);
        intent.putParcelableArrayListExtra("orderInfo", orders);
        context.startActivity(intent);
    }

    /**
     * 查看场馆信息的详情
     *
     * @param context
     * @param stadiumInfos 要查看的场馆信息组（第一个为点击进入的场馆信息）
     */
    public static void checkStadiumsInfo(Context context, ArrayList<StadiumInfo> stadiumInfos) {
        Intent intent = new Intent(context, InfoDisplayActivity.class);
        intent.putParcelableArrayListExtra("stadiumInfo", stadiumInfos);
        context.startActivity(intent);
    }
}
