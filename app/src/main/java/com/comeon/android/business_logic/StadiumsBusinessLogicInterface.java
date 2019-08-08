package com.comeon.android.business_logic;

import com.comeon.android.db.SportsType;
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
     * @param condition  装载条件的对象
     * @return
     */
    List<StadiumInfo> getStadiumsWithConditions(StadiumInfo condition);

    /**
     * 根据输入的场馆名查询该场馆
     * @param inputText_stadiumName  输入的场馆名
     * @return  场馆对象
     */
    StadiumInfo getStadiumByName(String inputText_stadiumName);

    /**
     * 通过场馆id获取该场馆的运动类型对象
     * @param stadiumId  查询的场馆id
     * @return  运动类型对象
     */
    SportsType getStadiumSportsTypeByStadiumId(long stadiumId);
}
