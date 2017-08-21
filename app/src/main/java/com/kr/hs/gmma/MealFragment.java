package com.kr.hs.gmma;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MealFragment extends Fragment implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CreateAnimator mAnimator;
    private LinearLayout today;
    private Button btn1, btn2, btn3, fab;
    private ProgressBar progress;
    private ArcLayout arc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.meal_fragment, container, false);
        mAnimator = new CreateAnimator();
        mRecyclerView = (RecyclerView)view.findViewById(R.id.meal_card_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MealListAdapter(MainActivity.mMealDataset);
        mRecyclerView.setAdapter(mAdapter);

        btn1 = (Button)view.findViewById(R.id.refresh_btn);
        btn2 = (Button)view.findViewById(R.id.meal_show_btn);
        btn3 = (Button)view.findViewById(R.id.allergy_info);
        arc = (ArcLayout)view.findViewById(R.id.arc_layout);
        fab = (Button)view.findViewById(R.id.fab_meal);
        progress = (ProgressBar)view.findViewById(R.id.meal_loading);
        progress.setVisibility(View.GONE);
        today = (LinearLayout)view.findViewById(R.id.today_check);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        fab.setOnClickListener(this);
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
                boolean status = getWhatKindOfNetwork(this.getContext());
                if(status){
                    IntroActivity.mDBManager.reset();
                    MainActivity.mMealDataset.clear();
                    RefreshLunch();
                } else {
                    Toast.makeText(this.getContext(), "네트워크 상태를 확인해주세요", Toast.LENGTH_SHORT).show();
                }

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

    public static boolean getWhatKindOfNetwork(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return true;
        }
        return false;
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

        animList.add(mAnimator.createShowAnimator(arc.getChildAt(2), fab, 1.5f, CreateAnimator.Direction.VERTICAL));
        animList.add(mAnimator.createShowAnimator(arc.getChildAt(1), fab, 1.5f, CreateAnimator.Direction.DIAGONAL));
        animList.add(mAnimator.createShowAnimator(arc.getChildAt(0), fab, 1.5f, CreateAnimator.Direction.HORIZONTAL));


        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(300);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animList);
        animSet.start();
    }

    @SuppressWarnings("NewApi")
    private void hideMenu(){
        List<Animator> animList = new ArrayList<>();

        animList.add(mAnimator.createHideAnimator(arc.getChildAt(2), fab, 1.5f, CreateAnimator.Direction.VERTICAL));
        animList.add(mAnimator.createHideAnimator(arc.getChildAt(1), fab, 1.5f, CreateAnimator.Direction.DIAGONAL));
        animList.add(mAnimator.createHideAnimator(arc.getChildAt(0), fab, 1.5f, CreateAnimator.Direction.HORIZONTAL));


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
