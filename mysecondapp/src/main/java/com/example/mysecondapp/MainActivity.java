package com.example.mysecondapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText imput,chinese;
    private Button search,translate,newWord;
    private ListView listView;
    private DBOpenHelper dbOpenHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbOpenHelper = new DBOpenHelper(MainActivity.this,"dictionary",null,1);

        imput = findViewById(R.id.input);
        chinese = findViewById(R.id.Chinese);
        listView = findViewById(R.id.listView);
        search = findViewById(R.id.search);
        translate = findViewById(R.id.translate);
        newWord = findViewById(R.id.newWord);

        search.setOnClickListener(this);
        translate.setOnClickListener(this);
        newWord.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search:
                search_word();
                break;
            case R.id.translate:
                translate_word();
                break;
            case R.id.newWord:
                new_activity();
                break;
            default:
                break;
        }

    }

    //跳转到单词添加页面
    private void new_activity() {
        Intent intent = new Intent(MainActivity.this,AddActivity.class);
        startActivity(intent);
    }

    //翻译单词
    private void translate_word() {
        String key = chinese.getText().toString();
        Cursor cursor = dbOpenHelper.getReadableDatabase().query("dictionary",null,"word=?",new String[]{key},null,null,null);
        List<Map<String,String>> resultList = new ArrayList<Map<String, String>>();
        Log.i("TAG","---------searching---------");

        //把查询结果保存到resultList中
        while (cursor.moveToNext()){
            Map<String,String> map = new HashMap<String, String>();
            map.put("word",cursor.getString(1));
            map.put("translate",cursor.getString(2));
            resultList.add(map);
            Log.i("TAG","------saved-------");
        }

        if(resultList == null || resultList.size() == 0){
            Toast.makeText(MainActivity.this,"No result!",Toast.LENGTH_LONG).show();
        }else {
            SimpleAdapter simpleAdapter = new SimpleAdapter(
                    MainActivity.this,
                    resultList,
                    R.layout.list_item,
                    new String[]{"word","translate"},
                    new int[]{R.id.word,R.id.translation});
            Log.i("TAG","-------add--to--listview-------");
            listView.setAdapter(simpleAdapter);
        }
    }

    //关闭数据库连接
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbOpenHelper!=null){
            dbOpenHelper.close();
        }
    }

    //查询单词
    private void search_word() {
    }

}
