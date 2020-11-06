package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddNote extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, TextWatcher {

    private RadioButton radioButton;
    private Button createButton;
    private EditText addTitle,addDetail;
    private RadioGroup radioGroup;
    private DBOpenHelper dbOpenHelperNote,dbOpenHelperTag;
    private Toolbar toolbar;
    private TextView title;
    private String selectTag;
    public static final String TB_NOTE = "notes";
    public static final String TB_TAG = "tags";
    private ArrayList<String> tags = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //设置标题栏
        toolbar = findViewById(R.id.addNoteToolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        dbOpenHelperNote = new DBOpenHelper(this,TB_NOTE,null,1);
        dbOpenHelperTag = new DBOpenHelper(this,TB_TAG,null,1);
        addTitle = findViewById(R.id.addTitle);
        addDetail = findViewById(R.id.addDetail);
        createButton = findViewById(R.id.createButton);
        title = findViewById(R.id.titleBar);
        radioGroup = findViewById(R.id.tagButtonGroup);
        addRadioButton();

        radioGroup.setOnCheckedChangeListener(this);
        createButton.setOnClickListener(this);
        addTitle.addTextChangedListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //动态添加按钮
    public void addRadioButton(){
        //获取标签分类数据表
        getTagData();
        int index = 0;
        for(String str:tags){
            RadioButton button = new RadioButton(this);
            button.setText(str);
            button.setId(index);
            button.setTextSize(18);
            radioGroup.addView(button);
            //设置按钮的margin边距
            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) button
                    .getLayoutParams();
            layoutParams.setMargins(0,0,0,10);
            button.setLayoutParams(layoutParams);
            index++;
        }
    }

    //获取数据库的数据
    private void getTagData() {
        Cursor cursor = dbOpenHelperTag.getReadableDatabase().query(TB_TAG,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String tag = cursor.getString(1);
            tags.add(tag);
        }
        Log.i("TAG","-----"+tags+"----");
    }

    //单选按钮监听
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        selectTag = radioButton.getText().toString();
        Log.i("TAG",selectTag);
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
        //获取当前时间
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(calendar.getTime());

        if(title.equals("")){
            Toast.makeText(getApplicationContext(), "标题不能为空！", Toast.LENGTH_SHORT).show();
        }else{
            SQLiteDatabase sqLiteDatabase = dbOpenHelperNote.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("title",title);
            values.put("time",time);
            values.put("tag",selectTag);
            values.put("detail",detail);
            sqLiteDatabase.insert(TB_NOTE,null,values);
            Log.i("TAG","------insert--data---------");
            finish();
        }
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