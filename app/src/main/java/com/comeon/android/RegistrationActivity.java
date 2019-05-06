package com.comeon.android;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.comeon.android.util.Activity_Parent;
import com.comeon.android.util.ViewUtil;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RegistrationActivity extends Activity_Parent {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //隐藏当前活动的状态栏（全屏模式）
        ViewUtil.transparentBar(this);

        Integer[] years=getYears();
        Log.d("abcasdf",String.valueOf(years.length));
        ArrayAdapter<Integer> adapter=new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, getYears());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
    public Integer[] getDays(int month){
        Integer[] months=new Integer[12];
        for(int i=1; i<=12; i++){
            months[i-1]=i;
        }
        return months;
    }
}
