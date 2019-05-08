package com.comeon.android.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * 评论表
 */
public class Comment extends LitePalSupport {

    private long id;
    private long stadium_id;
    private long order_id;
    private long user_id;
    private String comment_content;
    private Date audit_time;
    private byte audit_status;

    //用于评价星级
    @Column(ignore = true)
    private int comment_grade;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long getStadium_id() {
        return stadium_id;
    }

    public void setStadium_id(long stadium_id) {
        this.stadium_id = stadium_id;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public Date getAudit_time() {
        return audit_time;
    }

    public void setAudit_time(Date audit_time) {
        this.audit_time = audit_time;
    }

    public byte getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(byte audit_status) {
        this.audit_status = audit_status;
    }
}
