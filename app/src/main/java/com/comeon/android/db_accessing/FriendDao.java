package com.comeon.android.db_accessing;

import com.comeon.android.db.Friends;
import com.comeon.android.db.UserInfo;

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

    /**
     * 某个用户根据好友名进行模糊查询
     * @param userId 查询者的id
     * @param name  输入的好友名
     * @return  符合条件的好友
     */
    List<UserInfo> selectFriendsByNameOfUser(long userId, String name);

    /**
     * 检查查询用户是不是登录用户的好友
     * @param searchUserId   查询用户的id
     * @param loginUserId   登录用户的id
     * @return  行数
     */
    int checkIsFriend(long searchUserId, long loginUserId);


    /**
     * 添加好友
     * @param userId  添加的用户的id
     * @param loginUserId  登录用户的id
     * @return  添加结果
     */
    boolean addFriend(long userId, long loginUserId);
}
