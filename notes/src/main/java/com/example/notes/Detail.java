package com.example.notes;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Detail extends AppCompatActivity implements View.OnClickListener{

    private ImageButton back_bt,save_bt;
    private TextView timeDetail,titleDetail;
    private EditText editDetail;
    private String title,time,id;
    public static final String TB_NAME = "notes";
    private DBOpenHelper dbOpenHelper;
    private Map<String,String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbOpenHelper = new DBOpenHelper(this,TB_NAME,null,1);
        back_bt = findViewById(R.id.back_bt);
        save_bt = findViewById(R.id.save_bt);
        titleDetail = findViewById(R.id.titleDetail);
        timeDetail = findViewById(R.id.timeDetail);
        editDetail = findViewById(R.id.editDetail);

        //获取页面传来的数据
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        time = bundle.getString("time");
        id = bundle.getString("id");
        titleDetail.setText(title);
        timeDetail.setText(time);
        //editDetail.setText(getData("detail"));

        back_bt.setOnClickListener(this);
        save_bt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_bt:
                newAlertDialog();
                break;
            case R.id.save_bt:
                //saveData(title,time,tag,detail);
                break;
            default:
                break;
        }
    }

    //打开新窗口
    public void openRate(){
        Intent intent = new Intent(this, TaskListFragment.class);
        startActivity(intent);
    }
    //创建提示框
    public void newAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
        builder.setTitle("提示")
                .setMessage("请确认是否保存数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("TAG","OnClick：保存数据");
                        //saveData(title,time,tag,detail);
                        openRate();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openRate();
            }
        })
                .setNeutralButton("取消", null);
        builder.create().show();
    }

    //保存数据到数据库
    private void saveData(String title,String time,String tag,String detail) {
        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("time",time);
        values.put("tag",tag);
        values.put("detail",detail);
        dbOpenHelper.getReadableDatabase().update(TB_NAME,values,"id=?",new String[]{id});
        Log.i("TAG","-------update--data----------");
    }

    //获取数据库的数据
    private String getData(String what) {
        Cursor cursor = dbOpenHelper.getReadableDatabase().query(TB_NAME,null,"id=?",new String[]{id},null,null,null);
        String id = cursor.getString(0);
        String tag = cursor.getString(3);
        String detail = cursor.getString(4);
        Log.i("TAG",map.toString());
        if(what.equals("id")){
            return id;
        }else if(what.equals("tag")){
            return tag;
        }else if(what.equals("detail")){
            return detail;
        }else {
            return null;
        }
    }
}