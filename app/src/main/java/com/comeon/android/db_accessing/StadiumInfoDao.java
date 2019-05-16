package com.comeon.android.db_accessing;

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
}
