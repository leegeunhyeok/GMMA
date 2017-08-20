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
    private ArrayList<String> writer_list = new ArrayList<>();
    private ArrayList<String> url_list = new ArrayList<>();
    private ArrayList<String> date_list = new ArrayList<>();
    private ArrayList<String> title_list = new ArrayList<>();
    private IntroActivity inActivity;
    private NoticeFragment fr;
    int date_count, title_count, writer_count, url_count;
    static int page = 1;
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
        MainActivity.mNoticeDataset.clear();
        date_count = title_count = writer_count = url_count = 10;
        boolean result = false;
        try {
            Document doc = Jsoup.connect(url[0] + page).timeout(5000).get();
            Elements writer = doc.select(".writer"); // 작성자, 게시물 날짜
            Elements post = doc.select(".boardList_cont1 a"); // 본문 Url, 제목

            String postUrl = post.toString().replaceAll("<a href=\".", "");
            postUrl = postUrl.replaceAll("&amp;", "&");

            String writerNdate;
            writerNdate = writer.toString().replaceAll("<p class=\"writer\">", "");
            writerNdate = writerNdate.replaceAll("<span class=\"date\">", "");
            writerNdate = writerNdate.replaceAll("&nbsp", "");
            writerNdate = writerNdate.replaceAll("</span></p>", ";");
            writerNdate = writerNdate.replaceAll("\\s+", "");

            StringTokenizer writerNdate_tokens = new StringTokenizer(writerNdate, ";");
            StringTokenizer url_tokens = new StringTokenizer(postUrl, "\n");
            StringTokenizer title_tokens = new StringTokenizer(post.select(".boardList_tit").toString(), "</strong>");

            while(title_tokens.hasMoreTokens()) {
                String str = title_tokens.nextToken();
                if(str.charAt(0) == '"'){
                    title_count--;
                    title_list.add(title_tokens.nextToken());
                }
            }

            while(url_tokens.hasMoreTokens()) {
                String str = url_tokens.nextToken();
                str = str.substring(0, str.indexOf('"'));
                if(str.charAt(0) == '/') {
                    url_count--;
                    url_list.add("http://www.gmma.hs.kr/wah/main/mobile/bbs/" + str);
                }
            }

            while(writerNdate_tokens.hasMoreTokens()) {
                String str = writerNdate_tokens.nextToken();
                if(str.charAt(0)=='[') {
                    date_count--;
                    date_list.add(str.substring(1, str.indexOf(']')));
                } else {
                    writer_count--;
                    writer_list.add(str);
                }
            }
            result = true;
        } catch(IOException e){
            Log.e("ERROR", "In data parse progress (Notice) : IOException");
        } finally {
            for(int i=0; i<date_count; i++){
                date_list.add("0000. 0. 00");
            }

            for(int i=0; i<title_count; i++){
                title_list.add("표시할 데이터가 없습니다.");
            }

            for(int i=0; i<writer_count; i++){
                writer_list.add("---");
            }

            for(int i=0; i<url_count; i++){
                url_list.add(url[0] + page);
            }
        }
        for(int i=0; i<date_list.size(); i++){
            MainActivity.mNoticeDataset.add(new NoticeListItem(date_list.get(i), title_list.get(i), writer_list.get(i), url_list.get(i)));
        }

        if(inActivity != null){
            inActivity.NoticeOk = true;
        }
        return result;
    }

    protected void onPostExecute(Boolean status){
        if(Init){
            inActivity.LoadFinish();
        } else {
            fr.ReloadFragment();
        }
    }
}
