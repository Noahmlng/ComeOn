package com.comeon.android.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * 用户登录记录表
 */
public class UserLoginLog extends LitePalSupport {

    @Column(nullable = false)
    private long userId;

    private Date loginTime;
    @Column(defaultValue = "0")
    private int loginStatus;

    //用于记录登录的ip地址（迭代开发部分）
    @Column(ignore = true)
    private String loginIp;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }
}
