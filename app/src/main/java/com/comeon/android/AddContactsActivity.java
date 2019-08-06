package com.comeon.android;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comeon.android.adapter.UserAdapter;
import com.comeon.android.business_logic.UserBusiness;
import com.comeon.android.business_logic.UserBusinessInterface;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.Activity_Parent;
import com.comeon.android.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class AddContactsActivity extends Activity_Parent implements View.OnClickListener {

    /*
        控件
     */
    ImageButton btn_goBack;
    EditText editText_searchContent;
    LinearLayout linearLayout_scan;
    LinearLayout linearLayout_mobileContacts;
    TextView textView_emptyTip;

    UserBusinessInterface userBusiness = new UserBusiness();
    RecyclerView recyclerView;
    private List<UserInfo> userList = new ArrayList<UserInfo>();
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        //修改状态栏颜色
        ViewUtil.setStatusBarColor(this, Color.rgb(255, 255, 255), false);

        initControls(); //先初始化控件
        bindEvents();  //再绑定事件
    }

    /**
     * 初始化控件
     */
    private void initControls() {
        btn_goBack = (ImageButton) findViewById(R.id.btn_goback);
        editText_searchContent = (EditText) findViewById(R.id.search_text);
        linearLayout_scan = (LinearLayout) findViewById(R.id.scan_layout);
        linearLayout_mobileContacts = (LinearLayout) findViewById(R.id.mobile_contacts_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        textView_emptyTip = (TextView) findViewById(R.id.text_empty_result_tip);
    }

    /**
     * 为控件绑定事件
     */
    private void bindEvents() {
        btn_goBack.setOnClickListener(this);  //用当前类重写点击事件

        //设置recyclerview的layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        editText_searchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().trim();
                /*
                    根据是否有输入内容，选择是多种搜索方式的显示还是让查询的结果显示
                    有文字：显示搜索结果
                    没有文字：显示多种搜索方式
                 */
                if (searchText.length() > 0) {
                    linearLayout_scan.setVisibility(View.GONE);
                    linearLayout_mobileContacts.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    /*
                        根据搜索内容填充recyclerview
                        有结果：显示结果
                        没有结果：提示文字显示
                     */
                    List<UserInfo> users = userBusiness.getUsersByPhone(searchText);//根据输入的手机号进行查询

                    if (users != null) {
                        /*
                            要排除自己
                         */
                        for (int i = 0; i < users.size(); i++) {
                            if (users.get(i).getId() == loginUser.getId()) {
                                users.remove(i);
                                break;
                            }
                        }
                        if (userAdapter == null) {  //如果适配器为空，则实例化适配器
                            userList = users;
                            userAdapter = new UserAdapter(userList, loginUser);
                            recyclerView.setAdapter(userAdapter);
                        } else {  //如果适配器不为空，则刷新数据
                            reloadRecyclerView(users);
                        }
                        if (users.size() == 0) { //如果排除后没有结果
                            textView_emptyTip.setVisibility(View.VISIBLE);  //如果没有结果就显示无结果文字提示
                        } else {
                            textView_emptyTip.setVisibility(View.GONE);  //如果有结果就隐藏无结果文字提示
                        }

                    } else { //查询无结果
                        textView_emptyTip.setVisibility(View.VISIBLE);  //如果没有结果就显示无结果文字提示
                    }
                } else {
                    textView_emptyTip.setVisibility(View.GONE);  //未在查询的时候隐藏无结果文字提示
                    linearLayout_scan.setVisibility(View.VISIBLE);
                    linearLayout_mobileContacts.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

        linearLayout_scan.setOnClickListener(this);   //用当前类重写点击事件
        linearLayout_mobileContacts.setOnClickListener(this);   //用当前类重写点击事件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_goback:
                finish(); //点击返回键退出当前活动
                break;
            case R.id.scan_layout:
                Toast.makeText(this, "暂不支持扫码添加", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mobile_contacts_layout:
                /*
                    进入通讯录
                 */
                break;
        }
    }

    /**
     * 重新拉取数据
     *
     * @param users 查询到的用户列表
     */
    private void loadData(List<UserInfo> users) {
        if (userList != null) {
            userList.clear();
            if (users != null) {
                userList.addAll(users);
            }
        }
    }

    /**
     * UI层刷新好友列表
     *
     * @param users 输入的好友列表
     */
    private void reloadRecyclerView(List<UserInfo> users) {
        //1、先拉取数据
        loadData(users);
        //通知数据发生了改变，重新加载
        userAdapter.notifyDataSetChanged();
    }


}
