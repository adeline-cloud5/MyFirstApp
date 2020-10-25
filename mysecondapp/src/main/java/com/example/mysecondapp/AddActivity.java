package com.example.mysecondapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_word,edit_translate;
    private Button save,cancel;
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_word);

        dbOpenHelper = new DBOpenHelper(AddActivity.this,"dictionary",null,1);

        edit_word = findViewById(R.id.edit_word);
        edit_translate = findViewById(R.id.edit_translation);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);

        save.setOnClickListener(this);
        cancel.setOnClickListener(this);



    }

    //按钮单击监听事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save:
                save_word();
                break;
            case R.id.cancel:
                return_main();
                break;
            default:
                break;
        }
    }

    //返回主界面
    private void return_main() {
        Intent intent = new Intent(AddActivity.this,MainActivity.class);
        startActivity(intent);
    }

    //保存单词
    private void save_word() {
        String word = edit_word.getText().toString();
        String translate = edit_translate.getText().toString();

        if(word.equals("")||translate.equals("")){
            Toast.makeText(AddActivity.this,"Please input word and translation!",Toast.LENGTH_SHORT).show();
        }else {
            insertData(dbOpenHelper.getReadableDatabase(),word,translate);
            Toast.makeText(AddActivity.this,"Successfully add a new word!",Toast.LENGTH_SHORT).show();
            return_main();
        }
    }

    //插入数据
    private void insertData(SQLiteDatabase sqLiteDatabase,String word,String translate){
        ContentValues values = new ContentValues();
        values.put("word",word);
        values.put("translate",translate);
        sqLiteDatabase.insert("dictionary",null,values);
    }

    //关闭数据库连接
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbOpenHelper!=null){
            dbOpenHelper.close();
        }
    }
}