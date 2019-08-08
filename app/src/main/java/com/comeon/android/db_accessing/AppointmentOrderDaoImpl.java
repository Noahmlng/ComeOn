package com.comeon.android.db_accessing;

import android.database.Cursor;

import com.comeon.android.business_logic.OrderBusiness;
import com.comeon.android.business_logic.OrderBusinessInterface;
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
public class AppointmentOrderDaoImpl extends BaseDao implements AppointmentOrderDao {
    private static final String TAG = "AppointmentOrderDaoImpl";

    StadiumInfoDao stadiumInfoDao=new StadiumInfoDaoImpl();
    private AttendanceRecordDao attendanceRecordDao=new AttendanceRecordDaoImpl();

    @Override
    public ArrayList<AppointmentOrder> getAllOrders() {
        ArrayList<AppointmentOrder> orders = (ArrayList<AppointmentOrder>) LitePal.order("orderLaunchTime").find(AppointmentOrder.class);
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
    @Override
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

    @Override
    public long getOrderStadiumIdByOrderId(long orderId) {
        Cursor cursor=LitePal.findBySQL("select stadiuminfo_id from AppointmentOrder where id = ?",String.valueOf(orderId));
        long stadiumId=0;
        while (cursor.moveToNext()){
            stadiumId=cursor.getLong(0);
        }
        return stadiumId;
    }

    @Override
    public AppointmentOrder insertNewOrder(UserInfo loginUser, int peopleSize, String groupName, String contact, StadiumInfo stadiumInfo) {
        AppointmentOrder newOrder = new AppointmentOrder();
        newOrder.setOrderAppointTime(new Date(2019,8,30)); //暂时未处理约定时间
        newOrder.setOrderExpectedSize(peopleSize);
        newOrder.setOrderName(groupName);
        newOrder.setOrderContact(contact);
        newOrder.setOrderSponsor(loginUser);
        newOrder.setOrderSponsorId(loginUser.getId());
//        newOrder.setOrderSportsType(stadiumInfo.getSportsType());
//        newOrder.setOrderSportsTypeId(stadiumInfo.getSportsType().getId());
        newOrder.setOrderSportsTypeId(stadiumInfo.getSportsTypeId());
        newOrder.setOrderSportsType(stadiumInfoDao.getSportsTypeOfOneStadium(stadiumInfo.getId()));
        newOrder.setLongitude(stadiumInfo.getLongitude());
        newOrder.setLatitude(stadiumInfo.getLatitude());
        if(stadiumInfo.getStreetNumber()!=null && stadiumInfo.getStreetNumber().trim().length()>0){
            newOrder.setOrderLocation(stadiumInfo.getStreet()+stadiumInfo.getStreetNumber());
        }else{
            newOrder.setOrderLocation(stadiumInfo.getStreet());
        }
        newOrder.setOrderLaunchTime(Utilities.getNow());
        newOrder.setOrderStatus(0);
        newOrder.save();
        return newOrder;
    }

    @Override
    public List<AppointmentOrder> getOrdersWithCondition(AppointmentOrder conditionObj) {
        List<AppointmentOrder> ordersQualified=null;

        /*
            先拼接条件字符串
         */
        List<String> conditionValues=new ArrayList<String>();//装载条件的值

        StringBuilder conditionStr=new StringBuilder("1 = 1 ");  //条件字符串
        if(conditionObj.getOrderName()!=null){//1、根据组团名称的模糊查询
            conditionStr.append("and orderName like ? ");
            conditionValues.add("%"+conditionObj.getOrderName()+"%");
        }

//        if (conditionObj.getOrderSponsor()!=null){
//            conditionStr.append("and orderSponsor_id in (?)");
//            conditionValues.add("%"+conditionObj.getOrderName()+"%");
//        }

        if(conditionObj.getOrderSportsType()!=null){//2、根据运动类型进行查询
            conditionStr.append("and sportsType_id = ? ");
            conditionValues.add(String.valueOf(conditionObj.getOrderSportsType().getId()));
        }

        /*
            进行查询
         */
        if(conditionValues.size()>0){
            String[] conditions=new String[conditionValues.size()];
            conditions=conditionValues.toArray(conditions);
            switch (conditions.length){
                case 1:
                    ordersQualified=LitePal.where(conditionStr.toString(), conditions[0]).find(AppointmentOrder.class);
                    break;
                case 2:
                    ordersQualified=LitePal.where(conditionStr.toString(), conditions[0],conditions[conditions.length-1]).find(AppointmentOrder.class);
                    break;
            }
            /*
                处理从数据库提取的数据
             */
            for (int i=0; i<ordersQualified.size(); i++){
                AppointmentOrder loadedOrder=loadOrder(ordersQualified.get(i));
                ordersQualified.set(i, loadedOrder);
            }
        }else{
            ordersQualified=getAllOrders();
        }
        return ordersQualified;
    }


    /**
     * 根据面向对象的思想加载一个数据库提取的订单信息
     * @param orderFromDB  从数据体局的订单
     * @return  加载后的订单信息
     */
    public AppointmentOrder loadOrder(AppointmentOrder orderFromDB){
        AppointmentOrder order=orderFromDB;
        try {
            Cursor cursor = LitePal.findBySQL("select * from AppointmentOrder where id = "+order.getId());
            while (cursor.moveToNext()) {
                LogUtil.d("订单的sportsTypeId：",""+cursor.getLong(cursor.getColumnIndex("sportstype_id")));
                LogUtil.d("订单的sponsorId：",""+cursor.getLong(cursor.getColumnIndex("userinfo_id")));
                LogUtil.d("订单的stadiumId：",""+cursor.getLong(cursor.getColumnIndex("stadiuminfo_id")));

                SportsType sportsType = LitePal.find(SportsType.class, cursor.getLong(cursor.getColumnIndex("sportstype_id")));
                if (sportsType==null){
                    sportsType=LitePal.find(SportsType.class, order.getOrderSportsTypeId());
                }
                order.setOrderSportsType(sportsType);

                UserInfo sponsor = LitePal.find(UserInfo.class, cursor.getLong(cursor.getColumnIndex("userinfo_id")));
                if (sponsor==null){
                    sponsor=LitePal.find(UserInfo.class, order.getOrderSponsorId());
                }
                order.setOrderSponsor(sponsor);

                //                order.setOrderParticipants(getAllParticipantsByOrderId(order.getId()));   因为存在动态操作，所以不在此处进行加载
            }
        } catch (Exception ex) {
            LogUtil.e(TAG, ex.getMessage());
        }
        return order;
    }
}
