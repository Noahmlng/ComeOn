package com.comeon.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.comeon.android.business_logic.UserBusiness;
import com.comeon.android.business_logic.UserBusinessInterface;
import com.comeon.android.controls.GradientTextButton;
import com.comeon.android.db.UserInfo;
import com.comeon.android.db.UserLogin;
import com.comeon.android.fragment.SettingDistanceFragment;
import com.comeon.android.fragment.SettingHeadIconFragment;
import com.comeon.android.fragment.SettingPasswordFragment;
import com.comeon.android.fragment.SettingPersonalInfoFragment;
import com.comeon.android.fragment.SettingPhoneFragment;
import com.comeon.android.util.Activity_Parent;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.Utilities;
import com.comeon.android.util.ViewUtil;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.internal.Util;

public class RegistrationActivity extends Activity_Parent implements View.OnClickListener {
    private static final String TAG = "RegistrationActivity";

    private UserBusinessInterface userBusiness=new UserBusiness();

    public FragmentManager fragmentManager = this.getSupportFragmentManager();

    SettingPersonalInfoFragment settingPersonalInfoFragment = new SettingPersonalInfoFragment();
    SettingPhoneFragment settingPhoneFragment = new SettingPhoneFragment();
    SettingPasswordFragment settingPasswordFragment = new SettingPasswordFragment();
    SettingHeadIconFragment settingHeadIconFragment = new SettingHeadIconFragment();
    SettingDistanceFragment settingDistanceFragment = new SettingDistanceFragment();

    private UserLogin registerdUser;
    private UserInfo newUser;
    private byte[] imgBytes;

    public UserInfo getNewUser() {
        return newUser;
    }

    /*
            标注错误类型
         */
    public static final int FAULT_PHONENULL = 0;
    public static final int FAULT_VALIDATECODENULL = 1;
    public static final int FAULT_PHONEPATTERN = 2;
    public static final int FAULT_INVALIDATECODE = 3;
    public static final int FAULT_DUPLICATEPHONEREGISTRATION = 4;
    public static final int FAULT_UNKNOWN = 5;
    public static final int SUCCESS = 6;

    Button btn_next_step;
    TextView txt_tip;

    //记录当前页是哪一个fragment
    Fragment currentFragment;

    //手机号与验证码集合
    private Map<String, String> validateCodes = new HashMap<String, String>();

