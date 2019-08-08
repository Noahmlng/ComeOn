package com.comeon.android.db_accessing;

import android.database.Cursor;

import com.comeon.android.db.SportsType;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.util.LogUtil;

import org.litepal.LitePal;
import org.litepal.exceptions.LitePalSupportException;

import java.util.ArrayList;
import java.util.List;

/**
 * 场馆数据表数据提取实现类
 */
public class StadiumInfoDaoImpl extends BaseDao implements StadiumInfoDao {

    private static final String TAG = "StadiumInfoDaoImpl";
    private SportsTypeDao sportsTypeDao = new SportsTypeDaoImpl();

    /**
     * 返回所有的场馆对象
     *
     * @return 装载场馆对象的集合
     */
    @Override
    public List<StadiumInfo> getAllStadiums() {
        List<StadiumInfo> stadiumInfoList = null;
        try {
            stadiumInfoList = LitePal.findAll(StadiumInfo.class);
            for (int i = 0; i < stadiumInfoList.size(); i++) {
                StadiumInfo stadiumInfo = loadStadiumInfo(stadiumInfoList.get(i));
                stadiumInfoList.set(i, stadiumInfo);
            }
        } catch (LitePalSupportException ex) {
            LogUtil.e(TAG, ex.getMessage());
        }
        return stadiumInfoList;
    }

    @Override
    public List<StadiumInfo> getStadiumsWithCondition(StadiumInfo conditionObj) {
        List<StadiumInfo> stadiumsQualified = null;
        /*
            先拼接条件字符串
         */
        List<String> conditionValues = new ArrayList<String>();//装载条件的值

        StringBuilder conditionStr = new StringBuilder("1 = 1 ");  //条件字符串
        if (conditionObj.getStadiumName() != null) {//1、根据场馆名称的模糊查询
            conditionStr.append("and stadiumName like ? ");
            LogUtil.d(TAG, "where条件语句为："+conditionStr.toString());
            conditionValues.add("%"+conditionObj.getStadiumName()+"%");
        }

        if (conditionObj.getSportsType() != null) {//2、根据运动类型进行查询
            conditionStr.append("and sportsType_id = ? ");
            conditionValues.add(String.valueOf(conditionObj.getSportsType().getId()));
        }

        /*
            进行查询
         */
        try {
            if (conditionValues.size() > 0) {
                String[] conditions = conditionValues.toArray(new String[conditionValues.size()]);
                switch (conditions.length) {
                    case 1:
                        stadiumsQualified = LitePal.where(conditionStr.toString(), conditions[0]).find(StadiumInfo.class);
                        break;
                    case 2:
                        stadiumsQualified = LitePal.where(conditionStr.toString(), conditions[0], conditions[conditions.length - 1]).find(StadiumInfo.class);
                        break;
                }
            /*
                处理从数据库提取的数据
             */
                for (int i = 0; i < stadiumsQualified.size(); i++) {
                    StadiumInfo loadedStadium = loadStadiumInfo(stadiumsQualified.get(i));
                    stadiumsQualified.set(i, loadedStadium);
                }
            } else {
                stadiumsQualified = getAllStadiums();
            }
        } catch (Exception ex) {
            LogUtil.e(TAG, ex.getMessage());
        }
        return stadiumsQualified;
    }

    @Override
    public StadiumInfo getStadiumByName(String stadiumName) {
        List<StadiumInfo> stadiumInfos=LitePal.where("stadiumName = ?",stadiumName).find(StadiumInfo.class);
        if (stadiumInfos!=null && stadiumInfos.size()>0){
            return stadiumInfos.get(0);
        }
        return null;
    }

    @Override
    public SportsType getSportsTypeOfOneStadium(long stadiumId) {
        Cursor cursor=LitePal.findBySQL("select sportstype_id from StadiumInfo where id = ?",String.valueOf(stadiumId));
        while (cursor.moveToNext()){
            long sportsTypeId = cursor.getLong(0);
            return LitePal.find(SportsType.class, sportsTypeId);
        }
        return null;
    }

    /**
     * 加载数据库查询出的场馆对象
     *
     * @param stadiumInfoFromDB 数据提取出的场馆对象
     * @return 加载后的场馆对象
     */
    public StadiumInfo loadStadiumInfo(StadiumInfo stadiumInfoFromDB) {
        StadiumInfo stadiumInfo = stadiumInfoFromDB;
        try {
            Cursor cursor = LitePal.findBySQL("select * from StadiumInfo where id = " + stadiumInfo.getId());
            while (cursor.moveToNext()) {
                SportsType sportsType = LitePal.find(SportsType.class, cursor.getLong(cursor.getColumnIndex("sportstype_id")));
                if (sportsType==null){
                    sportsType=LitePal.find(SportsType.class, stadiumInfo.getSportsTypeId());//尚未考虑category的问题
                }
                stadiumInfo.setSportsType(sportsType);
            }
        } catch (Exception ex) {
            LogUtil.e(TAG, ex.getMessage());
        }
        return stadiumInfo;
    }

}
