package com.comeon.android.db;

import android.os.Parcel;
import android.os.Parcelable;

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
    private UserInfo orderSponsor;
    private List<UserInfo> orderParticipants;
    private StadiumInfo orderStadium;
    private Date orderLaunchTime;
    private Date orderAppointTime;
    private int orderStatus;
    private SportsType orderSportsType;
    private String orderLocation;
    private String orderContact;

    public AppointmentOrder() {
    }

    protected AppointmentOrder(Parcel in) {
        orderName = in.readString();
        orderExpectedSize = in.readInt();
        orderSponsor = in.readParcelable(UserInfo.class.getClassLoader());
        orderParticipants = in.createTypedArrayList(UserInfo.CREATOR);
        orderStatus = in.readInt();
        orderLocation = in.readString();
        orderContact = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderName);
        dest.writeInt(orderExpectedSize);
        dest.writeParcelable(orderSponsor, flags);
        dest.writeTypedList(orderParticipants);
        dest.writeInt(orderStatus);
        dest.writeString(orderLocation);
        dest.writeString(orderContact);
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
}
