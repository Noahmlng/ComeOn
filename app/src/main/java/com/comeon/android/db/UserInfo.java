package com.comeon.android.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * 用户基本信息表
 */
public class UserInfo extends LitePalSupport {

    private long id;
    @Column(nullable = false)
    private long user_login_id;

    @Column(nullable = false)
    private String user_phone;
    private String user_nickname;
    private Date user_birthday;
    private int user_gender;
    private byte[] head_icon;
    @Column(defaultValue = "这个人很无聊诶，什么都不说！")
    private String description;

    private float acepted_distance;

    private Date last_modified_time;

    /*
        以下为为迭代开发所提供的字段
     */
    @Column(ignore = true)
    private byte identity_card_type;
    @Column(ignore = true)
    private String identity_card_no;
    @Column(ignore = true)
    private int user_point;
    @Column(ignore = true)
    private byte vip_level;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_login_id() {
        return user_login_id;
    }

    public void setUser_login_id(long user_login_id) {
        this.user_login_id = user_login_id;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public Date getUser_birthday() {
        return user_birthday;
    }

    public void setUser_birthday(Date user_birthday) {
        this.user_birthday = user_birthday;
    }

    public int getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(int user_gender) {
        this.user_gender = user_gender;
    }

    public byte[] getHead_icon() {
        return head_icon;
    }

    public void setHead_icon(byte[] head_icon) {
        this.head_icon = head_icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getAcepted_distance() {
        return acepted_distance;
    }

    public void setAcepted_distance(float acepted_distance) {
        this.acepted_distance = acepted_distance;
    }

    public Date getLast_modified_time() {
        return last_modified_time;
    }

    public void setLast_modified_time(Date last_modified_time) {
        this.last_modified_time = last_modified_time;
    }
}
