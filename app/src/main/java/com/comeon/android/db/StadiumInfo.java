package com.comeon.android.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.location.Address;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * 场馆信息表
 */
public class StadiumInfo extends LitePalSupport implements Parcelable {

    private long id;
    private float avgConsumption;
    @Column(nullable = false)
    private String stadiumName;
    @Column(nullable = false)
    private String stadiumContact;
    private String stadiumDescription;
    private SportsType sportsType;  //后该做id
    private long sportsTypeId;

    private String province;
    private String city;
    private String district;
    private String street;
    private String streetNumber;
    @Column(defaultValue = "0")
    private int stadiumStatus;

    private double longitude; //代表位置的经度
    private double latitude; //代表位置的纬度

    private List<AppointmentOrder> orders;

    @Column(ignore = true)
    private byte[] stadiumIcon;

    public StadiumInfo(){}


    protected StadiumInfo(Parcel in) {
        id = in.readLong();
        avgConsumption = in.readFloat();
        stadiumName = in.readString();
        stadiumContact = in.readString();
        stadiumDescription = in.readString();
        sportsType = in.readParcelable(SportsType.class.getClassLoader());
        sportsTypeId = in.readLong();
        province = in.readString();
        city = in.readString();
        district = in.readString();
        street = in.readString();
        streetNumber = in.readString();
        stadiumStatus = in.readInt();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeFloat(avgConsumption);
        dest.writeString(stadiumName);
        dest.writeString(stadiumContact);
        dest.writeString(stadiumDescription);
        dest.writeParcelable(sportsType, flags);
        dest.writeLong(sportsTypeId);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(district);
        dest.writeString(street);
        dest.writeString(streetNumber);
        dest.writeInt(stadiumStatus);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StadiumInfo> CREATOR = new Creator<StadiumInfo>() {
        @Override
        public StadiumInfo createFromParcel(Parcel in) {
            return new StadiumInfo(in);
        }

        @Override
        public StadiumInfo[] newArray(int size) {
            return new StadiumInfo[size];
        }
    };

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

    public int getStadiumStatus() {
        return stadiumStatus;
    }

    public void setStadiumStatus(int stadiumStatus) {
        this.stadiumStatus = stadiumStatus;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getSportsTypeId() {
        return sportsTypeId;
    }

    public void setSportsTypeId(long sportsTypeId) {
        this.sportsTypeId = sportsTypeId;
    }
}
