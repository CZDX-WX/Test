package com.czdxwx.test;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.czdxwx.test.adapter.ClassmateAdapter;
import com.czdxwx.test.model.Classmate;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import java.util.List;

public class ClassmateActivity extends FinalActivity {
    private List<Classmate> data;
    @ViewInject(id = R.id.rv)
    RecyclerView rv;
    private SharedPreferences mShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classmate);
        initRecyclerView();
    }

    public void initRecyclerView() {
        //获取data
        mShared = getSharedPreferences("classmate", MODE_PRIVATE);
//        data=mShared.get
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ClassmateAdapter(data));
//        findViewByPosition(int position);//获取指定位置的Item View
//        findFirstCompletelyVisibleItemPosition();//获取第一个完全可见的Item位置
//        findFirstVisibleItemPosition();//获取第一个可见Item的位置
//        findLastCompletelyVisibleItemPosition();//获取最后一个完全可见的Item位置
//        findLastVisibleItemPosition();//获取最后一个可见Item的位置


    }
}