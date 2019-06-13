package com.comeon.android.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.comeon.android.MainActivity;
import com.comeon.android.R;
import com.comeon.android.StartActivity;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.Utilities;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的碎片页
 */
public class MineFragment extends BaseFragment {

    Button btn_exit;
    CircleImageView head_icon;
    TextView txt_userName;
    TextView txt_description;

    UserInfo loginUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginUser=((MainActivity)getActivity()).getLoginUser();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initControls(View view) {
        btn_exit = (Button) view.findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                /*
                    清除SharedPreferences文件中的数据
                 */
                clearSharedPreferences();
                getActivity().finish();
            }
        });

        head_icon=(CircleImageView)view.findViewById(R.id.head_icon);
        txt_userName=(TextView)view.findViewById(R.id.txt_loginUserName);
        txt_description=(TextView)view.findViewById(R.id.txt_loginUserDescription);
        if(loginUser!=null){
            head_icon.setImageBitmap(Utilities.translateBytes(loginUser.getHeadIcon()));
            txt_userName.setText(loginUser.getUserNickName());
            txt_description.setText(loginUser.getDescription());
        }
    }

    /**
     * 清空SharedPreferences文件中的用户信息
     */
    private void clearSharedPreferences(){
        SharedPreferences sp = getActivity().getSharedPreferences("loginData", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        loginUser=null;
    }
}
