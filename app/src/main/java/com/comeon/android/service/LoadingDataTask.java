package com.comeon.android.service;

import android.os.AsyncTask;

import com.comeon.android.R;
import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db.AttendanceRecord;
import com.comeon.android.db.Friends;
import com.comeon.android.db.Message;
import com.comeon.android.db.SportsType;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.db.UserInfo;
import com.comeon.android.db.UserLogin;
import com.comeon.android.db.UserLoginLog;
import com.comeon.android.db_accessing.SportsTypeDao;
import com.comeon.android.db_accessing.SportsTypeDaoImpl;
import com.comeon.android.util.LogUtil;
import com.comeon.android.util.Utilities;

import java.util.Date;

/**
 * 加载数据任务（异步任务）
 */
public class LoadingDataTask extends AsyncTask<Void, Integer, Integer> {

    //状态的类变量
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_PAUSED = 1;
    public static final int STATUS_CANCELED = 2;
    public static final int STATUS_FAILED = 3;
    private static final String TAG = "LoadingDataTask";
    //表示当前的任务的状态
    private boolean isPaused = false;
    private boolean isCanceled = false;

    //创建一个监控器
    private LoadingListener listener;
    //记录下载进度
    private int lastProgress;

    public LoadingDataTask(LoadingListener listener) {
        this.listener = listener;
    }

