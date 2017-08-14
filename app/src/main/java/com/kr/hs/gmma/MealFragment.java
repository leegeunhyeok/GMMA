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


public class MealFragment extends Fragment {
    private Button btn1, btn2, btn3;
    private ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.meal_fragment, container, false);
        btn1 = (Button)view.findViewById(R.id.refresh_btn);
        btn2 = (Button)view.findViewById(R.id.meal_show_btn);
        btn3 = (Button)view.findViewById(R.id.allergy_info);
        progress = (ProgressBar)view.findViewById(R.id.meal_loading);

        progress.setVisibility(View.GONE);

        btn1.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        LunchDataParser.info_list.clear();
                        LunchDataParser.date_list.clear();
                        RefreshLunch();
                    }
                }
        );

        btn2.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        Uri u = Uri.parse("http://www.gmma.hs.kr/wah/main/mobile/schoolmeal/list.htm?menuCode=102");
                        //버튼을 누르면 학교 홈페이지로 이동 (급식정보)
                        i.setData(u);
                        startActivity(i);
                    }
                }
        );

        btn3.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AllergyActivity.class);
                        startActivity(intent);
                    }
                }
        );


        MealListAdapter adapter = new MealListAdapter();
        ListView listView = (ListView)view.findViewById(R.id.meal_listview);
        listView.setAdapter(adapter);
        adapter.addItems();
        return view;
    }

    public void ReloadFragment(){
        progress.setVisibility(View.GONE);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    public void RefreshLunch(){
        progress.setVisibility(View.VISIBLE);
        new LunchDataParser(this, "http://stu.goe.go.kr/sts_sci_md00_001.do?schulCode=J100000488&schulCrseScCode=4&schulKndScCode=4");
        // 교육청의 급식 데이터 수집
    }
}
