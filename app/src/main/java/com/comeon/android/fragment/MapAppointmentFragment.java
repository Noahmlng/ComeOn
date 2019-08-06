package com.comeon.android.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.bikenavi.params.BikeNaviLaunchParam;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo;
import com.baidu.platform.comapi.walknavi.widget.ArCameraView;
import com.comeon.android.BNaviGuideActivity;
import com.comeon.android.InfoDisplayActivity;
import com.comeon.android.R;
import com.comeon.android.WNaviGuideActivity;
import com.comeon.android.business_logic.OrderBusiness;
import com.comeon.android.business_logic.OrderBusinessInterface;
import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 地图组团的碎片
 */
public class MapAppointmentFragment extends BaseFragment {

    public static final int MAP_PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "MapAppointmentFragment";
    private static boolean isFirst = true;
    public LocationClient mLocationClient;

    OrderBusinessInterface orderBusiness = new OrderBusiness();

    RelativeLayout appoinmentLayout;
    ImageView appoinmentImage;
    TextView appointmentName;
    TextView appointmentContact;
    TextView appointmentDistance;
    Button btn_navigate;
    Button btn_comeON;

    MapView bMapView;
    BaiduMap bMap;
    UiSettings bMapUISettings;
    private MapStatus last_status;
    private LatLng currentPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        isFirst = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //加载个性化地图文件
//                setMapCustomFile(getActivity(), "custom_map_config.json");
        View view = inflater.inflate(R.layout.fragment_map_appointment, container, false);
        /*
            加载基础控件
         */
        appoinmentLayout = (RelativeLayout) view.findViewById(R.id.mapappointment_layout);
        appoinmentImage = (ImageView) view.findViewById(R.id.mapappointment_image);
        appointmentName = (TextView) view.findViewById(R.id.txt_groupName);
        appointmentContact = (TextView) view.findViewById(R.id.txt_mapappointment_phone);
        appointmentDistance = (TextView) view.findViewById(R.id.txt_distance);
        btn_comeON = (Button) view.findViewById(R.id.btn_enterDetails);
        btn_navigate = (Button) view.findViewById(R.id.btn_navigate);

        /*
            加载地图
         */
        //填充布局
        bMapView = view.findViewById(R.id.bmapView);
        //设置百度地图logo的位置
        bMapView.setLogoPosition(LogoPosition.logoPostionleftTop);
        bMapView.setPadding(5, 5, 5, 5);
        //显示比例尺
        bMapView.showScaleControl(true);
        bMap = bMapView.getMap();
        //注册监控地图状态变化的监听器
        bMap.setOnMapStatusChangeListener(new MapStatusChangeListener());
        //设置我的位置可用
        bMap.setMyLocationEnabled(true);
        MapView.setMapCustomEnable(true);

