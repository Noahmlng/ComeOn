package com.comeon.android.business_logic;

import com.comeon.android.db.StadiumInfo;

import java.util.List;

/**
 * 场馆相关业务接口
 */
public interface StadiumsBusinessLogicInterface {

    /**
     * 获取所有的场馆对象
     * @return
     */
    List<StadiumInfo> getAllStadiums();

    /**
     * 根据场馆的名字碎片获取场馆集合
     * @param stadiumName
     * @return
     */
    List<StadiumInfo> getStadiumsByName(String stadiumName);
}
