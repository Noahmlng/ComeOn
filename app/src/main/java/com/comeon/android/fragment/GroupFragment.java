package com.comeon.android.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.comeon.android.R;
import com.comeon.android.controls.TabEntity;
import com.comeon.android.util.LogUtil;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

/**
 * 组团碎片
 */
public class GroupFragment extends BaseFragment {

    private static final String TAG = "GroupFragment";

    //创建碎片管理对象
    public FragmentManager fragmentManager;

    //设置适配器
    private MyPageAdapter mAdapter;
    private ArrayList<Fragment> mFragments;
    private String[] mTitles = {"场馆", "附近邀约","app"};

    SlidingTabLayout tabLayout;
    EditText search_text;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_group, container, false);
        fragmentManager= this.getActivity().getSupportFragmentManager();
        mFragments = new ArrayList<>();
        initControls(view);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initControls(View view){
        tabLayout=(SlidingTabLayout) view.findViewById(R.id.tablayout);
        viewPager=(ViewPager)view.findViewById(R.id.view_pager);
        initTab();
        /*
            监听tab选项，切换到相应的碎片
         */
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
            @Override
            public void onTabReselect(int position) {
            }
        });
        search_text=(EditText)view.findViewById(R.id.search_text);
    }

    /**
     * 加载Tab选项
     */
    private void initTab() {
        mFragments.add(new StadiumsFragment());
        mFragments.add(new StadiumsFragment());

        //实例化适配器（构造方法：放入碎片管理对象）
        mAdapter=new MyPageAdapter(fragmentManager);
        //设置ViewPager与适配器关联
        viewPager.setAdapter(mAdapter);
        LogUtil.e(TAG,String.valueOf(mFragments.size()));
        //设置Tab与ViewPager关联
        tabLayout.setViewPager(viewPager);
    }

    /**
     * 动态替换碎片的方法
     * @param fragment  要放入的碎片
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
    }


    /**
     * 适配器帮助读取tablayout中的值
     */
    private class MyPageAdapter extends FragmentPagerAdapter{

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public int getCount() {
            return  mFragments.size();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_group;
    }
}
