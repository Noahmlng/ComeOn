package com.comeon.android.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;
import java.util.List;

/**
 * 组团订单表
 */
public class AppointmentOrder extends LitePalSupport {

    private long id;
    private String order_name;
    @Column(defaultValue = "1")
    private int order_expected_size;
    private UserInfo order_sponsor;
    private List<UserInfo> order_participants;
    private StadiumInfo order_stadium;
    private Date order_launch_time;
    private Date order_appoint_time;
    private int order_status;
    private SportsType order_sports_type;

    private Location order_location;

    public void setId(long id) {
        this.id = id;
    }

    public SportsType getOrder_sports_type() {
        return order_sports_type;
    }

    public void setOrder_sports_type(SportsType order_sports_type) {
        this.order_sports_type = order_sports_type;
    }

    public Location getOrder_location() {
        return order_location;
    }

    public void setOrder_location(Location order_location) {
        this.order_location = order_location;
    }

    public long getId() {
        return id;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public int getOrder_expected_size() {
        return order_expected_size;
    }

    public void setOrder_expected_size(int order_expected_size) {
        this.order_expected_size = order_expected_size;
    }

    public UserInfo getOrder_sponsor() {
        return order_sponsor;
    }

    public void setOrder_sponsor(UserInfo order_sponsor) {
        this.order_sponsor = order_sponsor;
    }

    public List<UserInfo> getOrder_participants() {
        return order_participants;
    }

    public void setOrder_participants(List<UserInfo> order_participants) {
        this.order_participants = order_participants;
    }

    public StadiumInfo getOrder_stadium() {
        return order_stadium;
    }

    public void setOrder_stadium(StadiumInfo order_stadium) {
        this.order_stadium = order_stadium;
    }

    public Date getOrder_launch_time() {
        return order_launch_time;
    }

    public void setOrder_launch_time(Date order_launch_time) {
        this.order_launch_time = order_launch_time;
    }

    public Date getOrder_appoint_time() {
        return order_appoint_time;
    }

    public void setOrder_appoint_time(Date order_appoint_time) {
        this.order_appoint_time = order_appoint_time;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }
}
