package com.comeon.android.business_logic;

import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db_accessing.AppointmentOrderDao;
import com.comeon.android.db_accessing.AppointmentOrderDaoImpl;

import java.util.List;

/**
 * 组团订单相关的业务实现类
 */
public class OrderBusiness implements OrderBusinessInterface {
    private AppointmentOrderDao appointmentOrderDao=new AppointmentOrderDaoImpl();

    @Override
    public List<AppointmentOrder> getAllOrders() {
        return appointmentOrderDao.getAllOrders();
    }
}
