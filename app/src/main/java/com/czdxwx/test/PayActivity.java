package com.czdxwx.test;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.czdxwx.test.model.Major;
import com.czdxwx.test.model.Person;
import com.czdxwx.test.service.PayService;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.db.sqlite.DbModel;

import java.util.ArrayList;

public class PayActivity extends AppCompatActivity {
    private FinalDb db;
    private EditText number;
    private float money;
    private float balance;
    private TextView pay10;
    private TextView pay20;
    private TextView pay30;

    private int last = -1;
    private Button btn_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        db = FinalDb.create(this, "test");
        number = findViewById(R.id.pay_number);
        btn_pay = findViewById(R.id.btn_pay);
        pay10 = findViewById(R.id.pay10);
        pay10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (last == -1) {
                    last = v.getId();
                    v.setBackgroundColor(Color.parseColor("#0000ff"));
                } else {
                    if (last != v.getId()) {
                        findViewById(last).setBackgroundColor(Color.parseColor("#f5f5dc"));
                        last = v.getId();
                        v.setBackgroundColor(Color.parseColor("#0000ff"));
                    }
                }
                money = 10;
            }
        });
        pay20 = findViewById(R.id.pay20);
        pay20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (last == -1) {
                    last = v.getId();
                    v.setBackgroundColor(Color.parseColor("#0000ff"));
                } else {
                    if (last != v.getId()) {
                        findViewById(last).setBackgroundColor(Color.parseColor("#f5f5dc"));
                        last = v.getId();
                        v.setBackgroundColor(Color.parseColor("#0000ff"));
                    }
                }
                money = 20;
            }
        });
        pay30 = findViewById(R.id.pay30);
        pay30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (last == -1) {
                    last = v.getId();
                    v.setBackgroundColor(Color.parseColor("#0000ff"));
                } else {
                    if (last != v.getId()) {
                        findViewById(last).setBackgroundColor(Color.parseColor("#f5f5dc"));
                        last = v.getId();
                        v.setBackgroundColor(Color.parseColor("#0000ff"));
                    }
                }
                money = 30;
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindRemoteService();
            }
        });
    }

    private PayService payService;// 定义接口变量
    private ServiceConnection connection;
    private TextView input;

    private void bindRemoteService() {
        Intent intentService = new Intent();
        intentService.setClassName(this, "com.czdxwx.test.service.RemoteService");
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                // 从连接中获取Stub对象
                payService = PayService.Stub.asInterface(iBinder);
                // 调用Remote Service提供的方法
                    AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
                    builder.setTitle("请输入支付密码:");
                    input = new EditText(PayActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    builder.setView(input);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DbModel dbModel = db.findDbModelBySQL(String.format("select * from person where number='%s'", number.getText()));
                            if(input.getText().toString().equals(dbModel.getString("password"))){
                                balance = dbModel.getFloat("balance");
                                float state = 0;
                                try {
                                    state = payService.pay(money, balance);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
                                if (state == -1) {
                                    builder.setTitle("支付失败");
                                    builder.setMessage("余额不足");
                                } else {
                                    Person info = new Person();
                                    info.setName(dbModel.getString("name"));
                                    info.setNumber(dbModel.getString("number"));
                                    info.setGender(dbModel.getString("gender"));
                                    info.setHobby(dbModel.getString("hobby"));
                                    info.setNativePlace(dbModel.getString("nativePlace"));
                                    info.setMajorid(dbModel.getInt("majorid"));
                                    info.setIsOnFocus(dbModel.getInt("isOnFocus"));
                                    info.setProfile(dbModel.getString("profile"));
                                    info.setPassword(dbModel.getString("password"));
                                    info.setBalance(state);
                                    db.update(info, String.format("number='%s'", info.getNumber()));
                                    builder.setTitle("支付成功");
                                    builder.setMessage("余额为" + state);
                                }
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
                                builder.setTitle("密码错误");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();


            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                // 断开连接
                payService = null;
            }
        };
        bindService(intentService, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connection != null)
            unbindService(connection);// 解除绑定
    }

}
