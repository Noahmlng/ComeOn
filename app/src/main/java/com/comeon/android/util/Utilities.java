package com.comeon.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;

import com.comeon.android.MainActivity;
import com.comeon.android.R;

import org.litepal.LitePal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

/**
 * 工具类
 */
public class Utilities {
    private static final String TAG = "Utilities";

    /**
     * 更换碎片的方法
     *
     * @param fragmentManager    碎片的管理适配器
     * @param inputFragment      要放入的碎片对象
     * @param fragmentLaytout_id 装载碎片的layout id
     */
    public static void replaceFragment(FragmentManager fragmentManager, Fragment inputFragment, int fragmentLaytout_id) {
        //1、创建一个碎片的事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //2、替换碎片的操作
        fragmentTransaction.replace(fragmentLaytout_id, inputFragment);
        //3、如果需要模拟返回栈的模式（替换的操作相当于让新的碎片处于栈顶，返回键就是出栈）
        fragmentTransaction.addToBackStack(null);
        //4、提交事务
        fragmentTransaction.commit();
    }

    /**
     * 将图片资源转为byte[]数据
     *
     * @param resourceId 图片资源ID
     * @return
     */
    public static byte[] decodeResource(int resourceId) {
        Bitmap bmp = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), resourceId);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //压缩转化
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 将byte数组进行解码为图片
     *
     * @param bytes 传入的byte数组
     * @return
     */
    public static Bitmap translateBytes(byte[] bytes) {
        if (bytes.length != 0) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    /**
     * 将位图转换为byte数组
     *
     * @param bitmap 传入的bitmap位图对象
     * @return
     */
    public static byte[] translateBitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 打印数据表中的所有列的方法
     *
     * @param modelClass modelClass
     */
    public static void printAllColumns(Class modelClass) {
        String className = modelClass.getSimpleName();
        Cursor cursor = LitePal.findBySQL("select * from " + className);
        StringBuilder sb = new StringBuilder("\n");
        for (int i = 1; i <= cursor.getColumnNames().length; i++) {
            sb.append("第" + i + "列为：" + cursor.getColumnName(i - 1) + "\n");
        }
        LogUtil.e("ShowColumns", sb.toString());
    }

    /**
     * 计算放入时间和现在时间的时间差
     * @param inputDate
     * @return
     */
    public static String calculateTimeGapFromNowInMinutes(Date inputDate) {
        String gapInfo = null;
        Calendar now = Calendar.getInstance();

        int nowYear = now.get(Calendar.YEAR);
        int nowMonth = now.get(Calendar.MONTH) + 1;
        int nowDay = now.get(Calendar.DAY_OF_MONTH);
        int nowHour = now.get(Calendar.HOUR);
        int nowMinute = now.get(Calendar.MINUTE);

        int nowMinutes=translateDateIntoMinutes(new Date(nowYear, nowMonth, nowDay, nowHour, nowMinute));
        int inputMinutes=translateDateIntoMinutes(inputDate);

        int minutesDiff=nowMinutes-inputMinutes;

        int diffOnYear=minutesDiff/(60*24*365);
        int diffOnMonth=minutesDiff%(60*24*365)/(60*24*30);
        int diffOnDate=minutesDiff%(60*24*30)/(60*24);
        int diffOnHours=minutesDiff%(60*24)/60;
        int diffOnMinutes=minutesDiff%(60);

        if(diffOnYear==0){
            if(diffOnMonth==0){
                if(diffOnDate==0){
                    if(diffOnHours==0){
                        if(diffOnMinutes==0){
                            return "1分钟";
                        }else{
                            return diffOnMinutes+"分钟";
                        }
                    }else{
                        return diffOnHours+"小时";
                    }
                }else{
                    return diffOnDate+"日";
                }
            }else{
                return diffOnMonth+"月";
            }
        }else{
            return diffOnYear+"年";
        }
    }

    /**
     * 将日期对象转换为分钟值
     * @param inputDate
     * @return
     */
    public static int translateDateIntoMinutes(Date inputDate){
        int year = inputDate.getYear();
        int month = inputDate.getMonth();
        int date = inputDate.getDate();
        int hours = inputDate.getHours();
        int minutes = inputDate.getMinutes();
        return year*(60*24*365)+month*(60*24*30)+date*(60*24)+hours*(60)+minutes;
    }

    /**
     * 获取现在的时间对象
     * @return
     */
    public static Date getNow(){
        Calendar now = Calendar.getInstance();

        int nowYear = now.get(Calendar.YEAR);
        int nowMonth = now.get(Calendar.MONTH) + 1;
        int nowDay = now.get(Calendar.DAY_OF_MONTH);
        int nowHour = now.get(Calendar.HOUR);
        int nowMinute = now.get(Calendar.MINUTE);
        int nowSecond=now.get(Calendar.SECOND);

        return new Date(nowYear, nowMonth, nowDay, nowHour, nowMinute,nowSecond);
    }

}
