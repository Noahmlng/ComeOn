package com.comeon.android.db;

import org.litepal.crud.LitePalSupport;

/**
 * 用户的参与记录
 */
public class AttendanceRecord extends LitePalSupport {

    long orderId;

    long participantId;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(long participantId) {
        this.participantId = participantId;
    }
}
