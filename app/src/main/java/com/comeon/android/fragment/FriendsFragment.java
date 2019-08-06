package com.comeon.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.comeon.android.AddContactsActivity;
import com.comeon.android.MainActivity;
import com.comeon.android.R;
import com.comeon.android.adapter.FriendsAdapter;
import com.comeon.android.business_logic.UserBusiness;
import com.comeon.android.business_logic.UserBusinessInterface;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.MyApplication;

import java.util.List;

/**
 * 好友碎片
 */
public class FriendsFragment extends BaseFragment {
    private static final String TAG = "FriendsFragment";
    FriendsAdapter friendsAdapter;
    UserBusinessInterface userBusiness = new UserBusiness();
    private ImageButton btn_addFriend;
    private EditText editText_searchContent;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserInfo loginUser;
    private List<UserInfo> friendList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginUser = ((MainActivity) getActivity()).getLoginUser();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_friends;
    }

    @Override
    protected void initControls(View view) {
        btn_addFriend = (ImageButton) view.findViewById(R.id.btn_addFriend);
        btn_addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginUser == null) {
                    Toast.makeText(MyApplication.getContext(), "游客无法添加好友", Toast.LENGTH_SHORT).show();
                    return;
                }
                //创建Intent对象并进行实例化 ---> new Intent(Context packageContext, Class<?> cls)
                Intent intent = new Intent(FriendsFragment.this.getActivity(), AddContactsActivity.class);
                //调用Activity类中的startActivity()方法用于启用活动
                startActivity(intent);
            }
        });

        editText_searchContent = (EditText) view.findViewById(R.id.search_text);
        editText_searchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshContent(s.toString().trim());
            }

        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        if (loginUser != null) {
            friendList = userBusiness.getAllFriends(loginUser.getId());
            friendsAdapter = new FriendsAdapter(friendList, loginUser);
            recyclerView.setAdapter(friendsAdapter);
        }
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String search_name = editText_searchContent.getText().toString();
                if (loginUser != null) {

                    if (search_name.length() == 0) {
                        reloadRecyclerView(userBusiness.getAllFriends(loginUser.getId()));
                    } else {
                        reloadRecyclerView(userBusiness.getFriendsByName(loginUser.getId(), search_name));
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });
    }

    /**
     * 刷新场馆、订单的内容
     */
    public void refreshContent(String inputText) {
        //输入结束后的动态模糊查询
        LogUtil.d(TAG, "当前文字为：" + inputText + "；长度为：" + inputText.length());
        if (loginUser != null) {

            if (inputText.length() > 0) {
                LogUtil.d(TAG, "正在根据好友名查询用户");
                reloadRecyclerView(userBusiness.getFriendsByName(loginUser.getId(), inputText));
            } else if (inputText.length() == 0) {
                reloadRecyclerView(userBusiness.getAllFriends(loginUser.getId()));
            }
        }
    }

    /**
     * 重新拉取数据
     *
     * @param friends 好友列表
     */
    private void loadData(List<UserInfo> friends) {
        if (friendList != null) {
            friendList.clear();
            if (friends != null) {
                friendList.addAll(friends);
            }
        }
    }

    /**
     * UI层刷新好友列表
     *
     * @param friendList 输入的好友列表
     */
    private void reloadRecyclerView(List<UserInfo> friendList) {
        //1、先拉取数据
        loadData(friendList);
        //通知数据发生了改变，重新加载
        friendsAdapter.notifyDataSetChanged();
    }


}
