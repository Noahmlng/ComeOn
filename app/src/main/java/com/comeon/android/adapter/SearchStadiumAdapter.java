package com.comeon.android.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comeon.android.LaunchOrderActivity;
import com.comeon.android.R;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.util.LogUtil;

import java.util.List;

public class SearchStadiumAdapter extends RecyclerView.Adapter<SearchStadiumAdapter.ViewHolder> {
    private static final String TAG = "SearchStadiumAdapter";

    private List<StadiumInfo> stadiumInfos;

    private LaunchOrderActivity launchOrderActivity;  //记录调用此适配器的对象

    /**
     * 构造方式为列表赋值
     * @param stadiums  传入的场馆集合
     */
    public SearchStadiumAdapter(List<StadiumInfo> stadiums){
        this.stadiumInfos=stadiums;
    }

    /**
     * 构造方式为列表赋值
     * （）
     * @param stadiums            传入的场馆集合
     * @param launchOrderActivity 调用此适配器的活动对象
     */
    public SearchStadiumAdapter(List<StadiumInfo> stadiums, LaunchOrderActivity launchOrderActivity) {
        this.stadiumInfos = stadiums;
        this.launchOrderActivity = launchOrderActivity;
    }

    @NonNull
    @Override
    public SearchStadiumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_stadium_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        //绑定点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchStadiumAdapter.ViewHolder viewHolder, int i) {
        StadiumInfo stadiumInfo = stadiumInfos.get(i);

        try {
            viewHolder.txt_stadiumName.setText(stadiumInfo.getStadiumName());  //读取场馆的场馆名
            viewHolder.txt_avgConsumption.setText("人均：" + stadiumInfo.getAvgConsumption());  //读取人均消费
            viewHolder.txt_stadiumAddress.setText(stadiumInfo.getProvince() + stadiumInfo.getCity() + stadiumInfo.getDistrict() + stadiumInfo.getStreet());  //读取场馆详细地址
            if (stadiumInfo.getStreetNumber() != null && stadiumInfo.getStreetNumber().trim().length() > 0) {  //如果有街号，则进行拼接
                viewHolder.txt_stadiumAddress.setText(viewHolder.txt_stadiumAddress.getText() + stadiumInfo.getStreetNumber());
            }
        } catch (NullPointerException ex) {
            LogUtil.e(TAG, ex.getMessage());
        } catch (Exception ex) {
        }
    }

    @Override
    public int getItemCount() {
        if (stadiumInfos == null) {
            return 0;
        }
        return stadiumInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_stadiumName;
        TextView txt_stadiumAddress;
        TextView txt_avgConsumption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_stadiumName = (TextView) itemView.findViewById(R.id.txt_stadiumName);
            txt_stadiumAddress = (TextView) itemView.findViewById(R.id.txt_stadiumAddress);
            txt_avgConsumption = (TextView) itemView.findViewById(R.id.txt_avgConsumption);
        }
    }
}
