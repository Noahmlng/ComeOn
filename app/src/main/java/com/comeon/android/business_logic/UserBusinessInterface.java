package com.comeon.android.business_logic;

import com.comeon.android.db.Message;
import com.comeon.android.db.UserInfo;

import java.util.List;

/**
 * 用户相关业务接口
 */
public interface UserBusinessInterface {

    /**
     * 用户登录的方法
     * @param phone 登录用的手机号
     * @param password   登录密码
     * @return  登录成功的用户 OR null
     */
    UserInfo login(String phone, String password);

    /**
     * 查看手机是否已被注册
     * @param phone  待验证的手机号
     * @return 验证结果
     */
    boolean checkPhoneExist(String phone);

    /**
     * 注册登录用户
     * @param phone  手机号
     * @param password  密码
     * @return  登录用户 OR null
     */
    UserInfo registration(String phone, String password);

    /**
     * 获取一个用户的所有好友
     * @param userId  该用户的id
     * @return  所有好友的信息集合
     */
    List<UserInfo> getAllFriends(long userId);

    /**
     * 根据名字模糊查询获取一个用户的好友
     *
     * @param userId 该用户的id
     * @param name   模糊查询的名字
     * @return 符合条件的好友用户信息集合
     */
    List<UserInfo> getFriendsByName(long userId, String name);

    /**
     * 获取两个用户间最后一条消息记录内容
     * @param userId  用户id
     * @param friendId  好友id
     * @return  记录内容
     */
    String getLastMessageContent(long userId, long friendId);

    /**
     * 获取两个用户间的所有聊天记录
     * @param userId
     * @param friendId
     * @return
     */
    List<Message> loadAllMessages(long userId, long friendId);

    /**
     * 发送信息
     * @param userId
     * @param friendId
     * @param content
     * @return 发送出的信息
     */
    Message sendMessage(long userId, long friendId, String content);

    /**
     * 根据手机号查找用户
     *
     * @param phone 输入的手机号码
     * @return 符合模糊查询的用户
     */
    List<UserInfo> getUsersByPhone(String phone);

    /**
     * 判断查询用户和登录用户是否是好友关系
     *
     * @param searchId    查询用户的id
     * @param loginUserId 登录用户的id
     * @return 判断结果
     */
    boolean checkIsFriend(long searchId, long loginUserId);

    /**
     * 添加好友
     *
     * @param userId      添加的用户的id
     * @param loginUserId 登录用户的id
     * @return 添加结果
     */
    boolean addFriend(long userId, long loginUserId);
}
