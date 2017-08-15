package com.kr.hs.gmma;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ogaclejapan.arclayout.Arc;
import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.List;


public class MealFragment extends Fragment implements View.OnClickListener {
    private CreateAnimator mAnimator;
    private Button btn1, btn2, btn3, fab;
    private ProgressBar progress;
    private ArcLayout arc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.meal_fragment, container, false);
        mAnimator = new CreateAnimator();
        btn1 = (Button)view.findViewById(R.id.refresh_btn);
        btn2 = (Button)view.findViewById(R.id.meal_show_btn);
        btn3 = (Button)view.findViewById(R.id.allergy_info);
        arc = (ArcLayout)view.findViewById(R.id.arc_layout);
        fab = (Button)view.findViewById(R.id.fab_meal);
        progress = (ProgressBar)view.findViewById(R.id.meal_loading);
        progress.setVisibility(View.GONE);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        fab.setOnClickListener(this);

        MealListAdapter adapter = new MealListAdapter();
        ListView listView = (ListView)view.findViewById(R.id.meal_listview);
        listView.setAdapter(adapter);
        adapter.addItems();
        return view;
    }

    public void ReloadFragment(){
        fab.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    public void RefreshLunch(){
        progress.setVisibility(View.VISIBLE);
        new LunchDataParser(this, "http://stu.goe.go.kr/sts_sci_md00_001.do?schulCode=J100000488&schulCrseScCode=4&schulKndScCode=4");
        // 교육청의 급식 데이터 수집
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.fab_meal:
                onFabClick(v);
                break;

            case R.id.refresh_btn:
                LunchDataParser.info_list.clear();
                LunchDataParser.date_list.clear();
                RefreshLunch();
                break;

            case R.id.meal_show_btn:
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri u = Uri.parse("http://www.gmma.hs.kr/wah/main/mobile/schoolmeal/list.htm?menuCode=102");
                //버튼을 누르면 학교 홈페이지로 이동 (급식정보)
                i.setData(u);
                startActivity(i);
                break;

            case R.id.allergy_info:
                Intent intent = new Intent(getActivity(), AllergyActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void onFabClick(View v) {
        if (v.isSelected()) {
            hideMenu();
        } else {
            showMenu();
        }
        v.setSelected(!v.isSelected());
    }

    private void showMenu(){
        arc.setVisibility(View.VISIBLE);
        List<Animator> animList = new ArrayList<>();

        for(int i=0, len = arc.getChildCount(); i<len; i++){
            animList.add(mAnimator.createShowItemAnimator(arc.getChildAt(i), fab));
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
            animList.add(mAnimator.createHideItemAnimator(arc.getChildAt(i), fab));
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
}
