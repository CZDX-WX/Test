package com.czdxwx.test.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.czdxwx.test.R;
import com.czdxwx.test.model.Person;

import java.util.List;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.viewHolder> {
    //声明接口
    private ShareAdapter.MyItemClickListener listener;

    public class viewHolder extends RecyclerView.ViewHolder {
        private CheckBox name;
        private TextView number;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = (itemView.findViewById(R.id.ck_name));
            number = (itemView.findViewById(R.id.share_number));
        }
    }
    public interface MyItemClickListener {
        //item的回调方法
        void onItemClick(int position);
    }
    //set方法
    public void setListener(ShareAdapter.MyItemClickListener listener) {
        this.listener = listener;
    }

    public ShareAdapter(List<Person> mDates) {
        this.mDates = mDates;
    }

    private List<Person> mDates;


    @NonNull
    @Override
    public ShareAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_item, parent, false);
        return new ShareAdapter.viewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ShareAdapter.viewHolder holder, int position) {
        holder.name.setText(mDates.get(position).getName());
        holder.number.setText(mDates.get(position).getNumber());
    }


    @Override
    public int getItemCount() {
        return mDates.size();
    }


}
