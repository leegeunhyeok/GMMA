package com.kr.hs.gmma;

/**
 * Created by lghlo on 2017-07-25.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;


public class NoticeListAdapter extends RecyclerView.Adapter<MainActivity.ViewHolder> {
    private ArrayList<NoticeListItem> NoticeDataSet = new ArrayList<>();

    public NoticeListAdapter(ArrayList<NoticeListItem> mDataSet){
        NoticeDataSet  = mDataSet;
    }


    @Override
    public MainActivity.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        MainActivity.ViewHolder vh = new MainActivity.ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(MainActivity.ViewHolder holder, int position) {
        holder.mDate.setText(NoticeDataSet .get(position).getDate());
        holder.mInfo.setText(NoticeDataSet .get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return NoticeDataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }
}