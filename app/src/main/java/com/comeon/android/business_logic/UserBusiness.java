package com.comeon.android.business_logic;

import android.util.Log;

import com.comeon.android.db.Message;
import com.comeon.android.db.UserInfo;
import com.comeon.android.db.UserLogin;
import com.comeon.android.db_accessing.FriendDao;
import com.comeon.android.db_accessing.FriendDaoImpl;
import com.comeon.android.db_accessing.MessageDao;
import com.comeon.android.db_accessing.MessageDaoImpl;
import com.comeon.android.db_accessing.UserInfoDao;
import com.comeon.android.db_accessing.UserInfoDaoImpl;
import com.comeon.android.db_accessing.UserLoginDao;
import com.comeon.android.db_accessing.UserLoginDaoImpl;
import com.comeon.android.db_accessing.UserLoginLogDao;
import com.comeon.android.db_accessing.UserLoginLogDaoImpl;
import com.comeon.android.util.LogUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户相关业务接口实现类
 */
public class UserBusiness implements UserBusinessInterface {
    private static final String TAG = "UserBusiness";
    
    private UserLoginDao userLoginDao=new UserLoginDaoImpl();
    private UserInfoDao userInfoDao=new UserInfoDaoImpl();
    private UserLoginLogDao userLoginLogDao=new UserLoginLogDaoImpl();
    private FriendDao friendDao=new FriendDaoImpl();
    private MessageDao messageDao=new MessageDaoImpl();
    
    @Override
    public UserInfo login(String phone, String password) {
        UserLogin loginUser=new UserLogin();
        loginUser.setUserPhone(phone);
        loginUser.setUserPassword(password);
        long loginId=userLoginDao.selectUser(loginUser);
        LogUtil.d(TAG, "当前尝试登录的用户的id为："+loginId);
        if(loginId!=-1){
            UserInfo loginedUser=userInfoDao.selectUserByLoginId(loginId);
            if(loginUser!=null){
                userLoginLogDao.insertNewLog(loginId);
                return loginedUser;
            }
        }
        return null;
    }

    @Override
    public boolean checkPhoneExist(String phone) {
        UserLogin userLogin=new UserLogin();
        userLogin.setUserPhone(phone);
        int count=userLoginDao.checkExistOnPhone(userLogin);
        if(count>0){
            return true;
        }
        return false;
    }

    @Override
    public UserInfo registration(String phone, String password) {
        UserInfo newUser=null;
        /**
         * 新建登录用户先
         */
        UserLogin registeredUser=new UserLogin();
        registeredUser.setUserPhone(phone);
        registeredUser.setUserPassword(password);
        long loginId=userLoginDao.insertNewUser(registeredUser);
        if(loginId>0){
            newUser=userInfoDao.insertNewUser(loginId,phone);
            //注册之后加第一次登录记录
            userLoginLogDao.insertNewLog(loginId);
        }
        return newUser;
    }

    @Override
    public List<UserInfo> getAllFriends(long userId) {
        List<Long> friendsIds=friendDao.getAllFriendsOfUser(userId);
        List<UserInfo> friendsList=null;
        if(friendsIds.size()>0){
            friendsList=new ArrayList<UserInfo>();
        }
        for (int i=0; i<friendsIds.size(); i++){
            UserInfo friend=LitePal.find(UserInfo.class, friendsIds.get(i));
            friendsList.add(friend);
        }
        return friendsList;

    }

    @Override
    public String getLastMessageContent(long userId, long friendId) {
        Message lastMessage=messageDao.getLastMessage(userId, friendId);
        if(lastMessage==null){
            return "";
        }
        return lastMessage.getMessageContent();
    }

    @Override
    public List<Message> loadAllMessages(long userId, long friendId) {
        return messageDao.getAllMessages(userId, friendId);
    }

    @Override
    public Message sendMessage(long userId, long friendId, String content) {
        /*
        创建发送信息
         */
        Message sendMessage=new Message();
        sendMessage.setUserId(userId);
        sendMessage.setFriendId(friendId);
        sendMessage.setType(Message.TEXT_SENT);
        sendMessage.setMessageContent(content);
        messageDao.insertNewMessage(sendMessage);

        /*
        创建接收信息
         */
        Message receivedMessage=new Message();
        receivedMessage.setUserId(friendId);
        receivedMessage.setFriendId(userId);
        receivedMessage.setType(Message.TEXT_RECEIVED);
        receivedMessage.setMessageContent(content);
        messageDao.insertNewMessage(receivedMessage);

        return sendMessage;
    }

}
