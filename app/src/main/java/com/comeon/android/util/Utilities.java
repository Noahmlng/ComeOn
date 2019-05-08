package com.comeon.android.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.comeon.android.R;

import java.io.ByteArrayOutputStream;

/**
 * 工具类
 */
public class Utilities {

    /**
     * 更换碎片的方法
     * @param fragmentManager  碎片的管理适配器
     * @param inputFragment    要放入的碎片对象
     * @param fragmentLaytout_id   装载碎片的layout id
     */
    public static void replaceFragment(FragmentManager fragmentManager, Fragment inputFragment, int fragmentLaytout_id){
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
     * @param resourceId  图片资源ID
     * @return
     */
    public static byte[] decodeResource(int resourceId){
        Bitmap bmp=BitmapFactory.decodeResource(MyApplication.getContext().getResources(), resourceId);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        //压缩转化
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}
