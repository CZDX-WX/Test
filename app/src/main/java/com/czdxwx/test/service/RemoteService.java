package com.czdxwx.test.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.czdxwx.test.model.Payment;

public class RemoteService extends Service {
    public RemoteService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        return PayBinder;
    }

    // 实现接口中暴露给客户端的Stub--Stub继承自Binder，它实现了IBinder接口
    private final PayService.Stub PayBinder = new PayService.Stub() {
        @Override
        public float pay( float money,float balance) throws RemoteException {
            if(balance>=money){
                return (balance-money);
            }else{
                return -1;
            }
        }
    };
}