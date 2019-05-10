package com.comeon.android.business_logic;

import com.comeon.android.db.UserInfo;
import com.comeon.android.db.UserLogin;

/**
 * 用户相关业务接口
 */
public interface UserBusinessInterface {

    /**
     * 用户登录的方法
     * @param loginUser  装载登录数据的对象
     * @return  登录成功的用户 OR null
     */
    UserInfo login(UserLogin loginUser);

}
