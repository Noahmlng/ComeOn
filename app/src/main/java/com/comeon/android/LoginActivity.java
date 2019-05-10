package com.comeon.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.comeon.android.business_logic.UserBusiness;
import com.comeon.android.business_logic.UserBusinessInterface;
import com.comeon.android.db.UserInfo;
import com.comeon.android.db.UserLogin;
import com.comeon.android.util.Activity_Parent;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.ViewUtil;

public class LoginActivity extends Activity_Parent implements View.OnClickListener{

    private static final String TAG = "LoginActivity";

    private ImageButton btn_wechat_login;
    private ImageButton btn_qq_login;
    private Button btn_login;
    private EditText editText_userPhone;
    private EditText editText_password;

    private UserBusinessInterface userBusiness=new UserBusiness();
    private UserInfo loginedUserInfo;

    public static final int FAULT_PHONENULL=0;
    public static final int FAULT_PASSWORDNULL=1;
    public static final int FAULT_INVALID=2;
    public static final int SUCCESS=3;

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
        editText_userPhone=(EditText)findViewById(R.id.login_userPhone);
    }

    /**
     * 处理点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                switch (login()){
                    case FAULT_PHONENULL:
                        Toast.makeText(this,"手机号不能为空",Toast.LENGTH_SHORT).show();
                        break;
                    case FAULT_PASSWORDNULL:
                        Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
                        break;
                    case FAULT_INVALID:
                        Toast.makeText(this,"手机号或密码输入错误，登录失败",Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        Intent intent=new Intent(this, MainActivity.class);
                        //传递已登录的用户数据
                        intent.putExtra("login_user",loginedUserInfo);
                        startActivity(intent);
                        break;
                }
                break;
            case R.id.btn_wechat_login:
                Toast.makeText(this, "暂不支持微信登录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_qq_login:
                Toast.makeText(this, "暂不支持QQ登录",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 登录验证（非空验证+数据库验证）
     */
    private int login(){
        String phone=editText_userPhone.getText().toString();
        String pwd=editText_password.getText().toString();
        if(phone.length()==0){
            return FAULT_PHONENULL;
        }else if(pwd.length()==0){
            return FAULT_PASSWORDNULL;
        }else{
            UserLogin loginUser=new UserLogin();
            loginUser.setUserPhone(phone);
            loginUser.setUserPassword(pwd);

            //开始验证
            ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("登录验证");
            progressDialog.setMessage("请稍等一会......");
            progressDialog.setCancelable(false);
            progressDialog.show();

            UserInfo loginedUser=null;
            //模拟延时
            try{
                Thread.sleep(500);
                progressDialog.dismiss();
            }catch (InterruptedException ex){
                LogUtil.e(TAG, ex.getMessage());
            }
            loginedUser=userBusiness.login(loginUser);
            if(loginedUser!=null){
                loginedUserInfo=loginedUser;
                return SUCCESS;
            }else{
               return FAULT_INVALID;
            }
        }
    }
}
