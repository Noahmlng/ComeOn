package com.comeon.android.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.comeon.android.ChatActivity;
import com.comeon.android.R;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.MyApplication;

import java.util.List;

/**
 * 参与者数据的适配器
 */
public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder> {
    private List<UserInfo> participants;
    private UserInfo loginUser;

    public ParticipantAdapter(List<UserInfo> participants, UserInfo loginUser){
        this.participants=participants;
        this.loginUser=loginUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.participant_item, viewGroup, false);
        ViewHolder viewHolder=new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final UserInfo participant=participants.get(i);

        viewHolder.txt_participantName.setText(participant.getUserNickName());
        viewHolder.btn_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入到聊天页面的操作.....
                                /*
                排除和自己聊天的情况
                 */
                if(participant.getId()==loginUser.getId()){
                    Toast.makeText(MyApplication.getContext(), "无法和自己发起会话",Toast.LENGTH_SHORT).show();
                    return;
                }
                ChatActivity.enterChatPage(MyApplication.getContext(), participant.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_participantName;
        ImageButton btn_sendMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_participantName=(TextView)itemView.findViewById(R.id.txt_participantName);
            btn_sendMessage=(ImageButton)itemView.findViewById(R.id.btn_sendMessage);
        }
    }
}
