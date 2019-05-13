package com.comeon.android.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * 聊天记录表
 */
public class Message extends LitePalSupport {

    public static final int TEXT_SENT=0;
    public static final int TEXT_RECEIVED=1;

    @Column(nullable = false)
    private long friendId;
    @Column(nullable = false)
    private long userId;

    private int type;

    private String messageContent;
    private Date sendTime;

    //留做后续的发送状态记录
    @Column(ignore = true)
    private int messageStatus;

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
