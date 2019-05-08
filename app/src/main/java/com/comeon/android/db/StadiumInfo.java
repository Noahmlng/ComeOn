package com.comeon.android.db;

import com.baidu.location.Address;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * 场馆信息表
 */
public class StadiumInfo extends LitePalSupport {

    private long id;
    @Column(nullable = false)
    private String stadium_name;
    @Column(nullable = false)
    private String stadium_contact;
    private byte[] stadium_icon;
    private String stadium_description;
    @Column(defaultValue = "0")
    private double avg_consumption;
    private SportsType stadium_type;

    @Column(ignore = true)
    private byte stadium_status;

    private Location location;

    public SportsType getStadium_type() {
        return stadium_type;
    }

    public void setStadium_type(SportsType stadium_type) {
        this.stadium_type = stadium_type;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getAvg_consumption() {
        return avg_consumption;
    }

    public void setAvg_consumption(double avg_consumption) {
        this.avg_consumption = avg_consumption;
    }


    public long getId() {
        return id;
    }

    public String getStadium_name() {
        return stadium_name;
    }

    public void setStadium_name(String stadium_name) {
        this.stadium_name = stadium_name;
    }

    public String getStadium_contact() {
        return stadium_contact;
    }

    public void setStadium_contact(String stadium_contact) {
        this.stadium_contact = stadium_contact;
    }

    public byte[] getStadium_icon() {
        return stadium_icon;
    }

    public void setStadium_icon(byte[] stadium_icon) {
        this.stadium_icon = stadium_icon;
    }

    public String getStadium_description() {
        return stadium_description;
    }

    public void setStadium_description(String stadium_description) {
        this.stadium_description = stadium_description;
    }
}
