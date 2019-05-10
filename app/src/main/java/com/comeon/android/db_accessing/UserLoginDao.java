package com.comeon.android.db_accessing;

import com.comeon.android.db.UserLogin;

import java.nio.file.attribute.UserPrincipalLookupService;

/**
 * 用户登录信息数据表接口
 */
public interface UserLoginDao {

    /**
     * 新增User数据
     * @param newUser  装载数据的对象
     * @return  新增的user对象
     */
    UserLogin insertNewUser(UserLogin newUser);

    /**
     * 根据手机号，密码查询的方法
     * @param loginUser  装载数据的对象
     * @return  登录成功的用户登录id OR -1
     */
    long selectUser(UserLogin loginUser);

    /**
     * 查询手机号是否存在
     * @param registerUser  装载数据的注册用户对象
     * @return  符合条件的数据个数
     */
    int checkExistOnPhone(UserLogin registerUser);

}
