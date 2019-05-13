package com.comeon.android.db_accessing;

import android.database.Cursor;

import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db.AttendanceRecord;
import com.comeon.android.db.SportsType;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.Utilities;

import org.litepal.LitePal;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 组团订单数据库实现类
 */
public class AppointmentOrderDaoImpl implements AppointmentOrderDao {
    private static final String TAG = "AppointmentOrderDaoImpl";

    private AttendanceRecordDao attendanceRecordDao=new AttendanceRecordDaoImpl();

    @Override
    public ArrayList<AppointmentOrder> getAllOrders() {
        ArrayList<AppointmentOrder> orders = (ArrayList<AppointmentOrder>) LitePal.findAll(AppointmentOrder.class);
        for (int i=0; i<orders.size(); i++){
            AppointmentOrder loadedOrder=loadOrder(orders.get(i));
            orders.set(i, loadedOrder);
            LogUtil.d(TAG, "处理后的orderid为："+orders.get(i).getId());
        }
        Utilities.printAllColumns(AppointmentOrder.class);
        return orders;
    }

    @Override
    public AppointmentOrder getSpecificOrderById(long orderId) {
        AppointmentOrder order=LitePal.find(AppointmentOrder.class, orderId);
        order=loadOrder(order);
        return order;
    }


    /**
     * 通过id获取特定的order
     * @param orderId
     * @return
     */
    public List<UserInfo> getAllParticipantsByOrderId(long orderId) {
        List<AttendanceRecord> records=attendanceRecordDao.getParticipantsByOrderId(orderId);
        LogUtil.d(TAG,"参与当前订单的用户个数有："+records.size());
        List<UserInfo> participants=new ArrayList<UserInfo>();

        for(int i=0; i<records.size(); i++){
            AttendanceRecord record=records.get(i);
            UserInfo participant=LitePal.find(UserInfo.class ,record.getParticipantId());
            participants.add(participant);
        }
        return participants;
    }

    public AppointmentOrder loadOrder(AppointmentOrder orderFromDB){
        AppointmentOrder order=orderFromDB;
        try {
            Cursor cursor = LitePal.findBySQL("select * from AppointmentOrder where id = "+order.getId());
            while (cursor.moveToNext()) {
                SportsType sportsType = LitePal.find(SportsType.class, cursor.getLong(cursor.getColumnIndex("sportstype_id")));
                order.setOrderSportsType(sportsType);

                UserInfo sponsor = LitePal.find(UserInfo.class, cursor.getLong(cursor.getColumnIndex("userinfo_id")));
                order.setOrderSponsor(sponsor);

                StadiumInfo stadium = LitePal.find(StadiumInfo.class, cursor.getLong(cursor.getColumnIndex("stadiuminfo_id")));
                order.setOrderStadium(stadium);

                order.setOrderParticipants(getAllParticipantsByOrderId(order.getId()));
            }
        } catch (Exception ex) {
            LogUtil.e(TAG, ex.getMessage());
        }
        return order;
    }


    @Override
    public AppointmentOrder insertNewOrder(AppointmentOrder newOrder) {
        newOrder.setOrderSponsor(LitePal.find(UserInfo.class, newOrder.getOrderSponsor().getId()));
        newOrder.setOrderSportsType(LitePal.find(SportsType.class, newOrder.getOrderSportsType().getId()));
        newOrder.setOrderLaunchTime(new Date(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH)+1,Calendar.getInstance().get(Calendar.DATE),Calendar.getInstance().get(Calendar.HOUR),Calendar.getInstance().get(Calendar.MINUTE),Calendar.getInstance().get(Calendar.SECOND)));
        newOrder.setOrderStatus(0);
        newOrder.save();
        return newOrder;
    }
}
