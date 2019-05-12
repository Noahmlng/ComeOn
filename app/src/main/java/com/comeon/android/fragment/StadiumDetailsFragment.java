package com.comeon.android.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.ImageVideoBitmapDecoder;
import com.comeon.android.R;
import com.comeon.android.controls.GradientTextButton;
import com.comeon.android.db.StadiumInfo;

import org.w3c.dom.Text;

/**
 * 场馆详情页碎片
 */
@SuppressLint("ValidFragment")
public class StadiumDetailsFragment extends BaseFragment implements View.OnClickListener{

    private StadiumInfo stadiumInfo;

    TextView txt_stadiumName;
    TextView txt_stadiumAddress;
    ImageButton btn_sendMessage;
    ImageButton btn_call;
    GradientTextButton btn_joinGroup;

    /**
     * 构造方法传递要显示的对象
     * @param stadiumInfo
     */
    public StadiumDetailsFragment(StadiumInfo stadiumInfo){this.stadiumInfo=stadiumInfo;}

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_stadiuminfodetails;
    }

    @Override
    protected void initControls(View view) {
        txt_stadiumName=(TextView)view.findViewById(R.id.txt_stadiumName);
        txt_stadiumName.setText(stadiumInfo.getStadiumName());

        txt_stadiumAddress=(TextView)view.findViewById(R.id.txt_stadiumAddress);
        txt_stadiumAddress.setText(stadiumInfo.getProvince()+stadiumInfo.getCity()+stadiumInfo.getDistrict()+stadiumInfo.getStreet()+stadiumInfo.getStreetNumber());

        //加载三个按钮
        btn_sendMessage=(ImageButton)view.findViewById(R.id.btn_sendMessage);
        btn_call=(ImageButton)view.findViewById(R.id.btn_call);
        btn_joinGroup=(GradientTextButton)view.findViewById(R.id.btn_joinGroup);

        //绑定点击事件
        btn_sendMessage.setOnClickListener(this);
        btn_call.setOnClickListener(this);
        btn_joinGroup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sendMessage:
                //打开与发起者的聊天页面
                Toast.makeText(getActivity(), "打开与发起者的聊天页面", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_call:
                //给发起者打电话
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+stadiumInfo.getStadiumContact()));
                startActivity(intent);
                break;
            case R.id.btn_joinGroup:
                //加入组团的业务操作
                Toast.makeText(getActivity(), "加入组团的业务操作", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
