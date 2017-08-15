package com.kr.hs.gmma;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class PopupActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch(v.getId()){
                    case R.id.naver_btn:
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.blog.naver.com/lghlove0509"));
                        startActivity(intent);
                        break;

                    case R.id.tistory_btn:
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://codevkr.tistory.com"));
                        startActivity(intent);
                        break;

                    case R.id.git_btn:
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/leegeunhyeok/gmma"));
                        startActivity(intent);
                        break;
                }
            }
        };

        Button btn1 = (Button)findViewById(R.id.naver_btn);
        Button btn2 = (Button)findViewById(R.id.tistory_btn);
        Button btn3 = (Button)findViewById(R.id.git_btn);

        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
        btn3.setOnClickListener(listener);
    }
}