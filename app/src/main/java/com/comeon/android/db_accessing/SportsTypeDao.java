package com.comeon.android.db_accessing;

import com.comeon.android.db.SportsType;
import com.comeon.android.util.LogUtil;

import org.litepal.LitePal;

import java.util.List;

/**
 * 运动类型数据表数据提取接口
 */
public interface SportsTypeDao {

    /**
     * 根据运动名称返回数据类型对象
     * @param name 运动类型名称
     * @return 运动类型对象
     */
    SportsType findSportsTypeByName(String name);
}
