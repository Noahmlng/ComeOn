package com.comeon.android;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.comeon.android.util.Activity_Parent;
import com.comeon.android.util.ViewUtil;

public class LoginActivity extends Activity_Parent implements View.OnClickListener{

    private ImageButton btn_wechat_login;
    private ImageButton btn_qq_login;
    private Button btn_login;
    private EditText editText_userName;
    private EditText editText_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //修改当前的状态栏颜色和字体颜色
        ViewUtil.setStatusBarColor(this, Color.rgb(255,255,255),false);
        initControls();
    }

    /**
     * 初始化控件
     */
    private void initControls(){
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_wechat_login=(ImageButton) findViewById(R.id.btn_wechat_login);
        btn_qq_login=(ImageButton)findViewById(R.id.btn_qq_login);
        btn_login.setOnClickListener(this);
        btn_qq_login.setOnClickListener(this);
        btn_wechat_login.setOnClickListener(this);

        editText_password=(EditText)findViewById(R.id.login_password);
        editText_userName=(EditText)findViewById(R.id.login_userName);
    }

    /**
     * 处理点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                Intent intent=new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_wechat_login:
                break;
            case R.id.btn_qq_login:
                break;
        }
    }
}
