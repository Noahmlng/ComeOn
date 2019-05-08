package com.comeon.android.business_logic;

import android.database.Cursor;

import com.comeon.android.db.SportsType;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.db_accessing.SportsTypeDBAccessing;

import org.litepal.LitePal;

/**
 * 场馆相关业务管理层
 */
public class StadiumsBusiness {
    private SportsTypeDBAccessing sportsTypeDBAccessing=new SportsTypeDBAccessing();

    public boolean CreateNewStadium(StadiumInfo stadium, SportsType type){
        return false;
    }

}
