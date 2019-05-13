package com.comeon.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.comeon.android.R;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

/**
 * 组团碎片
 */
public class GroupFragment extends BaseFragment {

    private static final String TAG = "GroupFragment";

    //创建碎片管理对象
    public FragmentManager fragmentManager;
    SlidingTabLayout tabLayout;
    EditText search_text;
    ViewPager viewPager;
    FloatingActionButton btn_selectFilter;
    //设置适配器
    private MyPageAdapter mAdapter;
    private ArrayList<Fragment> mFragments;
    private String[] mTitles = {"场馆", "附近邀约"};
    private StadiumsFragment stadiumsFragment = new StadiumsFragment();
    private GroupInfoFragment groupInfoFragment = new GroupInfoFragment();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
            Activity中嵌套Fragment设置适配器管理：getSupportFragmentManager();
            Fragment中嵌套Fragment设置适配器管理  getChildFragmentManager();
         */
        fragmentManager = this.getChildFragmentManager();
        mFragments = new ArrayList<Fragment>();
        mFragments.add(stadiumsFragment);
        mFragments.add(groupInfoFragment);
        //实例化适配器（构造方法：放入碎片管理对象）
        mAdapter = new MyPageAdapter(fragmentManager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        initControls(view);
        return view;
    }

    /**
     * 初始化控件
     */
    protected void initControls(View view) {
        tabLayout = (SlidingTabLayout) view.findViewById(R.id.tablayout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        initTab();
        search_text = (EditText) view.findViewById(R.id.search_text);
        //        btn_selectFilter=(FloatingActionButton)view.findViewById(R.id.btn_selectFilter);
        //        btn_selectFilter.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                Toast.makeText(MyApplication.getContext(), "你点击了按钮",Toast.LENGTH_SHORT).show();
        //            }
        //        });


    }

    /**
     * 加载Tab选项
     */
    private void initTab() {
        //设置ViewPager与适配器关联
        viewPager.setAdapter(mAdapter);
        //设置Tab与ViewPager关联
        tabLayout.setViewPager(viewPager);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_group;
    }

    /**
     * 判断当前页面是哪一页
     *
     * @return true：场馆页；false：附近邀约页
     */
    private boolean getCurrentFragment() {
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.view_pager);
        if (currentFragment instanceof StadiumsFragment) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 适配器帮助读取tablayout中的值
     */
    private class MyPageAdapter extends FragmentPagerAdapter {

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
            return mFragments.size();
        }
    }
}
