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
import com.comeon.android.db_accessing.SportsTypeDao;
import com.comeon.android.db_accessing.SportsTypeDaoImpl;
import com.comeon.android.db_accessing.UserInfoDao;
import com.comeon.android.db_accessing.UserInfoDaoImpl;
import com.comeon.android.util.LogUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * 组团订单相关的业务实现类
 */
public class OrderBusiness implements OrderBusinessInterface {
    private static final String TAG = "OrderBusiness";
    
    private AppointmentOrderDao appointmentOrderDao=new AppointmentOrderDaoImpl();
    private AttendanceRecordDao attendanceRecordDao=new AttendanceRecordDaoImpl();
    private SportsTypeDao sportsTypeDao=new SportsTypeDaoImpl();

    @Override
    public ArrayList<AppointmentOrder> getAllOrders() {
        ArrayList<AppointmentOrder> orders=appointmentOrderDao.getAllOrders();
        return orders;
    }

    @Override
    public AppointmentOrder getSpecificOrderById(long orderId) {
        return appointmentOrderDao.getSpecificOrderById(orderId);
    }

    @Override
    public List<SportsType> loadSportsTypeInCategory(long categoryId) {
        return sportsTypeDao.getSportsTypesByCategoryId(categoryId);
    }

    @Override
    public boolean createNewOrder(UserInfo loginUser, int peopleSize, String groupName, String contact, String location, SportsType selectedSportsType) {
        try {
            AppointmentOrder newOrder = new AppointmentOrder();
            newOrder.setOrderName(groupName);
            newOrder.setOrderContact(contact);
            newOrder.setOrderExpectedSize(peopleSize);
            newOrder.setOrderLocation(location);
            newOrder.setOrderSponsor(loginUser);
            newOrder.setOrderSportsType(selectedSportsType);
            appointmentOrderDao.insertNewOrder(newOrder);

            //添加发起者为第一个参与者
            AttendanceRecord record = new AttendanceRecord();
            record.setOrderId(newOrder.getId());
            record.setParticipantId(loginUser.getId());
            attendanceRecordDao.insertNewRecord(record);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }


}
