package com.comeon.android.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * 运动类型表
 */
public class SportsType extends LitePalSupport {

    public SportsType(){}
    public SportsType(String typeName){
        this.typeName=typeName;
    }

    @Column(nullable = false)
    private String typeName;

    @Column(ignore = true)
    //迭代开发：运动归类
    private int cateGoryId;

    private List<StadiumInfo> stadiumInfos;
    private List<AppointmentOrder> ordersType;

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
}
