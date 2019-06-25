package com.comeon.android.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.mapapi.model.LatLng;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;
import java.util.List;

/**
 * 组团订单表
 */
public class AppointmentOrder extends LitePalSupport implements Parcelable {

    private long id;
    private String orderName;
    @Column(defaultValue = "10")
    private int orderExpectedSize;
    private UserInfo orderSponsor; //之后要改为Id
    private long orderSponsorId;

    private List<UserInfo> orderParticipants;
    private StadiumInfo orderStadium;  //可能不需要

    private Date orderLaunchTime;
    private Date orderAppointTime;
    private int orderStatus;
    private SportsType orderSportsType; //之后要改为Id
    private long orderSportsTypeId;

    private String orderLocation;
    private String orderContact;


    private double longitude; //代表位置的经度
    private double latitude; //代表位置的纬度

    public AppointmentOrder() {
    }


    protected AppointmentOrder(Parcel in) {
        id = in.readLong();
        orderName = in.readString();
        orderExpectedSize = in.readInt();
        orderSponsor = in.readParcelable(UserInfo.class.getClassLoader());
        orderSponsorId = in.readLong();
        orderParticipants = in.createTypedArrayList(UserInfo.CREATOR);
        orderStadium = in.readParcelable(StadiumInfo.class.getClassLoader());
        orderStatus = in.readInt();
        orderSportsType = in.readParcelable(SportsType.class.getClassLoader());
        orderSportsTypeId = in.readLong();
        orderLocation = in.readString();
        orderContact = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(orderName);
        dest.writeInt(orderExpectedSize);
        dest.writeParcelable(orderSponsor, flags);
        dest.writeLong(orderSponsorId);
        dest.writeTypedList(orderParticipants);
        dest.writeParcelable(orderStadium, flags);
        dest.writeInt(orderStatus);
        dest.writeParcelable(orderSportsType, flags);
        dest.writeLong(orderSportsTypeId);
        dest.writeString(orderLocation);
        dest.writeString(orderContact);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AppointmentOrder> CREATOR = new Creator<AppointmentOrder>() {
        @Override
        public AppointmentOrder createFromParcel(Parcel in) {
            return new AppointmentOrder(in);
        }

        @Override
        public AppointmentOrder[] newArray(int size) {
            return new AppointmentOrder[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public int getOrderExpectedSize() {
        return orderExpectedSize;
    }

    public void setOrderExpectedSize(int orderExpectedSize) {
        this.orderExpectedSize = orderExpectedSize;
    }

    public UserInfo getOrderSponsor() {
        return orderSponsor;
    }

    public void setOrderSponsor(UserInfo orderSponsor) {
        this.orderSponsor = orderSponsor;
    }

    public List<UserInfo> getOrderParticipants() {
        return orderParticipants;
    }

    public void setOrderParticipants(List<UserInfo> orderParticipants) {
        this.orderParticipants = orderParticipants;
    }

    public StadiumInfo getOrderStadium() {
        return orderStadium;
    }

    public void setOrderStadium(StadiumInfo orderStadium) {
        this.orderStadium = orderStadium;
    }

    public Date getOrderLaunchTime() {
        return orderLaunchTime;
    }

    public void setOrderLaunchTime(Date orderLaunchTime) {
        this.orderLaunchTime = orderLaunchTime;
    }

    public Date getOrderAppointTime() {
        return orderAppointTime;
    }

    public void setOrderAppointTime(Date orderAppointTime) {
        this.orderAppointTime = orderAppointTime;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public SportsType getOrderSportsType() {
        return orderSportsType;
    }

    public void setOrderSportsType(SportsType orderSportsType) {
        this.orderSportsType = orderSportsType;
    }

    public String getOrderLocation() {
        return orderLocation;
    }

    public void setOrderLocation(String orderLocation) {
        this.orderLocation = orderLocation;
    }

    public String getOrderContact() {
        return orderContact;
    }

    public void setOrderContact(String orderContact) {
        this.orderContact = orderContact;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getOrderSponsorId() {
        return orderSponsorId;
    }

    public void setOrderSponsorId(long orderSponsorId) {
        this.orderSponsorId = orderSponsorId;
    }

    public long getOrderSportsTypeId() {
        return orderSportsTypeId;
    }

    public void setOrderSportsTypeId(long orderSportsTypeId) {
        this.orderSportsTypeId = orderSportsTypeId;
    }
}
