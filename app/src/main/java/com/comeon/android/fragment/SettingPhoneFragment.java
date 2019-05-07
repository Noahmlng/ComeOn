package com.comeon.android.fragment;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.comeon.android.R;

/**
 * 注册：设置手机号码页碎片
 */
public class SettingPhoneFragment extends Fragment {

    Button btn_getValidateCode;
    Spinner phone_index;
    EditText phone;
    EditText validateCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.setting_phone_fragment, container, false);
        initControls(view);
        //如果有缓存数据，则先加载缓存数据
        if(savedInstanceState!=null){
            phone.setText(savedInstanceState.getString("phone"));
        }
        return view;
    }

    /**
     * 初始化控件
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initControls(View view){
        btn_getValidateCode=(Button)view.findViewById(R.id.btn_getValidateCode);
        phone_index=(Spinner)view.findViewById(R.id.phone_index);
        phone=(EditText)view.findViewById(R.id.phone);
        validateCode=(EditText)view.findViewById(R.id.validate_code);

        //为下拉选项框装载数据
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new String[]{"+86"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phone_index.setAdapter(adapter);
        phone_index.setSelection(0);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("phone",phone.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
