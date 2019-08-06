package com.comeon.android.db_accessing;

import com.comeon.android.db.Message;
import com.comeon.android.util.Utilities;

import org.litepal.LitePal;

import java.util.List;

import okhttp3.internal.Util;

/**
 * 消息的数据表接口实现类
 */
public class MessageDaoImpl extends BaseDao implements MessageDao {
    @Override
    public Message getLastMessage(long userId, long friendId) {
        Message lastMessage=LitePal.where("userId = ? and friendId = ?",String.valueOf(userId), String.valueOf(friendId)).findLast(Message.class);
        return lastMessage;
    }

    @Override
    public List<Message> getAllMessages(long userId, long friendId) {
        return LitePal.where("userId = ? and friendId = ?",String.valueOf(userId), String.valueOf(friendId)).order("sendTime").find(Message.class);
    }

    @Override
    public Message insertNewMessage(Message msg) {
        msg.setSendTime(Utilities.getNow());
        msg.save();
        return msg;
    }
}
