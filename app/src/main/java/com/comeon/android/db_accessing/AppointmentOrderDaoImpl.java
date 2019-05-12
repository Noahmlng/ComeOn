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
            AppointmentOrder order=loadOrder(orders.get(i));
            orders.set(i, order);
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
                SportsType sportsType = LitePal.find(SportsType.class, cursor.getInt(cursor.getColumnIndex("sportstype_id")));
                order.setOrderSportsType(sportsType);

                UserInfo sponsor = LitePal.find(UserInfo.class, cursor.getInt(cursor.getColumnIndex("userinfo_id")));
                order.setOrderSponsor(sponsor);

                StadiumInfo stadium = LitePal.find(StadiumInfo.class, cursor.getInt(cursor.getColumnIndex("stadiuminfo_id")));
                order.setOrderStadium(stadium);

                order.setOrderParticipants(getAllParticipantsByOrderId(order.getId()));
            }
        } catch (Exception ex) {
            LogUtil.e(TAG, ex.getMessage());
        }
        return order;
    }
}
