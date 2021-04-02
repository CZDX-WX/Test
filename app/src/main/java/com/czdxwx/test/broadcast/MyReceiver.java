package com.czdxwx.test.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //获取广播的名字
        String action=intent.getAction();
        if("com.czdxwx.updateMessage".equals(action)){
            //获取广播内容
            String s=intent.getStringExtra("content");
            Toast.makeText(context,"广播："+s,Toast.LENGTH_SHORT).show();
        }
    }
}
