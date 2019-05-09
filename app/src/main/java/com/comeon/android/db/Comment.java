package com.comeon.android.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * 评论表
 */
public class Comment extends LitePalSupport {

    private long stadiumId;
    private long orderId;
    private long userId;
    private String commentContent;
    private Date auditTime;
    private int auditStatus;

    //用于评价星级
    @Column(ignore = true)
    private int commentGrade;

    public long getStadiumId() {
        return stadiumId;
    }

    public void setStadiumId(long stadiumId) {
        this.stadiumId = stadiumId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }
}
