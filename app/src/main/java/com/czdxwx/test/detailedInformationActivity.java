package com.czdxwx.test;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class detailedInformationActivity extends FinalActivity {
    @ViewInject(id = R.id.toolBar3)
    Toolbar toolbar3;
    @ViewInject(id =R.id.tv_info)
    TextView textView;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_information);

        init_toolBar();

        //从前一个页面传来的Intent中获取包裹
        Bundle bundle = getIntent().getExtras();
        //将详细情况显示在文本上
        String sb = "姓名：" + bundle.get("name") + '\n' +
                "手机：" + bundle.get("number") + '\n' +
                "性别：" + bundle.get("gender") + '\n' +
                "爱好：" + bundle.get("hobby") + '\n' +
                "籍贯：" + bundle.get("nativePlace") + '\n' +
                "专业：" + bundle.get("major") + '\n';
        textView.setText(sb);
    }

    //标题栏初始化
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init_toolBar() {
        //隐藏默认actionbar
        ActionBar actionBar = this.getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //主标题，必须在setSupportActionBar之前设置，否则无效，如果放在其他位置，则直接setTitle即可
        toolbar3.setTitle("详细信息");
        //用toolbar替换actionbar
        setActionBar(toolbar3);
        getActionBar().setDisplayHomeAsUpEnabled(true);//显示toolbar的返回按钮
        toolbar3.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    //从xml中构建选项菜单界面
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }
}