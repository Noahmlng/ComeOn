package com.comeon.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.comeon.android.MainActivity;
import com.comeon.android.R;
import com.comeon.android.adapter.FriendsAdapter;
import com.comeon.android.business_logic.UserBusiness;
import com.comeon.android.business_logic.UserBusinessInterface;
import com.comeon.android.db.UserInfo;

/**
 * 好友碎片
 */
public class FriendsFragment extends BaseFragment {

    private ImageButton btn_addFriend;
    private EditText editText_searchContent;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private UserInfo loginUser;
    FriendsAdapter friendsAdapter;

    UserBusinessInterface userBusiness=new UserBusiness();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginUser=((MainActivity)getActivity()).getLoginUser();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_friends;
    }

    @Override
    protected void initControls(View view) {
        btn_addFriend=(ImageButton)view.findViewById(R.id.btn_addFriend);
        btn_addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "还未开放添加好友功能",Toast.LENGTH_SHORT).show();
            }
        });

        editText_searchContent=(EditText)view.findViewById(R.id.search_text);

        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        friendsAdapter=new FriendsAdapter(userBusiness.getAllFriends(loginUser.getId()),loginUser);
        recyclerView.setAdapter(friendsAdapter);

        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                friendsAdapter=new FriendsAdapter(userBusiness.getAllFriends(loginUser.getId()),loginUser);
                recyclerView.setAdapter(friendsAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
