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

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 加载数据任务（异步任务）
 */
public class LoadingDataTask extends AsyncTask<Void, Integer, Integer> {

    private static final String TAG = "LoadingDataTask";

    //状态的类变量
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_PAUSED = 1;
    public static final int STATUS_CANCELED = 2;
    public static final int STATUS_FAILED = 3;

    //表示当前的任务的状态
    private boolean isPaused = false;
    private boolean isCanceled = false;

    //创建一个监控器
    private LoadingListener listener;

    public LoadingDataTask(LoadingListener listener) {
        this.listener = listener;
    }

    //记录下载进度
    private int lastProgress;

    /**
     * 进行后台操作（已经开启了子线程）
     *
     * @return
     */
    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            for (int i = 1; i <= 5; i++) {
                if (isCanceled) {
                    return STATUS_CANCELED;
                } else if (isPaused) {
                    return STATUS_PAUSED;
                } else {
                    if (i == 1) {
                        publishProgress(0);
                    }
                    initTestData();
                    int progress = (int) (i * 100 / 5);
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
            user1.setHeadIcon(Utilities.decodeResource(R.drawable.init_portrait));
            user1.setUserNickName("Noah");
            user1.setUserPhone("17620388542");
            user1.setLastModifiedTime(new Date());
            user1.setRegisterTime(new Date(2016,10,15));
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
            user2.setHeadIcon(Utilities.decodeResource(R.drawable.init_portrait));
            user2.setUserPhone("17988554477");
            user2.setLastModifiedTime(new Date());
            user2.setRegisterTime(new Date(2017,4,1));
            user2.save();

            UserLogin ul3 = new UserLogin();
            ul3.setUserPhone("12345678912");
            ul3.setUserPassword("123456");
            ul3.setLastModifiedTime(new Date());
            ul3.save();

            UserInfo user3 = new UserInfo();
            user3.setUserLoginId(ul3.getId());
            user3.setUserBirthday(new Date(1940, 2, 8));
            user3.setHeadIcon(Utilities.decodeResource(R.drawable.init_portrait));
            user3.setUserPhone("13688975564");
            user3.setUserNickName("Mike");
            user3.setLastModifiedTime(new Date());
            user3.setRegisterTime(new Date(2014,3,10));
            user3.setUserGender(1);
            user3.save();


            SportsTypeDao sportsTypeDao = new SportsTypeDaoImpl();
            /*
                添加八个场馆信息
             */
            StadiumInfo stadium1 = new StadiumInfo();
            stadium1.setStadiumName("足球场1");
            stadium1.setStadiumContact("12345678910");
            stadium1.setAvgConsumption(90);
            stadium1.setProvince("广东省");
            stadium1.setCity("东莞市");
            stadium1.setDistrict("东莞市市辖区");
            stadium1.setStreet("建设路");
            stadium1.setStreetNumber("02号");
            SportsType type=sportsTypeDao.findSportsTypeByName("足球");
            stadium1.setSportsType(type);
            stadium1.save();

            StadiumInfo stadium2 = new StadiumInfo();
            stadium2.setStadiumName("足球场2");
            stadium2.setStadiumContact("12345678910");
            stadium2.setAvgConsumption(120);
            stadium2.setProvince("广东省");
            stadium2.setCity("东莞市");
            stadium2.setDistrict("东莞市市辖区");
            stadium2.setStreet("莞太路");
            stadium2.setStreetNumber("106号");
            stadium2.setSportsType(sportsTypeDao.findSportsTypeByName("足球"));
            stadium2.save();

            StadiumInfo stadium3 = new StadiumInfo();
            stadium3.setStadiumName("足球场3");
            stadium3.setStadiumContact("12345678910");
            stadium3.setAvgConsumption(5);
            stadium3.setProvince("广东省");
            stadium3.setCity("东莞市");
            stadium3.setDistrict("东莞市市辖区");
            stadium3.setStreet("莞太路");
            stadium3.setStreetNumber("120号");
            stadium3.setSportsType(sportsTypeDao.findSportsTypeByName("足球"));
            stadium3.save();

            StadiumInfo stadium4 = new StadiumInfo();
            stadium4.setStadiumName("篮球场1");
            stadium4.setStadiumContact("12345678910");
            stadium4.setAvgConsumption(5);
            stadium4.setProvince("广东省");
            stadium4.setCity("东莞市");
            stadium4.setDistrict("东莞市市辖区");
            stadium4.setStreet("体育路");
            stadium4.setStreetNumber("12号");
            stadium4.setSportsType(sportsTypeDao.findSportsTypeByName("篮球"));
            stadium4.save();

            StadiumInfo stadium5 = new StadiumInfo();
            stadium5.setStadiumName("篮球场2");
            stadium5.setStadiumContact("12345678910");
            stadium5.setAvgConsumption(50);
            stadium5.setProvince("广东省");
            stadium5.setCity("东莞市");
            stadium5.setDistrict("东莞市市辖区");
            stadium5.setStreet("体育路");
            stadium5.setStreetNumber("100号");
            stadium5.setSportsType(sportsTypeDao.findSportsTypeByName("篮球"));
            stadium5.save();

            StadiumInfo stadium6 = new StadiumInfo();
            stadium6.setStadiumName("羽毛球场1");
            stadium6.setStadiumContact("12345678910");
            stadium6.setAvgConsumption(30);
            stadium6.setProvince("广东省");
            stadium6.setCity("东莞市");
            stadium6.setDistrict("东莞市市辖区");
            stadium6.setStreet("嘉和街");
            stadium6.setStreetNumber("16号");
            stadium6.setSportsType(sportsTypeDao.findSportsTypeByName("羽毛球"));
            stadium6.save();

            StadiumInfo stadium7 = new StadiumInfo();
            stadium7.setStadiumName("羽毛球场2");
            stadium7.setStadiumContact("12345678910");
            stadium7.setAvgConsumption(50);
            stadium7.setProvince("广东省");
            stadium7.setCity("东莞市");
            stadium7.setDistrict("东莞市市辖区");
            stadium7.setStreet("莞太路");
            stadium7.setStreetNumber("50号");
            stadium7.setSportsType(sportsTypeDao.findSportsTypeByName("羽毛球"));
            stadium7.save();

            StadiumInfo stadium8 = new StadiumInfo();
            stadium8.setStadiumName("网球场1");
            stadium8.setStadiumContact("12345678910");
            stadium8.setAvgConsumption(1000);
            stadium8.setProvince("广东省");
            stadium8.setCity("东莞市");
            stadium8.setDistrict("东莞市市辖区");
            stadium8.setStreet("体育路");
            stadium8.setStreetNumber("106号");
            stadium8.setSportsType(sportsTypeDao.findSportsTypeByName("网球"));
            stadium8.save();


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
            message.setSendTime(new Date(2018, 11, 15));
            message.save();

            message = new Message();
            message.setFriendId(user2.getId());
            message.setUserId(user1.getId());
            message.setMessageContent("hello,hello");
            message.setSendTime(new Date(2018, 11, 16));
            message.save();

            message = new Message();
            message.setFriendId(user1.getId());
            message.setUserId(user2.getId());
            message.setMessageContent("你想不想去SHE的“十八”啊");
            message.setSendTime(new Date(2018, 11, 17));
            message.save();

            message = new Message();
            message.setFriendId(user2.getId());
            message.setUserId(user1.getId());
            message.setMessageContent("我好像去啊，你呢？");
            message.setSendTime(new Date(2018, 11, 18));
            message.save();

            message = new Message();
            message.setFriendId(user2.getId());
            message.setUserId(user1.getId());
            message.setMessageContent("那还用问，我都这样问你耶");
            message.setSendTime(new Date(2018, 11, 19));
            message.save();

            message = new Message();
            message.setFriendId(user2.getId());
            message.setUserId(user1.getId());
            message.setMessageContent("对吼，但我到时候出国要去上学了，看不了.....");
            message.setSendTime(new Date(2018, 11, 20));
            message.save();

            message = new Message();
            message.setFriendId(user2.getId());
            message.setUserId(user1.getId());
            message.setMessageContent("那你滚吧~");
            message.setSendTime(new Date(2018, 11, 20));
            message.save();

        /*
            创建三个订单，三人分别发布，都参与
         */
            AppointmentOrder order1 = new AppointmentOrder();
            order1.setOrderAppointTime(new Date());
            order1.setOrderExpectedSize(15);
            order1.setOrderLaunchTime(new Date(2019, 4, 19));
            order1.setOrderStadium(stadium1);
            order1.setOrderName("热血足球团");
            order1.setOrderSponsor(user1);
            order1.setOrderStatus(0);
            order1.setOrderSportsType(sportsTypeDao.findSportsTypeByName("足球"));
            order1.save();

            AttendanceRecord participant1=new AttendanceRecord();
            participant1.setOrderId(order1.getId());
            participant1.setParticipantId(user1.getId());
            participant1.save();

            AttendanceRecord participant2=new AttendanceRecord();
            participant2.setOrderId(order1.getId());
            participant2.setParticipantId(user2.getId());
            participant2.save();

            AttendanceRecord participant3=new AttendanceRecord();
            participant3.setOrderId(order1.getId());
            participant3.setParticipantId(user3.getId());
            participant3.save();

            AppointmentOrder order2 = new AppointmentOrder();
            order2.setOrderAppointTime(new Date(2019, 5, 15));
            order2.setOrderExpectedSize(9);
            order2.setOrderLaunchTime(new Date(2019, 4, 2));
            order2.setOrderStadium(stadium4);
            order2.setOrderName("NBA级别篮球局");
            order2.setOrderSponsor(user2);
            order2.setOrderStatus(0);
            type=sportsTypeDao.findSportsTypeByName("篮球");
            order2.setOrderSportsType(type);
            order2.save();

            AttendanceRecord participant4=new AttendanceRecord();
            participant4.setOrderId(order2.getId());
            participant4.setParticipantId(user1.getId());
            participant4.save();

            AttendanceRecord participant5=new AttendanceRecord();
            participant5.setOrderId(order2.getId());
            participant5.setParticipantId(user2.getId());
            participant5.save();

            AttendanceRecord participant6=new AttendanceRecord();
            participant6.setOrderId(order2.getId());
            participant6.setParticipantId(user3.getId());
            participant6.save();

            AppointmentOrder order3 = new AppointmentOrder();
            order3.setOrderAppointTime(new Date(2018, 10, 5));
            order3.setOrderExpectedSize(2);
            order3.setOrderLaunchTime(new Date(2017, 4, 12));
            order3.setOrderName("打个羽毛球？");
            order3.setOrderSponsor(user3);
            order3.setOrderStatus(1);
            type=sportsTypeDao.findSportsTypeByName("羽毛球");
            order3.setOrderSportsType(type);
            order3.setOrderLocation("广东省东莞市市辖区建设路一号球场");
            order3.save();

            AttendanceRecord participant7=new AttendanceRecord();
            participant7.setOrderId(order3.getId());
            participant7.setParticipantId(user1.getId());
            participant7.save();

            AttendanceRecord participant8=new AttendanceRecord();
            participant8.setOrderId(order3.getId());
            participant8.setParticipantId(user2.getId());
            participant8.save();

            AttendanceRecord participant9=new AttendanceRecord();
            participant9.setOrderId(order3.getId());
            participant9.setParticipantId(user3.getId());
            participant9.save();

        } catch (Exception ex) {
            LogUtil.e(TAG, "初始化数据出错："+ex.getMessage());
        }
    }
}
