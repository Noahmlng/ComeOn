package com.comeon.android.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private static final String TAG = "UserAdapter";  //报错时标注的标签
    UserBusinessInterface userBusiness = new UserBusiness();  //调用业务层的方法
    private List<UserInfo> users;  //数据列表
    private UserInfo loginUser; //传递进入的已登录用户（判断用户是否已经添加搜索的用户为好友）

    /**
     * 有参构造方法
     * @param users   用户列表
     * @param loginUser 已登录用户
     */
    public UserAdapter(List<UserInfo> users, UserInfo loginUser) {
        this.users = users;
        this.loginUser = loginUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        //绑定点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                进入用户详情页
                 */
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        UserInfo user = users.get(i);
        try{
            viewHolder.head_icon.setImageBitmap(Utilities.translateBytes(user.getHeadIcon()));  //读取用户头像
            viewHolder.txt_userName.setText(user.getUserNickName());  //读取用户的昵称
            viewHolder.txt_userDescription.setText(user.getDescription());  //读取用户的个性签名
            /*
                查询该用户是不是已经是登录用户的好友
                是：显示tip
                否：显示添加好友键
             */
                            /*
                    绑定添加好友的事件
                 */
            viewHolder.btn_addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        /*
                            添加好友的操作
                            添加成功：Toast提示添加成功，并将添加好友按钮替换为已添加
                            添加失败：Toast提示添加失败
                         */
                    boolean result=userBusiness.addFriend(user.getId(),loginUser.getId());
                    if (result){
                        Toast.makeText(MyApplication.getContext(), "添加成功！",Toast.LENGTH_SHORT).show();
                        viewHolder.btn_addFriend.setVisibility(View.GONE);
                        viewHolder.txt_tip.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(MyApplication.getContext(), "添加失败",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if (userBusiness.checkIsFriend(user.getId(), loginUser.getId())){
                viewHolder.btn_addFriend.setVisibility(View.GONE);
                viewHolder.txt_tip.setVisibility(View.VISIBLE);
            }else{
                viewHolder.btn_addFriend.setVisibility(View.VISIBLE);
                viewHolder.txt_tip.setVisibility(View.GONE);
            }
        }catch (NullPointerException ex) {
            LogUtil.e(TAG, "添加好友活动读取用户基本信息失败，空指针异常：" + ex.getMessage());
        } catch (Exception ex) {
            LogUtil.e(TAG, "进入好友聊天页面时，出现未知异常：" + ex.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        if (users != null) {
            return users.size();
        }
        return 0;
    }

    /*
        内部类定义控件包
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView head_icon;
        TextView txt_userName;
        TextView txt_userDescription;
        Button btn_addFriend;
        TextView txt_tip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            head_icon = (CircleImageView) itemView.findViewById(R.id.head_icon);
            txt_userName = (TextView) itemView.findViewById(R.id.txt_userName);
            txt_userDescription = (TextView) itemView.findViewById(R.id.txt_description);
            btn_addFriend=(Button)itemView.findViewById(R.id.btn_addFriend);
            txt_tip=(TextView)itemView.findViewById(R.id.txt_already_added_tip);
        }
    }
}
