package com.comeon.android.db_accessing;

import com.comeon.android.db.UserLoginLog;

import java.util.Date;

/**
 * 用户登录记录数据库接口实现类
 */
public class UserLoginLogDaoImpl implements UserLoginLogDao {
    @Override
    public void insertNewLog(long loginUserId) {
        UserLoginLog newLog=new UserLoginLog();
        newLog.setUserId(loginUserId);
        newLog.setLoginStatus(0);
        newLog.setLoginTime(new Date());
    }
}
