package com.kr.hs.gmma;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by lghlo on 2017-07-25.
 */

public class NoticeFragment extends Fragment {
    private ProgressBar progress;
    private Button next, prev, page_btn;
    private TextView page;
    private int n_page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.notice_fragment, container, false);
        next = (Button)view.findViewById(R.id.next_notice_btn);
        prev = (Button)view.findViewById(R.id.prev_notice_btn);
        page_btn = (Button)view.findViewById(R.id.notice_page_btn);

        page = (TextView)view.findViewById(R.id.notice_page);
        page.setText("- " + NoticeDataParser.page +" -");

        progress = (ProgressBar)view.findViewById(R.id.notice_loading);
        progress.setVisibility(View.GONE);

        next.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        next.setEnabled(false);
                        prev.setEnabled(false);
                        n_page = ++NoticeDataParser.page;
                        RefreshNotice();
                    }
                }
        );

        prev.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        if(n_page>1) {
                            next.setEnabled(false);
                            prev.setEnabled(false);
                            n_page = --NoticeDataParser.page;
                            RefreshNotice();
                        }
                    }
                }
        );

        page_btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri u = Uri.parse("http://www.gmma.hs.kr/wah/main/mobile/bbs/list.htm?menuCode=69&scale=10&searchField=&searchKeyword=&pageNo=" + NoticeDataParser.page);
                i.setData(u);
                startActivity(i);
            }
        });

        NoticeListAdapter adapter = new NoticeListAdapter();
        ListView listView = (ListView)view.findViewById(R.id.notice_listview);
        listView.setAdapter(adapter);
        adapter.addItems();
        return view;
    }

    public void ReloadFragment(){
        progress.setVisibility(View.GONE);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    public void RefreshNotice(){
        page.setText("- " + n_page + " -");
        progress.setVisibility(View.VISIBLE);
        NoticeDataParser.date_list.clear();
        NoticeDataParser.title_list.clear();
        new NoticeDataParser(this ,"http://www.gmma.hs.kr/wah/main/mobile/bbs/list.htm?menuCode=69&scale=10&searchField=&searchKeyword=&pageNo=", n_page);
    }
}
