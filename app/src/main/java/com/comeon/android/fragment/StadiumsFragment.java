package com.comeon.android.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private RecyclerView recyclerView;
    private StadiumsBusinessLogicInterface stadiumsBusinessLogic=new StadiumsBusiness();

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
        loadRecyclerView(stadiumsBusinessLogic.getAllStadiums());
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_group_stadiums;
    }


    /**
     * 向Recyclerview中填充数据
     * @param stadiumInfoList  场馆集合
     */
    private void loadRecyclerView(List<StadiumInfo> stadiumInfoList) {
        if(stadiumInfoList!=null){
            StadiumsAdapter stadiumsAdapter=new StadiumsAdapter(stadiumInfoList);
            recyclerView.setAdapter(stadiumsAdapter);
        }else{
            LogUtil.e(TAG, "传入的场馆集合为空");
        }
    }
}
