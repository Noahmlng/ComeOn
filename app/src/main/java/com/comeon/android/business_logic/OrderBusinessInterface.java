package com.comeon.android.business_logic;

import com.comeon.android.db.AppointmentOrder;

import java.util.List;

/**
 * 组团订单相关的业务接口
 */
public interface OrderBusinessInterface {

    List<AppointmentOrder> getAllOrders();

}
