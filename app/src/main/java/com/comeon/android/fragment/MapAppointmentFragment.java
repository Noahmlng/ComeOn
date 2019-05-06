package com.comeon.android.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.comeon.android.R;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 地图组团的碎片
 */
public class MapAppointmentFragment extends Fragment {

    MapView bMapView;
    BaiduMap bMap;
    UiSettings bMapUISettings;

    public LocationClient mLocationClient;

    public static final int MAP_PERMISSION_REQUEST_CODE = 1;

    private static final String TAG = "MapAppointmentFragment";

    private static boolean isFirst = true;

    private MapStatus last_status;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getActivity());
        initLocationSettings();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(MyApplication.getContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        isFirst = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //申请权限
        requestPermission();
        //加载个性化地图文件
        setMapCustomFile(getActivity(), "custom_map_config.json");
        //填充布局
        View view = inflater.inflate(R.layout.fragment_map_appointment, container, false);
        bMapView = view.findViewById(R.id.bmapView);
        //设置百度地图logo的位置
        bMapView.setLogoPosition(LogoPosition.logoPostionleftTop);
        bMapView.setPadding(5, 5, 5, 5);
        //显示比例尺
        bMapView.showScaleControl(true);
        bMap = bMapView.getMap();

        MapView.setMapCustomEnable(true);
        //设置我的位置可用
        bMap.setMyLocationEnabled(true);
        //注册监控地图状态变化的监听器
        bMap.setOnMapStatusChangeListener(new MapStatusChangeListener());

        bMapUISettings = bMap.getUiSettings();
        //关闭指南针
        bMapUISettings.setCompassEnabled(false);
        //开启缩放手势
        bMapUISettings.setZoomGesturesEnabled(true);
        //开启平移手势
        bMapUISettings.setScrollGesturesEnabled(true);


        /*
        根据最后一次的位置，构建我的位置
        第一次进入时，last_location为空，所以进行非空判断
         */
        BDLocation last_location = mLocationClient.getLastKnownLocation();
        if (last_location != null) {
            //建立位置标识
            buildSignal(last_location);
        }

        //回到上次操作地图的状态
        if (last_status != null) {
            MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(last_status);
            bMap.animateMapStatus(update);
        }
        return view;
    }


    /**
     * 将个性化文件写入本地后调用MapView.setCustomMapStylePath加载
     *
     * @param context
     * @param fileName assets目录下自定义样式文件的文件名
     */
    private void setMapCustomFile(Context context, String fileName) {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        String moduleName = null;
        try {
            inputStream = context.getAssets().open("customConfigDir/" + fileName);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            moduleName = context.getFilesDir().getAbsolutePath();
            File file = new File(moduleName + "/" + fileName);
            if (file.exists())
                file.delete();
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            //将自定义样式文件写入本地
            fileOutputStream.write(b);
        } catch (IOException e) {
            LogUtil.e(TAG, e.getMessage());
            LogUtil.e(TAG, e.getMessage() + moduleName);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                LogUtil.e(TAG, e.getMessage());
            }
        }
        //设置自定义样式文件
        MapView.setCustomMapStylePath(moduleName + "/" + fileName);
    }

    /**
     * 开始获取位置的请求
     */
    private void requestLocation() {
        mLocationClient.start();
    }

    /**
     * 处于活动状态时触发
     */
    @Override
    public void onResume() {
        bMapView.onResume();
        super.onResume();
    }

    /**
     * 当碎片处于暂停状态时触发
     */
    @Override
    public void onPause() {
        bMapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 当碎片销毁时触发
     */
    @Override
    public void onDestroy() {
        mLocationClient.stop();
        bMap.setMyLocationEnabled(false);
        // 销毁地图前线关闭个性化地图，否则会出现资源无法释放
        MapView.setMapCustomEnable(false);
        bMapView.onDestroy();
        bMapView = null;
        super.onDestroy();
    }


    /**
     * 在某某位置设置我的位置图标（包括旁边的辐射圈）
     *
     * @param location
     */
    private void buildSignal(BDLocation location) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(location.getDirection()).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        bMap.setMyLocationData(locData);
    }


    /**
     * 将地图焦点放置与location
     * @param location  位置装载器（经纬度）
     * @param zoomValue 放大缩小的比例尺值
     */
    private void navigateTo(LatLng location, float zoomValue) {
        //2、创建一个更新状态的对象，将位置和地图缩放级别逐个进行更新
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(location);
        bMap.animateMapStatus(update);

        update = MapStatusUpdateFactory.zoomTo(zoomValue);
        bMap.animateMapStatus(update);
    }

    /**
     * 创建LocationClientOption对象设置LocationClient对象的属性
     */
    private void initLocationSettings() {
        //通过一个对象来保存定位设置
        LocationClientOption option = new LocationClientOption();
        //设置更新位置的间隔
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        /*
        设置定位模式
        1、只有在用户的手机GPS定位设置为高精度模式和仅限设备模式才可以使用GPS定位
        2、三个可选值分别为：High_Accuracy（默认模式）、Battery_Saving、Device_Sensors
        3、只要手机拿到室外可以接收到GPS信号，就可以自动切换到GPS定位模式
         */
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        //获取位置信息一定需要网络，因此即使我们将定位模式指定为Device_Sensors，也会自动开启网络定位功能
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new MyLocatioListener());
    }

    /**
     * 申请权限
     */
    private void requestPermission() {
        List<String> permissionList = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }

        //如果还有需要申请的权限，则开始发起申请
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, MAP_PERMISSION_REQUEST_CODE);
        } else {
            requestLocation();
        }
    }

    /**
     * 处理返回的权限结果
     *
     * @param requestCode  请求码
     * @param permissions  权限列表
     * @param grantResults 申请权限的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MAP_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            //当用户拒绝授予权限时的操作
                        }
                    }
                    requestLocation();
                }
                break;
        }
    }

    /**
     * 内部类的形式监听我的位置情况
     */
    public class MyLocatioListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //            mapView 销毁后不在处理新接收的位置
            if (bdLocation == null || bMapView == null) {

                return;
            }
            //            获取位置信息后执行的操作
            //            第一次进入：建立我的位置标识+确定至我的位置
            if (isFirst) {
                navigateTo(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()), 15.5f);
                isFirst = false;
                MapStatus.Builder builder = new MapStatus.Builder();
                last_status = builder.target(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude())).zoom(15.5f).build();
            }
            buildSignal(bdLocation);
        }
    }

    /**
     * 内部类监听地图变化（包括：拖拽、双击、滑动地图）
     */
    public class MapStatusChangeListener implements BaiduMap.OnMapStatusChangeListener {

        /**
         * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
         *
         * @param status 地图状态改变开始时的地图状态
         */
        public void onMapStatusChangeStart(MapStatus status) {
        }

        /**
         * 因某种操作导致地图状态开始改变。
         *
         * @param status 地图状态改变开始时的地图状态
         * @param reason 表示地图状态改变的原因
         *               取值有： 1：用户手势触发导致的地图状态改变,比如双击、拖拽、滑动底图
         *               2：SDK导致的地图状态改变, 比如点击缩放控件、指南针图标
         *               3：开发者调用,导致的地图状态改变
         */
        public void onMapStatusChangeStart(MapStatus status, int reason) {

        }

        /**
         * 地图状态变化中
         *
         * @param status 当前地图状态
         */
        public void onMapStatusChange(MapStatus status) {
        }

        /**
         * 地图状态改变结束
         *
         * @param status 地图状态改变结束后的地图状态
         */
        public void onMapStatusChangeFinish(MapStatus status) {
            //记录下最后一次的状态
            last_status = status;
        }
    }


}
