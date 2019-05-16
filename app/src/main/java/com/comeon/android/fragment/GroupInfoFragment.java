package com.comeon.android.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.comeon.android.R;
import com.comeon.android.adapter.GroupInfoAdapter;
import com.comeon.android.business_logic.OrderBusiness;
import com.comeon.android.business_logic.OrderBusinessInterface;
import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 附近邀约信息碎片
 */
public class GroupInfoFragment extends BaseFragment {
    private static final String TAG = "GroupInfoFragment";
    GroupInfoAdapter groupInfoAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private OrderBusinessInterface orderBusiness = new OrderBusiness();
    private ArrayList<AppointmentOrder> orders;
    private AppointmentOrder selectFilter;

    public AppointmentOrder getSelectFilter() {
        return selectFilter;
    }

    public void setSelectFilter(AppointmentOrder selectFilter) {
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
        orders = orderBusiness.getAllOrders();
        groupInfoAdapter = new GroupInfoAdapter(orders);
        recyclerView.setAdapter(groupInfoAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新操作
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
        return R.layout.fragment_group_info;
    }

    /**
     * 根据类成员筛选器加载数据的方法
     */
    public void loadData() {
        if(orders!=null){
            if (selectFilter == null) {
                this.orders.clear();
                this.orders.addAll(orderBusiness.getAllOrders());
            }else{
                this.orders.clear();
                List<AppointmentOrder> qualifiedOrders = orderBusiness.getOrdersWithCondition(selectFilter);
                if (qualifiedOrders != null) {
                    LogUtil.d(TAG, "有"+qualifiedOrders.size()+"个符合条件的订单");
                    this.orders.addAll(qualifiedOrders);
                }
            }
        }

    }

    /**
     * 向Recyclerview中填充数据
     */
    public void reloadRecyclerView() {
        //重新获取数据
        loadData();
        if (orders != null) {
            //通知数据发生了改变，重新加载
            groupInfoAdapter.notifyDataSetChanged();
            //表示刷新结束，并隐藏进度条
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}