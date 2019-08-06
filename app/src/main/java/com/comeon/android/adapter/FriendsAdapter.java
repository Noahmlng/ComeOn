package com.comeon.android.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comeon.android.ChatActivity;
import com.comeon.android.R;
import com.comeon.android.business_logic.UserBusiness;
import com.comeon.android.business_logic.UserBusinessInterface;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.MyApplication;
import com.comeon.android.util.Utilities;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 好友页的适配器
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private static final String TAG = "FriendsAdapter";  //报错时标注的标签
    UserBusinessInterface userBusiness = new UserBusiness();  //调用业务层的方法
    private List<UserInfo> friends;  //数据列表
    private UserInfo loginUser; //传递进入的已登录用户

    /**
     * 有参构造方法
     *
     * @param friends   好友列表
     * @param loginUser 已登录用户
     */
    public FriendsAdapter(List<UserInfo> friends, UserInfo loginUser) {
        this.friends = friends;
        this.loginUser = loginUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        //绑定点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo friend = friends.get(viewHolder.getAdapterPosition());
                /*
                    点击好友项时，进入与该好友的聊天页面
                 */
                //跳转至聊天活动
                try {
                    ChatActivity.enterChatPage(MyApplication.getContext(), friend.getId());
                } catch (NullPointerException ex) {
                    LogUtil.e(TAG, "读取好友的id失败，空指针异常：" + ex.getMessage());
                } catch (Exception ex) {
                    LogUtil.e(TAG, "进入好友聊天页面时，出现未知异常：" + ex.getMessage());
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        UserInfo friend = friends.get(i);
        try{
            viewHolder.head_icon.setImageBitmap(Utilities.translateBytes(friend.getHeadIcon()));  //读取好友头像
            viewHolder.txt_friendName.setText(friend.getUserNickName());  //读取好友的昵称
        }catch (NullPointerException ex) {
            LogUtil.e(TAG, "好友页读取好友基本信息失败，空指针异常：" + ex.getMessage());
        } catch (Exception ex) {
            LogUtil.e(TAG, "进入好友聊天页面时，出现未知异常：" + ex.getMessage());
        }

        viewHolder.txt_lastMessage.setText(userBusiness.getLastMessageContent(loginUser.getId(), friend.getId()));  //读取与该好友的最后一条信息
    }

    @Override
    public int getItemCount() {
        if (friends != null) {
            return friends.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView head_icon;
        TextView txt_friendName;
        TextView txt_lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            head_icon = (CircleImageView) itemView.findViewById(R.id.head_icon);
            txt_friendName = (TextView) itemView.findViewById(R.id.txt_friendName);
            txt_lastMessage = (TextView) itemView.findViewById(R.id.txt_lastMessage);
        }
    }
}
