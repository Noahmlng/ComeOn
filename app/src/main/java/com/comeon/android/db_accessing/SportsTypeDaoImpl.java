package com.comeon.android.db_accessing;

import com.comeon.android.db.SportsType;
import com.comeon.android.util.LogUtil;

import org.litepal.LitePal;

import java.util.List;

/**
 * 运动类型数据表数据提取层
 */
public class SportsTypeDaoImpl implements SportsTypeDao {

    private static final String TAG = "SportsTypeDaoImpl";
    
    /**
     * 根据运动名称返回数据类型对象
     * @param name 运动类型名称
     * @return 运动类型对象
     */
    public SportsType findSportsTypeByName(String name) {
        SportsType sportsType = null;
        try {
            List<SportsType> targetTypes= LitePal.findAll(SportsType.class);
            sportsType=targetTypes.get(0);
        } catch (Exception ex) {
            LogUtil.e(TAG, ex.getMessage());
        }
        return sportsType;
    }
}
