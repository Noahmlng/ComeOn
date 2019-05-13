package com.comeon.android.db_accessing;

import com.comeon.android.db.AppointmentOrder;

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
     * @param newOrder
     * @return  新增订单对象
     */
    AppointmentOrder insertNewOrder(AppointmentOrder newOrder);
}
