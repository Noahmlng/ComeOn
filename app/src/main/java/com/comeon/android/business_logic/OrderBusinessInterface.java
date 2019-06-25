package com.comeon.android.business_logic;

import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db.SportsType;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.db.UserInfo;
import com.comeon.android.db_accessing.AppointmentOrderDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 组团订单相关的业务接口
 */
public interface OrderBusinessInterface {

    /**
     * 获取所有订单
     *
     * @return
     */
    ArrayList<AppointmentOrder> getAllOrders();

    /**
     * 通过id获取特定的order
     *
     * @param orderId
     * @return
     */
    AppointmentOrder getSpecificOrderById(long orderId);

    /**
     * 加载某个种类的所有所属的运动分类
     *
     * @param categoryId
     * @return
     */
    List<SportsType> loadSportsTypeInCategory(long categoryId);


    /**
     * 发布新的组团信息
     *
     * @param loginUser          登录用户为发起者
     * @param peopleSize         组团人数
     * @param groupName          团名
     * @param contact            订单联系电话
     * @param location           地址
     * @param selectedSportsType 选中的运动类型
     * @return
     */
    boolean createNewOrder(UserInfo loginUser, int peopleSize, String groupName, String contact, String location, SportsType selectedSportsType);

    /**
     * 发布新的组团信息
     *
     * @param loginUser          登录用户为发起者
     * @param peopleSize         组团人数
     * @param groupName          团名
     * @param contact            订单联系电话
     * @param stadiumInfo        选中场馆
     * @return
     */
    boolean createNewOrder(UserInfo loginUser, int peopleSize, String groupName, String contact, StadiumInfo stadiumInfo);


    /**
     * 加入组团
     */
    boolean participateGroup(UserInfo loginUser, AppointmentOrder order);

    /**
     * 刷新某个订单的参与者列表
     *
     * @param order
     * @return
     */
    List<UserInfo> loadParticipantsList(AppointmentOrder order);

    /**
     * 查询符合条件的组团邀约
     *
     * @param condition 带有条件的装载对象
     * @return 符合条件的组团邀约集合
     */
    List<AppointmentOrder> getOrdersWithCondition(AppointmentOrder condition);

}