package com.czdxwx.test.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.czdxwx.test.R;

import java.util.ArrayList;

public class PersonAdapter extends BaseAdapter implements Filterable {
    private Context mContext;//声明一个上下文对象
    private ArrayList<String> mPersonList;//声明一个陌生人信息队列
    private String s;
    //过滤器
    private ArrayList<String> mFilterList = new ArrayList<>();

    //传入的特别关心列表
    private ArrayList<String> personConcern = new ArrayList<>();

    //专业适配器的构造函数，传入上下文与专业队列
    public PersonAdapter(Context context, int layout_id, ArrayList<String> mPersonList, int background,ArrayList<String> arrayList) {
        mContext = context;
        this.mPersonList = mPersonList;
        personConcern=arrayList;
    }

    @Override
    //获取列表项的个数
    public int getCount() {
        //注意这里需要是过滤后的list
        return mFilterList.size();
    }

    @Override
    //获取列表项的数据
    public Object getItem(int arg0) {
        return mFilterList.get(arg0);
    }

    @Override
    //获取列表项的编号
    public long getItemId(int arg0) {
        return arg0;
    }


    @Override
    //获取指定位置的列表项视图
    public View getView(final int position, View convertView, ViewGroup parent) {
        PersonAdapter.ViewHolder holder = null;
        if (convertView == null) {//转换视图为空
            holder = new PersonAdapter.ViewHolder();//创建一个新的视图持有者
            //根据布局文件item_list.xml生成转换视图对象
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item, null);
            holder.tv_name = convertView.findViewById(R.id.item_name);
            holder.tv_name.setGravity(View.TEXT_ALIGNMENT_CENTER);
            //将视图持有者保存到转换视图中
            convertView.setTag(holder);
        } else {//转换视图非空
            //从转换视图中获取之前保存的视图保持者
            holder = (PersonAdapter.ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(mFilterList.get(position));//显示姓名
        //特别关心设置特殊背景
        if(personConcern.contains(mFilterList.get(position))){
            convertView.setBackgroundColor(Color.parseColor("#d3d3d3"));
        }else{
            convertView.setBackgroundColor(Color.parseColor("#f5f5dc"));
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override//执行过滤操作
            protected FilterResults performFiltering(CharSequence charSequence) {
                s="";
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilterList = mPersonList;//没有过滤的内容，则使用源数据
                } else {
                    ArrayList<String> filteredList = new ArrayList<>();
                    for (String name : mPersonList) {
                        if (name.contains(charString)) {//这里根据需求，添加匹配规则
                            s+=name;
                            filteredList.add(name);
                        }
                    }
                    s+='\n'+filteredList.toString();
                    mFilterList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterList;
                return filterResults;
            }
            @Override     //把过滤后的值返回出来
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilterList = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    //视图持有者
    public final class ViewHolder {
        private LinearLayout ll_item;
        public TextView tv_name;
    }

    public String getS() {
        return s;
    }
}
