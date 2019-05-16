package com.comeon.android.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.comeon.android.R;
import com.comeon.android.adapter.SportsTypeAdapter;
import com.comeon.android.business_logic.OrderBusiness;
import com.comeon.android.business_logic.OrderBusinessInterface;
import com.comeon.android.db.SportsType;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.MyApplication;

import java.util.List;

/**
 * 选择运动类型的碎片
 */
@SuppressLint("ValidFragment")
public class SportsTypeChoiceFragment extends BaseFragment {
    private static final String TAG = "SportsTypeChoiceFragment";
    SportsTypeAdapter sportsTypeAdapter;
    RecyclerView recyclerView;
    private OrderBusinessInterface orderBusiness = new OrderBusiness();

    private List<SportsType> sportsTypeList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_sportstypechoice;
    }

    @Override
    protected void initControls(View view) {
//        sportsTypeList = orderBusiness.loadSportsTypeInCategory(categoryId);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        //添加自定义的分割线
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(MyApplication.getContext(), R.drawable.recyclerview_divider));
        recyclerView.addItemDecoration(divider);

        //设置布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyApplication.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        //加载数据
        sportsTypeAdapter = new SportsTypeAdapter(sportsTypeList);

        recyclerView.setAdapter(sportsTypeAdapter);
        LogUtil.d(TAG, "正在填充数据");
    }

    /**
     * 刷新
     *
     * @param categoryId
     */
    public void refresh(long categoryId) {
        if (sportsTypeList == null) {
            sportsTypeList = orderBusiness.loadSportsTypeInCategory(categoryId);
        } else {
            sportsTypeList.clear();
            sportsTypeList.addAll(orderBusiness.loadSportsTypeInCategory(categoryId));
            sportsTypeAdapter.notifyDataSetChanged();
        }
    }
}
