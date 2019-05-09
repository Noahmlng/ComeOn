package com.comeon.android.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;
import java.util.List;

/**
 * 用户基本信息表
 */
public class UserInfo extends LitePalSupport {

    private long id;
    @Column(nullable = false)
    private long userLoginId;

    @Column(nullable = false)
    private String userPhone;
    private String userNickName;
    private Date userBirthday;
    private int userGender;
    private byte[] headIcon;
    @Column(defaultValue = "这个人很无聊诶，什么都不说！")
    private String description;

    private float acceptedDistance;

    private Date lastModifiedTime;

    private List<AppointmentOrder> sponsoredOrder;

    private AppointmentOrder participatedOrder;
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

    public long getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(long userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public Date getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(Date userBirthday) {
        this.userBirthday = userBirthday;
    }

    public int getUserGender() {
        return userGender;
    }

    public void setUserGender(int userGender) {
        this.userGender = userGender;
    }

    public byte[] getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(byte[] headIcon) {
        this.headIcon = headIcon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getAcceptedDistance() {
        return acceptedDistance;
    }

    public void setAcceptedDistance(float acceptedDistance) {
        this.acceptedDistance = acceptedDistance;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public List<AppointmentOrder> getSponsoredOrder() {
        return sponsoredOrder;
    }

    public void setSponsoredOrder(List<AppointmentOrder> sponsoredOrder) {
        this.sponsoredOrder = sponsoredOrder;
    }

    public AppointmentOrder getParticipatedOrder() {
        return participatedOrder;
    }

    public void setParticipatedOrder(AppointmentOrder participatedOrder) {
        this.participatedOrder = participatedOrder;
    }
}
