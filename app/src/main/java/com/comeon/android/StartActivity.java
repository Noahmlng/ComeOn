package com.comeon.android;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.entity.pb.WalkPlan;
import com.comeon.android.business_logic.UserBusiness;
import com.comeon.android.business_logic.UserBusinessInterface;
import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db.Friends;
import com.comeon.android.db.Message;
import com.comeon.android.db.SportsType;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.db.UserInfo;
import com.comeon.android.db.UserLogin;
import com.comeon.android.db.UserLoginLog;
import com.comeon.android.service.LoadingService;
import com.comeon.android.util.Activity_Parent;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.Utilities;
import com.comeon.android.util.ViewUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StartActivity extends Activity_Parent implements View.OnClickListener {

    private static final String TAG = "StartActivity";

    private Button btn_login;
    private Button btn_register;
    private Button btn_visitor_login;

    //LoadingService中的绑定器对象
    private LoadingService.LoadingBinder loadingBinder;

    /**
     * 建立与服务的连接（活动和服务进行交互）
     */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            loadingBinder = (LoadingService.LoadingBinder) service;
            if (!checkInitDataExist()) {
                loadSportsType();
                //开始执行实例数据的加载
                loadingBinder.startLoading();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //修改当前状态栏的颜色和字体颜色
        ViewUtil.setStatusBarColor(this, Color.rgb(77, 77, 77), true);
        //调用初始化控件的方法
        initControls();
        //绑定并开启服务
        Intent loadingService = new Intent(StartActivity.this, LoadingService.class);
        startService(loadingService);
        bindService(loadingService, connection, BIND_AUTO_CREATE);
        if (ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        UserLogin userLogin=loadLoginDataInSharedPreferences();
        LogUtil.d(TAG, "记住用户的手机号为："+userLogin.getUserPhone()+"；密码为："+userLogin.getUserPassword());

        UserBusinessInterface userBusiness=new UserBusiness();
        /*
            使用SharedPreferences文件中登录对象尝试登录
         */
        UserInfo loginUser=userBusiness.login(userLogin.getUserPhone(),userLogin.getUserPassword());
        if(loginUser!=null){
            LogUtil.d(TAG, "记住用户的昵称为："+loginUser.getUserNickName());
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            //传递已登录的用户数据
            intent.putExtra("login_user",loginUser);
            startActivity(intent);
        }
    }

    /**
     * 初始化控件
     */
    private void initControls() {
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_visitor_login = (Button) findViewById(R.id.btn_visitor_login);
        Drawable arrow = btn_visitor_login.getCompoundDrawables()[3];
        arrow.setBounds(0, 0, 40, 20);
        btn_visitor_login.setCompoundDrawables(null, null, null, arrow);

        //绑定点击事件
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_visitor_login.setOnClickListener(this);
    }

    /**
     * 处理整个页面的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                //跳转至登录页面
                Intent login_intent = new Intent(this, LoginActivity.class);
                startActivity(login_intent);
                break;
            case R.id.btn_register:
                Intent register_intent = new Intent(this, RegistrationActivity.class);
                startActivity(register_intent);
                break;
            case R.id.btn_visitor_login:
                Intent entry = new Intent(this, MainActivity.class);
                startActivity(entry);
                //                Toast.makeText(this, "暂不支持访客登录",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "拒绝读写权限将无法加载初始数据",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    /**
     * 加载运动类型初始数据
     */
    private void loadSportsType() {
        List<SportsType> sportsTypes = new ArrayList<SportsType>();
        sportsTypes.add(new SportsType("足球"));
        sportsTypes.add(new SportsType("篮球"));
        sportsTypes.add(new SportsType("排球"));
        sportsTypes.add(new SportsType("手球"));
        sportsTypes.add(new SportsType("棒球"));
        sportsTypes.add(new SportsType("乒乓球"));
        sportsTypes.add(new SportsType("羽毛球"));
        sportsTypes.add(new SportsType("网球"));
        sportsTypes.add(new SportsType("高尔夫球"));
        sportsTypes.add(new SportsType("跑步"));
        sportsTypes.add(new SportsType("健身"));
        sportsTypes.add(new SportsType("瑜伽"));
        LitePal.saveAll(sportsTypes);
    }


    /**
     * 检查初始数据是否存在
     *
     * @return true：存在；false：不存在
     */
    private boolean checkInitDataExist() {
        SQLiteDatabase db = LitePal.getDatabase();
        int count = LitePal.count("UserInfo");
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 加载SharedPreferences文件中的登录数据
     * @return  SharedPreferences文件中的登录对象
     */
    private UserLogin loadLoginDataInSharedPreferences(){
        SharedPreferences loginData=getSharedPreferences("loginData",MODE_PRIVATE);

        String phone=loginData.getString("phone","");
        String password=loginData.getString("password","");

        UserLogin loginUser=new UserLogin();
        loginUser.setUserPhone(phone);
        loginUser.setUserPassword(password);
        return loginUser;
    }
}
