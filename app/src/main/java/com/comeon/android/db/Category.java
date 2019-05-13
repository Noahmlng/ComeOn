package com.comeon.android.db;

import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * 运动种类数据表
 */
public class Category extends LitePalSupport {

    private long id;
    private String sportsType;

    public Category(){}

    public Category(String sportsType) {
        this.sportsType = sportsType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSportsType() {
        return sportsType;
    }

    public void setSportsType(String sportsType) {
        this.sportsType = sportsType;
    }

    private List<SportsType> sportsTypes;
}
