package com.comeon.android.business_logic;

import com.comeon.android.db.SportsType;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.db_accessing.SportsTypeDao;
import com.comeon.android.db_accessing.SportsTypeDaoImpl;
import com.comeon.android.db_accessing.StadiumInfoDao;
import com.comeon.android.db_accessing.StadiumInfoDaoImpl;

import java.util.List;

/**
 * 场馆相关业务管理层
 */
public class StadiumsBusiness implements StadiumsBusinessLogicInterface {
    private SportsTypeDao sportsTypeDao=new SportsTypeDaoImpl();
    private StadiumInfoDao stadiumInfoDao =new StadiumInfoDaoImpl();

    public boolean CreateNewStadium(StadiumInfo stadium, SportsType type){
        return false;
    }

    @Override
    public List<StadiumInfo> getAllStadiums() {
        return stadiumInfoDao.getAllStadiums();
    }

    @Override
    public List<StadiumInfo> getStadiumsByName(String stadiumName) {
        List<StadiumInfo> stadiumInfoList=stadiumInfoDao.getAllStadiums();
        return null;
    }
}
