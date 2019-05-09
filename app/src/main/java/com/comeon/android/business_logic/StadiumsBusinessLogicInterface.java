package com.comeon.android.business_logic;

import com.comeon.android.db.StadiumInfo;

import java.util.List;

/**
 * 场馆相关业务接口
 */
public interface StadiumsBusinessLogicInterface {

    List<StadiumInfo> getAllStadiums();
}
