package com.czdxwx.test;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import com.czdxwx.test.adapter.ShareAdapter;
import com.czdxwx.test.model.Person;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;

public class ShareFriendActivity extends AppCompatActivity {
    private FinalDb db;
    private ShareAdapter adapter;
    private RecyclerView rv;
    private Toolbar mToolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_friend);
        db = FinalDb.create(this, "test");
        //隐藏默认actionbar
        ActionBar actionBar = this.getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //获取toolbar
        mToolbar = findViewById(R.id.sharefriend_toolBar);
        //主标题，必须在setSupportActionBar之前设置，否则无效，如果放在其他位置，则直接setTitle即可
        mToolbar.setTitle("同学好友");
        //用toolbar替换actionbar
        mToolbar.inflateMenu(R.menu.share_menu);
        setActionBar(mToolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);//显示toolbar的返回按钮
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        init();
    }

    private void init() {
        //实例化RecyclerView
        rv = findViewById(R.id.rv_share);
        //设置RecyclerView 的布局方式为线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        //实例化SongAdapter，并为RecyclerView设置实例化的SongAdapter对象
        ArrayList<Person> personList = (ArrayList<Person>) db.findAll(Person.class);
        adapter = new ShareAdapter(personList);
        //添加Android自带的分割线
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
        //设置RecyclerView子项点击事件

    }


}