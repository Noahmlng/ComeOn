package com.comeon.android.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.comeon.android.R;
import com.comeon.android.RegistrationActivity;
import com.comeon.android.business_logic.UserBusiness;
import com.comeon.android.business_logic.UserBusinessInterface;
import com.comeon.android.controls.GradientTextButton;
import com.comeon.android.util.LogUtil;

import java.util.Random;

/**
 * 注册：设置手机号码页碎片
 */
public class SettingPhoneFragment extends Fragment implements View.OnClickListener {
    public GradientTextButton btn_getValidateCode;
    public Spinner phone_index;
    public EditText editText_phone;
    public EditText editText_validateCode;
    private UserBusinessInterface userBusiness = new UserBusiness();
    private String testCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_phone_fragment, container, false);
        initControls(view);
        //如果有缓存数据，则先加载缓存数据
        if (savedInstanceState != null) {
            editText_phone.setText(savedInstanceState.getString("phone"));
            editText_validateCode.setText(savedInstanceState.getString("validateCode"));
        }
        return view;
    }

    /**
     * 初始化控件
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initControls(View view) {
        btn_getValidateCode = (GradientTextButton) view.findViewById(R.id.btn_getValidateCode);
        phone_index = (Spinner) view.findViewById(R.id.phone_index);
        editText_phone = (EditText) view.findViewById(R.id.phone);
        editText_validateCode = (EditText) view.findViewById(R.id.validate_code);

        //为下拉选项框装载数据
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new String[]{"+86"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phone_index.setAdapter(adapter);
        phone_index.setSelection(0);

        btn_getValidateCode.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("phone", editText_phone.getText().toString());
        outState.putString("validateCode", editText_validateCode.getText().toString());
        super.onSaveInstanceState(outState);
    }

    /**
     * 绑定控件的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getValidateCode:
                switch (checkPhone()) {
                    case RegistrationActivity.SUCCESS:
                        sendValidateCode();
                        if (LogUtil.level == LogUtil.NOTING) {
                            Toast.makeText(this.getActivity(), "验证发送成功，请注意接收", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this.getActivity(), "验证发送成功，测试阶段：此验证码为：" + testCode, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case RegistrationActivity.FAULT_PHONENULL:
                        Toast.makeText(this.getActivity(), "手机号码不能为空", Toast.LENGTH_SHORT).show();
                        break;
                    case RegistrationActivity.FAULT_PHONEPATTERN:
                        Toast.makeText(this.getActivity(), "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                        break;
                    case RegistrationActivity.FAULT_DUPLICATEPHONEREGISTRATION:
                        Toast.makeText(this.getActivity(), "此电话号码已被注册，请转到登录页面", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }

    /**
     * 验证手机号码的方法（非空验证+正则表达式验证）
     *
     * @return int返回结果
     */
    private int checkPhone() {
        String phone = editText_phone.getText().toString().trim();
        String validateCode = editText_validateCode.getText().toString().trim();
        /*
            先进行非空验证，之后使用正则表示式进行验证
         */
        if (phone.length() == 0) {
            return RegistrationActivity.FAULT_PHONENULL;
        } else if (!phone.matches("^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$")) {
            /*
            使用正则表达式验证手机号码格式
            */
            return RegistrationActivity.FAULT_PHONEPATTERN;
        } else if (userBusiness.checkPhoneExist(phone)) {
            /*
                查询数据库，查看该手机号码是否已被注册
             */
            return RegistrationActivity.FAULT_DUPLICATEPHONEREGISTRATION;
        }
        return RegistrationActivity.SUCCESS;
    }

    /**
     * 发送验证码
     */
    private void sendValidateCode() {
        String phone = editText_phone.getText().toString().trim();
        //生成随机数验证码
        Random random = new Random();
        String randomCode = String.valueOf(random.nextInt(9000) + 1000);
        //用于测试展示验证码
        testCode = randomCode;
        /*
            因为map集合会自动覆盖，所以无需判断是否已存在
         */
        ((RegistrationActivity) getActivity()).addNewPhoneValidateCodes(phone, randomCode);
    }
}
