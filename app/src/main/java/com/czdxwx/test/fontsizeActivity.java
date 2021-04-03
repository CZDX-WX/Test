package com.czdxwx.test;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Size;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toolbar;

public class fontsizeActivity extends AppCompatActivity {
private SeekBar seekBar;
private TextView fontshow;
private float size;
private Toolbar mToolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fontsize);
        //获取toolbar
        mToolbar =findViewById(R.id.fontsize_toolbar);
        //主标题，必须在setSupportActionBar之前设置，否则无效，如果放在其他位置，则直接setTitle即可
        mToolbar.setTitle("字体大小");
        //用toolbar替换actionbar
        //用toolbar替换actionbar
        setActionBar(mToolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);//显示toolbar的返回按钮
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fontshow=findViewById(R.id.fontshow);
        fontshow.setText("预览字体大小");
        seekBar=findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                size = (float) progress;
                fontshow.setTextSize(size);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBar.setProgress(50);
    }
}