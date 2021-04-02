package com.czdxwx.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.renderscript.Allocation;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.czdxwx.test.adapter.PersonAdapter;
import com.czdxwx.test.broadcast.MyReceiver;
import com.czdxwx.test.fragment.FriendFragment;
import com.czdxwx.test.fragment.MainFragment;
import com.czdxwx.test.model.Person;
import com.czdxwx.test.service.NormalService;
import com.czdxwx.test.service.ReStartService;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.HashMap;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class TabFragmentActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent ttt = new Intent();
    public static long N = -1;
    //广播接收器
    private MyReceiver myReceiver;
    private IntentFilter intentFilter;
    //服务相关意图
    private Intent mIntent;

    private FriendFragment friendFragment;
    private MainFragment mainFragment;

    private View friendLayout;
    private View mainLayout;

    private TextView friendText;
    private TextView mainText;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tab_fragment);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Toast.makeText(this, bundle.get("content").toString(), Toast.LENGTH_SHORT).show();
        }
        //连接SQLite Studio
        SQLiteStudioService.instance().start(this);

        //广播
        myReceiver = new MyReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.czdxwx.updateMessage");
        // 初始化布局元素
        initViews();
        fragmentManager = getSupportFragmentManager();
        // 第一次启动时选中第0个tab
        setTabSelection(0);
    }

    /**
     * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
     */
    private void initViews() {

        friendLayout = findViewById(R.id.friend_layout);
        mainLayout = findViewById(R.id.main_layout);
        friendText = findViewById(R.id.friend_text);
        mainText = findViewById(R.id.main_text);
        friendLayout.setOnClickListener(this);
        mainLayout.setOnClickListener(this);

        //隐藏默认actionbar
        ActionBar actionBar =getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.friend_layout:
                // 当点击了联系人tab时，选中第1个tab
                setTabSelection(0);
                break;
            case R.id.main_layout:
                // 当点击了设置tab时，选中第2个tab
                setTabSelection(1);
                break;
            default:
                break;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示好友，1表示主页。
     */
    private void setTabSelection(int index) {
        // 每次选中之前先清除掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 当点击了好友tab时，改变控件文字颜色
                friendText.setTextColor(Color.BLACK);
                if (friendFragment == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    friendFragment = new FriendFragment();
                    transaction.add(R.id.content, friendFragment);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(friendFragment);
                }
                break;
            case 1:
                // 当点击了主页tab时，改变控件的文字颜色
                mainText.setTextColor(Color.BLACK);
                if (mainFragment == null) {
                    // 如果SettingFragment为空，则创建一个并添加到界面上
                    mainFragment = new MainFragment();
                    transaction.add(R.id.content, mainFragment);
                } else {
                    // 如果SettingFragment不为空，则直接将它显示出来
                    transaction.show(mainFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {

        friendText.setTextColor(Color.parseColor("#dcdcdc"));

        mainText.setTextColor(Color.parseColor("#dcdcdc"));
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (friendFragment != null) {
            transaction.hide(friendFragment);
        }

        if (mainFragment != null) {
            transaction.hide(mainFragment);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销注册
        unregisterReceiver(myReceiver);
        if (N == -1) {
            //创建一个通往普通服务的意图
            ttt.setClass(this, NormalService.class);
            ttt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //启动指定意图的服务
            startService(ttt);
        } else {
            //创建一个通往立即绑定服务的意图
            mIntent = new Intent(this, ReStartService.class);
            mIntent.putExtra("Delayed", (long)N * 1000);
            mIntent.putExtra("PackageName", this.getPackageName());
            //绑定服务。如果服务未启动，则系统先启动该服务再绑定
            boolean bindFlag = bindService(mIntent, mFirstConn, Context.BIND_AUTO_CREATE);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //启动指定意图的服务
            startService(mIntent);
        }
        N = -1;
    }

    private ReStartService reStartService;//声明一个服务对象
    //绑定服务连接
    private final ServiceConnection mFirstConn = new ServiceConnection() {
        @Override
        //获取对象时的操作
        public void onServiceConnected(ComponentName name, IBinder service) {
            //如果服务运行于另外一个进程，则不能直接强制转换类型
            //否则会报错
            reStartService = ((ReStartService.LocalBinder) service).getService();
        }

        @Override
        //无法获取到服务对象时的操作
        public void onServiceDisconnected(ComponentName name) {
            reStartService = null;
        }
    };

    public void onResume() {
        super.onResume();
        //注册广播
        registerReceiver(myReceiver, intentFilter);
    }

    public void onStop() {
        super.onStop();
        if (reStartService != null) {
            //解绑服务。如果先前服务立即绑定，则此时解绑之后自动停止服务
            unbindService(mFirstConn);
            reStartService = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}