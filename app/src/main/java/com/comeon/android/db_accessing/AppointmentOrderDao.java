package com.comeon.android.db_accessing;

import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.db.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 组团订单数据表接口
 */
public interface AppointmentOrderDao {

    /**
     * 获取所有组团信息的方法
     * @return  返回所有的组团信息集合
     */
    ArrayList<AppointmentOrder> getAllOrders();

    /**
     * 根据订单id获取特定的order对象
     * @param orderId
     * @return  赋好了sportsType、StadiumInfo、Sponsor的对象
     */
    AppointmentOrder getSpecificOrderById(long orderId);

    /**
     * 新增订单
     * @return  新增订单对象
     */
    AppointmentOrder insertNewOrder(UserInfo loginUser, int peopleSize, String groupName, String contact, StadiumInfo stadiumInfo);


    /**
     * 条件查询订单信息
     * @param conditionObj  装载条件的订单对象
     * @return  符合条件的订单对象
     */
    List<AppointmentOrder> getOrdersWithCondition(AppointmentOrder conditionObj);

    /**
     * 根据邀约号，加载此邀约的所有参与者
     * @param orderId  订单号
     * @return   参与者集合
     */
    List<UserInfo> getAllParticipantsByOrderId(long orderId);

    /**
     * 通过订单id获取对应的场馆id
     * @param orderId  订单id
     * @return  订单的场馆id
     */
    long getOrderStadiumIdByOrderId(long orderId);
}
