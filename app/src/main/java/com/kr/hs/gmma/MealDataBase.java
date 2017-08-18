package com.kr.hs.gmma;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by lghlo on 2017-08-19.
 */

//TODO: 데이터베이스 효율성 높이기 위해 생각하기

public class MealDataBase {
    private static MealDataBase mDBManager = null;
    private SQLiteDatabase mDatabase = null;

    public static final int DB_VERSION = 1;
    public static final String _TABLE_NAME_ = "meal_data"; // 테이블 이름
    public static final String _DATABASE_NAME_ = "meal_month.db";

    public static final String DATE = "date"; // 날짜
    public static final String CONTENT = "content"; //급식 데이터

    Context mContext;

    //테이블 생성 SQL문 문자열
    public static final String _CREATE_ = "CREATE TABLE IF NOT EXISTS " + _TABLE_NAME_ + "("
            + DATE + " INTEGER, "
            + CONTENT + " TEXT NOT NULL);";

    public static MealDataBase getInstance(Context context){
        if(mDBManager == null){
            mDBManager = new MealDataBase(context);
        }
        return mDBManager;
    }

    // 생성자
    private MealDataBase(Context context){
        Log.i("GMMAHS", "GET DB Manager Instance");
        mContext = context;
        mDatabase = context.openOrCreateDatabase(_DATABASE_NAME_, Context.MODE_PRIVATE, null);

        mDatabase.execSQL(_CREATE_);
    }

    public void insert(String date, String content){
        Log.i("GMMAHS", "ADD Data in database");
        mDatabase.execSQL("INSERT INTO " + _TABLE_NAME_ + " VALUES(\'"
                            + date + "\', \'"
                            + content + "\');");
    }

    public void reset(){
        Log.i("GMMAHS", "RESET Database");
        mDatabase.execSQL("DELETE FROM " + _TABLE_NAME_);
    }

    public Cursor getAllData(){
        return mDatabase.rawQuery("SELECT * FROM " + _TABLE_NAME_, null);
    }


    public void close(){

    }
}
