package com.comeon.android.db_accessing;

import com.comeon.android.db.Friends;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 好友表的数据库接口实现类
 */
public class FriendDaoImpl implements FriendDao {
    @Override
    public List<Long> getAllFriendsOfUser(long userId) {
        List<Friends> friendsId=LitePal.where("userId = ?",String.valueOf(userId)).find(Friends.class);
        List<Long> ids=new ArrayList<>();
        for (int i=0; i<friendsId.size(); i++){
            ids.add(friendsId.get(i).getFriendId());
        }
        return ids;
    }
}
