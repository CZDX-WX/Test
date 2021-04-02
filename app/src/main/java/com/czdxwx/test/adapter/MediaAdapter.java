package com.czdxwx.test.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.czdxwx.test.R;
import com.czdxwx.test.model.Classmate;
import com.czdxwx.test.model.Media;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.viewHolder>{
//创建viewHolder
public static class viewHolder extends RecyclerView.ViewHolder {


    public viewHolder(@NonNull View itemView) {
        super(itemView);

    }

}

    public MediaAdapter(List<Media> mDates) {
        this.mDates = mDates;
    }

    private List<Media> mDates;

    @NonNull
    @Override
    public MediaAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classmate, parent, false);
        return new MediaAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaAdapter.viewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return mDates.size();
    }
}
