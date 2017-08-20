package com.kr.hs.gmma;

/**
 * Created by lghlo on 2017-07-25.
 */

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;


public class NoticeListAdapter extends RecyclerView.Adapter<MainActivity.NoticeViewHolder> {
    private ArrayList<NoticeListItem> NoticeDataSet = new ArrayList<>();

    public NoticeListAdapter(ArrayList<NoticeListItem> mDataSet){
        NoticeDataSet  = mDataSet;
    }


    @Override
    public MainActivity.NoticeViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notice_card_layout, parent, false);
        MainActivity.NoticeViewHolder vh = new MainActivity.NoticeViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(final MainActivity.NoticeViewHolder holder, final int position) {
        holder.mDate.setText(NoticeDataSet .get(position).getDate());
        holder.mTitle.setText(NoticeDataSet .get(position).getTitle());
        holder.mWriter.setText(NoticeDataSet.get(position).getWriter());
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = NoticeDataSet.get(position).getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                holder.mContext.startActivity(intent);
            }
        });
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