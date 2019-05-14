package com.comeon.android.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.comeon.android.ChatActivity;
import com.comeon.android.InfoDisplayActivity;
import com.comeon.android.MainActivity;
import com.comeon.android.R;
import com.comeon.android.StartActivity;
import com.comeon.android.adapter.ParticipantAdapter;
import com.comeon.android.business_logic.OrderBusiness;
import com.comeon.android.business_logic.OrderBusinessInterface;
import com.comeon.android.controls.GradientTextButton;
import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.MyApplication;
import com.comeon.android.util.Utilities;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 组团信息详情页碎片
 */
@SuppressLint("ValidFragment")
public class GroupDetailsFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "GroupDetailsFragment";

    CircleImageView head_icon;
    TextView txt_sponsorName;
    TextView txt_sponsorPhone;
    RecyclerView recyclerView_participants;
    FrameLayout stadiumFragment_layout;
    ImageButton btn_sendMessage;
    ImageButton btn_call;
    GradientTextButton btn_joinGroup;
    ParticipantAdapter participantAdapter;
    private AppointmentOrder group;

    private UserInfo loginUser;
    private OrderBusinessInterface orderBusiness=new OrderBusiness();

    public GroupDetailsFragment(AppointmentOrder group) {
        this.group = group;
        LogUtil.d(TAG, String.valueOf(group.getId()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginUser=((InfoDisplayActivity)getActivity()).getLoginUser();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_groupinfodetails;
    }

    @Override
    protected void initControls(View view) {
        //加载发起者头像
        head_icon = (CircleImageView) view.findViewById(R.id.head_icon);
        head_icon.setImageBitmap(Utilities.translateBytes(group.getOrderSponsor().getHeadIcon()));

        //加载组团名（/发起人姓名）
        txt_sponsorName = (TextView) view.findViewById(R.id.txt_sponsorName);
        if (group.getOrderName() != null) {
            txt_sponsorName.setText(group.getOrderName());
        } else {
            txt_sponsorName.setText(group.getOrderSponsor().getUserNickName());
        }

        //加载发起者电话
        txt_sponsorPhone = (TextView) view.findViewById(R.id.txt_sponsorPhone);
        txt_sponsorPhone.setText(group.getOrderContact());

        //参与者列表
        recyclerView_participants = (RecyclerView) view.findViewById(R.id.recycler_view_participants);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_participants.setLayoutManager(linearLayoutManager);
        participantAdapter = new ParticipantAdapter(group.getOrderParticipants(),loginUser);
        recyclerView_participants.setAdapter(participantAdapter);

        //如果选择了体育场馆，则加载出体育场馆
        stadiumFragment_layout = (FrameLayout) view.findViewById(R.id.fragment_layout);

        //加载三个按钮
        btn_sendMessage = (ImageButton) view.findViewById(R.id.btn_sendMessage);
        btn_call = (ImageButton) view.findViewById(R.id.btn_call);
        btn_joinGroup = (GradientTextButton) view.findViewById(R.id.btn_joinGroup);

        //绑定点击事件
        btn_sendMessage.setOnClickListener(this);
        btn_call.setOnClickListener(this);
        btn_joinGroup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sendMessage:
                //打开与发起者的聊天页面
                /*
                排除和自己聊天的情况
                 */
                if (loginUser==null){
                    Toast.makeText(MyApplication.getContext(), "游客无法发起会话",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(group.getOrderSponsor().getId()==loginUser.getId()){
                    Toast.makeText(MyApplication.getContext(), "无法和自己发起会话",Toast.LENGTH_SHORT).show();
                    return;
                }
                ChatActivity.enterChatPage(MyApplication.getContext(), group.getOrderSponsor().getId());
                break;
            case R.id.btn_call:
                //给发起者打电话
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + group.getOrderSponsor().getUserPhone()));
                startActivity(intent);
                break;
            case R.id.btn_joinGroup:
                //加入组团的业务操作
                /*
                    先判断是否登录
                 */
                if(loginUser==null){
                    Toast.makeText(MyApplication.getContext(), "游客无法加入组团，请先登录",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (orderBusiness.participateGroup(loginUser,group)){
                    Toast.makeText(MyApplication.getContext(), "参与组团成功！",Toast.LENGTH_SHORT).show();
                    //刷新participant列表
                    participantAdapter = new ParticipantAdapter(orderBusiness.refreshParticipantsList(group),loginUser);
                    recyclerView_participants.setAdapter(participantAdapter);
                }else{
                    Toast.makeText(MyApplication.getContext(), "您已参加了此订单，记得要准时哦~",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
