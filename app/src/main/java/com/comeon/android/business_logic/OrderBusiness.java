package com.comeon.android.business_logic;

import android.database.Cursor;
import android.nfc.Tag;

import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db.AttendanceRecord;
import com.comeon.android.db.SportsType;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.db.UserInfo;
import com.comeon.android.db_accessing.AppointmentOrderDao;
import com.comeon.android.db_accessing.AppointmentOrderDaoImpl;
import com.comeon.android.db_accessing.AttendanceRecordDao;
import com.comeon.android.db_accessing.AttendanceRecordDaoImpl;
import com.comeon.android.util.LogUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * 组团订单相关的业务实现类
 */
public class OrderBusiness implements OrderBusinessInterface {
    private static final String TAG = "OrderBusiness";
    
    private AppointmentOrderDao appointmentOrderDao=new AppointmentOrderDaoImpl();
    private AttendanceRecordDao attendanceRecordDao=new AttendanceRecordDaoImpl();

    @Override
    public ArrayList<AppointmentOrder> getAllOrders() {
        ArrayList<AppointmentOrder> orders=appointmentOrderDao.getAllOrders();

        return orders;
    }

    @Override
    public AppointmentOrder getSpecificOrderById(long orderId) {
        return appointmentOrderDao.getSpecificOrderById(orderId);
    }

}
