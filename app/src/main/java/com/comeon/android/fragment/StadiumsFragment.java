package com.comeon.android.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.comeon.android.R;
import com.comeon.android.adapter.StadiumsAdapter;
import com.comeon.android.business_logic.StadiumsBusiness;
import com.comeon.android.business_logic.StadiumsBusinessLogicInterface;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.util.LogUtil;

import java.util.List;

/**
 * 组团碎片中：场馆碎片
 */
public class StadiumsFragment extends BaseFragment {

    private static final String TAG = "StadiumsFragment";
    StadiumsAdapter stadiumsAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private StadiumsBusinessLogicInterface stadiumsBusinessLogic = new StadiumsBusiness();
    private List<StadiumInfo> stadiumInfoList;
    private StadiumInfo selectFilter;

    public StadiumInfo getSelectFilter() {
        return selectFilter;
    }

    public void setSelectFilter(StadiumInfo selectFilter) {
        this.selectFilter = selectFilter;
    }

    @Override
    protected void initControls(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        /*
            设置RecyclerView中子项的布局方式
         */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        /*
            初始化数据时，将所有的场馆加载入recyclerview中
         */
        stadiumInfoList = stadiumsBusinessLogic.getAllStadiums();
        stadiumsAdapter = new StadiumsAdapter(stadiumInfoList);
        recyclerView.setAdapter(stadiumsAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                    /*
                        模拟网络提取数据制造延迟
                     */
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            LogUtil.e(TAG, ex.getMessage());
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                reloadRecyclerView();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_group_stadiums;
    }

    /**
     * 根据类成员加载数据的方法
     */
    public void loadData() {
        /*
            没有筛选情况的时候调用加载全部场馆的方法
         */
        if (stadiumInfoList != null) {
            if (selectFilter == null) {
                this.stadiumInfoList.clear();
                this.stadiumInfoList.addAll(stadiumsBusinessLogic.getAllStadiums());
            } else {
                this.stadiumInfoList.clear();
                List<StadiumInfo> qualifiedStadiums = stadiumsBusinessLogic.getStadiumsWithConditions(selectFilter);
                if (qualifiedStadiums != null) {
                    LogUtil.d(TAG, "有"+qualifiedStadiums.size()+"个符合条件的场馆");
                    this.stadiumInfoList.addAll(qualifiedStadiums);
                }
            }
        }

    }

    /**
     * 向Recyclerview中重新填充数据
     */
    public void reloadRecyclerView() {
        //重新获取数据
        loadData();
        if (stadiumInfoList != null) {
            //通知数据发生了改变，重新加载
            stadiumsAdapter.notifyDataSetChanged();
            //表示刷新结束，并隐藏进度条
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
