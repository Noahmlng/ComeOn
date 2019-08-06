package com.comeon.android.db_accessing;

import android.database.Cursor;

import com.comeon.android.db.Friends;
import com.comeon.android.db.SportsType;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.LogUtil;

import org.litepal.LitePal;
import org.litepal.LitePalDB;

import java.util.ArrayList;
import java.util.List;

/**
 * 好友表的数据库接口实现类
 */
public class FriendDaoImpl extends BaseDao implements FriendDao {
    private static final String TAG = "FriendDaoImpl";

    @Override
    public List<Long> getAllFriendsOfUser(long userId) {
        List<Friends> friendsId=LitePal.where("userId = ?",String.valueOf(userId)).find(Friends.class);
        List<Long> ids=new ArrayList<>();
        for (int i=0; i<friendsId.size(); i++){
            ids.add(friendsId.get(i).getFriendId());
        }
        return ids;
    }

    @Override
    public List<UserInfo> selectFriendsByNameOfUser(long userId, String name) {
        List<UserInfo> friends=null;
        try {
            //用子查询锁定符合条件的用户id
            Cursor cursor=LitePal.findBySQL("select id from UserInfo where id in (select friendId from Friends where userId = ?) and userNickName like ?", String.valueOf(userId),name+"%"); //在?号外有单引号，但是在sql中like语句的值和%号需要用引号围着。
            while (cursor.moveToNext()) {
                friends=new ArrayList<UserInfo>();
                //用找到的id建立该用户的对象
                UserInfo friend=LitePal.find(UserInfo.class, cursor.getLong(cursor.getColumnIndex("id")));
                friends.add(friend);
            }
        } catch (Exception ex) {
            LogUtil.e("根据输入用户名查询对应的好友", ex.getMessage());
        }
        return friends;
    }

    @Override
    public int checkIsFriend(long searchUserId, long loginUserId) {
        //统计有没有对应的数据行
        int count=LitePal.where("userId = ? and friendId = ?",String.valueOf(loginUserId), String.valueOf(searchUserId)).count(Friends.class);
        return count;
    }

    @Override
    public boolean addFriend(long userId, long loginUserId) {
        try{
            /*
                添加好友包括
                1、登录用户添加该用户为好友
                2、用户添加登录用户为好友
             */
            //1、登录用户添加该用户为好友
            Friends addFriend=new Friends();
            addFriend.setUserId(loginUserId);
            addFriend.setFriendId(userId);
            addFriend.setRelationshipStatus(0);
            boolean result1=addFriend.save();

            //2、用户添加登录用户为好友
            Friends acceptFriend=new Friends();
            acceptFriend.setUserId(userId);
            acceptFriend.setFriendId(loginUserId);
            acceptFriend.setRelationshipStatus(0);
            boolean result2=acceptFriend.save();

            return result1&&result2; //只有当两个同时为true，才会返回true
        }catch (Exception ex){
            LogUtil.e(TAG,"添加好友出现异常："+ex.getMessage());
        }
        return false;
    }

}
