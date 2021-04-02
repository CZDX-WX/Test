package com.czdxwx.test;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.czdxwx.test.adapter.MajorAdapter;
import com.czdxwx.test.adapter.PersonAdapter;
import com.czdxwx.test.model.Major;
import com.czdxwx.test.model.Person;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;

public class majorSetActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_set);
        db = FinalDb.create(this, "test");
        //隐藏默认actionbar
        ActionBar actionBar = this.getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //获取toolbar
        mToolbar = findViewById(R.id.majorset_toolbar);
        mToolbar.inflateMenu(R.menu.majorset_menu);
        //主标题，必须在setSupportActionBar之前设置，否则无效，如果放在其他位置，则直接setTitle即可
        mToolbar.setTitle("专业设置");
        //用toolbar替换actionbar
        setActionBar(mToolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);//显示toolbar的返回按钮
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init_listView();
    }

    private ArrayList<String> majorList=new ArrayList<String>();
    private MajorAdapter adapter;
    private ListView lv_major;
    private EditText input;
    private FinalDb db;

    //初始化列表
    private void init_listView() {
        ArrayList<Major> List = (ArrayList<Major>) db.findAll(Major.class);
        majorList.clear();
        for (Major it : List) {
            majorList.add(it.getMajor());
        }
        //构建一个专业列表适配器
        adapter = new MajorAdapter(this, R.layout.item, majorList, Color.WHITE);
        adapter.p=-1;
        lv_major = findViewById(R.id.lv_major);
        lv_major.setAdapter(adapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lv_major.setPadding(0, 100, 0, 100);//设置四周空白

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.majorset_popupmenu, menu);
    }

    @Override
    //选项菜单监听
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_major:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("添加专业");
                builder.setMessage("请输入专业名称");
                input = new EditText(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                builder.setView(input);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Major tmp = new Major();
                        tmp.setMajorid(majorList.size());
                        tmp.setMajor(input.getText().toString());
                        db.save(tmp);
                        ArrayList<Major> List = (ArrayList<Major>) db.findAll(Major.class);
                        majorList.clear();
                        for (Major it : List) {
                            majorList.add(it.getMajor());
                        }
                        adapter.notifyDataSetChanged();
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
                break;
        }
        return true;
    }

    @Override
    //弹出菜单监听
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.majorset_edit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("更新专业");
                builder.setMessage("请输入专业名称");
                input = new EditText(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                builder.setView(input);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Major tmp = new Major();
                        tmp.setMajorid(info.position);
                        tmp.setMajor(input.getText().toString());
                        db.update(tmp, String.format("majorid='%s'", info.position));
                        ArrayList<Major> List = (ArrayList<Major>) db.findAll(Major.class);
                        majorList.clear();
                        for (Major it : List) {
                            majorList.add(it.getMajor());
                        }
                        adapter.notifyDataSetChanged();
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
                break;
            case R.id.majorset_delete:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("提示");
                builder2.setMessage("确定删除吗？");
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteByWhere(Major.class, String.format("majorid='%s'", info.position));
                        ArrayList<Major> List = (ArrayList<Major>) db.findAll(Major.class);
                        majorList.clear();
                        for (Major it : List) {
                            majorList.add(it.getMajor());
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert2 = builder2.create();
                alert2.show();
                break;
        }
        return true;
    }

    public void onStart() {
        super.onStart();
        //注册上下文菜单
        registerForContextMenu(lv_major);
    }

    public void onStop() {
        super.onStop();
        //取消注册
        unregisterForContextMenu(lv_major);
    }
    //封装Toast
    private void showToast(String desc) {
        Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
    }
}