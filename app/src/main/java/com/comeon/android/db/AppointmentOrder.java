package com.comeon.android.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;
import java.util.List;

/**
 * 组团订单表
 */
public class AppointmentOrder extends LitePalSupport {

    private String orderName;
    @Column(defaultValue = "1")
    private int orderExpectedSize;
    private UserInfo orderSponsor;
    private List<UserInfo> orderParticipants;
    private StadiumInfo orderStadium;
    private Date orderLaunchTime;
    private Date orderAppointTime;
    private int orderStatus;
    private SportsType orderSportsType;
    private String orderLocation;

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
}
