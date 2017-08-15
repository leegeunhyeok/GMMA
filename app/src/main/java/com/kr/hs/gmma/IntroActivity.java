package com.kr.hs.gmma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class IntroActivity extends AppCompatActivity {
    private String msg;
    private Toast toast;
    private boolean dsModeOn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        SharedPreferences mPref = getSharedPreferences("Setting", Context.MODE_PRIVATE);
        dsModeOn = mPref.getBoolean("SAVEMODE_SET", false);

        boolean status = getWhatKindOfNetwork(this);

        LunchDataParser.info_list.clear();
        LunchDataParser.date_list.clear();
        NoticeDataParser.title_list.clear();
        NoticeDataParser.date_list.clear();

        if(!dsModeOn){ //데이터 절약모드가 아닐 때 데이터 로딩
            new LunchDataParser(this, "http://stu.goe.go.kr/sts_sci_md00_001.do?schulCode=J100000488&schulCrseScCode=4&schulKndScCode=4");
            new NoticeDataParser(this, "http://www.gmma.hs.kr/wah/main/mobile/bbs/list.htm?menuCode=69&scale=10&searchField=&searchKeyword=&pageNo=", 1);
        } else {
            for(int i=0; i<5; i++){
                LunchDataParser.date_list.add("[ ]");
                LunchDataParser.info_list.add("급식 데이터가 없습니다.");
            }

            for(int i=0; i<10; i++){
                NoticeDataParser.date_list.add("[ ]");
                NoticeDataParser.title_list.add("데이터 없음.");
            }
        }

        if(status){
            msg = "환영합니다";
            //status = !status;
        } else {
            msg = "네트워크 끊김";
        }
        toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        if(!status || dsModeOn){
            LoadFinish(true);
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

    public void LoadFinish(boolean status){
        if(!status){
            toast.makeText(this, "데이터 로딩 실패", Toast.LENGTH_SHORT).show();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 100);
    }
}
