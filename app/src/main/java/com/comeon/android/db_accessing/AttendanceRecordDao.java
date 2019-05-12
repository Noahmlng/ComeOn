package com.comeon.android.db_accessing;

import com.comeon.android.db.AttendanceRecord;

import java.util.List;

/**
 * 用户参加组团记录数据库访问接口
 */
public interface AttendanceRecordDao {

    /**
     * 添加参与记录
     * @param record 添加记录
     */
    void insertNewRecord(AttendanceRecord record);

    /**
     * 通过订单号查询所有的相关订单
     * @param orderId
     * @return
     */
    List<AttendanceRecord> getParticipantsByOrderId(long orderId);

    /**
     * 通过参与者的id查询他所参与的所有订单
     * @param participantId
     * @return
     */
    List<AttendanceRecord> getOrderIdByParticipantId(long participantId);
}
