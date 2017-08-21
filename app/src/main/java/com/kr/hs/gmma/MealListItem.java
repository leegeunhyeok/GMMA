package com.kr.hs.gmma;

/**
 * Created by lghlo on 2017-07-23.
 */

public class MealListItem {
    private String date;
    private String food_info;

    public MealListItem(String date, String info){
        this.date = date;
        food_info = info;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setFood_info(String food_info){
        this.food_info = food_info;
    }

    public String getDate(){
        return date;
    }

    public String getFood_info(){
        return food_info;
    }

    public int getDateInt(){
        if(!date.equals("[ ]")){
            String temp;
            int index = date.indexOf("일");
            temp = date.substring(index-2, index);
            return Integer.parseInt(temp.replaceAll("\\s+", ""));
        } else return 0;
    }
}
