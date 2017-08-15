package com.kr.hs.gmma;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

public class LunchDataParser extends AsyncTask<String, String, Boolean> {
    private ArrayList<String> date_list = new ArrayList<>();
    private ArrayList<String> info_list = new ArrayList<>();
    private boolean Init;
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
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK); //1 일요일 ~ 7 토요일
        int day =c.get(Calendar.DAY_OF_MONTH); // 오늘 날짜
        int day_count = 0; // 이번주 데이터만 골라내기 위해 몇일인지 카운트
        int week_count = 0; // 요일 저장 변수 // 0 월, 1 화... 4 금

        try {
            Document doc = Jsoup.connect(url[0]).timeout(5000).get(); // 광명경영회계고등학교의 이번달 급식 데이터 조회링크
            Elements e = doc.select("tbody"); // HTML의 tbody태그 내의 코드 수집
            String str = e.toString().replaceAll("\\s+", ""); // 파싱한 데이터의 공백, 개행문자 모두 제거

            StringBuffer buf = new StringBuffer();

            boolean inDiv = false;
            for(int i=0; i<str.length(); i++) {
                if(str.charAt(i)=='v') { //div 태그 제거
                    if(inDiv) {
                        buf.delete(buf.length() - 4, buf.length());
                        if (buf.length() > 0)
                            //버퍼에 있는 데이터 길이가 > 0 이면
                            if(++day_count >= day - (day_of_week-2) && info_list.size() < 5) {
                                // 오늘 날짜가 포함된 주(Week)의 급식데이터 5개 불러오기
                                info_list.add(parseDayMeal(buf.toString() + " ", day_count));
                                String weekday = "";
                                switch (week_count++){
                                    case 0:
                                        weekday = day_count + "일 - [월]";
                                        break;

                                    case 1:
                                        weekday = day_count + "일 - [화]";
                                        break;

                                    case 2:
                                        weekday = day_count + "일 - [수]";
                                        break;

                                    case 3:
                                        weekday = day_count + "일 - [목]";
                                        break;

                                    case 4:
                                        weekday = day_count + "일 - [금]";
                                        break;

                                    default:
                                        weekday = "[ ]";
                                        break;
                                }
                                date_list.add(weekday);
                            }
                        buf.setLength(0);
                    } else {
                        i++;
                    }
                    inDiv = !inDiv;
                } else if (inDiv) {
                    buf.append(str.charAt(i));
                }
            }
            result = true;
        } catch (IOException e) {
            Log.e("ERROR", "In data parse progress (Lunch) : IOException");
            info_list.clear();
            date_list.clear();
            for (int i = 0; i < 5; i++) {
                info_list.add("급식 데이터가 없습니다.");
                date_list.add("[ ]");
            }
        }

        for(int i=0; i<info_list.size(); i++){
            MainActivity.mMealDataset.add(new MealListItem(date_list.get(i), info_list.get(i)));
        }

        return result;
    }

    public String parseDayMeal(String data, int day) {
        String parse = "";
        if(data.charAt(1)==' ' || data.charAt(2)==' ') {
            // 데이터가 없는 경우
            parse = "급식 데이터가 없습니다.";
        } else {
            // 1, 2번째 br 태그를 제외한 나머지를 모두 개행문자로 바꿈
            parse = data.replaceFirst(day + "", "");
            parse = parse.replaceAll("\\[중식\\]", ""); // [중식] 문자 제거
            parse = parse.replaceFirst("<br>", ""); // 1번째 br 제거
            parse = parse.replaceFirst("<br>", ""); // 2번째 br 제거
            parse = parse.replaceAll("<br>", "\n"); // 남은 br태그들을 개행으로 변환

            // 음식9.13.5 이런 형식으로 급식 메뉴와 알레르기 정보가 합쳐져 있음.
            // 메뉴와 알레르기 정보 사이에 공백을 집어넣는 작업
            StringBuffer sb = new StringBuffer();
            sb.append(parse.charAt(0));
            boolean first = true;
            for(int i=0; i<parse.length()-1; i++) {
                char temp = parse.charAt(i+1);
                if(temp == '\n') {
                    sb.append(temp);
                    first = true;
                } else if(temp>=48 && temp<=57 && first) {
                    sb.append("  /  ");
                    sb.append(temp);
                    first = false;
                } else {
                    sb.append(temp);
                }
            }
            parse = sb.toString();
        }
        return parse; //리스트에 데이터 추가
    }

    @Override
    protected void onPostExecute(Boolean status){
        if(!Init){
            fr.ReloadFragment();
        }
    }
}
