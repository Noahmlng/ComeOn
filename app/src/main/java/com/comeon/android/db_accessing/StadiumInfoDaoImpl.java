package com.comeon.android.db_accessing;

import android.database.Cursor;

import com.comeon.android.db.SportsType;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.util.LogUtil;

import org.litepal.LitePal;
import org.litepal.exceptions.LitePalSupportException;

import java.util.List;

/**
 * 场馆数据表数据提取实现类
 */
public class StadiumInfoDaoImpl implements StadiumInfoDao {

    private static final String TAG = "StadiumInfoDaoImpl";
    private SportsTypeDao sportsTypeDao =new SportsTypeDaoImpl();

    /**
     * 返回所有的场馆对象
     * @return  装载场馆对象的集合
     */
    @Override
    public List<StadiumInfo> getAllStadiums() {
        List<StadiumInfo> stadiumInfoList=null;
        StadiumInfo info;
        int index=0;
        try{
            stadiumInfoList=LitePal.findAll(StadiumInfo.class);
            Cursor cursor=LitePal.findBySQL("select sportstype_id from StadiumInfo");
            while(cursor.moveToNext()){
                long sportsTypeId=cursor.getInt(0);
                SportsType sportsType=LitePal.find(SportsType.class,sportsTypeId);
                stadiumInfoList.get(index).setSportsType(sportsType);
                index++;
            }
        }catch (LitePalSupportException ex){
            LogUtil.e(TAG, ex.getMessage());
        }
        LogUtil.e(TAG, "数据表中共有："+LitePal.count("StadiumInfo")+"条场馆数据");
        LogUtil.e(TAG, "数组共有："+stadiumInfoList.size()+"条场馆数据");
        return stadiumInfoList;
    }

    @Override
    public List<StadiumInfo> selectStadiumsByName(String stadiumName) {
        return LitePal.where("stadiumName like %?%",stadiumName).find(StadiumInfo.class);
    }
}
