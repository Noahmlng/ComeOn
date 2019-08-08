package com.comeon.android.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.baidu.mapapi.model.LatLng;

import java.util.List;

/**
 * 地图工具类
 */
public class MapUtil {

    /**
     * BD-09 坐标转换成 GCJ-02 坐标
     */
    public static LatLng translateBD2GCJtoGCJ02(LatLng bd) {
        double x = bd.longitude - 0.0065, y = bd.latitude - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);

        double lng = z * Math.cos(theta);//lng
        double lat = z * Math.sin(theta);//lat
        return new LatLng(lat, lng);
    }

    /**
     * GCJ-02 坐标转换成 BD-09 坐标
     */
    public static LatLng translateGCJ2BDtoBD09(LatLng bd) {
        double x = bd.longitude, y = bd.latitude;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;
        return new LatLng(tempLat, tempLon);
    }


    /*
     * 检测程序是否安装
     *
     * @param packageName
     * @return
     */
    public static boolean isInstalled(String packageName) {
        PackageManager manager = MyApplication.getContext().getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
        if (installedPackages != null) {
            for (PackageInfo info : installedPackages) {
                if (info.packageName.equals(packageName))
                    return true;
            }
        }
        return false;

    }
}