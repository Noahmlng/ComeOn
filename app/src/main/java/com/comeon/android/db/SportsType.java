package com.comeon.android.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * 运动类型表
 */
public class SportsType extends LitePalSupport implements Parcelable {

    public SportsType(){}
    public SportsType(String typeName, Category category){
        this.typeName=typeName;
        this.category=category;
    }

    private long id;

    @Column(nullable = false)
    private String typeName;

    //迭代开发：运动归类
    private Category category;

    private List<StadiumInfo> stadiumInfos;
    private List<AppointmentOrder> ordersType;


    protected SportsType(Parcel in) {
        id = in.readLong();
        typeName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(typeName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SportsType> CREATOR = new Creator<SportsType>() {
        @Override
        public SportsType createFromParcel(Parcel in) {
            return new SportsType(in);
        }

        @Override
        public SportsType[] newArray(int size) {
            return new SportsType[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<AppointmentOrder> getOrdersType() {
        return ordersType;
    }

    public void setOrdersType(List<AppointmentOrder> ordersType) {
        this.ordersType = ordersType;
    }

    public List<StadiumInfo> getStadiumInfos() {
        return stadiumInfos;
    }

    public void setStadiumInfos(List<StadiumInfo> stadiumInfos) {
        this.stadiumInfos = stadiumInfos;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
