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
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Detail extends AppCompatActivity implements View.OnClickListener{

    private ImageButton back_bt,tag_bt;
    private TextView timeDetail,tagText;
    private EditText titleDetail,editDetail;
    private String title,time,id;
    public static final String TB_NAME = "notes";
    private DBOpenHelper dbOpenHelper;
    private Map<String,String> map;
    private String[] tagList = new String[]{"学习","工作","生活","默认"};
    public int checkeditem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbOpenHelper = new DBOpenHelper(this,TB_NAME,null,1);
        back_bt = findViewById(R.id.back_bt);
        tag_bt = findViewById(R.id.tag_bt);
        titleDetail = findViewById(R.id.titleDetail);
        timeDetail = findViewById(R.id.timeDetail);
        editDetail = findViewById(R.id.editDetail);
        tagText = findViewById(R.id.tagText);

        //获取页面传来的数据
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        time = bundle.getString("time");
        id = bundle.getString("id");
        titleDetail.setText(title);
        timeDetail.setText(time);
        editDetail.setText("detail");
        tagText.setText(getData("tag"));
        editDetail.setText(getData("detail"));

        back_bt.setOnClickListener(this);
        tag_bt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_bt:
                saveAlertDialog();
                break;
            case R.id.tag_bt:
                tagAlertDialog();
                break;
            default:
                break;
        }
    }

    //选择标签提示框
    private void tagAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
        builder.setTitle("选择标签")
                .setSingleChoiceItems(tagList, checkeditem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkeditem = which;
                        Toast.makeText(getApplicationContext(), "成功加入" + tagList[which]+"标签", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        tagText.setText(tagList[checkeditem]);
                    }
                });
        builder.create().show();
    }

    //保存提示框
    public void saveAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
        builder.setTitle("提示")
                .setMessage("请确认是否保存数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //获取修改后的数据
                        String detailText = editDetail.getText().toString();
                        String titleText = titleDetail.getText().toString();
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = sdf.format(calendar.getTime());

                        if(titleText.equals("")){
                            Toast.makeText(getApplicationContext(), "请输入标题！", Toast.LENGTH_SHORT).show();
                        }else if(detailText.equals("")){
                            Toast.makeText(getApplicationContext(), "请输入内容！", Toast.LENGTH_SHORT).show();
                        }else{
                            saveData(titleText,date,tagList[checkeditem],detailText);
                            Log.i("TAG","OnClick：保存数据");
                            openActivity(TaskListFragment.class);
                        }
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openActivity(MainActivity.class);
            }
        })
                .setNeutralButton("取消", null);
        builder.create().show();
    }

    //打开新窗口
    public void openActivity(Class activity){
        Intent intent = new Intent(this,activity);
        startActivity(intent);
    }

    //保存数据到数据库
    private void saveData(String title,String date,String tag,String detail) {
        if(isChanged()){
            ContentValues values = new ContentValues();
            values.put("title",title);
            values.put("time",date);
            values.put("tag",tag);
            values.put("detail",detail);
            dbOpenHelper.getReadableDatabase().update(TB_NAME,values,"id=?",new String[]{id});
            Log.i("TAG","-------update--data----------");
        }else{
            return;
        }
    }

    //判断页面数据是否被修改
    public boolean isChanged(){
        String preTitle = getData("title");
        String preTag = getData("tag");
        String preDetail = getData("detail");
        String newTitle = titleDetail.getText().toString();
        String newDetail = editDetail.getText().toString();
        String newTag = tagList[checkeditem];
        if(preTitle!=newTitle || preTag!=newTag || preDetail!=newDetail){
            Log.i("TAG","-------changed------");
            return true;
        }else{
            Log.i("TAG","-------not changed------");
            return false;
        }
    }

    //获取数据库的数据
    private String getData(String what) {
        Cursor cursor = dbOpenHelper.getReadableDatabase().query(TB_NAME,null,"id=?",new String[]{id},null,null,null);
        String id = null,tag = null,detail = null,title = null;
        while (cursor.moveToNext()){
            id = cursor.getString(0);
            title = cursor.getString(1);
            tag = cursor.getString(3);
            detail = cursor.getString(4);
        }
        Log.i("TAG",id+"-----"+tag+"----"+detail);
        if(what.equals("id")){
            return id;
        }else if(what.equals("tag")){
            return tag;
        }else if(what.equals("detail")){
            return detail;
        }else if(what.equals("title")){
            return title;
        }else {
            return null;
        }
    }
}