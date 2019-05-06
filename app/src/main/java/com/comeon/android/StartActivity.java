package com.comeon.android;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.comeon.android.util.Activity_Parent;
import com.comeon.android.util.ViewUtil;

public class StartActivity extends Activity_Parent implements View.OnClickListener{

    private Button btn_login;
    private Button btn_register;
    private Button btn_visitor_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //修改当前状态栏的颜色和字体颜色
        ViewUtil.setStatusBarColor(this,Color.rgb(77,77,77),true);
        //调用初始化控件的方法
        initControls();
    }

    /**
     * 初始化控件
     */
    private void initControls(){
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_register=(Button)findViewById(R.id.btn_register);
        btn_visitor_login=(Button) findViewById(R.id.btn_visitor_login);
        Drawable arrow=btn_visitor_login.getCompoundDrawables()[3];
        arrow.setBounds(0, 0, 40, 20);
        btn_visitor_login.setCompoundDrawables(null, null, null, arrow);

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
