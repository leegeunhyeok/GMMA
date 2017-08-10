package com.kr.hs.gmma;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


public class IntroActivity extends AppCompatActivity {
    private String msg;
    private Toast toast;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        boolean status = getWhatKindOfNetwork(this);
        new LunchDataParser(this, "http://www.gmma.hs.kr/wah/main/mobile/schoolmeal/list.htm?menuCode=102");
        new NoticeDataParser(this, "http://www.gmma.hs.kr/wah/main/mobile/bbs/list.htm?menuCode=69&scale=10&searchField=&searchKeyword=&pageNo=", 1);
        if(status){
            msg = "환영합니다";
            //status = !status;
        } else {
            msg = "네트워크 끊김";
        }
        toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        if(!status){
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
