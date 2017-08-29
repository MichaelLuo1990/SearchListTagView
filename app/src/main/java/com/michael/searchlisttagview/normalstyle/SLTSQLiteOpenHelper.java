package com.michael.searchlisttagview.normalstyle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SLTSQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    private static String name = "searchlisttag.db";
    private static Integer version = 1;

    public SLTSQLiteOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table records(id integer primary key autoincrement,name varchar(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}