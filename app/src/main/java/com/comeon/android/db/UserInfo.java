package com.comeon.android.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;
import java.util.List;

/**
 * 用户基本信息表
 */
public class UserInfo extends LitePalSupport implements Parcelable {

    private long id;
    @Column(nullable = false)
    private long userLoginId;

    @Column(nullable = false)
    private String userPhone;
    @Column(defaultValue = "小明")
    private String userNickName;
    private Date userBirthday;
    @Column(defaultValue = "0")
    private int userGender;
    private byte[] headIcon;
    @Column(defaultValue = "这个人很无聊诶，什么都不说！")
    private String description;
    private Date registerTime;

    private float acceptedDistance;

    private Date lastModifiedTime;

    private List<AppointmentOrder> sponsoredOrder;

    private List<AttendanceRecord> participatedOrder;

    /*
        以下为为迭代开发所提供的字段
     */
    @Column(ignore = true)
    private int identityCardType;
    @Column(ignore = true)
    private String identityCardNo;
    @Column(ignore = true)
    private int userPoint;
    @Column(ignore = true)
    private byte vipLevel;

    public UserInfo(){}

    protected UserInfo(Parcel in) {
        id = in.readLong();
        userLoginId = in.readLong();
        userPhone = in.readString();
        userNickName = in.readString();
        userGender = in.readInt();
        headIcon = in.createByteArray();
        description = in.readString();
        acceptedDistance = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(userLoginId);
        dest.writeString(userPhone);
        dest.writeString(userNickName);
        dest.writeInt(userGender);
        dest.writeByteArray(headIcon);
        dest.writeString(description);
        dest.writeFloat(acceptedDistance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

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

    public List<AttendanceRecord> getParticipatedOrder() {
        return participatedOrder;
    }

    public void setParticipatedOrder(List<AttendanceRecord> participatedOrder) {
        this.participatedOrder = participatedOrder;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }
}
