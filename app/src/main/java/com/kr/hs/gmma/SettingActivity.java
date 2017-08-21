package com.kr.hs.gmma;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by lghlo on 2017-08-15.
 */

public class SettingActivity extends AppCompatActivity {
    private TextView reset_text = null;
    private TextView data_count = null;
    private Button reset = null;
    private Switch sw_meal = null;
    private Switch sw_notice = null;
    private SharedPreferences mPref = null;
    private boolean isNowReset = false;

    SharedPreferences.OnSharedPreferenceChangeListener mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            boolean dsModeON = sharedPreferences.getBoolean(key, false); // 저장된 값이 없으면 기본값 false
            String s = "";
            if(dsModeON){
                s = "새로고침 버튼을 눌러 수동으로 데이터를 불러올 수 있습니다.";
            } else {
                s = "앱 실행시 자동으로 데이터를 불러옵니다.";
            }
            Toast.makeText(SettingActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);

        reset_text = (TextView)findViewById(R.id.reset_text);

        Button btn = (Button)findViewById(R.id.setting_submit_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        reset = (Button)findViewById(R.id.reset_db_btn);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNowReset){
                    IntroActivity.mDBManager.reset();
                    reset_text.setTextColor(Color.RED);
                    reset_text.setText("초기화되었습니다.");
                    data_count.setText("저장된 이번달 데이터 : 0");
                    reset.setEnabled(false);
                    isNowReset = true;
                }
            }
        });

        sw_meal = (Switch)findViewById(R.id.datasave_switch_meal);
        sw_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isOn = sw_meal.isChecked();
                SharedPreferences.Editor prefEditor = mPref.edit();
                prefEditor.putBoolean("SAVEMODE_SET_MEAL", isOn);
                prefEditor.apply();
            }
        });

        sw_notice = (Switch)findViewById(R.id.datasave_switch_notice);
        sw_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isOn = sw_notice.isChecked();
                SharedPreferences.Editor prefEditor = mPref.edit();
                prefEditor.putBoolean("SAVEMODE_SET_NOTICE", isOn);
                prefEditor.apply();
            }
        });

        Cursor c = IntroActivity.mDBManager.getAllData();
        data_count = (TextView)findViewById(R.id.db_data_count);
        data_count.setText("저장된 이번달 데이터 : " + c.getCount());
        if(!c.isClosed()){
            c.close();
        }

        mPref = getSharedPreferences("Setting", Context.MODE_PRIVATE);
        boolean isOnMeal = mPref.getBoolean("SAVEMODE_SET_MEAL", false);
        boolean isOnNotice = mPref.getBoolean("SAVEMODE_SET_NOTICE", false);
        mPref.registerOnSharedPreferenceChangeListener(mListener);
        sw_meal.setChecked(isOnMeal);
        sw_notice.setChecked(isOnNotice);
    }

    @Override
    public void onDestroy(){
        mPref.unregisterOnSharedPreferenceChangeListener(mListener);
        super.onDestroy();
    }
}
