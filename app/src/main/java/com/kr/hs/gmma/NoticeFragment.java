package com.kr.hs.gmma;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lghlo on 2017-07-25.
 */

public class NoticeFragment extends Fragment implements View.OnClickListener {
    private CreateAnimator mAnimator;
    private ProgressBar progress;
    private Button next, prev, page_btn, fab;
    private LinearLayout arc;
    private boolean show = false;
    private int n_page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.notice_fragment, container, false);
        mAnimator = new CreateAnimator();
        next = (Button)view.findViewById(R.id.next_notice_btn);
        prev = (Button)view.findViewById(R.id.prev_notice_btn);
        page_btn = (Button)view.findViewById(R.id.notice_browser_btn);
        fab = (Button)view.findViewById(R.id.notice_page_fab);
        arc = (LinearLayout)view.findViewById(R.id.notice_arc);
        progress = (ProgressBar)view.findViewById(R.id.notice_loading);

        fab.setText(NoticeDataParser.page + "");

        if(show){
            arc.setVisibility(View.VISIBLE);
        }

        progress.setVisibility(View.GONE);

        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        page_btn.setOnClickListener(this);
        fab.setOnClickListener(this);

        NoticeListAdapter adapter = new NoticeListAdapter();
        ListView listView = (ListView)view.findViewById(R.id.notice_listview);
        listView.setAdapter(adapter);
        adapter.addItems();
        return view;
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.notice_page_fab:
                onFabClick(v);
                break;

            case R.id.next_notice_btn:
                next.setEnabled(false);
                prev.setEnabled(false);
                n_page = ++NoticeDataParser.page;
                RefreshNotice();
                break;

            case R.id.prev_notice_btn:
                if(n_page>1) {
                    next.setEnabled(false);
                    prev.setEnabled(false);
                    n_page = --NoticeDataParser.page;
                    RefreshNotice();
                }
                break;

            case R.id.notice_browser_btn:
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri u = Uri.parse("http://www.gmma.hs.kr/wah/main/mobile/bbs/list.htm?menuCode=69&scale=10&searchField=&searchKeyword=&pageNo=" + NoticeDataParser.page);
                i.setData(u);
                startActivity(i);
                break;
        }
    }

    private void onFabClick(View v) {
        if (show) {
            hideMenu();
        } else {
            showMenu();
        }
        show = !show;
    }

    private void showMenu(){
        arc.setVisibility(View.VISIBLE);
        List<Animator> animList = new ArrayList<>();

        for(int i=0, len = arc.getChildCount(); i<len; i++){
            animList.add(mAnimator.createLineShowItemAnimator(arc.getChildAt(i), fab));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(300);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animList);
        animSet.start();
    }

    @SuppressWarnings("NewApi")
    private void hideMenu(){
        List<Animator> animList = new ArrayList<>();

        for (int i = arc.getChildCount() - 1; i >= 0; i--) {
            animList.add(mAnimator.createLineHideItemAnimator(arc.getChildAt(i), fab));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new AnticipateInterpolator());
        animSet.playTogether(animList);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                arc.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
            }
        });
        animSet.start();
    }

    public void ReloadFragment(){
        progress.setVisibility(View.GONE);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    public void RefreshNotice(){
        fab.setText(n_page + "");
        progress.setVisibility(View.VISIBLE);
        NoticeDataParser.date_list.clear();
        NoticeDataParser.title_list.clear();
        new NoticeDataParser(this ,"http://www.gmma.hs.kr/wah/main/mobile/bbs/list.htm?menuCode=69&scale=10&searchField=&searchKeyword=&pageNo=", n_page);
    }
}
