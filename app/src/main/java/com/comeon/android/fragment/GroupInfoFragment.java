package com.comeon.android.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.comeon.android.R;
import com.comeon.android.adapter.GroupInfoAdapter;
import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db_accessing.AppointmentOrderDao;
import com.comeon.android.db_accessing.AppointmentOrderDaoImpl;
import com.comeon.android.util.LogUtil;

import java.util.List;

/**
 * 附近邀约信息碎片
 */
public class GroupInfoFragment extends BaseFragment {
    private static final String TAG = "GroupInfoFragment";
    
    private RecyclerView recyclerView;
    private AppointmentOrderDao orderDao=new AppointmentOrderDaoImpl();

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
        loadRecyclerView(orderDao.getAllOrders());
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_group_info;
    }


    /**
     * 向Recyclerview中填充数据
     * @param appointmentOrders  邀约集合
     */
    private void loadRecyclerView(List<AppointmentOrder> appointmentOrders) {
        if(appointmentOrders!=null){
            GroupInfoAdapter groupInfoAdapter=new GroupInfoAdapter(appointmentOrders);
            recyclerView.setAdapter(groupInfoAdapter);
        }else{
            LogUtil.e(TAG, "传入的邀约集合为空");
        }
    }
}