    /**
     * 进行后台操作（已经开启了子线程）
     *
     * @return
     */
    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            for (int i = 1; i <= 1; i++) {
                if (isCanceled) {
                    return STATUS_CANCELED;
                } else if (isPaused) {
                    return STATUS_PAUSED;
                } else {
                    if (i == 1) {
                        publishProgress(0);
                    }
                    initTestData();
                    int progress = (int) (i * 100 / 1);
                    publishProgress(progress);
                }
            }
            return STATUS_SUCCESS;
        } catch (Exception ex) {
            LogUtil.e(TAG, ex.getMessage());
        }
        return STATUS_FAILED;
    }

    /**
     * 更新进度时，回调的方法
     *
     * @param values 进度值
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress >= lastProgress) {
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    /**
     * 提交执行结果的方法
     *
     * @param status 状态的值
     */
    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case STATUS_CANCELED:
                listener.onCancel();
                break;
            case STATUS_FAILED:
                listener.onFail();
                break;
            case STATUS_PAUSED:
                listener.onPause();
                break;
            case STATUS_SUCCESS:
                listener.onSuccess();
                break;
            default:
                break;
        }
    }

    //编写两个映射方法，从而改变在外部改变任务状态
    public void pauseDownload() {
        isPaused = true;
    }

    public void cancelDownload() {
        isCanceled = true;
    }

    /**
     * 加入测试数据
     */
    private void initTestData() {
        try {

        /*
            添加三个用户
         */
            UserLogin ul1 = new UserLogin();
            ul1.setUserPhone("17620388542");
            ul1.setUserPassword("123456");
            ul1.setUserStatus(0);
            ul1.save();

            UserInfo user1 = new UserInfo();
            user1.setUserLoginId(ul1.getId());
            user1.setAcceptedDistance(5000f);
            user1.setDescription("Nothing to specifically to describe");
            user1.setUserBirthday(new Date(2000, 11, 15));
            user1.setHeadIcon(Utilities.decodeResource(R.drawable.head_icon_sample2));
            user1.setUserNickName("Noah");
            user1.setUserPhone("17620388542");
            user1.setRegisterTime(new Date(2016, 10, 15));
            user1.setUserGender(0);
            user1.save();

            UserLoginLog user1_log = new UserLoginLog();
            user1_log.setUserId(user1.getId());
            user1_log.setLoginTime(new Date());
            user1_log.save();

            UserLogin ul2 = new UserLogin();
            ul2.setUserPhone("12345678900");
            ul2.setUserPassword("123456");
            ul2.setLastModifiedTime(new Date());
            ul2.save();

            UserInfo user2 = new UserInfo();
            user2.setUserLoginId(ul2.getId());
            user2.setUserBirthday(new Date(1980, 12, 5));
            user2.setHeadIcon(Utilities.decodeResource(R.drawable.head_icon_sample1));
            user2.setUserPhone("17988554477");
            user2.setRegisterTime(new Date(2017, 4, 1));
            user2.save();

            UserLogin ul3 = new UserLogin();
            ul3.setUserPhone("12345678912");
            ul3.setUserPassword("123456");
            ul3.setLastModifiedTime(new Date());
            ul3.save();

            UserInfo user3 = new UserInfo();
            user3.setUserLoginId(ul3.getId());
            user3.setUserBirthday(new Date(1940, 2, 8));
            user3.setHeadIcon(Utilities.decodeResource(R.drawable.head_icon_sample3));
            user3.setUserPhone("13688975564");
            user3.setUserNickName("Mike");
            user3.setRegisterTime(new Date(2014, 3, 10));
            user3.setUserGender(1);
            user3.save();


            SportsTypeDao sportsTypeDao = new SportsTypeDaoImpl();
            SportsType soccer = sportsTypeDao.findSportsTypeByName("足球");
            SportsType basketball = sportsTypeDao.findSportsTypeByName("篮球");
            SportsType badminton = sportsTypeDao.findSportsTypeByName("羽毛球");

            /*
                添加八个场馆信息
             */
//            StadiumInfo stadium1 = new StadiumInfo();
//            stadium1.setStadiumName("泥岗社区公园");
//            stadium1.setAvgConsumption(0);
//            stadium1.setSportsType(basketball);
//            stadium1.setSportsTypeId(basketball.getId());
//            /*
//            位置信息
//             */
//            stadium1.setProvince("广东省");
//            stadium1.setCity("深圳市");
//            stadium1.setDistrict("罗湖区");
//            stadium1.setStreet("金碧路");
//            stadium1.setStreetNumber("16号");
//            /*
//            经纬度信息：泥岗社区公园
//             */
//            stadium1.setLongitude(114.105508);
//            stadium1.setLatitude(22.575146);
//            stadium1.save();
//
//            StadiumInfo stadium2 = new StadiumInfo();
//            stadium2.setStadiumName("深圳市风光小学");
//            stadium2.setStadiumContact("075582423696");
//            stadium2.setAvgConsumption(0);
//            stadium2.setProvince("广东省");
//            stadium2.setCity("深圳市");
//            stadium2.setDistrict("罗湖区");
//            stadium2.setStreet("泥岗西路");
//            stadium2.setStreetNumber("40号");
//            stadium2.setSportsType(soccer);
//            stadium2.setSportsTypeId(soccer.getId());
//            /*
//                经纬度信息：深圳凤光小学
//             */
//            stadium2.setLongitude(114.106742);
//            stadium2.setLatitude(22.575534);
//            stadium2.save();
//
//
//            StadiumInfo stadium3 = new StadiumInfo();
//            stadium3.setStadiumName("深圳体育馆篮球训练基地游泳馆分部");
//            stadium3.setAvgConsumption(50.0f);
//            stadium3.setProvince("广东省");
//            stadium3.setCity("深圳市");
//            stadium3.setDistrict("福田区");
//            stadium3.setStreet("笋岗西路");
//            stadium3.setStreetNumber("2006号");
//            stadium3.setSportsType(basketball);
//            stadium3.setSportsTypeId(basketball.getId());
//            /*
//            经纬度信息：深圳体育馆篮球训练基地游泳馆分部
//             */
//            stadium3.setLongitude(114.09504);
//            stadium3.setLatitude(22.562973);
//            stadium3.save();
//
//
//            StadiumInfo stadium4 = new StadiumInfo();
//            stadium4.setStadiumName("华富村社区-乒乓羽毛球场");
//            stadium4.setAvgConsumption(0);
//            stadium4.setSportsType(badminton);
//            stadium4.setSportsTypeId(badminton.getId());
//            /*
//            位置信息：华富村社区-乒乓羽毛球场
//             */
//            stadium4.setProvince("广东省");
//            stadium4.setCity("深圳市");
//            stadium4.setDistrict("福田区");
//            stadium4.setStreet("华富一街");
//            /*
//            经纬度信息：华富村社区-乒乓羽毛球场（无法handle复合类型）
//             */
//            stadium4.setLongitude(114.088307);
//            stadium4.setLatitude(22.562051);
//            stadium4.save();
//
//
//            StadiumInfo stadium5 = new StadiumInfo();
//            stadium5.setStadiumName("深圳中心公园篮球场");
//            stadium5.setAvgConsumption(10);
//            stadium5.setProvince("广东省");
//            stadium5.setCity("深圳市");
//            stadium5.setDistrict("福田区");
//            stadium5.setStreet("振华西路");
//            stadium5.setStreetNumber("35号");
//            stadium5.setSportsType(basketball);
//            stadium5.setSportsTypeId(basketball.getId());
//            /*
//                经纬度信息：深圳中心公园篮球场
//             */
//            stadium5.setLongitude(114.080936);
//            stadium5.setLatitude(22.552041);
//            stadium5.save();
//
//
//            StadiumInfo stadium6 = new StadiumInfo();
//            stadium6.setStadiumName("深圳中心公园足球场");
//            stadium6.setAvgConsumption(10);
//            stadium6.setProvince("广东省");
//            stadium6.setCity("深圳市");
//            stadium6.setDistrict("福田区");
//            stadium6.setStreet("振华西路");
//            stadium6.setStreetNumber("35号");
//            stadium6.setSportsType(soccer);
//            stadium6.setSportsTypeId(soccer.getId());
//            /*
//                经纬度信息：深圳中心公园足球场
//             */
//            stadium6.setLongitude(114.080936);
//            stadium6.setLatitude(22.552041);
//            stadium6.save();
//
//            StadiumInfo stadium7 = new StadiumInfo();
//            stadium7.setStadiumName("香蜜湖体育中心");
//            stadium7.setAvgConsumption(10);
//            stadium7.setProvince("广东省");
//            stadium7.setCity("深圳市");
//            stadium7.setDistrict("福田区");
//            stadium7.setStreet("侨香路");
//            stadium7.setStreetNumber("1001号");
//            stadium7.setSportsType(basketball);
//            stadium7.setSportsTypeId(basketball.getId());
//            /*
//                经纬度信息：香蜜湖体育中心
//             */
//            stadium7.setLongitude(114.038406);
//            stadium7.setLatitude(22.558441);
//            stadium7.save();

            StadiumInfo stadium8 = new StadiumInfo();
            stadium8.setStadiumName("海淀综合训练馆篮球场");
            stadium8.setAvgConsumption(0);
            stadium8.setProvince("北京市");
            stadium8.setCity("北京市");
            stadium8.setDistrict("海淀区");
            stadium8.setStreet("颐和园路");
            stadium8.setStadiumContact("01062627895");
            stadium8.setStreetNumber("12号");
            stadium8.setSportsType(basketball);
            stadium8.setSportsTypeId(basketball.getId());
            /*
                经纬度信息：海淀综合训练馆篮球场
             */
            stadium8.setLongitude(116.311265);
            stadium8.setLatitude(39.995519);
            stadium8.save();
//
//            StadiumInfo stadium9 = new StadiumInfo();
//            stadium9.setStadiumName("莞城建设小学");
//            stadium9.setAvgConsumption(0);
//            stadium9.setProvince("广东");
//            stadium9.setCity("东莞市");
//            stadium9.setDistrict("市辖区");
//            stadium9.setStreet("运河东三路");
//            stadium9.setStreetNumber("51号");
//            stadium9.setSportsType(soccer);
//            stadium9.setSportsTypeId(soccer.getId());
//            /*
//                经纬度信息：莞城建设小学
//             */
//            stadium9.setLongitude(113.747615);
//            stadium9.setLatitude(23.039432);
//            stadium9.save();
//
//            StadiumInfo stadium10 = new StadiumInfo();
//            stadium10.setStadiumName("广东宏远篮球训练营");
//            stadium10.setAvgConsumption(100);
//            stadium10.setProvince("广东");
//            stadium10.setCity("东莞市");
//            stadium10.setDistrict("南城区");
//            stadium10.setStreet("宏远社区居委会旁");
//            stadium10.setSportsType(basketball);
//            stadium10.setSportsTypeId(basketball.getId());
//            /*
//                经纬度信息：广东宏远篮球训练营
//             */
//            stadium10.setLongitude(113.734694);
//            stadium10.setLatitude(23.020323);
//            stadium10.save();
//
//            StadiumInfo stadium11 = new StadiumInfo();
//            stadium11.setStadiumName("鼎鑫羽毛球馆");
//            stadium11.setAvgConsumption(30);
//            stadium11.setProvince("广东");
//            stadium11.setCity("东莞市");
//            stadium11.setDistrict("东城区");
//            stadium11.setStreet("新源路");
//            stadium11.setStreetNumber("128号");
//            stadium11.setSportsType(badminton);
//            stadium11.setSportsTypeId(badminton.getId());
//            /*
//                经纬度信息：鼎鑫羽毛球馆
//             */
//            stadium11.setLongitude(113.778238);
//            stadium11.setLatitude(23.003711);
//            stadium11.save();

            StadiumInfo stadium12 = new StadiumInfo();
            stadium12.setStadiumName("中关村文化体育中心");
            stadium12.setAvgConsumption(30);
            stadium12.setStadiumContact("01062627996");
            stadium12.setProvince("北京市");
            stadium12.setCity("北京市");
            stadium12.setDistrict("海淀区");
            stadium12.setStreet("南三街");
            stadium12.setStreetNumber("10号");
            stadium12.setSportsType(badminton);
            stadium12.setSportsTypeId(badminton.getId());
            /*
                经纬度信息：中关村文化体育中心——羽毛球馆
             */
            stadium12.setLongitude(116.334588);
            stadium12.setLatitude(39.988372);
            stadium12.save();

            StadiumInfo stadium13 = new StadiumInfo();
            stadium13.setStadiumName("笼式足球俱乐部");
            stadium13.setAvgConsumption(50);
            stadium13.setStadiumContact("01062262727");
            stadium13.setProvince("北京市");
            stadium13.setCity("北京市");
            stadium13.setDistrict("海淀区");
            stadium13.setStreet("学院路");
            stadium13.setStreetNumber("1号");
            stadium13.setSportsType(soccer);
            stadium13.setSportsTypeId(soccer.getId());
            /*
                经纬度信息：27进行时笼式足球俱乐部
             */
            stadium13.setLongitude(116.345879);
            stadium13.setLatitude(39.994633);
            stadium13.save();


        /*
            初始化好友各自为好友
         */
            Friends friends = new Friends();
            friends.setFriendId(user2.getId());
            friends.setRemarks("好友一号");
            friends.setUserId(user1.getId());
            friends.setRelationshipStatus(0);
            friends.save();

            friends = new Friends();
            friends.setFriendId(user3.getId());
            friends.setUserId(user1.getId());
            friends.setRelationshipStatus(0);
            friends.save();

            friends = new Friends();
            friends.setFriendId(user1.getId());
            friends.setRemarks("好友一号");
            friends.setUserId(user2.getId());
            friends.setRelationshipStatus(0);
            friends.save();

            friends = new Friends();
            friends.setFriendId(user3.getId());
            friends.setUserId(user2.getId());
            friends.setRelationshipStatus(0);
            friends.save();

            friends = new Friends();
            friends.setFriendId(user2.getId());
            friends.setRemarks("好友一号");
            friends.setUserId(user3.getId());
            friends.setRelationshipStatus(0);
            friends.save();

            friends = new Friends();
            friends.setFriendId(user1.getId());
            friends.setUserId(user3.getId());
            friends.setRelationshipStatus(0);
            friends.save();

        /*
            创建聊天记录
         */

            Message message = new Message();
            message.setFriendId(user1.getId());
            message.setUserId(user2.getId());
            message.setMessageContent("你好啊，朋友1号");
            message.setType(Message.TEXT_SENT);
            message.setSendTime(new Date(2018, 11, 15));
            message.save();

            message = new Message();
            message.setFriendId(user2.getId());
            message.setUserId(user1.getId());
            message.setType(Message.TEXT_RECEIVED);
            message.setMessageContent("你好啊，朋友1号");
            message.setSendTime(new Date(2018, 11, 15));
            message.save();

            message = new Message();
            message.setFriendId(user2.getId());
            message.setUserId(user1.getId());
            message.setType(Message.TEXT_SENT);
            message.setMessageContent("hello,hello");
            message.setSendTime(new Date(2018, 11, 16));
            message.save();

            message = new Message();
            message.setFriendId(user1.getId());
            message.setUserId(user2.getId());
            message.setType(Message.TEXT_RECEIVED);
            message.setMessageContent("hello,hello");
            message.setSendTime(new Date(2018, 11, 16));
            message.save();

            message = new Message();
            message.setFriendId(user1.getId());
            message.setUserId(user2.getId());
            message.setMessageContent("你想不想去SHE的“十八”啊");
            message.setType(Message.TEXT_SENT);
            message.setSendTime(new Date(2018, 11, 17));
            message.save();

            message = new Message();
            message.setFriendId(user2.getId());
            message.setUserId(user1.getId());
            message.setMessageContent("你想不想去SHE的“十八”啊");
            message.setType(Message.TEXT_RECEIVED);
            message.setSendTime(new Date(2018, 11, 17));
            message.save();

            message = new Message();
            message.setFriendId(user2.getId());
            message.setUserId(user1.getId());
            message.setMessageContent("我好像去啊，你呢？");
            message.setType(Message.TEXT_SENT);
            message.setSendTime(new Date(2018, 11, 18));
            message.save();

            message = new Message();
            message.setFriendId(user1.getId());
            message.setUserId(user2.getId());
            message.setMessageContent("我好像去啊，你呢？");
            message.setType(Message.TEXT_RECEIVED);
            message.setSendTime(new Date(2018, 11, 18));
            message.save();

            message = new Message();
            message.setFriendId(user1.getId());
            message.setUserId(user2.getId());
            message.setType(Message.TEXT_SENT);
            message.setMessageContent("那还用问，我都这样问你耶");
            message.setSendTime(new Date(2018, 11, 19));
            message.save();

            message = new Message();
            message.setFriendId(user2.getId());
            message.setUserId(user1.getId());
            message.setMessageContent("那还用问，我都这样问你耶");
            message.setType(Message.TEXT_RECEIVED);
            message.setSendTime(new Date(2018, 11, 19));
            message.save();

            message = new Message();
            message.setFriendId(user2.getId());
            message.setUserId(user1.getId());
            message.setType(Message.TEXT_SENT);
            message.setMessageContent("对吼，但我到时候出国要去上学了，看不了.....");
            message.setSendTime(new Date(2018, 11, 20));
            message.save();

            message = new Message();
            message.setFriendId(user1.getId());
            message.setUserId(user2.getId());
            message.setType(Message.TEXT_RECEIVED);
            message.setMessageContent("对吼，但我到时候出国要去上学了，看不了.....");
            message.setSendTime(new Date(2018, 11, 20));
            message.save();

            message = new Message();
            message.setFriendId(user1.getId());
            message.setUserId(user2.getId());
            message.setType(Message.TEXT_SENT);
            message.setMessageContent("那你滚吧~");
            message.setSendTime(new Date(2018, 11, 20));
            message.save();

            message = new Message();
            message.setFriendId(user2.getId());
            message.setUserId(user1.getId());
            message.setType(Message.TEXT_RECEIVED);
            message.setMessageContent("那你滚吧~");
            message.setSendTime(new Date(2018, 11, 20));
            message.save();

        /*
            创建三个订单，三人分别发布，都参与
         */
            AppointmentOrder order1 = new AppointmentOrder();
            order1.setOrderAppointTime(new Date(2019, 8, 10, 19, 30, 0));
            order1.setOrderExpectedSize(22);
            order1.setOrderLaunchTime(new Date(2019, 8, 5, 4, 35, 8));
            order1.setOrderName("周末养生足球团");
            order1.setOrderSponsor(user1);
            order1.setOrderStatus(0);
            order1.setOrderContact(user1.getUserPhone());
            order1.setOrderSportsType(soccer);
            order1.setOrderSportsTypeId(soccer.getId());
            order1.setLongitude(stadium13.getLongitude());
            order1.setLatitude(stadium13.getLatitude());
            if (stadium13.getStreetNumber() != null) {
                order1.setOrderLocation(stadium13.getStreet() + stadium13.getStreetNumber());
            } else {
                order1.setOrderLocation(stadium13.getStreet());
            }
            order1.save();

            AttendanceRecord participant1 = new AttendanceRecord();
            participant1.setOrderId(order1.getId());
            participant1.setParticipantId(user1.getId());
            participant1.save();


            AppointmentOrder order2 = new AppointmentOrder();
            order2.setOrderAppointTime(new Date(2019, 8, 3, 19, 30, 0));
            order2.setOrderExpectedSize(22);
            order2.setOrderLaunchTime(new Date(2019, 8, 1));
            order2.setOrderName("周末养生足球团");
            order2.setOrderSponsor(user1);
            order2.setOrderStatus(3); //已取消
            order2.setOrderContact(user1.getUserPhone());
            order2.setOrderSportsType(soccer);
            order2.setOrderSportsTypeId(soccer.getId());
            order2.setLongitude(stadium13.getLongitude());
            order2.setLatitude(stadium13.getLatitude());
            if (stadium13.getStreetNumber() != null) {
                order2.setOrderLocation(stadium13.getStreet() + stadium13.getStreetNumber());
            } else {
                order2.setOrderLocation(stadium13.getStreet());
            }
            order2.save();

            AttendanceRecord participant4 = new AttendanceRecord();
            participant4.setOrderId(order2.getId());
            participant4.setParticipantId(user1.getId());
            participant4.save();


            AppointmentOrder order3 = new AppointmentOrder();
            order3.setOrderAppointTime(new Date(2019, 8, 10, 19, 30, 0));
            order3.setOrderExpectedSize(2);
            order3.setOrderLaunchTime(new Date(2019, 8, 5, 14, 30, 00));
            order3.setOrderName("羽毛球，约吗？");
            order3.setOrderSponsor(user1);
            order3.setOrderStatus(1);  //组团成功
            order3.setOrderContact(user1.getUserPhone());
            order3.setOrderSportsType(badminton);
            order3.setOrderSportsTypeId(badminton.getId());
            order3.setLongitude(stadium12.getLongitude());
            order3.setLatitude(stadium12.getLatitude());
            if (stadium12.getStreetNumber() != null && stadium12.getStreetNumber().length() > 0) {
                order3.setOrderLocation(stadium12.getStreet() + stadium12.getStreetNumber());
            } else {
                order3.setOrderLocation(stadium12.getStreet());
            }
            order3.save();

            AttendanceRecord participant7 = new AttendanceRecord();
            participant7.setOrderId(order3.getId());
            participant7.setParticipantId(user1.getId());
            participant7.save();

            AttendanceRecord participant8 = new AttendanceRecord();
            participant8.setOrderId(order3.getId());
            participant8.setParticipantId(user2.getId());
            participant8.save();


            AppointmentOrder order4 = new AppointmentOrder();
            order4.setOrderAppointTime(new Date(2019, 8, 2, 19, 30, 0));
            order4.setOrderExpectedSize(3);
            order4.setOrderLaunchTime(new Date(2019, 8, 1, 14, 30, 00));
            order4.setOrderName("泥岗青年篮球");
            order4.setOrderSponsor(user2);
            order4.setOrderStatus(2);  //组团成功，已过期
            order4.setOrderContact(user2.getUserPhone());
            order4.setOrderSportsType(basketball);
            order4.setOrderSportsTypeId(basketball.getId());
            order4.setLongitude(stadium8.getLongitude());
            order4.setLatitude(stadium8.getLatitude());
            if (stadium8.getStreetNumber() != null) {
                order4.setOrderLocation(stadium8.getStreet() + stadium8.getStreetNumber());
            } else {
                order4.setOrderLocation(stadium8.getStreet());
            }
            order4.save();

            AttendanceRecord participant9 = new AttendanceRecord();
            participant9.setOrderId(order4.getId());
            participant9.setParticipantId(user1.getId());
            participant9.save();

            AttendanceRecord participant10 = new AttendanceRecord();
            participant10.setOrderId(order4.getId());
            participant10.setParticipantId(user2.getId());
            participant10.save();

            AttendanceRecord participant11 = new AttendanceRecord();
            participant11.setOrderId(order4.getId());
            participant11.setParticipantId(user3.getId());
            participant11.save();


            AppointmentOrder order5 = new AppointmentOrder();
            order5.setOrderAppointTime(new Date(2019, 8, 10, 19, 30, 0));
            order5.setOrderExpectedSize(2);
            order5.setOrderLaunchTime(new Date(2019, 8, 5, 14, 30, 00));
            order5.setOrderName("找个女生打羽毛球");
            order5.setOrderSponsor(user3);
            order5.setOrderStatus(0);  //组团中
            order5.setOrderContact(user3.getUserPhone());
            order5.setOrderSportsType(badminton);
            order5.setOrderSportsTypeId(badminton.getId());
            order5.setLongitude(stadium12.getLongitude());
            order5.setLatitude(stadium12.getLatitude());
            if (stadium12.getStreetNumber() != null && stadium12.getStreetNumber().length() > 0) {
                order5.setOrderLocation(stadium12.getStreet() + stadium12.getStreetNumber());
            } else {
                order5.setOrderLocation(stadium12.getStreet());
            }
            order5.save();

            AttendanceRecord participant12 = new AttendanceRecord();
            participant12.setOrderId(order5.getId());
            participant12.setParticipantId(user3.getId());
            participant12.save();

        } catch (Exception ex) {
            LogUtil.e(TAG, "初始化数据出错：" + ex.getMessage());
        }
    }
}
