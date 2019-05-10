package com.comeon.android.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * 好友联系人表
 */
public class Friends extends LitePalSupport {

    @Column(nullable = false)
    private long friendId;
    @Column(nullable = false)
    private long userId;

    private String remarks;
    @Column(defaultValue = "0")
    private int relationshipStatus;

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(int relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }
}
