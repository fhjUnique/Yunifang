package com.bwie.hhww.yunifang.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dell on 2017/3/29.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper{

    public MySQLiteOpenHelper(Context context) {
        super(context, "1502H.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user(id Integer primary key autoincrement,image text not null,price float(20),title text not null,Sid text not null,num Integer(10))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
