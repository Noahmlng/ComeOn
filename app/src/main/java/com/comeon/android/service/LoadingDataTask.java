package com.comeon.android.service;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.DownloadListener;

import com.comeon.android.R;
import com.comeon.android.db.AppointmentOrder;
import com.comeon.android.db.Friends;
import com.comeon.android.db.Location;
import com.comeon.android.db.Message;
import com.comeon.android.db.SportsType;
import com.comeon.android.db.StadiumInfo;
import com.comeon.android.db.UserInfo;
import com.comeon.android.db.UserLogin;
import com.comeon.android.db.UserLoginLog;
import com.comeon.android.db_accessing.SportsTypeDBAccessing;
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
            ul1.setUser_name("Hi");
            ul1.setUser_phone("17620388542");
            ul1.setLast_modified_time(new Date());
            ul1.save();

            UserInfo user1 = new UserInfo();
            user1.setUser_login_id(ul1.getId());
            user1.setAcepted_distance(5000f);
            user1.setDescription("Nothing to specifically to describe");
            user1.setUser_birthday(new Date(2000, 11, 15));
            user1.setHead_icon(Utilities.decodeResource(R.drawable.head_protrait_initial_background));
            user1.setUser_nickname("无名氏");
            user1.setUser_phone("17620388542");
            user1.setLast_modified_time(new Date());
            user1.setUser_gender(0);
            user1.save();

            UserLoginLog user1_log = new UserLoginLog();
            user1_log.setUser_id(user1.getId());
            user1_log.setLogin_time(new Date());
            user1_log.setLogin_status(0);
            user1_log.save();

            UserLogin ul2 = new UserLogin();
            ul2.setUser_name("Hello");
            ul2.setUser_phone("12345678900");
            ul2.setUser_name("test02");
            ul2.setUser_password("123456");
            ul2.setLast_modified_time(new Date());
            ul2.save();

            UserInfo user2 = new UserInfo();
            user2.setUser_login_id(ul2.getId());
            user2.setUser_birthday(new Date(1980, 12, 5));
            user2.setHead_icon(Utilities.decodeResource(R.drawable.head_protrait_initial_background));
            user2.setUser_nickname("无名氏");
            user2.setUser_phone("17988554477");
            user2.setLast_modified_time(new Date());
            user2.save();

            UserLogin ul3 = new UserLogin();
            ul3.setUser_name("Hey");
            ul3.setUser_phone("12345678912");
            ul3.setUser_name("test03");
            ul3.setUser_password("123456");
            ul3.setLast_modified_time(new Date());
            ul3.save();

            UserInfo user3 = new UserInfo();
            user3.setUser_login_id(ul3.getId());
            user3.setUser_birthday(new Date(1940, 2, 8));
            user3.setHead_icon(Utilities.decodeResource(R.drawable.head_protrait_initial_background));
            user3.setUser_phone("13688975564");
            user3.setLast_modified_time(new Date());
            user3.setUser_gender(1);
            user3.save();


            SportsTypeDBAccessing sportsTypeDBAccessing = new SportsTypeDBAccessing();
        /*
            添加八个场馆信息
         */
            StadiumInfo stadium1 = new StadiumInfo();
            stadium1.setStadium_name("足球场1");
            stadium1.setStadium_contact("12345678910");
            stadium1.setAvg_consumption(90);
            Location location = new Location();
            location.setProvince("广东省");
            location.setCity("东莞市");
            location.setDistrict("东莞市市辖区");
            location.setStreet("建设路");
            location.setStreetNumber("02号");
            stadium1.setLocation(location);
            stadium1.setStadium_icon(Utilities.decodeResource(R.drawable.stadium_sample_soccer1));
            SportsType type=sportsTypeDBAccessing.findSportsTypeByName("足球");
            stadium1.setStadium_type(type);
            stadium1.save();

            StadiumInfo stadium2 = new StadiumInfo();
            stadium2.setStadium_name("足球场2");
            stadium2.setStadium_contact("12345678910");
            stadium2.setAvg_consumption(120);
            location = new Location();
            location.setProvince("广东省");
            location.setCity("东莞市");
            location.setDistrict("东莞市市辖区");
            location.setStreet("莞太路");
            location.setStreetNumber("106号");
            stadium2.setLocation(location);
            stadium2.setStadium_icon(Utilities.decodeResource(R.drawable.stadium_sample_soccer2));
            stadium2.setStadium_type(sportsTypeDBAccessing.findSportsTypeByName("足球"));
            stadium2.save();

            StadiumInfo stadium3 = new StadiumInfo();
            stadium3.setStadium_name("足球场3");
            stadium3.setStadium_contact("12345678910");
            stadium3.setAvg_consumption(5);
            location = new Location();
            location.setProvince("广东省");
            location.setCity("东莞市");
            location.setDistrict("东莞市市辖区");
            location.setStreet("莞太路");
            location.setStreetNumber("120号");
            stadium3.setLocation(location);
            stadium3.setStadium_icon(Utilities.decodeResource(R.drawable.stadium_sample_soccer3));
            stadium3.setStadium_type(sportsTypeDBAccessing.findSportsTypeByName("足球"));
            stadium3.save();

            StadiumInfo stadium4 = new StadiumInfo();
            stadium4.setStadium_name("篮球场1");
            stadium4.setStadium_contact("12345678910");
            stadium4.setAvg_consumption(5);
            location = new Location();
            location.setProvince("广东省");
            location.setCity("东莞市");
            location.setDistrict("东莞市市辖区");
            location.setStreet("体育路");
            location.setStreetNumber("12号");
            stadium4.setLocation(location);
            stadium4.setStadium_icon(Utilities.decodeResource(R.drawable.stadium_sample_basketball1));
            stadium4.setStadium_type(sportsTypeDBAccessing.findSportsTypeByName("篮球"));
            stadium4.save();

            StadiumInfo stadium5 = new StadiumInfo();
            stadium5.setStadium_name("篮球场2");
            stadium5.setStadium_contact("12345678910");
            stadium5.setAvg_consumption(50);
            location = new Location();
            location.setProvince("广东省");
            location.setCity("东莞市");
            location.setDistrict("东莞市市辖区");
            location.setStreet("体育路");
            location.setStreetNumber("100号");
            stadium5.setLocation(location);
            stadium5.setStadium_icon(Utilities.decodeResource(R.drawable.stadium_sample_basketball2));
            stadium5.setStadium_type(sportsTypeDBAccessing.findSportsTypeByName("篮球"));
            stadium5.save();

            StadiumInfo stadium6 = new StadiumInfo();
            stadium6.setStadium_name("羽毛球场1");
            stadium6.setStadium_contact("12345678910");
            stadium6.setAvg_consumption(30);
            location = new Location();
            location.setProvince("广东省");
            location.setCity("东莞市");
            location.setDistrict("东莞市市辖区");
            location.setStreet("嘉和街");
            location.setStreetNumber("16号");
            stadium6.setLocation(location);
            stadium6.setStadium_icon(Utilities.decodeResource(R.drawable.stadium_sample_badminton1));
            stadium6.setStadium_type(sportsTypeDBAccessing.findSportsTypeByName("羽毛球"));
            stadium6.save();

            StadiumInfo stadium7 = new StadiumInfo();
            stadium7.setStadium_name("羽毛球场2");
            stadium7.setStadium_contact("12345678910");
            stadium7.setAvg_consumption(50);
            location = new Location();
            location.setProvince("广东省");
            location.setCity("东莞市");
            location.setDistrict("东莞市市辖区");
            location.setStreet("莞太路");
            location.setStreetNumber("50号");
            stadium7.setLocation(location);
            stadium7.setStadium_icon(Utilities.decodeResource(R.drawable.stadium_sample_badminton2));
            stadium7.setStadium_type(sportsTypeDBAccessing.findSportsTypeByName("羽毛球"));
            stadium7.save();

            StadiumInfo stadium8 = new StadiumInfo();
            stadium8.setStadium_name("网球场1");
            stadium8.setStadium_contact("12345678910");
            stadium8.setAvg_consumption(1000);
            location = new Location();
            location.setProvince("广东省");
            location.setCity("东莞市");
            location.setDistrict("东莞市市辖区");
            location.setStreet("体育路");
            location.setStreetNumber("106号");
            stadium8.setLocation(location);
            stadium8.setStadium_icon(Utilities.decodeResource(R.drawable.stadium_sample_tennis1));
            stadium8.setStadium_type(sportsTypeDBAccessing.findSportsTypeByName("网球"));
            stadium8.save();


        /*
            初始化好友各自为好友
         */
            Friends friends = new Friends();
            friends.setFriend_id(user2.getId());
            friends.setRemarks("好友一号");
            friends.setUser_id(user1.getId());
            friends.setRelationship_status(0);
            friends.save();

            friends = new Friends();
            friends.setFriend_id(user3.getId());
            friends.setUser_id(user1.getId());
            friends.setRelationship_status(0);
            friends.save();

            friends = new Friends();
            friends.setFriend_id(user1.getId());
            friends.setRemarks("好友一号");
            friends.setUser_id(user2.getId());
            friends.setRelationship_status(0);
            friends.save();

            friends = new Friends();
            friends.setFriend_id(user3.getId());
            friends.setUser_id(user2.getId());
            friends.setRelationship_status(0);
            friends.save();

            friends = new Friends();
            friends.setFriend_id(user2.getId());
            friends.setRemarks("好友一号");
            friends.setUser_id(user3.getId());
            friends.setRelationship_status(0);
            friends.save();

            friends = new Friends();
            friends.setFriend_id(user1.getId());
            friends.setUser_id(user3.getId());
            friends.setRelationship_status(0);
            friends.save();

        /*
            创建聊天记录
         */

            Message message = new Message();
            message.setFriend_id(user1.getId());
            message.setUser_id(user2.getId());
            message.setMessage_content("你好啊，朋友1号");
            message.setSend_time(new Date(2018, 11, 15));
            message.save();

            message = new Message();
            message.setFriend_id(user2.getId());
            message.setUser_id(user1.getId());
            message.setMessage_content("hello,hello");
            message.setSend_time(new Date(2018, 11, 16));
            message.save();

            message = new Message();
            message.setFriend_id(user1.getId());
            message.setUser_id(user2.getId());
            message.setMessage_content("你想不想去SHE的“十八”啊");
            message.setSend_time(new Date(2018, 11, 17));
            message.save();

            message = new Message();
            message.setFriend_id(user2.getId());
            message.setUser_id(user1.getId());
            message.setMessage_content("我好像去啊，你呢？");
            message.setSend_time(new Date(2018, 11, 18));
            message.save();

            message = new Message();
            message.setFriend_id(user2.getId());
            message.setUser_id(user1.getId());
            message.setMessage_content("那还用问，我都这样问你耶");
            message.setSend_time(new Date(2018, 11, 19));
            message.save();

            message = new Message();
            message.setFriend_id(user2.getId());
            message.setUser_id(user1.getId());
            message.setMessage_content("对吼，但我到时候出国要去上学了，看不了.....");
            message.setSend_time(new Date(2018, 11, 20));
            message.save();

            message = new Message();
            message.setFriend_id(user2.getId());
            message.setUser_id(user1.getId());
            message.setMessage_content("那你滚吧~");
            message.setSend_time(new Date(2018, 11, 20));
            message.save();

        /*
            创建三个订单，三人分别发布，都参与
         */
            List<UserInfo> members = new ArrayList<UserInfo>();
            members.add(user1);
            members.add(user2);
            members.add(user3);

            AppointmentOrder order1 = new AppointmentOrder();
            order1.setOrder_appoint_time(new Date());
            order1.setOrder_expected_size(15);
            order1.setOrder_launch_time(new Date(2019, 4, 19));
            order1.setOrder_stadium(stadium1);
            order1.setOrder_name("热血足球团");
            order1.setOrder_participants(members);
            order1.setOrder_sponsor(user1);
            order1.setOrder_status(0);
            order1.setOrder_sports_type(sportsTypeDBAccessing.findSportsTypeByName("足球"));
            order1.save();

            AppointmentOrder order2 = new AppointmentOrder();
            order2.setOrder_appoint_time(new Date(2019, 5, 15));
            order2.setOrder_expected_size(9);
            order2.setOrder_launch_time(new Date(2019, 4, 2));
            order2.setOrder_stadium(stadium4);
            order2.setOrder_name("NBA级别篮球局");
            order2.setOrder_participants(members);
            order2.setOrder_sponsor(user2);
            order2.setOrder_status(0);
            order2.setOrder_sports_type(sportsTypeDBAccessing.findSportsTypeByName("篮球"));
            order2.save();

            AppointmentOrder order3 = new AppointmentOrder();
            order3.setOrder_appoint_time(new Date(2018, 10, 5));
            order3.setOrder_expected_size(2);
            order3.setOrder_launch_time(new Date(2017, 4, 12));
            order3.setOrder_name("打个羽毛球？");
            order3.setOrder_location(location);
            order3.setOrder_participants(members);
            order3.setOrder_sponsor(user3);
            order3.setOrder_status(1);
            order3.setOrder_sports_type(sportsTypeDBAccessing.findSportsTypeByName("羽毛球"));
            order3.save();
        } catch (Exception ex) {
            LogUtil.e(TAG, "初始化数据出错："+ex.getMessage());
        }
    }
}
