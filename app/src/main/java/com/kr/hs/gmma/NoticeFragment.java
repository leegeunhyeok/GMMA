package com.kr.hs.gmma;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lghlo on 2017-07-25.
 */

public class NoticeFragment extends Fragment implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CreateAnimator mAnimator;
    private ProgressBar progress;
    private Button next, prev, page_btn, refresh, fab;
    private LinearLayout arc;
    private boolean show = false;
    private int n_page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.notice_fragment, container, false);
        mAnimator = new CreateAnimator();
        mAnimator = new CreateAnimator();
        mRecyclerView = (RecyclerView)view.findViewById(R.id.notice_card_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new NoticeListAdapter(MainActivity.mNoticeDataset);
        mRecyclerView.setAdapter(mAdapter);

        next = (Button)view.findViewById(R.id.next_notice_btn);
        prev = (Button)view.findViewById(R.id.prev_notice_btn);
        page_btn = (Button)view.findViewById(R.id.notice_browser_btn);
        fab = (Button)view.findViewById(R.id.notice_page_fab);
        refresh = (Button)view.findViewById(R.id.notice_refresh_btn);
        arc = (LinearLayout)view.findViewById(R.id.notice_arc);

        n_page = NoticeDataParser.page;
        fab.setText(n_page + "");

        if(show){
            arc.setVisibility(View.VISIBLE);
        }

        progress = (ProgressBar)view.findViewById(R.id.notice_loading);
        progress.setVisibility(View.GONE);

        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        page_btn.setOnClickListener(this);
        fab.setOnClickListener(this);
        refresh.setOnClickListener(this);
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

            case R.id.notice_refresh_btn:
                RefreshNotice();
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
            animList.add(mAnimator.createShowAnimator(arc.getChildAt(i), fab, 5, CreateAnimator.Direction.HORIZONTAL));
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
            animList.add(mAnimator.createHideAnimator(arc.getChildAt(i), fab, 5, CreateAnimator.Direction.HORIZONTAL));
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
        new NoticeDataParser(this ,"http://www.gmma.hs.kr/wah/main/mobile/bbs/list.htm?menuCode=69&scale=10&searchField=&searchKeyword=&pageNo=", n_page);
    }
}