        requestAccessLocationPermission(); //申请定位权限
        mLocationClient = new LocationClient(this.getActivity());
        initLocationSettings();
        MyLocationListener myLocatioListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocatioListener);
        mLocationClient.start();
        LogUtil.d(TAG, "地图管理开启情况：" + mLocationClient.isStarted());

        bMapUISettings = bMap.getUiSettings();
        //关闭指南针
        bMapUISettings.setCompassEnabled(false);
        //开启缩放手势
        bMapUISettings.setZoomGesturesEnabled(true);
        //开启平移手势
        bMapUISettings.setScrollGesturesEnabled(true);

        bMap.setOnMapClickListener(new baiduMapOnClickListener());//注册点击事件

        /*
            根据最后一次的位置，构建我的位置
            第一次进入时，last_location为空，所以进行非空判断
         */
        BDLocation last_location = mLocationClient.getLastKnownLocation();
        if (last_location != null) {
            //建立位置标识
            buildSignal(last_location);
        }

        //初次加载所有的订单
        loadRelatedOrders();

        //回到上次操作地图的状态
        if (last_status != null) {
            MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(last_status);
            bMap.animateMapStatus(update);
        }
        return view;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_map_appointment;
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
     *
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
        option.setScanSpan(5000);
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
    }

    /**
     * 生成marker的icon
     *
     * @return
     */
    private BitmapDescriptor getMarkerBitMapDescriptor(String typeName) {
        switch (typeName){
            case "篮球":
                return BitmapDescriptorFactory.fromResource(R.drawable.marker_basketball);

            case "足球":
                return BitmapDescriptorFactory.fromResource(R.drawable.marker_soccer);

            case "羽毛球":
                return BitmapDescriptorFactory.fromResource(R.drawable.marker_badminton);

        }
        return BitmapDescriptorFactory.fromResource(R.mipmap.marker);
    }

    /**
     * 在地图上生成组团位置标注
     * @param orderList 组团列表
     */
    private void markAppointmentList(List<AppointmentOrder> orderList) {
        //1、创建OverlayOptions的集合
        List<OverlayOptions> options = new ArrayList<OverlayOptions>();

        //2、添加overlay
        for (int i = 0; i < orderList.size(); i++) {
            //3、装载额外信息
            Bundle info = new Bundle();
            info.putParcelable("order", orderList.get(i));

            try{

            //4、为overlay设置属性
            OverlayOptions option = new MarkerOptions().position(new LatLng(orderList.get(i).getLatitude(), orderList.get(i).getLongitude())).icon(getMarkerBitMapDescriptor(orderList.get(i).getOrderSportsType().getTypeName()))
                    .animateType(MarkerOptions.MarkerAnimateType.jump)
                    .extraInfo(info);

            options.add(option);
            }catch (Exception ex){
                LogUtil.e("marker标注错误",ex.getMessage());
            }

        }

        //5、在地图上加入overlay
        bMap.addOverlays(options);
    }

    /**
     * 加载相关的订单
     */
    private void loadRelatedOrders() {
        //1、加载相关的订单列表
        List<AppointmentOrder> orderList = orderBusiness.getAllOrders();

        //2、加载该订单列表的图标
        markAppointmentList(orderList);
        bMap.setOnMarkerClickListener(new OnMarkerClickListener());
    }

    /**
     * 处理放入的距离计算数据
     *
     * @param actualDistance 计算的距离数据
     * @return UI显示的距离约值
     */
    private String workWithDistanceData(double actualDistance) {
        if (actualDistance < 1000) {
            return Math.round(actualDistance) + "m";  //四舍五入取整
        } else if (actualDistance < 10000) {
            int kmDigit = (int) actualDistance / 1000; //只取整（千米值）
            long mDigit = (int) actualDistance / 1000 % 100;  ///百米值
            return kmDigit + "." + mDigit + "km";
        } else {
            return "大于10km";
        }
    }

    /**
     * 内部类的形式监听我的位置情况
     */
    public class MyLocationListener extends BDAbstractLocationListener {
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
            currentPosition = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            buildSignal(bdLocation);
            LogUtil.e(TAG, bdLocation.getAddress().address);
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

    /**
     * marker的点击事件
     */
    class OnMarkerClickListener implements BaiduMap.OnMarkerClickListener {
        /**
         * 设置marker的点击事件
         *
         * @param marker 相对应的marker
         * @return
         */
        @Override
        public boolean onMarkerClick(final Marker marker) {
            /*
                1、显示layout
                2、加载信息
                3、重写事件
             */
            //显示layout
            appoinmentLayout.setVisibility(View.VISIBLE);
            appoinmentLayout.bringToFront();
            //加载各控件
            /*
                获取marker的extrainfo——对应的order
             */
            final AppointmentOrder order = marker.getExtraInfo().getParcelable("order");
            appointmentName.setText(order.getOrderName());
            if (order.getOrderContact()!=null){
                appointmentContact.setText(order.getOrderContact());
            }
            final double actualDistance = DistanceUtil.getDistance(new LatLng(order.getLatitude(), order.getLongitude()), currentPosition);
            appointmentDistance.setText(workWithDistanceData(actualDistance));

            btn_comeON.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<AppointmentOrder> orderList = new ArrayList<AppointmentOrder>();
                    orderList.add(order);
                    InfoDisplayActivity.checkOrderInfo(getActivity(), orderList);  //进入对应的详情页
                }
            });

            btn_navigate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestCameraPermission();
