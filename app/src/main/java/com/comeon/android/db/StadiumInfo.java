package com.comeon.android.db;

import com.baidu.location.Address;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * 场馆信息表
 */
public class StadiumInfo extends LitePalSupport {

    private long id;
    private float avgConsumption;
    @Column(nullable = false)
    private String stadiumName;
    @Column(nullable = false)
    private String stadiumContact;
    private String stadiumDescription;
    private SportsType sportsType;
    private String province;
    private String city;
    private String district;
    private String street;
    private String streetNumber;

    private List<AppointmentOrder> orders;

    @Column(ignore = true)
    private byte[] stadiumIcon;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    public String getStadiumContact() {
        return stadiumContact;
    }

    public void setStadiumContact(String stadiumContact) {
        this.stadiumContact = stadiumContact;
    }

    public String getStadiumDescription() {
        return stadiumDescription;
    }

    public void setStadiumDescription(String stadiumDescription) {
        this.stadiumDescription = stadiumDescription;
    }

    public float getAvgConsumption() {
        return avgConsumption;
    }

    public void setAvgConsumption(float avgConsumption) {
        this.avgConsumption = avgConsumption;
    }

    public SportsType getSportsType() {
        return sportsType;
    }

    public void setSportsType(SportsType sportsType) {
        this.sportsType = sportsType;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public List<AppointmentOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<AppointmentOrder> orders) {
        this.orders = orders;
    }
}
