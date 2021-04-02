package com.czdxwx.test.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;


import com.czdxwx.test.TabFragmentActivity;

import static java.lang.Thread.sleep;

public class ReStartService extends Service {
    //创造一个粘合剂对象
    private final IBinder mBinder = new LocalBinder();

    //定义一个当前服务的粘合剂，用于将该服务黏到活动页面的进程中
    public class LocalBinder extends Binder {
        public ReStartService getService() {
            return ReStartService.this;
        }
    }

    //绑定服务，返回该服务的粘合剂对象
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //解绑服务，返回false表示只能绑定一次
    public boolean onUnbind(Intent intent) {
        return false;
    }

    private long stopDelayed;
    private String PackageName;
    private final Handler handler = new Handler();

    @Override
    //启动服务
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopDelayed = intent.getLongExtra("Delayed", 3000);
        Intent temp = new Intent(this, TabFragmentActivity.class);
        temp.putExtra("content", "程序关闭" + stopDelayed / 1000 + "秒后重新启动");
        PendingIntent pi = PendingIntent.getActivity(this, 0, temp, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + stopDelayed, pi);
        android.os.Process.killProcess(android.os.Process.myPid());
        return super.onStartCommand(intent, flags, startId);
    }
}
