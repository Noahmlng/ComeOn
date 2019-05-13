package com.comeon.android.db_accessing;

import com.comeon.android.db.Message;

import java.util.List;

/**
 * 消息的数据表接口
 */
public interface MessageDao {

    /**
     * 获取两人间的最后一条信息
     * @param userId  用户id
     * @param friendId  好友id
     * @return  最后一条信息对象
     */
    Message getLastMessage(long userId, long friendId);

    /**
     * 加载两人间的所有消息记录
     * @param userId
     * @param friendId
     * @return
     */
    List<Message> getAllMessages(long userId, long friendId);

    /**
     * 添加新的消息记录
     * @param msg
     * @return
     */
    Message insertNewMessage(Message msg);
}
