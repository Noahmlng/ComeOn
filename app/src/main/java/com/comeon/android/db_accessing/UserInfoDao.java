package com.comeon.android.db_accessing;

import com.comeon.android.db.UserInfo;

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

    UserInfo insertNewUser(long loginId, String phone);
}
