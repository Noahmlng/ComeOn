package com.comeon.android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.comeon.android.business_logic.OrderBusiness;
import com.comeon.android.business_logic.OrderBusinessInterface;
import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db.SportsType;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.Activity_Parent;
import com.comeon.android.util.MyApplication;
import com.comeon.android.util.ViewUtil;

import java.nio.channels.SelectableChannel;

public class LaunchOrderActivity extends Activity_Parent implements View.OnClickListener {

    ImageButton btn_goback;
    Button btn_launchAppointment;
    ImageButton btn_addPeople;
    ImageButton btn_reducePeople;
    EditText editText_peopleSize;
    EditText editText_groupName;
    EditText editText_location;
    EditText editText_phone;
    OrderBusinessInterface orderBusiness = new OrderBusiness();

    private SportsType selectedSportsType;
    private StadiumInfo selectedStadium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setStatusBarColor(this, Color.WHITE, false);
        setContentView(R.layout.activity_launch_order);
        selectedSportsType = (SportsType) getIntent().getParcelableExtra("selectedSportsType");
        selectedStadium=(StadiumInfo)getIntent().getParcelableExtra("selectedStadium");
        if (selectedSportsType==null){
            selectedSportsType=selectedStadium.getSportsType();
        }
        initControls();
    }

    protected void initControls() {
        btn_goback = (ImageButton) findViewById(R.id.btn_goback);
        btn_launchAppointment = (Button) findViewById(R.id.btn_launchAppointment);
        btn_addPeople = (ImageButton) findViewById(R.id.btn_addPeople);
        btn_reducePeople = (ImageButton) findViewById(R.id.btn_reducePeople);

        editText_peopleSize = (EditText) findViewById(R.id.edittext_peoplesize);
        editText_groupName = (EditText) findViewById(R.id.edittext_groupName);
        editText_location = (EditText) findViewById(R.id.edittext_location);
        /*
            如果通过场馆进入，则进行场馆名加载且不可修改
         */
        if (selectedStadium!=null){
            editText_location.setText(selectedStadium.getStadiumName());
            /*
                模拟readonly
             */
            editText_location.setInputType(InputType.TYPE_NULL);
            editText_location.setTextColor(Color.GRAY);
        }

        editText_phone = (EditText) findViewById(R.id.edittext_phone);
        if (loginUser!=null){
            editText_phone.setText(loginUser.getUserPhone());
        }

        //绑定点击事件
        btn_goback.setOnClickListener(this);
        btn_launchAppointment.setOnClickListener(this);
        btn_addPeople.setOnClickListener(this);
        btn_reducePeople.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addPeople:
                int peopleSize = Integer.valueOf(editText_peopleSize.getText().toString());
                peopleSize += 1;
                editText_peopleSize.setText(String.valueOf(peopleSize));
                break;
            case R.id.btn_reducePeople:
                peopleSize = Integer.valueOf(editText_peopleSize.getText().toString());
                peopleSize -= 1;
                editText_peopleSize.setText(String.valueOf(peopleSize));
                break;
            case R.id.btn_launchAppointment:
                launchAppointment();
                break;
            case R.id.btn_goback:
                this.finish();
                break;
        }
    }

    /**
     * 发布组团的方法
     */
    public void launchAppointment() {
        if (loginUser == null) {
            Toast.makeText(this, "不支持游客发起订单", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String expectedSize = editText_peopleSize.getText().toString().trim();
        String groupName = editText_groupName.getText().toString().trim();
        String contact = editText_phone.getText().toString().trim();
        String location = editText_location.getText().toString().trim();

        /*
            先进行输入验证
         */
        if(checkInputValidity(expectedSize,groupName,contact, location)){
            /*
                判断是否已选中场馆，选用不同的重载方法
             */
            boolean result=false;
            if (selectedStadium!=null){
                result=orderBusiness.createNewOrder(loginUser, Integer.valueOf(expectedSize),groupName, contact, selectedStadium);
            }else{
                result=orderBusiness.createNewOrder(loginUser,Integer.valueOf(expectedSize),groupName,contact,location,selectedSportsType);
            }
            //根据结果判断，将结果反馈给用户（UI层）
            if (result){
                Toast.makeText(this, "发布成功！请静静等待你的成员来到吧~",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "发生未知错误！",Toast.LENGTH_SHORT).show();
            }
            this.finish();
        }
    }

    /**
     * 进行输入的表单验证
     * @return  验证结果
     */
    public boolean checkInputValidity(String expectedSize, String groupName, String contact, String location){
        /*
            先进行非空验证
         */
        if (expectedSize.length() == 0) {
            Toast.makeText(this, "组团人数至少为2人", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (groupName.length() == 0) {
            Toast.makeText(this, "取个团名吧！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (contact.length() == 0) {
            Toast.makeText(this, "联系人手机号码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!contact.matches("^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$")) {
            /*
            使用正则表达式验证手机号码格式
            */
            Toast.makeText(this, "订单联系电话格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (location.length() == 0) {
            Toast.makeText(this, "约定地址不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     * 提供运动类型，由自主选择发起订单
     * @param sportsType  自主选中的运动类型
     */
    public static void launchWithSportsType(SportsType sportsType){
        //传递数据到下一个页面
        Intent intent=new Intent(MyApplication.getContext(), LaunchOrderActivity.class);
        intent.putExtra("selectedSportsType",sportsType);
        MyApplication.getContext().startActivity(intent);
    }

    /**
     * 通过场馆详情页发起订单
     * @param stadiumInfo  选中的场馆
     */
    public static void launchWithSpecificStadium(StadiumInfo stadiumInfo){
        //传递数据到下一个页面
        Intent intent=new Intent(MyApplication.getContext(), LaunchOrderActivity.class);
        intent.putExtra("selectedStadium",stadiumInfo);
        MyApplication.getContext().startActivity(intent);
    }


}
