package com.comeon.android.db_accessing;

import com.comeon.android.db.SportsType;
import com.comeon.android.db.StadiumInfo;

import java.util.List;

/**
 *  场馆数据库数据提取接口
 */
public interface StadiumInfoDao {

    /**
     * 返回所有的场馆对象
     * @return  装载场馆对象的集合
     */
    List<StadiumInfo> getAllStadiums();

    /**
     * 根据条件查询场馆信息
     * @param condition  装载条件的场馆对象
     * @return  符合条件的场馆信息
     */
    List<StadiumInfo> getStadiumsWithCondition(StadiumInfo condition);

    /**
     * 根据场馆名精确查找某个场馆
     * @param stadiumName  场馆名
     * @return  查询到的场馆
     */
    StadiumInfo getStadiumByName(String stadiumName);

    /**
     * 查询某个场馆的运动类型
     * @param stadiumId  查询的场馆id
     * @return  运动类型对象
     */
    SportsType getSportsTypeOfOneStadium(long stadiumId);
}
