package com.comeon.android.db_accessing;

import com.comeon.android.db.AttendanceRecord;
import com.comeon.android.util.LogUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户参加组团记录数据库访问接口实现类
 */
public class AttendanceRecordDaoImpl extends BaseDao implements AttendanceRecordDao {
    private static final String TAG = "AttendanceRecordDaoImpl";

    @Override
    public void insertNewRecord(AttendanceRecord record) {
        record.save();
    }

    @Override
    public List<AttendanceRecord> getParticipantsByOrderId(long orderId) {
        return LitePal.where("orderId = ?",String.valueOf(orderId)).find(AttendanceRecord.class);
    }

    @Override
    public List<AttendanceRecord> getOrderIdByParticipantId(long participantId) {
        return LitePal.where("participantId = ?",String.valueOf(participantId)).find(AttendanceRecord.class);
    }

    @Override
    public int checkIfAlreadyParticipated(long participantId, long orderId) {
        int count=LitePal.where("orderId = ? and participantId = ?",String.valueOf(orderId),String.valueOf(participantId)).count(AttendanceRecord.class);
        LogUtil.d(TAG, participantId+"有"+count+"记录"+orderId);
        return count;
    }

}
