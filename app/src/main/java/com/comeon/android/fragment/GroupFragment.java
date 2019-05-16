package com.comeon.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.comeon.android.R;
import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.util.LogUtil;
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
    //条件装载器
    StadiumInfo stadium_condition;
    AppointmentOrder order_condition;
    //设置适配器
    private MyPageAdapter mAdapter;
    private ArrayList<Fragment> mFragments;
    private String[] mTitles = {"场馆", "附近邀约"};
    private StadiumsFragment stadiumsFragment = new StadiumsFragment();
    private GroupInfoFragment groupInfoFragment = new GroupInfoFragment();
    private boolean isInStadiumFragment = true; //监控当前在哪个页面碎片

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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        isInStadiumFragment = true;
                        search_text.setHint("请输入场馆名称");
                        refreshContent(search_text.getText().toString().trim());//在切换碎片时，也要根据搜索框内容进行判断
                        break;
                    case 1:
                        isInStadiumFragment = false;
                        search_text.setHint("请输入邀约团名");
                        refreshContent(search_text.getText().toString().trim());//在切换碎片时，也要根据搜索框内容进行判断
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        search_text = (EditText) view.findViewById(R.id.search_text);
        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshContent(s.toString().trim());
            }

        });

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
     * 刷新场馆、订单的内容
     */
    public void refreshContent(String inputText) {
        //输入结束后的动态模糊查询
                /*
                    先判断当前的碎片，再执行相应的操作
                 */
        if (isInStadiumFragment) {
            LogUtil.d(TAG, "当前文字为：" + inputText + "；长度为：" + inputText.length());
            if (inputText.length() > 0) {
                LogUtil.d(TAG, "当前在场馆碎片进行查询");
                //场馆页操作
                stadium_condition = new StadiumInfo();
                stadium_condition.setStadiumName(inputText);
                stadiumsFragment.setSelectFilter(stadium_condition);
                stadiumsFragment.reloadRecyclerView();
            } else {
                //全加载
                stadium_condition = null;
                stadiumsFragment.setSelectFilter(stadium_condition);
                stadiumsFragment.reloadRecyclerView();
            }
        } else {
            if (inputText.length() > 0) {
                LogUtil.d(TAG, "当前在附近邀约碎片进行查询");
                order_condition = new AppointmentOrder();
                order_condition.setOrderName(inputText);
                groupInfoFragment.setSelectFilter(order_condition);
                groupInfoFragment.reloadRecyclerView();
            } else {
                //全加载
                order_condition = null;
                groupInfoFragment.setSelectFilter(order_condition);
                groupInfoFragment.reloadRecyclerView();
            }
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
