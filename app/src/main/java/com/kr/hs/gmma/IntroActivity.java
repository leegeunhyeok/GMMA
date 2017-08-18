package com.kr.hs.gmma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class IntroActivity extends AppCompatActivity {
    public static MealDataBase mDBManager = null;
    public static Cursor c = null;
    public Cursor mCursor;
    private String msg;
    private Toast toast;
    private boolean dsModeOn;
    private boolean dbLoad = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        SharedPreferences mPref = getSharedPreferences("Setting", Context.MODE_PRIVATE);
        dsModeOn = mPref.getBoolean("SAVEMODE_SET", false);

        boolean status = getWhatKindOfNetwork(this);

        MainActivity.mMealDataset.clear();
        MainActivity.mNoticeDataset.clear();

        mDBManager = MealDataBase.getInstance(this);
        c = mDBManager.getAllData();


        //TODO: 코드 정리하기
        int count = c.getCount();
        Log.i("GMMAHS", "Data Count: " + count);

        if(c == null || count != 5){
            Log.i("GMMAHS", "Data Redownload.. ");
            if(!dsModeOn){ //데이터 절약모드가 아닐 때 데이터 로딩
                Log.i("GMMAHS", "Parsing now");
                new LunchDataParser(this, "http://stu.goe.go.kr/sts_sci_md00_001.do?schulCode=J100000488&schulCrseScCode=4&schulKndScCode=4");
                new NoticeDataParser(this, "http://www.gmma.hs.kr/wah/main/mobile/bbs/list.htm?menuCode=69&scale=10&searchField=&searchKeyword=&pageNo=", 1);
            } else {
                Log.i("GMMAHS", "Data Save Mode ON!");
                for(int i=0; i<5; i++){
                    MainActivity.mMealDataset.add(new MealListItem("[ ]", "급식 데이터가 없습니다."));
                }

                for(int i=0; i<10; i++){
                    MainActivity.mNoticeDataset.add(new NoticeListItem("[ ]", "표시할 데이터가 없습니다."));
                }
            }
        } else {
            new NoticeDataParser(this, "http://www.gmma.hs.kr/wah/main/mobile/bbs/list.htm?menuCode=69&scale=10&searchField=&searchKeyword=&pageNo=", 1);

            Log.i("GMMAHS", "LOAD Database..");
            while(c.moveToNext()){
                String date = c.getString(0);
                String info = c.getString(1);
                MainActivity.mMealDataset.add(new MealListItem(date, info));
            }
            Log.i("GMMAHS", "LOADED Database!");
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
