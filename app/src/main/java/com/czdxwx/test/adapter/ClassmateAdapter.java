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

import java.util.List;

public class ClassmateAdapter extends RecyclerView.Adapter<ClassmateAdapter.viewHolder> {

    //创建viewHolder
    public static class viewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public final TextView name;
        public final TextView number;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = (itemView.findViewById(R.id.classmate_chkbtn));
            name = (itemView.findViewById(R.id.classmate_name));
            number = (itemView.findViewById(R.id.classmate_number));
        }

    }

    public ClassmateAdapter(List<Classmate> mDates) {
        this.mDates = mDates;
    }

    private List<Classmate> mDates;

    @NonNull
    @Override
    public ClassmateAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classmate, parent, false);
        return new ClassmateAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassmateAdapter.viewHolder holder, int position) {
        holder.checkBox.setChecked(mDates.get(position).isBeSelected());
        holder.name.setText(mDates.get(position).getName());
        holder.number.setText(mDates.get(position).getNumber());
    }


    @Override
    public int getItemCount() {
        return mDates.size();
    }
}
