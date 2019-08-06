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
import android.widget.ImageView;

import com.comeon.android.R;
import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db.SportsType;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.db_accessing.SportsTypeDao;
import com.comeon.android.db_accessing.SportsTypeDaoImpl;
import com.comeon.android.util.LogUtil;
import com.flyco.tablayout.SlidingTabLayout;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;

/**
 * 组团碎片
 */
public class GroupFragment extends BaseFragment {

    private static final String TAG = "GroupFragment";

    //创建碎片管理对象
    public FragmentManager fragmentManager;
    public FloatingActionMenu actionMenu;
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

    /*
        用静态常量标记运动类型编号
     */
    public static final int ST_EMPTY=-1;
    public static final int ST_BASKETBALL=1;
    public static final int ST_SOCCER=2;
    public static final int ST_RUN=3;
    public static final int ST_BADMINTON=4;

    /*
        4个ImageView记录icon
     */
    ImageView ic_basketball;
    ImageView ic_soccer;
    ImageView ic_run;
    ImageView ic_badminton;

    int sportsChoice = ST_EMPTY; //保存已选择的运动类型编号

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
                        break;
                    case 1:
                        isInStadiumFragment = false;
                        search_text.setHint("请输入邀约团名");
                        break;
                }
                refreshContentBySportsType(sportsChoice);//切换碎片时，也根据选定的运动类型进行刷新
                refreshContent(search_text.getText().toString().trim());//在切换碎片时，也要根据搜索框内容进行判断
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

        btn_selectFilter = (FloatingActionButton) view.findViewById(R.id.btn_selectFilter);
        initCircularMenu();
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

    /**
     * 加载CircularFloatingActionMenu
     */
    private void initCircularMenu() {
        ImageView fabIconNew = new ImageView(this.getActivity());
        fabIconNew.setImageResource(R.mipmap.ic_plus);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this.getActivity());
        ic_basketball=new ImageView(this.getActivity());
        ic_basketball.setImageResource(R.mipmap.ic_basketball);
        SubActionButton subActionButton1 = itemBuilder.setContentView(ic_basketball).build();
        subActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshItemIcon(sportsChoice, ST_BASKETBALL);
                refreshContentBySportsTypeOnUI(sportsChoice, ST_BASKETBALL);
            }
        });

        ic_soccer= new ImageView(this.getActivity());
        ic_soccer.setImageResource(R.mipmap.ic_football);
        SubActionButton subActionButton2 = itemBuilder.setContentView(ic_soccer).build();
        subActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshItemIcon(sportsChoice, ST_SOCCER);
                refreshContentBySportsTypeOnUI(sportsChoice, ST_SOCCER);
            }
        });

        ic_run = new ImageView(this.getActivity());
        ic_run.setImageResource(R.mipmap.ic_run);
        SubActionButton subActionButton3 = itemBuilder.setContentView(ic_run).build();
        subActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshItemIcon(sportsChoice, ST_RUN);
                refreshContentBySportsTypeOnUI(sportsChoice, ST_RUN);
            }
        });

        ic_badminton = new ImageView(this.getActivity());
        ic_badminton.setImageResource(R.mipmap.ic_badminton);
        SubActionButton subActionButton4 = itemBuilder.setContentView(ic_badminton).build();
        subActionButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshItemIcon(sportsChoice, ST_BADMINTON);
                refreshContentBySportsTypeOnUI(sportsChoice, ST_BADMINTON);
            }
        });

        actionMenu = new FloatingActionMenu.Builder(this.getActivity()).addSubActionView(subActionButton1).addSubActionView(subActionButton2).addSubActionView(subActionButton3).addSubActionView(subActionButton4).attachTo(btn_selectFilter).build();
        actionMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {

            }

            @Override
            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {

            }
        });

    }


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_group;
    }

    /**
     * 控制FloatingActionMenu上的imageview切换
     * @param pastChoice
     * @param currentChoice
     */
    public void refreshItemIcon(int pastChoice, int currentChoice){
        LogUtil.d(TAG, "pastChoice："+pastChoice+"；currentChoice："+currentChoice);
        switch (pastChoice){
            case ST_EMPTY:
                break;
            case ST_BASKETBALL:
                ic_basketball.setImageResource(R.mipmap.ic_basketball);
                break;
            case ST_SOCCER:
                ic_soccer.setImageResource(R.mipmap.ic_football);
                break;
            case ST_RUN:
                ic_run.setImageResource(R.mipmap.ic_run);
                break;
            case ST_BADMINTON:
                ic_badminton.setImageResource(R.mipmap.ic_badminton);
                break;
        }

        /*
            进行判断，如果和先前选择相同，则取消颜色不进行颜色的加深
         */
        if(pastChoice!=currentChoice){
            switch (currentChoice){
                case ST_BASKETBALL:
                    ic_basketball.setImageResource(R.mipmap.ic_basketball_selected);
                    break;
                case ST_SOCCER:
                    ic_soccer.setImageResource(R.mipmap.ic_football_selected);
                    break;
                case ST_RUN:
                    ic_run.setImageResource(R.mipmap.ic_run_selected);
                    break;
                case ST_BADMINTON:
                    ic_badminton.setImageResource(R.mipmap.ic_badminton_selected);
                    break;
            }
        }

    }

    /**
     * 控制FloatingActionMenu上的数据集切换
     * @param pastChoice
     * @param currentChoice
     */
    public void refreshContentBySportsTypeOnUI(int pastChoice, int currentChoice){
        if(pastChoice==currentChoice){
            refreshContentBySportsType(ST_EMPTY);
            sportsChoice=ST_EMPTY;
            return;
        }
        refreshContentBySportsType(currentChoice);
        sportsChoice=currentChoice;
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
                if (stadium_condition == null) {
                    stadium_condition = new StadiumInfo();
                }
                stadium_condition.setStadiumName(inputText);
            } else {
                if (stadium_condition != null) {
                    if (stadium_condition.getSportsType() == null) {
                        stadium_condition = null;//当没有搜索关键字，并且没有选择种类时，则全加载
                    } else {
                        stadium_condition.setStadiumName(null);//当已经选择了运动种类，则保留运动种类的筛选结果
                    }
                }
            }
            stadiumsFragment.setSelectFilter(stadium_condition);
            stadiumsFragment.reloadRecyclerView();
        } else {
            if (inputText.length() > 0) {
                LogUtil.d(TAG, "当前在附近邀约碎片进行查询");
                if (order_condition == null) {
                    order_condition = new AppointmentOrder();
                }
                order_condition.setOrderName(inputText);
            } else {
                if (order_condition != null) {
                    if (order_condition.getOrderSportsType() == null) {
                        //当没有搜索关键字，并且没有选择种类时，则全加载
                        order_condition = null;
                    } else {
                        order_condition.setOrderName(null);
                    }
                }
            }
            groupInfoFragment.setSelectFilter(order_condition);
            groupInfoFragment.reloadRecyclerView();
        }
    }

    /**
     * 根据传递的运动类型刷新集合
     * @param choice   传入的运动类型编号
     */
    private void refreshContentBySportsType(int choice) {
        SportsTypeDao sportsTypeDao = new SportsTypeDaoImpl();//用于使用名字查询SportsType
        SportsType selectedSportsType = null;
        switch (choice) {
            case ST_EMPTY:
                selectedSportsType = null;
                break;
            case ST_RUN:
                selectedSportsType = sportsTypeDao.findSportsTypeByName("跑步");
                break;
            case ST_SOCCER:
                selectedSportsType = sportsTypeDao.findSportsTypeByName("足球");
                break;
            case ST_BASKETBALL:
                selectedSportsType = sportsTypeDao.findSportsTypeByName("篮球");
                break;
            case ST_BADMINTON:
                selectedSportsType = sportsTypeDao.findSportsTypeByName("羽毛球");
                break;
        }

        /*
            执行刷新操作
         */
        if (isInStadiumFragment) {
            if (stadium_condition == null) {
                stadium_condition = new StadiumInfo();
            }
            stadium_condition.setSportsType(selectedSportsType);
            stadiumsFragment.setSelectFilter(stadium_condition);
            stadiumsFragment.reloadRecyclerView();
        } else {
            if (order_condition == null) {
                order_condition = new AppointmentOrder();
            }
            order_condition.setOrderSportsType(selectedSportsType);
            groupInfoFragment.setSelectFilter(order_condition);
            groupInfoFragment.reloadRecyclerView();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        actionMenu.close(false);
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