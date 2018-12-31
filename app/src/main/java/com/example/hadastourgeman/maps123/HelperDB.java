package com.example.hadastourgeman.maps123;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.hadastourgeman.maps123.DATA.DIRECTION;
import static com.example.hadastourgeman.maps123.DATA.DIS;
import static com.example.hadastourgeman.maps123.DATA.GPS;
import static com.example.hadastourgeman.maps123.DATA.TABLE_DATA;
import static com.example.hadastourgeman.maps123.Place.KEY_ID;
import static com.example.hadastourgeman.maps123.Place.POS;
import static com.example.hadastourgeman.maps123.Place.TABLE_PLACE;

public class HelperDB extends SQLiteOpenHelper {
    String strCreate, strDelete;
    private static final String DATABASE_NAME = "dbexam.db";
    private static final int DATABASE_VERSION = 1;


    public HelperDB(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        strCreate="CREATE TABLE "+TABLE_PLACE;
        strCreate+=" ("+KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+POS+" TEXT";
        strCreate+=");";
        db.execSQL(strCreate);

        strCreate="CREATE TABLE "+TABLE_DATA;
        strCreate+=" ("+KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+DIS+" INTEGER,";
        strCreate+=" "+GPS+" INTEGER,";
        strCreate+=" "+DIRECTION+" INTEGER";
        strCreate+=");";
        db.execSQL(strCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        strDelete="DROP TABLE IF EXISTS "+TABLE_PLACE;
        db.execSQL(strDelete);
        strDelete="DROP TABLE IF EXISTS "+TABLE_DATA;
        db.execSQL(strDelete);

        onCreate(db);
    }
}