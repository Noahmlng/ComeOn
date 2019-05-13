package com.comeon.android.db_accessing;

import com.comeon.android.db.Friends;

import java.util.List;

/**
 * 好友表的数据库接口层
 */
public interface FriendDao {

    /**
     * 获取一个用户的所有好友
     * @param userId  查询用户的id
     * @return  所有好友的id
     */
    List<Long> getAllFriendsOfUser(long userId);

}
