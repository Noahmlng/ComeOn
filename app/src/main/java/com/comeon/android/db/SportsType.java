package com.comeon.android.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * 运动类型表
 */
public class SportsType extends LitePalSupport {

    public SportsType(){}
    public SportsType(String type_name){
        this.type_name=type_name;
    }

    private long id;

    @Column(nullable = false)
    private String type_name;

    //迭代开发：运动归类
    private int category_id;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
}
