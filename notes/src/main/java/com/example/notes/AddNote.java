package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNote extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, TextWatcher {

    private RadioButton tagButton1,tagButton2,tagButton3,tagButton4;
    private Button createButton;
    private EditText addTitle,addDetail;
    private RadioGroup radioGroup;
    private DBOpenHelper dbOpenHelper;
    private Toolbar toolbar;
    private TextView title;
    public static final String TB_NAME = "notes";
    private int checkedTag = 3;
    private String[] tagList = new String[]{"学习","工作","生活","默认"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //设置标题栏
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        dbOpenHelper = new DBOpenHelper(this,TB_NAME,null,1);
        radioGroup = findViewById(R.id.tagButtonGroup);
        addTitle = findViewById(R.id.addTitle);
        addDetail = findViewById(R.id.addDetail);

        tagButton1 = findViewById(R.id.tagButton1);
        tagButton2 = findViewById(R.id.tagButton2);
        tagButton3 = findViewById(R.id.tagButton3);
        tagButton4 = findViewById(R.id.tagButton4);
        createButton = findViewById(R.id.createButton);
        title = findViewById(R.id.titleBar);

        radioGroup.setOnCheckedChangeListener(this);
        createButton.setOnClickListener(this);
        addTitle.addTextChangedListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MainActivity.class);
            }
        });

    }

    //单选按钮监听
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        checkedTag = checkedId-2131231061;
        Log.i("TAG","checked:"+checkedTag);
    }

    //新建按钮监听
    @Override
    public void onClick(View v) {
        insertData();
    }

    //向数据库中插入数据
    private void insertData(){

        String title = addTitle.getText().toString();
        String detail = addDetail.getText().toString();
        String tag = tagList[checkedTag];
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(calendar.getTime());

        if(title.equals("")){
            Toast.makeText(getApplicationContext(), "标题不能为空！", Toast.LENGTH_SHORT).show();
        }else{
            SQLiteDatabase sqLiteDatabase = dbOpenHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("title",title);
            values.put("time",time);
            values.put("tag",tag);
            values.put("detail",detail);
            sqLiteDatabase.insert(TB_NAME,null,values);
            Log.i("TAG","------insert--data---------");
            openActivity(MainActivity.class);
        }
    }
    //打开新窗口
    public void openActivity(Class activity){
        Intent intent = new Intent(AddNote.this,activity);
        startActivity(intent);
    }

    //输入框改变时
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String i = s.toString();
        title.setText(i);
    }

    //输入框改变后
    @Override
    public void afterTextChanged(Editable s) {
    }
    //输入框改变前
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
}