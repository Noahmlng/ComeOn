package com.comeon.android.fragment;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.comeon.android.R;
import com.comeon.android.adapter.GroupInfoAdapter;
import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db_accessing.AppointmentOrderDao;
import com.comeon.android.db_accessing.AppointmentOrderDaoImpl;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.MyApplication;

import org.litepal.LitePal;

import java.util.List;

/**
 * 附近邀约信息碎片
 */
public class GroupInfoFragment extends BaseFragment {
    private static final String TAG = "GroupInfoFragment";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AppointmentOrderDao orderDao=new AppointmentOrderDaoImpl();
    private List<AppointmentOrder> orders;
    GroupInfoAdapter groupInfoAdapter;

    @Override
    protected void initControls(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        /*
            设置RecyclerView中子项的布局方式
         */
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        /*
            初始化数据时，将所有的场馆加载入recyclerview中
         */
        orders=orderDao.getAllOrders();
        groupInfoAdapter=new GroupInfoAdapter(orders);
        recyclerView.setAdapter(groupInfoAdapter);

        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新操作
                reLoadRecyclerView(orderDao.getAllOrders());
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_group_info;
    }

    public void loadData(List<AppointmentOrder> orders){
        this.orders=orders;
    }

    /**
     * 向Recyclerview中填充数据
     * @param appointmentOrders  邀约集合
     */
    private void reLoadRecyclerView(final List<AppointmentOrder> appointmentOrders) {
        if(appointmentOrders!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    /*
                        模拟网络提取数据制造延迟
                     */
                    try{
                        Thread.sleep(2000);
                    }catch(InterruptedException ex){
                        LogUtil.e(TAG, ex.getMessage());
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //重新获取数据
                            loadData(appointmentOrders);
                            //通知数据发生了改变，重新加载
                            groupInfoAdapter.notifyDataSetChanged();
                            //表示刷新结束，并隐藏进度条
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }).start();
        }else{
            LogUtil.e(TAG, "传入的邀约集合为空");
        }
    }
}