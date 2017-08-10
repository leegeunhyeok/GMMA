package com.kr.hs.gmma;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by lghlo on 2017-07-24.
 */

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_layout, container, false);
        Button schedule_btn = (Button)v.findViewById(R.id.schedule_btn);
        Button info_btn = (Button)v.findViewById(R.id.info_btn);
        Button road_btn = (Button)v.findViewById(R.id.road_btn);

        schedule_btn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        Uri u = Uri.parse("http://www.gmma.hs.kr/wah/main/mobile/schedule/list.htm?menuCode=120");
                        i.setData(u);
                        startActivity(i);
                    }
                }
        );


        info_btn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        Uri u = Uri.parse("http://www.gmma.hs.kr/wah/main/mobile/html/view.htm?menuCode=11");
                        i.setData(u);
                        startActivity(i);
                    }
                }
        );

        road_btn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        Uri u = Uri.parse("http://www.gmma.hs.kr/wah/main/mobile/html/view.htm?menuCode=15");
                        i.setData(u);
                        startActivity(i);
                    }
                }
        );

        return v;
    }
}
