package com.czdxwx.test.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.czdxwx.test.R;
import com.czdxwx.test.model.Major;

import java.util.ArrayList;

public class MajorAdapter extends BaseAdapter {
    private Context mContext;//声明一个上下文对象
    private ArrayList<String> mMajorList;//声明一个专业信息队列
    private LayoutInflater mInflater;//声明一个填充器
    private int mLayoutId;
    private int mBackground;
    public int p=0;

    //专业适配器的构造函数，传入上下文与专业队列
    public MajorAdapter(Context context, int layout_id, ArrayList<String> major_list, int background) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mLayoutId = layout_id;
        mMajorList = major_list;
        mBackground = background;
    }

    @Override
    //获取列表项的个数
    public int getCount() {
        return mMajorList.size();
    }

    @Override
    //获取列表项的数据
    public Object getItem(int arg0) {
        return mMajorList.get(arg0);
    }

    @Override
    //获取列表项的编号
    public long getItemId(int arg0) {
        return arg0;
    }


    @Override
    //获取指定位置的列表项视图
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {//转换视图为空
            holder = new ViewHolder();//创建一个新的视图持有者
            //根据布局文件item_list.xml生成转换视图对象
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item, null);
            holder.tv_name = convertView.findViewById(R.id.item_name);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                holder.tv_name.setGravity(View.TEXT_ALIGNMENT_CENTER);
            }
            //将视图持有者保存到转换视图中
            convertView.setTag(holder);
        } else {//转换视图非空
            //从转换视图中获取之前保存的视图保持者
            holder = (ViewHolder) convertView.getTag();
        }
        if(position==p){
            convertView.setBackgroundColor(Color.parseColor("#0000ff"));
        }else {
            convertView.setBackgroundColor(Color.parseColor("#f5f5dc"));
        }
        holder.tv_name.setText(mMajorList.get(position));//显示专业名称
        return convertView;
    }

    //视图持有者
    public final class ViewHolder {
        private LinearLayout ll_item;
        public TextView tv_name;
    }
}
