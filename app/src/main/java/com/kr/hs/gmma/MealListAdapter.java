package com.kr.hs.gmma;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kr.hs.gmma.MainActivity.ViewHolder;

import java.util.ArrayList;


public class MealListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<MealListItem> MealDataSet = new ArrayList<>();

    public MealListAdapter(ArrayList<MealListItem> mDataSet){
        MealDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mDate.setText(MealDataSet.get(position).getDate());
        holder.mInfo.setText(MealDataSet.get(position).getFood_info());
    }


    @Override
    public int getItemCount() {
        return MealDataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }
}
