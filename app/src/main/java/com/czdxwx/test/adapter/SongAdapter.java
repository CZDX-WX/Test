package com.czdxwx.test.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.czdxwx.test.R;
import com.czdxwx.test.MyApplication;
import com.czdxwx.test.model.Song;
import com.czdxwx.test.utils.MusicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView的适配器
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> implements Filterable {

    private List<Song> mSongList;
    private static final String TAG = "SongAdapter";

    /**
     * 定义接口，传递点击事件时的下标条目
     */
    public interface MyItemClickListener {
        //item的回调方法
        void onItemClick(int position);
    }

    //声明接口
    private MyItemClickListener listener;

    //set方法
    public void setListener(MyItemClickListener listener) {
        this.listener = listener;
    }

    //获取到子项布局的各部件实例
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView songName;
        TextView singer;
        TextView duration;
        ImageView imageView;
        View songView;

        public ViewHolder(View view){
            super(view);
            songView=view;
            songName=(TextView) view.findViewById(R.id.item_song_name);
            singer=(TextView) view.findViewById(R.id.item_song_singer);
            duration=(TextView) view.findViewById(R.id.item_song_duration);
            imageView=(ImageView) view.findViewById(R.id.item_song_image);
        }
    }
    //SongAdapter类的构造函数，用于数据传入
    public SongAdapter(List<Song> songList){
        mSongList=songList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.songView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用接口方法传递下标
                listener.onItemClick(holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //设置子项数据的显示
        Song song=mSongList.get(position);
        holder.songName.setText(song.getSong());
        holder.singer.setText(song.getSinger());
        String time= MusicUtils.formatTime(song.getDuration());
        holder.duration.setText(time);
        Glide.with(MyApplication.getContext()).load(song.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    //过滤器
    private ArrayList<Song> mFilterList = new ArrayList<>();

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override//执行过滤操作
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilterList = (ArrayList<Song>) mSongList;//没有过滤的内容，则使用源数据
                } else {
                    ArrayList<Song> filteredList = new ArrayList<>();
                    for (Song song : mSongList) {
                        if (song.getSong().contains(charString)) {//这里根据需求，添加匹配规则
                            filteredList.add(song);
                        }else if(song.getSinger().contains(charString)){
                            filteredList.add(song);
                        }
                    }
                    mFilterList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterList;
                return filterResults;
            }
            @Override     //把过滤后的值返回出来
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilterList = (ArrayList<Song>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
