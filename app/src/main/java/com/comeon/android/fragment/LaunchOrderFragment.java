package com.comeon.android.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.comeon.android.R;

import java.util.ArrayList;

/**
 * 发布组团信息的管理页面
 */
public class LaunchOrderFragment extends BaseFragment {
    private static final String TAG = "LaunchOrderFragment";

    //碎片中管理碎片则使用getChildFragmentManager()
    FragmentManager fragmentManager;
    SportsTypeChoiceFragment sportsTypeChoiceFragment;
    ViewPager viewPager;
    //设置适配器
    private MyPageAdapter mAdapter;
    private ArrayList<Fragment> mFragments;

    public void addFragment(long categoryId) {
        if (mFragments.size() == 1) {
            sportsTypeChoiceFragment = new SportsTypeChoiceFragment(categoryId);
            mFragments.add(sportsTypeChoiceFragment);
            mAdapter.notifyDataSetChanged();
        } else {
            sportsTypeChoiceFragment.refresh(categoryId);
        }
        viewPager.setCurrentItem(1);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_launch_order;
    }

    @Override
    protected void initControls(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mFragments = new ArrayList<Fragment>();
        mFragments.add(new CategoryChoiceFragment());

        fragmentManager = getChildFragmentManager();
        mAdapter = new MyPageAdapter(fragmentManager);
        viewPager.setAdapter(mAdapter);
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

}
