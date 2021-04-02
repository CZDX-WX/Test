package com.czdxwx.test.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.czdxwx.test.R;
import com.czdxwx.test.model.Classmate;
import com.czdxwx.test.model.Main;
import com.czdxwx.test.utils.ImgUtil;

import java.util.ArrayList;
import java.util.List;

import static com.czdxwx.test.adapter.MainAdapter.*;

public class MainAdapter extends RecyclerView.Adapter<viewHolder> implements View.OnClickListener {
    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    //创建viewHolder
    public static class viewHolder extends RecyclerView.ViewHolder {
        public TextView item_name;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = (itemView.findViewById(R.id.item_name));
        }
    }

    public MainAdapter(List<Main> mDates) {
        this.mDates = mDates;
    }

    private List<Main> mDates;

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_body, parent, false);
        v.setOnClickListener(this);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.item_name.setText(mDates.get(position).getSetting_name());
        holder.itemView.setTag(position);
    }


    @Override
    public int getItemCount() {
        return mDates.size();
    }
}
