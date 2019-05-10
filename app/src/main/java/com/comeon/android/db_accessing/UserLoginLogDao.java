package com.comeon.android.db_accessing;

/**
 * 用户登录记录数据库接口
 */
public interface UserLoginLogDao {

    /**
     * 添加登录记录
     * @param loginUserId  登录用户的Id
     */
    void insertNewLog(long loginUserId);
}
