package com.comeon.android.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * 用户登录表
 */
public class UserLogin extends LitePalSupport {

    private long id;

    public void setId(long id) {
        this.id = id;
    }

    private String user_name;

    @Column(nullable = false)
    private String user_phone;

    private String user_password;
    @Column(defaultValue = "0")
    private byte user_status;
    private Date last_modified_time;

    public long getId() {
        return id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public byte getUser_status() {
        return user_status;
    }

    public void setUser_status(byte user_status) {
        this.user_status = user_status;
    }

    public Date getLast_modified_time() {
        return last_modified_time;
    }

    public void setLast_modified_time(Date last_modified_time) {
        this.last_modified_time = last_modified_time;
    }
}
