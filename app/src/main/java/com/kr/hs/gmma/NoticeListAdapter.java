package com.kr.hs.gmma;

/**
 * Created by lghlo on 2017-07-25.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


public class NoticeListAdapter extends BaseAdapter {
    private ArrayList<NoticeListItem> ItemList = new ArrayList<>();

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
        TextView title = (TextView)convertView.findViewById(R.id.food_menu_text);
        NoticeListItem listViewItem = ItemList.get(position);

        date.setText(listViewItem.getDate());
        title.setText(listViewItem.getTitle());
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

    public void addItem(String date, String title) {
        NoticeListItem item = new NoticeListItem();
        item.setDate(date);
        item.setTitle(title);
        ItemList.add(item);
    }

    public void addItems(){
        for(int i=0; i<NoticeDataParser.date_list.size(); i++) {
            NoticeListItem item = new NoticeListItem();
            if(!(NoticeDataParser.date_list.isEmpty())) {
                item.setDate(NoticeDataParser.date_list.get(i));
                if(!(NoticeDataParser.title_list.isEmpty())){
                    item.setTitle(NoticeDataParser.title_list.get(i));
                }
                ItemList.add(item);
            }
        }
    }
}