package com.kr.hs.gmma;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by lghlo on 2017-07-25.
 */

public class NoticeDataParser extends AsyncTask<String, String, Boolean> {
    static ArrayList<String> date_list = new ArrayList<>();
    static ArrayList<String> title_list = new ArrayList<>();
    IntroActivity inActivity;
    NoticeFragment fr;
    int date_count = 10, title_count=10;
    static int page;
    private boolean Init = true;

    public NoticeDataParser(IntroActivity activity, String url, int n){
        Init = true;
        inActivity = activity;
        page = n;
        this.execute(url);
    }


    public NoticeDataParser(NoticeFragment fr, String url, int n){
        Init = false;
        this.fr = fr;
        page = n;
        this.execute(url);
    }

    public Boolean doInBackground(String... url){
        boolean result = false;
        try {
            Document doc = Jsoup.connect(url[0] + page).timeout(5000).get();
            Elements title = doc.select(".boardList_tit");
            Elements date = doc.select(".date");

            StringTokenizer date_tokens = new StringTokenizer(date.toString(), "</span>");
            StringTokenizer title_tokens = new StringTokenizer(title.toString(), "</strong>");

            while(date_tokens.hasMoreTokens()) {
                String str = date_tokens.nextToken();
                if(str.charAt(0)=='[') {
                    date_count--;
                    date_list.add(str);
                }
            }

            while(title_tokens.hasMoreTokens()) {
                String str = title_tokens.nextToken();
                if(str.charAt(0) == '"'){
                    title_count--;
                    title_list.add(title_tokens.nextToken());
                }
            }
            result = true;
        } catch(IOException e){
            Log.e("ERROR", "In data parse progress (Notice) : IOException");
        } finally {
            for(int i=0; i<date_count; i++){
                date_list.add("[ ]");
            }

            for(int i=0; i<title_count; i++){
                title_list.add("데이터 없음.");
            }
        }
        return result;
    }

    protected void onPostExecute(Boolean status){
        if(Init){
            inActivity.LoadFinish(status);
        } else {
            fr.ReloadFragment();
        }
    }
}
