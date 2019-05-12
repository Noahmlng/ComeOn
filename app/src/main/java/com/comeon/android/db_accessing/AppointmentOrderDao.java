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

    AppointmentOrder getSpecificOrderById(long orderId);
}
