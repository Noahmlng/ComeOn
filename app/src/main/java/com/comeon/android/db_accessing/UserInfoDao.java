package com.comeon.android.db_accessing;

import com.comeon.android.db.UserInfo;

import java.util.List;

/**
 * 用户基本信息数据表接口
 */
public interface UserInfoDao {

    /**
     * 根据loginId查询对应的用户基本信息
     * @param loginId
     * @return  登录的用户基本信息对象 OR null
     */
    UserInfo selectUserByLoginId(long loginId);

    /**
     * 插入新的用户
     * @param loginId  用户的登录id
     * @param phone  用户的手机号
     * @return
     */
    UserInfo insertNewUser(long loginId, String phone);

    /**
     * 根据手机号码模糊查询用户
     * @param phone  输入的手机号码
     * @return  符合条件的用户列表 OR null
     */
    List<UserInfo> selectUsersByPhone(String phone);
}
