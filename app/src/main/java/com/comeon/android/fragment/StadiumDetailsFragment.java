package com.comeon.android.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.comeon.android.LaunchOrderActivity;
import com.comeon.android.R;
import com.comeon.android.controls.GradientTextButton;
import com.comeon.android.db.SportsType;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.db_accessing.SportsTypeDao;
import com.comeon.android.db_accessing.SportsTypeDaoImpl;
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
    ScrollView scrollView;

    SportsTypeDao sportsTypeDao=new SportsTypeDaoImpl();

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
        if (stadiumInfo.getStreetNumber()!=null && stadiumInfo.getStreetNumber().length()>0){
            txt_stadiumAddress.setText(stadiumInfo.getProvince() + stadiumInfo.getCity() + stadiumInfo.getDistrict() + stadiumInfo.getStreet() + stadiumInfo.getStreetNumber());
        }else{
            txt_stadiumAddress.setText(stadiumInfo.getProvince() + stadiumInfo.getCity() + stadiumInfo.getDistrict() + stadiumInfo.getStreet());
        }

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

        scrollView=(ScrollView)view.findViewById(R.id.stadium_images_layout);
        if (stadiumInfo.getSportsType().getId()==sportsTypeDao.findSportsTypeByName("篮球").getId()){
            scrollView.setBackgroundResource(R.drawable.stadium_sample_basketball2);
        }else if (stadiumInfo.getSportsType().getId()==sportsTypeDao.findSportsTypeByName("足球").getId()){
            scrollView.setBackgroundResource(R.drawable.stadium_sample_soccer1);
        }else if (stadiumInfo.getSportsType().getId()==sportsTypeDao.findSportsTypeByName("羽毛球").getId()){
            scrollView.setBackgroundResource(R.drawable.stadium_sample_badminton1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sendMessage:
                //给场馆方发信息
                break;
            case R.id.btn_call:
                //如果没有设置场馆的联系方式或者联系方式为空，则显示暂无联系方式
                if(stadiumInfo.getStadiumContact()==null){
                    Toast.makeText(this.getContext(), "暂无联系方式", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (stadiumInfo.getStadiumContact().trim().length()==0){
                    Toast.makeText(this.getContext(), "暂无联系方式", Toast.LENGTH_SHORT).show();
                    return;
                }
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
