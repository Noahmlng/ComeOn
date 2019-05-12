package com.comeon.android.business_logic;

import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 组团订单相关的业务接口
 */
public interface OrderBusinessInterface {

    /**
     * 获取所有订单
     * @return
     */
    ArrayList<AppointmentOrder> getAllOrders();

    /**
     * 通过id获取特定的order
     * @param orderId
     * @return
     */
    AppointmentOrder getSpecificOrderById(long orderId);

}
