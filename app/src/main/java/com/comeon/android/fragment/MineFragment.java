package com.comeon.android.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.comeon.android.MainActivity;
import com.comeon.android.R;
import com.comeon.android.db.UserInfo;
import com.comeon.android.util.Utilities;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的碎片页
 */
public class MineFragment extends BaseFragment {

    Button btn_exit;
    CircleImageView head_icon;

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
                getActivity().finish();
                /*
                    清除SharedPreferences文件中的数据
                 */
                SharedPreferences sp = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("phone", "");
                editor.putString("password", "");
                editor.apply();

                //                Intent goBack=new Intent(getActivity(), StartActivity.class);
                //                startActivity(goBack);
            }
        });

        head_icon=(CircleImageView)view.findViewById(R.id.head_icon);
        if(loginUser!=null){
            head_icon.setImageBitmap(Utilities.translateBytes(loginUser.getHeadIcon()));
        }
    }
}
