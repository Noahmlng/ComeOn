package com.comeon.android.adapter;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comeon.android.ChatActivity;
import com.comeon.android.R;
import com.comeon.android.business_logic.OrderBusiness;
import com.comeon.android.business_logic.OrderBusinessInterface;
import com.comeon.android.business_logic.UserBusiness;
import com.comeon.android.business_logic.UserBusinessInterface;
import com.comeon.android.db.Friends;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.MyApplication;
import com.comeon.android.util.Utilities;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 好友页的适配器
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private List<UserInfo> friends;
    private UserInfo loginUser;

    UserBusinessInterface userBusiness=new UserBusiness();

    public void setFriends(List<UserInfo> friends) {
        this.friends = friends;
    }

    public FriendsAdapter(List<UserInfo> friends, UserInfo loginUser){
        this.friends=friends;
        this.loginUser=loginUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_item,viewGroup,false);
        final ViewHolder viewHolder=new ViewHolder(view);

        //绑定点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo friend=friends.get(viewHolder.getAdapterPosition());
                //跳转至聊天活动
                ChatActivity.enterChatPage(MyApplication.getContext(),friend.getId());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        UserInfo friend =friends.get(i);
        viewHolder.head_icon.setImageBitmap(Utilities.translateBytes(friend.getHeadIcon()));
        viewHolder.txt_lastMessage.setText(userBusiness.getLastMessageContent(loginUser.getId(), friend.getId()));
        viewHolder.txt_friendName.setText(friend.getUserNickName());
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView head_icon;
        TextView txt_friendName;
        TextView txt_lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            head_icon=(CircleImageView)itemView.findViewById(R.id.head_icon);
            txt_friendName=(TextView)itemView.findViewById(R.id.txt_friendName);
            txt_lastMessage=(TextView)itemView.findViewById(R.id.txt_lastMessage);
        }
    }
}
