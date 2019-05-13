package com.comeon.android.fragment;

import android.view.View;
import android.widget.EditText;

import com.comeon.android.R;

/**
 * 注册：输入密码页
 */
public class SettingPasswordFragment extends BaseFragment {
    public EditText editText_password;
    public EditText editText_checkPassword;

    @Override
    protected int getContentViewId() {
        return R.layout.setting_password_fragment;
    }

    @Override
    protected void initControls(View view) {
        editText_password = (EditText) view.findViewById(R.id.password);
        editText_checkPassword = (EditText) view.findViewById(R.id.check_password);
    }
}
