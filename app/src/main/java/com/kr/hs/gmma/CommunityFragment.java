package com.kr.hs.gmma;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by lghlo on 2017-07-25.
 */

public class CommunityFragment extends Fragment {
    Button page, doje, club, dev, share, setting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.community_layout, container, false);
        page = (Button)view.findViewById(R.id.school_page_btn);
        doje = (Button)view.findViewById(R.id.doje_btn);
        club = (Button)view.findViewById(R.id.student_club_btn);
        dev = (Button)view.findViewById(R.id.dev_info_btn);
        share = (Button)view.findViewById(R.id.share_btn);
        setting = (Button)view.findViewById(R.id.setting_btn);

        page.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri u = Uri.parse("http://www.gmma.hs.kr/wah/main/mobile/index.htm");
                i.setData(u);
                startActivity(i);
            }
        });

        doje.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri u = Uri.parse("http://www.gmma.hs.kr/wah/main/mobile/html/view.htm?menuCode=583");
                i.setData(u);
                startActivity(i);
            }
        });

        club.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri u = Uri.parse("https://www.facebook.com/gmmahs/?fref=ts");
                i.setData(u);
                startActivity(i);
            }
        });

        dev.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Bundle bundleAim = ActivityOptions.makeCustomAnimation(getActivity(),
                        R.anim.slide_up, R.anim.slide_down).toBundle();
                Intent intent = new Intent(getActivity(), PopupActivity.class);
                startActivity(intent, bundleAim); // 액티비티 호출 시 위로 올라오는 애니메이션 적용
            }
        });

        share.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent msg = new Intent(Intent.ACTION_SEND);
                msg.addCategory(Intent.CATEGORY_DEFAULT);
                msg.putExtra(Intent.EXTRA_SUBJECT, "광명경영회계고등학교 앱");
                msg.putExtra(Intent.EXTRA_TEXT, "다운받아보기!\n링크: http://lghlove0509.blog.me/220961533600");
                msg.putExtra(Intent.EXTRA_TITLE, "공유하기");
                msg.setType("text/plain");
                startActivity(Intent.createChooser(msg, "공유하기"));
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
