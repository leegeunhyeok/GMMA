package com.kr.hs.gmma;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class LunchDataParser extends AsyncTask<String, String, Boolean> {
    static ArrayList<String> date_list = new ArrayList<>();
    static ArrayList<String> info_list = new ArrayList<>();
    private boolean Init;
    int date_count=7, info_count=7;
    IntroActivity inActivity;
    MealFragment fr;

    public LunchDataParser(IntroActivity activity, String url){
        Init = true;
        inActivity = activity;
        this.execute(url);
    }

    public LunchDataParser(MealFragment fr, String url){
        Init = false;
        this.fr = fr;
        this.execute(url);
    }


    @Override
    public Boolean doInBackground(String... url){
        boolean result = false;
        try{
            Document doc = Jsoup.connect(url[0]).timeout(5000).get();
            Elements date = doc.select(".food_date");
            Elements info = doc.select(".boardList_tit");

            StringTokenizer date_tokens = new StringTokenizer(date.toString(), "\n");
            StringTokenizer info_tokens = new StringTokenizer(info.toString(), "\n");

            for(int i=1; date_tokens.hasMoreElements(); i++){
                String str = date_tokens.nextToken();
                if(str.charAt(0) != '<'){
                    date_count--;
                    date_list.add(str);
                }
            }


            for(int i=1; info_tokens.hasMoreElements(); i++){
                String str = info_tokens.nextToken();
                String food_data = "";
                if(str.charAt(0)=='<' && str.charAt(1)=='s') {
                    while(true) {
                        str = info_tokens.nextToken();
                        if(str.charAt(0) == '"') {
                            food_data += str + "\n";
                        } else if(str.charAt(0)=='<' && str.charAt(1)=='/') {
                            info_count--;
                            info_list.add(food_data);
                            break;
                        }
                    }
                }
            }
            result = true;
        } catch(IOException e){
            Log.e("ERROR", "In data parse progress (Lunch) : IOException");
        } finally {
            for(int i=0; i<date_count; i++){
                this.date_list.add("[ ]");
            }

            for(int i=0; i<info_count; i++){
                this.info_list.add("급식 데이터가 없습니다.");
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean status){
        if(!Init){
            fr.ReloadFragment();
        }
    }
}
