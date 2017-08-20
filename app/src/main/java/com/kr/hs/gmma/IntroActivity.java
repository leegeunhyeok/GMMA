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

import java.util.Calendar;


public class IntroActivity extends AppCompatActivity {
    private final String NOTICE_DEFAULT_URL = "http://www.gmma.hs.kr/wah/main/mobile/bbs/list.htm?menuCode=69&scale=10&searchField=&searchKeyword=&pageNo=";
    private final String MEAL_DEFAULT_URL = "http://stu.goe.go.kr/sts_sci_md00_001.do?schulCode=J100000488&schulCrseScCode=4&schulKndScCode=4";
    public static MealDataBase mDBManager = null;
    public static Cursor c = null;
    private String msg;
    private boolean MealOn, NoticeOn;
    protected boolean MealOk, NoticeOk;
    private String Now_Month, month="";
    private int count;
    private boolean dbLoad = false;
    private boolean net_status;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Calendar cal = Calendar.getInstance();
        Now_Month = cal.get(Calendar.MONTH) + 1 + "";

        SharedPreferences mPref = getSharedPreferences("Setting", Context.MODE_PRIVATE);
        MealOn = mPref.getBoolean("SAVEMODE_SET_MEAL", false);
        NoticeOn = mPref.getBoolean("SAVEMODE_SET_NOTICE", false);
        MealOk = false;
        NoticeOk = false;
        net_status = getWhatKindOfNetwork(this);

        MainActivity.mMealDataset.clear();
        MainActivity.mNoticeDataset.clear();

        mDBManager = MealDataBase.getInstance(this);
        try{
            c = mDBManager.getWeekData(cal.get(Calendar.WEEK_OF_MONTH));
            c.moveToFirst();
            month = c.getString(0);
            count = c.getCount();
        }catch(Exception e){
            Log.e("GMMAHS", "DB 초기 작업중 오류 발생");
        }


        if(net_status){
            if(!NoticeOn){
                new NoticeDataParser(this, NOTICE_DEFAULT_URL, 1);
            } else {
                for(int i=0; i<10; i++){
                    MainActivity.mNoticeDataset.add(new NoticeListItem("[ ]", "표시할 데이터가 없습니다.", "---", NOTICE_DEFAULT_URL+1));
                }
                NoticeOk = true;
            }

            if(!Now_Month.equals(month) && !MealOn){
                new LunchDataParser(this, MEAL_DEFAULT_URL);
                Toast.makeText(this, "이번달 급식 데이터를 새로 불러옵니다", Toast.LENGTH_SHORT).show();
            } else if(count != 0){
                Log.i("GMMAHS", "Load Database..");
                MainActivity.mMealDataset.add(new MealListItem(c.getString(2), c.getString(3)));
                while(c.moveToNext()){
                    String date = c.getString(2);
                    String info = c.getString(3);
                    MainActivity.mMealDataset.add(new MealListItem(date, info));
                }
                dbLoad = MealOk = true;
                Log.i("GMMAHS", "Loaded Database!");
            } else if(!MealOn){
                new LunchDataParser(this, MEAL_DEFAULT_URL);
            } else {
                for(int i=0; i<5; i++){
                    MainActivity.mMealDataset.add(new MealListItem("[ ]", "급식 데이터가 없습니다."));
                }
                MealOk = true;
            }
        } else {
            for(int i=0; i<10; i++){
                MainActivity.mNoticeDataset.add(new NoticeListItem("[ ]", "표시할 데이터가 없습니다.", "---", NOTICE_DEFAULT_URL+1));
            }

            if(count != 0){
                Log.i("GMMAHS", "Load Database..");
                MainActivity.mMealDataset.add(new MealListItem(c.getString(2), c.getString(3)));
                while(c.moveToNext()){
                    String date = c.getString(2);
                    String info = c.getString(3);
                    MainActivity.mMealDataset.add(new MealListItem(date, info));
                }
                Log.i("GMMAHS", "Loaded Database!");
            } else {
                for(int i=0; i<5; i++){
                    MainActivity.mMealDataset.add(new MealListItem("[ ]", "저장된 급식 데이터가 없습니다."));
                }
            }

            MealOk = NoticeOk = true;
        }


        if(!net_status){
            Toast.makeText(this, "네트워크 접속상태를 확인해주세요", Toast.LENGTH_SHORT).show();
        }

        if(!net_status || (NoticeOn && (MealOn || dbLoad))){
            LoadFinish();
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

    public void LoadFinish(){
        if(NoticeOk && MealOk){
            if(net_status) {
                Toast.makeText(this, "환영합니다", Toast.LENGTH_SHORT).show();
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
}
