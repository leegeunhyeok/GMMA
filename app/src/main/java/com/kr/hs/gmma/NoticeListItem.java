package com.kr.hs.gmma;

/**
 * Created by lghlo on 2017-07-25.
 */

public class NoticeListItem {
    private String title;
    private String date;
    private String writer;
    private String post_url;

    public NoticeListItem(String date, String title, String writer, String post_url){
        this.date = date;
        this.title = title;
        this.writer = writer;
        this.post_url = post_url;
    }

    public String getTitle(){
        return title;
    }

    public String getDate(){
        return date;
    }

    public String getWriter(){
        return writer;
    }

    public String getUrl() {
        return post_url;
    }
}
