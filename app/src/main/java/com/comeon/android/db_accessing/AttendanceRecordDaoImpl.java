package com.comeon.android.db_accessing;

import com.comeon.android.db.AttendanceRecord;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户参加组团记录数据库访问接口实现类
 */
public class AttendanceRecordDaoImpl implements AttendanceRecordDao {
    @Override
    public void insertNewRecord(AttendanceRecord record) {
        record.save();
    }

    @Override
    public List<AttendanceRecord> getParticipantsByOrderId(long orderId) {
        return LitePal.where(" orderId = ?",String.valueOf(orderId)).find(AttendanceRecord.class);
    }

    @Override
    public List<AttendanceRecord> getOrderIdByParticipantId(long participantId) {
        return LitePal.where(" participantId = ?",String.valueOf(participantId)).find(AttendanceRecord.class);
    }
}
