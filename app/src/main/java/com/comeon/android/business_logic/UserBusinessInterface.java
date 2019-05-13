package com.comeon.android.business_logic;

import com.comeon.android.db.SportsType;
import com.comeon.android.db.UserInfo;
import com.comeon.android.db.UserLogin;

import java.util.List;

/**
 * 用户相关业务接口
 */
public interface UserBusinessInterface {

    /**
     * 用户登录的方法
     * @param phone 登录用的手机号
     * @param password   登录密码
     * @return  登录成功的用户 OR null
     */
    UserInfo login(String phone, String password);

    /**
     * 查看手机是否已被注册
     * @param phone  待验证的手机号
     * @return 验证结果
     */
    boolean checkPhoneExist(String phone);

    /**
     * 注册登录用户
     * @param phone  手机号
     * @param password  密码
     * @return  登录用户 OR null
     */
    UserInfo registration(String phone, String password);


}