    /**
     * 提供对外添加手机验证码的接口
     *
     * @param phone        手机号
     * @param validateCode 验证码
     */
    public void addNewPhoneValidateCodes(String phone, String validateCode) {
        validateCodes.put(phone, validateCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //修改状态栏颜色和字体颜色：浅色模式
        ViewUtil.setStatusBarColor(this, Color.rgb(255, 255, 255), false);
        initControls();
        //注册碎片返回栈监听器
        fragmentManager.addOnBackStackChangedListener(new RegistrationActivity.fragmentBackStackListener());
        replaceFragment(settingPhoneFragment);
    }

    /**
     * 因为需要记录当前页的情况，所以需要加上对类成员的赋值
     *
     * @param fragment 要改变为的碎片
     */
    private void replaceFragment(Fragment fragment) {
        Utilities.replaceFragment(fragmentManager, fragment, R.id.fragment_layout);
        currentFragment = fragment;
    }

    /**
     * 初始化控件
     */
    private void initControls() {
        btn_next_step = (Button) findViewById(R.id.btn_next_step);
        txt_tip = (TextView) findViewById(R.id.txt_tip);
        //为按钮绑定点击事件
        btn_next_step.setOnClickListener(this);
    }

    /**
     * 返回键的操作事件
     */
    @Override
    public void onBackPressed() {
        if (currentFragment instanceof SettingPhoneFragment) {
            finish();
        } else if (currentFragment instanceof SettingPasswordFragment) {
            txt_tip.setText("请设置个人信息");
            btn_next_step.setText("下一步");
            replaceFragment(settingPhoneFragment);
        } else if (currentFragment instanceof SettingPersonalInfoFragment) {
            finish();
        } else if (currentFragment instanceof SettingDistanceFragment) {
            txt_tip.setText("请设置个人信息");
            btn_next_step.setText("下一步");
            replaceFragment(settingPersonalInfoFragment);
        } else if (currentFragment instanceof SettingHeadIconFragment) {
            txt_tip.setText("请输入可接受的约球距离");
            btn_next_step.setText("下一步");
            replaceFragment(settingDistanceFragment);
        }
    }

    /**
     * 处理点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next_step:
                /*
                    切换到下一个碎片，如果是头像碎片了就跳转到MainActivity
                    改变按钮的文字
                    同时改变上方的文字
                 */
                if (currentFragment instanceof SettingPhoneFragment) {
                    /*
                        手机页执行的操作
                     */
                    switch (checkPhone()) {
                        case FAULT_PHONENULL:
                            Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        case FAULT_PHONEPATTERN:
                            /*
                                非必需（获取验证码时已经进行了判断）
                             */
                            Toast.makeText(this, "手机号码格式错误", Toast.LENGTH_SHORT).show();
                            return;
                        case FAULT_VALIDATECODENULL:
                            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        case FAULT_INVALIDATECODE:
                            Toast.makeText(this, "验证码不匹配", Toast.LENGTH_SHORT).show();
                            return;
                        case FAULT_UNKNOWN:
                            Toast.makeText(this, "出现未知错误", Toast.LENGTH_SHORT).show();
                            return;
                        case SUCCESS:
                            txt_tip.setText("请设置登录密码");
                            btn_next_step.setText("注册");
                            //实例化注册的用户，并赋电话
                            registerdUser=new UserLogin();
                            registerdUser.setUserPhone(settingPhoneFragment.editText_phone.getText().toString().trim());
                            replaceFragment(settingPasswordFragment);
                            break;
                    }
                } else if (currentFragment instanceof SettingPasswordFragment) {
                    /*
                        密码页执行的注册操作：因为传递过来的用户手机号已经验证成功，此处验证密码，验证成功则新增登录用户
                     */
                    if(checkPassword()){
                        String phone=registerdUser.getUserPhone();
                        String password=settingPasswordFragment.editText_password.getText().toString().trim();
                        final UserInfo newUser=userBusiness.registration(phone, password);
                        this.newUser=newUser;
                        if(newUser!=null){
                            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                            //记住登陆数据(UserLogin)
                            writeInLoginDataInSharePreferences(phone, password);
                            LogUtil.d(TAG, "正在写入数据.....（手机号："+phone+"；密码："+password+"）");
                            /*
                                提示用户注册成功！
                                询问用户是否需要更个性化的服务
                             */
                            //通过AlertDialog.Builder创建一个实例
                            AlertDialog.Builder dialog=new AlertDialog.Builder(this);

                            //设置标题，内容，是否可以取消等等一系列的属性
                            dialog.setTitle("个性化服务设置");
                            dialog.setMessage("让我们更了解你吧！");
                            dialog.setCancelable(false);

                            //为对话框设置点击事件
                            dialog.setPositiveButton("没问题啊", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    txt_tip.setText("请设置个人信息");
                                    btn_next_step.setText("下一步");
                                    replaceFragment(settingPersonalInfoFragment);
                                }
                            });

                            dialog.setNegativeButton("残忍拒绝", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                    //直接跳转至主页面
                                    Intent intent=new Intent(RegistrationActivity.this, MainActivity.class);
                                    //传递已登录的用户数据
                                    intent.putExtra("login_user",newUser);
                                    startActivity(intent);
                                }
                            });
                            //show()的方法显示对话框
                            dialog.show();
                        }else{
                            Toast.makeText(this, "出现未知错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (currentFragment instanceof SettingPersonalInfoFragment) {
                    String nickName=settingPersonalInfoFragment.editText_nickname.getText().toString().trim();
                    nickName=(nickName.length()==0)?"小明":nickName;
                    int birthdayYear=0;
                    int birthdayMonth=0;
                    int birthdayDay=0;
                    try{
                        birthdayYear=(int)settingPersonalInfoFragment.birthday_year.getSelectedItem();
                        birthdayMonth=(int)settingPersonalInfoFragment.birthday_month.getSelectedItem();
                        birthdayDay=(int)settingPersonalInfoFragment.birthday_day.getSelectedItem();
                    }catch (Exception ex){
                        LogUtil.e(TAG, ex.getMessage());
                    }
                    LogUtil.d(TAG,"输入的生日为"+birthdayYear+"."+birthdayMonth+"."+birthdayDay);

                    int sex=0;
                    if(settingPersonalInfoFragment.sex_male.isChecked()){
                        sex=0;
                    }else if(settingPersonalInfoFragment.sex_female.isChecked()){
                        sex=1;
                    }

                    //加入数据
                    newUser.setUserNickName(nickName);
                    newUser.setUserBirthday(new Date(birthdayYear, birthdayMonth, birthdayDay));
                    newUser.setUserGender(sex);
                    //阶段性的更新（防止用户使用跳过按钮，导致数据丢失）
                    newUser.save();
                    txt_tip.setText("请输入可接受的约球距离");
                    btn_next_step.setText("下一步");
                    replaceFragment(settingDistanceFragment);
                } else if (currentFragment instanceof SettingDistanceFragment) {
                    txt_tip.setText("请设置头像");
                    btn_next_step.setText("开始吧！");
                    replaceFragment(settingHeadIconFragment);
                } else if (currentFragment instanceof SettingHeadIconFragment) {
                    if(imgBytes!=null){
                        newUser.setHeadIcon(imgBytes);
                    }
                    newUser.save();
                    finish();
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    //传递已登录的用户数据
                    intent.putExtra("login_user",newUser);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 内部类监听自定义碎片返回栈
     */
    public class fragmentBackStackListener implements FragmentManager.OnBackStackChangedListener {
        @Override
        public void onBackStackChanged() {
            /*
            当返回栈中没有碎片时，则关闭活动
             */
            if (fragmentManager.getBackStackEntryCount() == 0) {
                finish();
            }
        }
    }

    /**
     * 验证手机号码的方法
     * @return
     */
    private int checkPhone() {
        try {
            String phone = settingPhoneFragment.editText_phone.getText().toString().trim();
            String validateCode = settingPhoneFragment.editText_validateCode.getText().toString().trim();
        /*
            先进行非空验证，之后使用正则表示式进行验证
         */
            if (phone.length() == 0) {
                return FAULT_PHONENULL;
            } else if (!phone.matches("^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$")) {
            /*
                使用正则表达式验证手机号码格式
            */
                return FAULT_PHONEPATTERN;
            } else if (validateCode.length() == 0) {
                return FAULT_VALIDATECODENULL;
            } else if (!isValidateCodeMatch()) {
            /*
                是否匹配
             */
                return FAULT_INVALIDATECODE;
            } else {
            /*
                进行业务新增用户的功能
             */
                return SUCCESS;
            }
        } catch (Exception ex) {
            LogUtil.e(TAG, ex.getMessage());
            return FAULT_UNKNOWN;
        }
    }


    /**
     * 验证密码的方法
     * @return
     */
    private boolean checkPassword(){
        String pwd=settingPasswordFragment.editText_password.getText().toString().trim();
        String checkPwd=settingPasswordFragment.editText_checkPassword.getText().toString().trim();
        /*
            先进行非空验证
         */
        if(pwd.length()==0){
            Toast.makeText(this, "密码框不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else if(checkPwd.length()==0){
            Toast.makeText(this, "确认密码框不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        /*
            使用正则表达式判断密码框的输入规范
         */
        else if(!pwd.matches("^(\\w){6,18}$")){
            Toast.makeText(this, "密码输入的格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }
        /*
            密码和确认密码的一致性判断
         */
        else if(!pwd.equals(checkPwd)){
            Toast.makeText(this, "两次输入的密码不匹配", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }


    /**
     * 判断当前验证码和手机号是否匹配
     *
     * @return 返回匹配结果
     */
    public boolean isValidateCodeMatch() {
        String phone = settingPhoneFragment.editText_phone.getText().toString();
        String validateCode = settingPhoneFragment.editText_validateCode.getText().toString();
        //遍历查询
        LogUtil.d(TAG, "验证码为："+validateCode+"；当前集合的大小为："+validateCodes.size());
        for (Map.Entry<String, String> entry :
                validateCodes.entrySet()) {
            if (entry.getKey().equals(phone) && entry.getValue().equals(validateCode)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 返回结果的回调方法
     * @param requestCode   请求码
     * @param resultCode   返回的结果
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case SettingHeadIconFragment.TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    try{
                        //通过拍摄图片的Uri获取流对象，并对流对象进行解码
                        Bitmap bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(settingHeadIconFragment.getNewImageUri()));
                        settingHeadIconFragment.head_icon.setImageBitmap(bitmap);
//                        imgBytes=Utilities.translateBitmapToBytes(bitmap);
                    }catch(FileNotFoundException ex){
                        ex.printStackTrace();
                    }
                }
                break;

                case SettingHeadIconFragment.PICK_PHOTO:
                    if(resultCode==RESULT_OK){
                        //判断执行当前应用程序的手机安卓版本
                        if(Build.VERSION.SDK_INT >= 19){
                            //4.4以上的系统使用这个方法处理图片
                            String imagePath=settingHeadIconFragment.handleImageOnKitKat(data);
//                            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
//                            settingHeadIconFragment.head_icon.setImageBitmap(bitmap);
//                            imgBytes=Utilities.translateBitmapToBytes(BitmapFactory.decodeFile(imagePath));
                            Glide.with(this).load(imagePath).into(settingHeadIconFragment.head_icon);
                        }else{
                            //4.4以下的系统使用这个方法处理图片
                            String imagePath=settingHeadIconFragment.handleImageBeforeKitKat(data);
//                            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
//                            settingHeadIconFragment.head_icon.setImageBitmap(bitmap);
//                            imgBytes=Utilities.translateBitmapToBytes(BitmapFactory.decodeFile(imagePath));
                            Glide.with(this).load(imagePath).into(settingHeadIconFragment.head_icon);
                        }
                    }
                    break;

            default:
                break;
        }
    }

    /**
     * 记住用户功能——将登陆数据写进SharedPreferences文件中
     * @param phone  登陆用的手机号
     * @param pwd    登陆密码
     */
    private void writeInLoginDataInSharePreferences(String phone, String pwd){
        SharedPreferences sp=getSharedPreferences("loginData",MODE_PRIVATE);
        /*
            写入操作
         */
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("phone",phone);
        editor.putString("password",pwd);
        editor.apply();
    }
}

