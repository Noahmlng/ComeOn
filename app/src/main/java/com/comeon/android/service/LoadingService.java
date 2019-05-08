package com.comeon.android.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.webkit.DownloadListener;
import android.widget.Toast;

import com.comeon.android.MainActivity;
import com.comeon.android.R;
import com.comeon.android.StartActivity;

import java.io.File;

public class LoadingService extends Service {

    private LoadingDataTask loadingDataTask;

    //匿名内部类重写listener中的方法
    private LoadingListener listener=new LoadingListener() {
        @Override
        public void onProgress(int progress) {
            //正在下载时，不断更新通知
            getNotificationManager().notify(1, getNotification("Loading.....",progress));
        }

        @Override
        public void onSuccess() {
            //清空loadingDataTask
            loadingDataTask=null;
            //将进度设为-1
            getNotificationManager().notify(1, getNotification("Load Data Success",-1));
            stopForeground(true);
            Toast.makeText(LoadingService.this, "Load Data Success",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFail() {
            //清空loadingDataTask
            loadingDataTask=null;
            //将进度设为-1
            getNotificationManager().notify(1, getNotification("Load Data Failed",-1));
            Toast.makeText(LoadingService.this, "Load Data Failed",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPause() {
            //清空loadingDataTask
            loadingDataTask=null;
            Toast.makeText(LoadingService.this, "Load Data Paused",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            //清空loadingDataTask
            loadingDataTask=null;
            //关闭前台服务
            stopForeground(true);
            Toast.makeText(LoadingService.this, "Load Data Canceled",Toast.LENGTH_SHORT).show();
        }
    };

    public LoadingService(){}


    private LoadingBinder loadingBinder=new LoadingBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return loadingBinder;
    }

    /**
     * 获取一个NotificationManager
     * @return
     */
    private NotificationManager getNotificationManager(){
        return (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    /**
     * 创建显示下载进度的消息
     * @param title  下载标题
     * @param progress  下载进度
     * @return
     */
    private Notification getNotification(String title, int progress){
        Intent intent=new Intent(this,StartActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent, 0);

        NotificationCompat.Builder notification=new NotificationCompat.Builder(LoadingService.this, "default")
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(pi);
        if(progress>=0){
            notification.setContentText(progress+"%");
            notification.setProgress(100,progress,false);
        }
        return notification.build();
    };

    public class LoadingBinder extends Binder{
        public void startLoading(){
            if(loadingDataTask==null){
                loadingDataTask=new LoadingDataTask(listener);
                //开始加载
                loadingDataTask.execute();
                //开启前台服务
                startForeground(1, getNotification("Loading Data....",0));
                Toast.makeText(LoadingService.this, "Loading.....", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
