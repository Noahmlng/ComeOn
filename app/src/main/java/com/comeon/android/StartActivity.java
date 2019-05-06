package com.comeon.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.comeon.android.util.Activity_Parent;
import com.comeon.android.util.ViewUtil;

public class StartActivity extends Activity_Parent implements View.OnClickListener{

    private ImageButton btn_login;
    private ImageButton btn_register;
    private ImageButton btn_visitor_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //隐藏当前活动的状态栏（全屏模式）
        ViewUtil.transparentBar(this);
        //调用初始化控件的方法
        initControls();
    }

    /**
     * 初始化控件
     */
    private void initControls(){
        btn_login=(ImageButton)findViewById(R.id.btn_login);
        btn_register=(ImageButton)findViewById(R.id.btn_register);
        btn_visitor_login=(ImageButton)findViewById(R.id.btn_visitor_login);

        //绑定点击事件
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_visitor_login.setOnClickListener(this);
    }

    /**
     * 处理整个页面的点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                //跳转至登录页面
                Intent login_intent=new Intent(this, LoginActivity.class);
                startActivity(login_intent);
                break;
            case R.id.btn_register:
                Intent register_intent=new Intent(this, RegistrationActivity.class);
                startActivity(register_intent);
                break;
            case R.id.btn_visitor_login:
                Intent entry=new Intent(this, MainActivity.class);
                startActivity(entry);
//                Toast.makeText(this, "暂不支持访客登录",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
