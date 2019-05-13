package com.comeon.android;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.comeon.android.business_logic.OrderBusiness;
import com.comeon.android.business_logic.OrderBusinessInterface;
import com.comeon.android.db.SportsType;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.Activity_Parent;
import com.comeon.android.util.ViewUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setStatusBarColor(this, Color.WHITE, false);
        setContentView(R.layout.activity_launch_order);
        selectedSportsType = (SportsType) getIntent().getParcelableExtra("selectedSportsType");
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
        editText_phone = (EditText) findViewById(R.id.edittext_phone);
        editText_phone.setText(loginUser.getUserPhone());

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
        String locaion = editText_location.getText().toString().trim();
        /*
        先进行非空验证（组团名不需要）
         */
        if (expectedSize.length() == 0) {
            Toast.makeText(this, "组团人数至少为2人", Toast.LENGTH_SHORT).show();
            return;
        }
        if (contact.length() == 0) {
            Toast.makeText(this, "联系人手机号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (!contact.matches("^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$")) {
            /*
            使用正则表达式验证手机号码格式
            */
            Toast.makeText(this, "订单联系电话格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        if (locaion.length() == 0) {
            Toast.makeText(this, "约定地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = orderBusiness.createNewOrder(loginUser, Integer.valueOf(expectedSize), groupName, contact, locaion, selectedSportsType);
        if (result) {
            Toast.makeText(this, "组团邀约发起成功，请耐心等待小伙伴吧！", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "发生未知错误！", Toast.LENGTH_SHORT).show();
        }
    }
}
