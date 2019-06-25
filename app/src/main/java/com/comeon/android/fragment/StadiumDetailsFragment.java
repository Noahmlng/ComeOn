package com.comeon.android.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.comeon.android.LaunchOrderActivity;
import com.comeon.android.R;
import com.comeon.android.controls.GradientTextButton;
import com.comeon.android.db.SportsType;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.util.MyApplication;

/**
 * 场馆详情页碎片
 */
@SuppressLint("ValidFragment")
public class StadiumDetailsFragment extends BaseFragment implements View.OnClickListener {

    TextView txt_stadiumName;
    TextView txt_stadiumAddress;
    ImageButton btn_sendMessage;
    ImageButton btn_call;
    GradientTextButton btn_launchAppoinment;
    private StadiumInfo stadiumInfo;

    /**
     * 构造方法传递要显示的对象
     *
     * @param stadiumInfo
     */
    public StadiumDetailsFragment(StadiumInfo stadiumInfo) {
        this.stadiumInfo = stadiumInfo;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_stadiuminfodetails;
    }

    @Override
    protected void initControls(View view) {
        txt_stadiumName = (TextView) view.findViewById(R.id.txt_stadiumName);
        txt_stadiumName.setText(stadiumInfo.getStadiumName());

        txt_stadiumAddress = (TextView) view.findViewById(R.id.txt_stadiumAddress);
//        txt_stadiumAddress.setText(stadiumInfo.getProvince() + stadiumInfo.getCity() + stadiumInfo.getDistrict() + stadiumInfo.getStreet() + stadiumInfo.getStreetNumber());

        //加载三个按钮
        btn_sendMessage = (ImageButton) view.findViewById(R.id.btn_sendMessage);
        btn_sendMessage.setVisibility(View.GONE);

        btn_call = (ImageButton) view.findViewById(R.id.btn_call);
        btn_launchAppoinment = (GradientTextButton) view.findViewById(R.id.btn_joinGroup);
        btn_launchAppoinment.setText("发起组团");

        //绑定点击事件
//        btn_sendMessage.setOnClickListener(this);
        btn_call.setOnClickListener(this);
        btn_launchAppoinment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sendMessage:
                //给场馆方发信息
                break;
            case R.id.btn_call:
                //给场馆方打电话
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + stadiumInfo.getStadiumContact()));
                startActivity(intent);
                break;
            case R.id.btn_joinGroup:
                //进入添加组团页
                LaunchOrderActivity.launchWithSpecificStadium(stadiumInfo);
                break;
        }
    }
}
