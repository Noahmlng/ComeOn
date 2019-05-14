package com.comeon.android.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comeon.android.R;
import com.comeon.android.db.Message;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.MyApplication;
import com.comeon.android.util.Utilities;

import org.litepal.LitePal;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 聊天记录的适配器
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> messages;

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public MessageAdapter(List<Message> messages){this.messages=messages;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item, viewGroup, false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Message message=messages.get(i);
        if(message.getType()==Message.TEXT_SENT){
            //隐藏接收框
            viewHolder.messageReceivedLayout.setVisibility(View.GONE);

            //设置发送框
            viewHolder.messageSentLayout.setVisibility(View.VISIBLE);
            viewHolder.txt_sendText.setText(message.getMessageContent());
            viewHolder.userHeadIcon.setImageBitmap(Utilities.translateBytes(LitePal.find(UserInfo.class,message.getUserId()).getHeadIcon()));
        }else if(message.getType()==Message.TEXT_RECEIVED){
            //隐藏发送框
            viewHolder.messageSentLayout.setVisibility(View.GONE);

            //设置接收框
            viewHolder.messageReceivedLayout.setVisibility(View.VISIBLE);
            viewHolder.txt_friendText.setText(message.getMessageContent());
            viewHolder.friendHeadIcon.setImageBitmap(Utilities.translateBytes(LitePal.find(UserInfo.class,message.getFriendId()).getHeadIcon()));
        }
    }

    @Override
    public int getItemCount() {
        if(messages!=null){
            return messages.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView friendHeadIcon;
        TextView txt_friendText;
        LinearLayout messageReceivedLayout;

        CircleImageView userHeadIcon;
        TextView txt_sendText;
        LinearLayout messageSentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendHeadIcon=(CircleImageView)itemView.findViewById(R.id.friend_headIcon);
            txt_friendText=(TextView)itemView.findViewById(R.id.txt_receivedMessage);
            messageReceivedLayout=(LinearLayout)itemView.findViewById(R.id.linearLayoutReceived);

            userHeadIcon=(CircleImageView)itemView.findViewById(R.id.user_headIcon);
            txt_sendText=(TextView)itemView.findViewById(R.id.txt_sentMessage);
            messageSentLayout=(LinearLayout)itemView.findViewById(R.id.linearLayoutSent);
        }
    }
}
