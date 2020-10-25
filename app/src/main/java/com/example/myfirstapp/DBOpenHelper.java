package com.example.myfirstapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {

    private String CREATE_TABLE_SQL = "create table tb_rates (id integer primary key autoincrement,country,rate)";

    public DBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建单词的数据表
        db.execSQL(CREATE_TABLE_SQL);
        Log.i("TAG","-------create--new--table------");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("TAG","----------版本更新----------"+oldVersion+"----->"+newVersion);
    }
}
