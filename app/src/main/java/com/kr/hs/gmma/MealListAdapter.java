package com.kr.hs.gmma;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


public class MealListAdapter extends BaseAdapter {
    private ArrayList<MealListItem> ItemList = new ArrayList<MealListItem>();

    @Override
    public int getCount(){
        return ItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_layout, parent, false);
        }

        TextView date = (TextView)convertView.findViewById(R.id.date_text);
        TextView food_info = (TextView)convertView.findViewById(R.id.food_menu_text);
        MealListItem listViewItem = ItemList.get(position);

        date.setText(listViewItem.getDate());
        food_info.setText(listViewItem.getFood_info());
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return ItemList.get(position) ;
    }

    public void addItems(){
        if(LunchDataParser.date_list.size() == 0) return;
        for(int i=0; i<5; i++) {
            MealListItem item = new MealListItem();
            item.setDate(LunchDataParser.date_list.get(i));
            item.setFood_info(LunchDataParser.info_list.get(i));
            ItemList.add(item);
        }
    }
}
