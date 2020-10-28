package com.example.notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupDetail extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private TextView tagTitle;
    private ListView taglist;
    private DBOpenHelper dbOpenHelper;
    public static final String TB_NAME = "notes";
    private ListAdapter adapter;
    private String tagName;
    private Toolbar toolbar;
    private ArrayList<Map<String, String>> maplist = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        toolbar = findViewById(R.id.tagBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        dbOpenHelper = new DBOpenHelper(GroupDetail.this,TB_NAME,null,1);
        tagTitle = findViewById(R.id.tagTitle);
        taglist = findViewById(R.id.tagList);

        Bundle bundle = getIntent().getExtras();
        tagName = bundle.getString("tagName");
        tagTitle.setText(tagName);
        getData();
        adapter = new ListAdapter(GroupDetail.this,R.layout.list_item,maplist);
        taglist.setAdapter(adapter);

        //创建点击事件监听器
        taglist.setOnItemClickListener(this);
        taglist.setOnItemLongClickListener(this);
    }

    //列表点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(GroupDetail.this, ListDetail.class);
        Object itemAtPosition = taglist.getItemAtPosition(position);
        HashMap<String,String> map = (HashMap<String, String>) itemAtPosition;
        String title = map.get("title");
        String time = map.get("time");
        String idText = map.get("id");
        String tag = map.get("tag");
        Log.i("TAG", "onItemClick: title=" + title);
        Log.i("TAG", "onItemClick: time=" + time);
        Log.i("TAG", "onItemClick: id=" + idText);

        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("time",time);
        bundle.putString("id",idText);
        bundle.putString("tag",tag);
        intent.putExtras(bundle);
        startActivity(intent);
        Log.i("TAG","--------open----------");
    }

    //返回后重新启动activity并刷新
    @Override
    protected void onResume() {
        super.onResume();
        maplist = new ArrayList<Map<String, String>>();
        getData();
        adapter = new ListAdapter(GroupDetail.this,R.layout.list_item,maplist);
        taglist.setAdapter(adapter);
    }

    //列表长按点击事件
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("请确认是否删除数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Object itemAtPosition = taglist.getItemAtPosition(position);
                        HashMap<String,String> map = (HashMap<String, String>) itemAtPosition;
                        String idText = map.get("id");
                        maplist.remove(position);
                        deleteData(idText);
                        //刷新
                        adapter.notifyDataSetChanged();
                        Log.i("TAG","------刷新--------");
                    }
                }).setNegativeButton("否",null);
        builder.create().show();
        return true;
    }

    //点击返回按钮返回上一级
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //获取数据库的数据
    private void getData() {
        Cursor cursor = dbOpenHelper.getReadableDatabase().query(TB_NAME,null,"tag=?",new String[]{tagName},null,null,null);
        Log.i("TAG","---------searching--data---------");

        //把查询结果保存到resultList中
        while (cursor.moveToNext()){
            Map<String,String> map = new HashMap<String, String>();
            map.put("id",cursor.getString(0));
            map.put("title",cursor.getString(1));
            map.put("time",cursor.getString(2));
            map.put("tag",cursor.getString(3));
            map.put("detail",cursor.getString(4));
            maplist.add(map);
        }
        Log.i("TAG",maplist.toString());
    }

    //数据库删除数据
    private void deleteData(String idtext){

        SQLiteDatabase sqLiteDatabase = dbOpenHelper.getReadableDatabase();
        sqLiteDatabase.delete(TB_NAME,"id=?",new String[]{idtext});
        Log.i("TAG","------delete--data---------");
    }

    //关闭数据库连接
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(dbOpenHelper!=null){
            dbOpenHelper.close();
        }
    }
}