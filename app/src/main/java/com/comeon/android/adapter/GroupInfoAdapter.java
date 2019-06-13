package com.comeon.android.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comeon.android.InfoDisplayActivity;
import com.comeon.android.R;
import com.comeon.android.business_logic.OrderBusiness;
import com.comeon.android.business_logic.OrderBusinessInterface;
import com.comeon.android.controls.GradientTextButton;
import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.MyApplication;
import com.comeon.android.util.Utilities;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 组团信息适配器
 */
public class GroupInfoAdapter extends RecyclerView.Adapter<GroupInfoAdapter.ViewHolder> {

    private static final String TAG = "GroupInfoAdapter";
    private OrderBusinessInterface orderBusiness=new OrderBusiness();

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

        /*
            展示组团的类型以及仍需组团的人数
         */
        List<UserInfo> participants=orderBusiness.loadParticipantsList(order); //参与者的List
        int memberGapCount=order.getOrderExpectedSize()-participants.size();//记录缺口人数
        String typeName=order.getOrderSportsType().getTypeName();//记录订单的运动类型

        /*
            用SpannableString给文字做特殊处理
         */

        //1、处理运动类型文字
        SpannableStringBuilder typeSizeTip=new SpannableStringBuilder("发出" + typeName+"邀约\t  ");
        typeSizeTip.setSpan(new ForegroundColorSpan(Color.RED),2, 2+typeName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //为组团的运动类型加上前景色
        typeSizeTip.setSpan(new StyleSpan(Typeface.BOLD),2, 2+typeName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //给组团的运动类型加粗处理


        //2、处理缺口人数文字
        /*
            如果缺口人数为正，则显示“仍需……人”；
            如果缺口人数为负或者0，则显示“已成功组团（……人）”。
         */
        if(memberGapCount>0){
            typeSizeTip.append("仍需" +memberGapCount + "人");
            int length=(memberGapCount>=10)?2:1; //记录缺口人数的数字长度
            typeSizeTip.setSpan(new ForegroundColorSpan(Color.RED),2+typeName.length()+7, 2+typeName.length()+7+length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //为组团的缺口人数加上前景色
            typeSizeTip.setSpan(new StyleSpan(Typeface.BOLD),2+typeName.length()+7, 2+typeName.length()+7+length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //给组团的缺口人数加粗处理
        }else{
            typeSizeTip.append("已成功组团（" +participants.size() + "人）");
            int length=(participants.size()>=10)?2:1; //记录已成功组团的人数的数字长度
            typeSizeTip.setSpan(new ForegroundColorSpan(Color.RED),2+typeName.length()+11, 2+typeName.length()+11+length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //为组团的缺口人数加上前景色
            typeSizeTip.setSpan(new StyleSpan(Typeface.BOLD),2+typeName.length()+11, 2+typeName.length()+11+length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //给组团的缺口人数加粗处理
        }
        viewHolder.txt_groupInfo.setText(typeSizeTip);

        viewHolder.txt_groupName.setText(order.getOrderName());

        /*
        展示组团进度的功能：
            根据memberGapCount的符号来做出不同的UI展示
            1. 人数未到预计人数
            2. 人数刚好到达预计人数
            3. 人数超过预计人数
         */
    }

    @Override
    public int getItemCount() {
        if(orders!=null){
            return orders.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        /*
            子项中的控件
         */
        CircleImageView img_headIcon;
        TextView txt_groupName;
        TextView txt_groupInfo;
        TextView txt_groupLocation;
        GradientTextButton txt_launchTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_headIcon = (CircleImageView) itemView.findViewById(R.id.img_sponsorHeadIcon);
            txt_groupName = (TextView) itemView.findViewById(R.id.txt_groupName);
            txt_groupInfo = (TextView) itemView.findViewById(R.id.txt_groupInfo);
            txt_groupLocation = (TextView) itemView.findViewById(R.id.txt_groupLocation);
            txt_launchTime = (GradientTextButton) itemView.findViewById(R.id.txt_launchTime);
        }
    }


}
