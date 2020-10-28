package com.example.notes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {

    private String CREATE_TABLE1_SQL = "create table notes (id integer primary key autoincrement,title text,time datatime,tag text,detail text)";
    private String CREATE_TABLE2_SQL = "create table tags (id integer primary key autoincrement,tag text)";

    public DBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建单词的数据表
        db.execSQL(CREATE_TABLE1_SQL);
        Log.i("TAG","-------create--table1------");
        db.execSQL(CREATE_TABLE2_SQL);
        Log.i("TAG","-------create--table2------");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("TAG","----------版本更新----------"+oldVersion+"----->"+newVersion);
    }
}
