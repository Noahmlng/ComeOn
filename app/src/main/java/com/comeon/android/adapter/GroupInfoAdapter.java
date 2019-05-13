package com.comeon.android.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comeon.android.InfoDisplayActivity;
import com.comeon.android.R;
import com.comeon.android.controls.GradientTextButton;
import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.MyApplication;
import com.comeon.android.util.Utilities;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 组团信息适配器
 */
public class GroupInfoAdapter extends RecyclerView.Adapter<GroupInfoAdapter.ViewHolder> {

    private static final String TAG = "GroupInfoAdapter";

    //类成员：附近组团信息的集合
    private ArrayList<AppointmentOrder> orders;

    public ArrayList<AppointmentOrder> getOrders() {
        return orders;
    }

    /**
     * 构造方法为类成员赋值
     *
     * @param orders 传入的订单集合
     */
    public GroupInfoAdapter(ArrayList<AppointmentOrder> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.group_info_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        //点击进入组团详情页
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppointmentOrder onClickOrder = orders.get(viewHolder.getAdapterPosition());
                ArrayList<AppointmentOrder> orders = new ArrayList<AppointmentOrder>();

                //将点击的order置于第一个
                orders.add(0, onClickOrder);
                InfoDisplayActivity.checkOrderInfo(MyApplication.getContext(), orders);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AppointmentOrder order = orders.get(i);
        /*
            展示数据库的值
         */
        viewHolder.img_headIcon.setImageBitmap(Utilities.translateBytes(order.getOrderSponsor().getHeadIcon()));
        viewHolder.txt_launchTime.setText("发布于" + Utilities.calculateTimeGapFromNowInMinutes(order.getOrderLaunchTime()) + "前");
        /*
            如果用户选择的是场馆信息，则输入场馆的地址
            如果是地址，则直接赋地址值
         */
        if (order.getOrderStadium() != null) {
            viewHolder.txt_groupLocation.setText(order.getOrderStadium().getStreet() + order.getOrderStadium().getStreetNumber());
        } else {
            viewHolder.txt_groupLocation.setText(order.getOrderLocation());
        }
        viewHolder.txt_groupInfo.setText("发出" + order.getOrderSportsType().getTypeName() + "邀约\t需组队" + order.getOrderExpectedSize() + "人");
        viewHolder.txt_sponsorName.setText(order.getOrderSponsor().getUserNickName());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        /*
            子项中的控件
         */
        CircleImageView img_headIcon;
        TextView txt_sponsorName;
        TextView txt_groupInfo;
        TextView txt_groupLocation;
        GradientTextButton txt_launchTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_headIcon = (CircleImageView) itemView.findViewById(R.id.img_sponsorHeadIcon);
            txt_sponsorName = (TextView) itemView.findViewById(R.id.txt_sponsorName);
            txt_groupInfo = (TextView) itemView.findViewById(R.id.txt_groupInfo);
            txt_groupLocation = (TextView) itemView.findViewById(R.id.txt_groupLocation);
            txt_launchTime = (GradientTextButton) itemView.findViewById(R.id.txt_launchTime);
        }
    }


}
