package com.comeon.android.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * 好友联系人表
 */
public class Friends extends LitePalSupport {

    @Column(nullable = false)
    private long friend_id;
    @Column(nullable = false)
    private long user_id;

    private String remarks;
    private int relationship_status;

    public long getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(long friend_id) {
        this.friend_id = friend_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getRelationship_status() {
        return relationship_status;
    }

    public void setRelationship_status(int relationship_status) {
        this.relationship_status = relationship_status;
    }
}
