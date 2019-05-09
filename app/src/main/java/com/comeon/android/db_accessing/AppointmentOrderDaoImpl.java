package com.comeon.android.db_accessing;

import android.database.Cursor;

import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db.SportsType;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.Utilities;

import org.litepal.LitePal;

import java.util.List;

/**
 * 组团订单数据库实现类
 */
public class AppointmentOrderDaoImpl implements AppointmentOrderDao {
    private static final String TAG = "AppointmentOrderDaoImpl";

    @Override
    public List<AppointmentOrder> getAllOrders() {
        List<AppointmentOrder> orders=LitePal.findAll(AppointmentOrder.class);
        int index=0;
        try{
            Cursor cursor=LitePal.findBySQL("select * from AppointmentOrder");
            while(cursor.moveToNext()){
                SportsType sportsType=LitePal.find(SportsType.class, cursor.getInt(cursor.getColumnIndex("sportstype_id")));
                orders.get(index).setOrderSportsType(sportsType);

                UserInfo sponsor=LitePal.find(UserInfo.class, cursor.getInt(cursor.getColumnIndex("userinfo_id")));
                orders.get(index).setOrderSponsor(sponsor);

                StadiumInfo stadium=LitePal.find(StadiumInfo.class, cursor.getInt(cursor.getColumnIndex("stadiuminfo_id")));
                orders.get(index).setOrderStadium(stadium);
                index++;
            }
        }catch (Exception ex){
            LogUtil.e(TAG, ex.getMessage());
        }
        Utilities.printAllColumns(AppointmentOrder.class);
        return orders;
    }
}