//                    if (actualDistance<1000){
//                        walkNavigate(new LatLng(order.getLatitude(),order.getLongitude())); //步行导航暂未成功
//                    }else
                     if (actualDistance<10000){
                        bikeNavigate(new LatLng(order.getLatitude(),order.getLongitude())); //骑行导航
                    }else{
                        Toast.makeText(getActivity(), "距离超过可导航范围（"+workWithDistanceData(actualDistance)+"）", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return true;
        }
    }

    /**
     * 设置地图的基础点击事件
     */
    class baiduMapOnClickListener implements BaiduMap.OnMapClickListener {
        @Override
        public void onMapClick(LatLng latLng) {
            appoinmentLayout.setVisibility(View.GONE);
        }

        @Override
        public boolean onMapPoiClick(MapPoi mapPoi) {
            return false;
        }
    }


    /**
     * 步行导航
     * @param endPt  终点
     */
    private void walkNavigate(final LatLng endPt){
        // 获取导航控制类
        // 引擎初始化
        WalkNavigateHelper.getInstance().initNaviEngine(this.getActivity(), new IWEngineInitListener() {
            @Override
            public void engineInitSuccess() {
                //引擎初始化成功的回调
                walkRoutePlanWithParam(endPt);
            }

            @Override
            public void engineInitFail() {
                //引擎初始化失败的回调
                Toast.makeText(getActivity(), "导航引擎初始化失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 步行算路
     * @param endPt  终点LatLng
     */
    private void walkRoutePlanWithParam(LatLng endPt){
        //构造WalkNaviLaunchParam
        WalkNaviLaunchParam param = new WalkNaviLaunchParam().stPt(currentPosition).endPt(endPt);

        //发起算路
        WalkNavigateHelper.getInstance().routePlanWithParams(param, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                //开始算路的回调
            }

            @Override
            public void onRoutePlanSuccess() {
                //算路成功的回调
                //跳转至诱导页面
                Intent intent = new Intent(getActivity(), WNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError walkRoutePlanError) {
                //算路失败的回调
            }
        });
    }


    /**
     * 自行车导航
     * @param endPt  终点LatLng
     */
    private void bikeNavigate(final LatLng endPt){
        // 获取导航控制类
        // 引擎初始化
        BikeNavigateHelper.getInstance().initNaviEngine(getActivity(), new IBEngineInitListener() {
            @Override
            public void engineInitSuccess() {
                //骑行导航初始化成功之后的回调
                bikeRoutePlanWithParam(endPt);
            }

            @Override
            public void engineInitFail() {
                //骑行导航初始化失败之后的回调
            }
        });
    }

    private void bikeRoutePlanWithParam(LatLng endPt){
        //构造BikeNaviLaunchParam
        //.vehicle(0)默认的普通骑行导航
        BikeNaviLaunchParam param = new BikeNaviLaunchParam().stPt(currentPosition).endPt(endPt).vehicle(0);
        //发起算路
        BikeNavigateHelper.getInstance().routePlanWithParams(param, new IBRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                //执行算路开始的逻辑
            }

            @Override
            public void onRoutePlanSuccess() {
                //算路成功
                //跳转至诱导页面
                Intent intent = new Intent(getActivity(), BNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(BikeRoutePlanError bikeRoutePlanError) {
                //执行算路失败的逻辑
            }
        });
    }


    /**
     * 申请定位权限
     */
    private void requestAccessLocationPermission(){
        List<String> permissionList = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        //如果还有需要申请的权限，则开始发起申请
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this.getActivity(), permissions, 1);
        }
    }

    /**
     * 申请照相机权限
     */
    private void requestCameraPermission(){
        List<String> permissionList = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }

        //如果还有需要申请的权限，则开始发起申请
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this.getActivity(), permissions, 2);
        }
    }


    /**
     * 处理返回的权限结果
     * @param requestCode  请求码
     * @param permissions  权限列表
     * @param grantResults 申请权限的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (permissions.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this.getActivity(), "附近服务需要位置服务权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;

            case 2:
                if (permissions.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this.getActivity(), "导航需要使用摄像机权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }
}
