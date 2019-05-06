package com.comeon.android.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.comeon.android.R;
import com.comeon.android.util.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 注册：设置个人信息页碎片
 */
public class SettingPersonalInfoFragment extends Fragment {

    private static final String TAG = "SettingPersonalInfoFrag";

    EditText editText_nickname;
    Spinner birthday_year;
    Spinner birthday_month;
    Spinner birthday_day;
    RadioButton sex_male;
    RadioButton sex_female;

    int selectedYear;
    int selectedMonth;
    int selectedDay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.from(getActivity()).inflate(R.layout.setting_personal_info_fragment,container, false);
        initControls(view);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initControls(View view){
        editText_nickname=(EditText)view.findViewById(R.id.nickname);
        birthday_year=(Spinner) view.findViewById(R.id.birthday_year);
        birthday_month=(Spinner)view.findViewById(R.id.birthday_month);
        birthday_day=(Spinner)view.findViewById(R.id.birthday_day);
        sex_male=(RadioButton)view.findViewById(R.id.sex_male);
        sex_female=(RadioButton)view.findViewById(R.id.sex_female);

        /*
            清除单选按钮所自带的圆圈
         */
        Bitmap clearCircle=null;
        sex_male.setButtonDrawable(new BitmapDrawable(clearCircle));
        sex_female.setButtonDrawable(new BitmapDrawable(clearCircle));

        /*
            监听单选按钮的状态，动态修改图片
         */
        sex_male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sex_male.setBackgroundResource(R.drawable.male_checked);
                }else{
                    sex_male.setBackgroundResource(R.drawable.male);
                }
            }
        });

        sex_female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sex_female.setBackgroundResource(R.drawable.female_checked);
                }else{
                    sex_female.setBackgroundResource(R.drawable.female);
                }
            }
        });

        /*
            填充年月日的生日选项
         */
        final Integer[] years=getYears();
        selectedYear=years[0];
        ArrayAdapter<Integer> adapter=new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        birthday_year.setAdapter(adapter);
        birthday_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //先记录之前选择的年份
                int pre_selectedYear=selectedYear;

                selectedYear=years[position];
                //调用刷新的方法
                if(isRequiredRefresh(pre_selectedYear,selectedMonth)){
                    refreshDaysAdapter();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Integer[] months=getMonth();
        selectedMonth=months[0];
        adapter=new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, getMonth());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        birthday_month.setAdapter(adapter);
        birthday_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //先记录之前选择的月份
                int pre_selectedMonth=selectedMonth;

                selectedMonth=months[position];
                if(isRequiredRefresh(selectedYear, pre_selectedMonth)){
                    refreshDaysAdapter();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Integer[] days=getDays(selectedYear,selectedMonth);
        selectedDay=days[0];
        adapter=new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /*
            记忆用户的上一次输入，致使用户更改年份和月份时无需再一次选日期
         */
        birthday_day.setAdapter(adapter);
        birthday_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDay=days[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 判断是否需要刷新
     * @param pre_selectedYear  之前选择的年份
     * @param pre_selectedMonth  之前选择的月份
     * @return
     */
    private boolean isRequiredRefresh(int pre_selectedYear, int pre_selectedMonth){
        List<Integer> oddMonth=new ArrayList<Integer>();
        oddMonth.add(1);
        oddMonth.add(3);
        oddMonth.add(5);
        oddMonth.add(7);
        oddMonth.add(8);
        oddMonth.add(10);
        oddMonth.add(12);

        List<Integer> evenMonth=new ArrayList<Integer>();
        evenMonth.add(4);
        evenMonth.add(6);
        evenMonth.add(9);
        evenMonth.add(11);

        /*
            第一层判断之前选择的月份
            第二层判断现在选择的月份
            进行比较
         */
        if(oddMonth.contains(pre_selectedMonth)){
            if(oddMonth.contains(selectedMonth)){
                return false;
            }
        }else if(evenMonth.contains(pre_selectedMonth)){
            if(evenMonth.contains(selectedMonth)){
                return false;
            }
        }else{
            if(selectedMonth!=2){
                return true;
            }else {
                //现在也是二月的情况则要判断闰年
                //普通闰年   世纪闰年
                if (pre_selectedYear % 4 == 0 && pre_selectedYear % 100 != 0 || pre_selectedYear % 400 == 0) {
                    if (selectedYear % 4 == 0 && selectedYear % 100 != 0 || selectedYear % 400 == 0) {
                        return false;
                    }
                } else {
                    if (selectedYear % 4 == 0 && selectedYear % 100 != 0 || selectedYear % 400 == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * 刷新日的装载器
     */
    private void refreshDaysAdapter(){
        Integer[] days=getDays(selectedYear,selectedMonth);
        ArrayAdapter<Integer> adapter=new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        birthday_day.setAdapter(adapter);

        /*
            视情况选择之前选择的日期
            如果当前选择的日期超过了新日子极限，则调为1
         */
        if(selectedDay>days.length){
            birthday_day.setSelection(0);
            selectedDay=days[0];
        }else{
            birthday_day.setSelection(selectedDay-1);
        }
    }


    /**
     * 获取所有年份
     * @return
     */
    public Integer[] getYears(){
        List<Integer> yearList=new ArrayList<Integer>();

        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        for(int i=1930; i<=year; i++){
            yearList.add(i);
        }
        Integer[] years=new Integer[yearList.size()];
        years=yearList.toArray(years);
        return years;
    }

    /**
     * 获取所有月份
     * @return
     */
    public Integer[] getMonth(){
        Integer[] months=new Integer[12];
        for(int i=1; i<=12; i++){
            months[i-1]=i;
        }
        return months;
    }

    /**
     * 获取所有日期
     * @return
     */
    public Integer[] getDays(int year,int month){
        int days=0;
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days=31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days=30;
                break;
            case 2:
                //普通闰年   世纪闰年
                if(year%4==0 && year%100!=0 || year%400==0){
                    days=29;
                }else{
                    days=28;
                }
                break;
        }
        Integer[] date=new Integer[days];
        for(int i=1; i<=days;i++){
            date[i-1]=i;
        }
        return date;
    }
}
