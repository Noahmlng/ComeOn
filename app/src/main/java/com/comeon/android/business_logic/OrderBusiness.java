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
import com.comeon.android.util.Utilities;

import org.litepal.LitePal;
import org.litepal.util.LitePalLog;

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
    private AppointmentOrderDaoImpl appointmentOrderDaoImpl=new AppointmentOrderDaoImpl();

    @Override
    public ArrayList<AppointmentOrder> getAllOrders() {
        ArrayList<AppointmentOrder> orders=appointmentOrderDao.getAllOrders();
        //进行逆序处理
        ArrayList<AppointmentOrder> ordersDesc=new ArrayList<AppointmentOrder>();
        for(int i=orders.size()-1; i>=0; i--){
            ordersDesc.add(orders.get(i));
        }
        return ordersDesc;
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
        /*
            尚未实现
         */
        try {
            AppointmentOrder newOrder = new AppointmentOrder();
            newOrder.setOrderName(groupName);
            newOrder.setOrderContact(contact);
            newOrder.setOrderExpectedSize(peopleSize);
            newOrder.setOrderLocation(location);
            newOrder.setOrderSponsor(loginUser);
            newOrder.setOrderSportsType(selectedSportsType);
            newOrder.setOrderSponsorId(selectedSportsType.getId());
//            appointmentOrderDao.insertNewOrder(newOrder);

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

    @Override
    public boolean createNewOrder(UserInfo loginUser, int peopleSize, String groupName, String contact, StadiumInfo stadiumInfo) {
        try {
            AppointmentOrder newOrder=appointmentOrderDao.insertNewOrder(loginUser, peopleSize, groupName, contact, stadiumInfo);

            //添加发起者为第一个参与者
            AttendanceRecord record = new AttendanceRecord();
            record.setOrderId(newOrder.getId());
            record.setParticipantId(loginUser.getId());
            attendanceRecordDao.insertNewRecord(record);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            LogUtil.e(TAG, "发起订单出错："+ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean participateGroup(UserInfo loginUser, AppointmentOrder order) {
        if(attendanceRecordDao.checkIfAlreadyParticipated(loginUser.getId(),order.getId())>0){
            return false;
        }else{
            AttendanceRecord record=new AttendanceRecord();
            record.setParticipantId(loginUser.getId());
            record.setOrderId(order.getId());
            attendanceRecordDao.insertNewRecord(record);
            return true;
        }
    }

    @Override
    public List<UserInfo> loadParticipantsList(AppointmentOrder order) {
        return appointmentOrderDaoImpl.getAllParticipantsByOrderId(order.getId());
    }

    @Override
    public List<AppointmentOrder> getOrdersWithCondition(AppointmentOrder condition) {
        List<AppointmentOrder> orders=appointmentOrderDao.getOrdersWithCondition(condition);
        //进行逆序处理
        List<AppointmentOrder> ordersDesc=new ArrayList<AppointmentOrder>();
        for(int i=orders.size()-1; i>=0; i--){
            ordersDesc.add(orders.get(i));
        }
        return ordersDesc;
    }

    @Override
    public String getOrderStadiumNameWithOrderId(long orderId) {
        long stadiumId=appointmentOrderDao.getOrderStadiumIdByOrderId(orderId);
        if (stadiumId==0){
            return "";
        }
        return LitePal.find(StadiumInfo.class, stadiumId).getStadiumName();
    }


}
