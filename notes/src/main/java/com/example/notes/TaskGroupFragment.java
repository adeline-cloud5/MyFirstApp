package com.example.notes;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TaskGroupFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {

    private ListView groupList;
    private GroupAdapter adapter;
    private DBOpenHelper dbOpenHelperNote,dbOpenHelperTag;
    private Button createTag;
    private String imputTag;
    public static final String TB_NAME = "notes";
    public static final String TB_TAG = "tags";
    private ArrayList<String> tags = new ArrayList<String>();
    private ArrayList<Map<String, String>> taglist = new ArrayList<Map<String, String>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_group, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dbOpenHelperNote = new DBOpenHelper(getActivity(),TB_NAME,null,1);
        dbOpenHelperTag = new DBOpenHelper(getActivity(),TB_TAG,null,1);
        groupList = getView().findViewById(R.id.groupList);
        createTag = getView().findViewById(R.id.createTag);

        getData();
        adapter = new GroupAdapter(getActivity(),R.layout.list_item,taglist);
        groupList.setAdapter(adapter);
        groupList.setOnItemClickListener(this);
        groupList.setOnItemLongClickListener(this);
        createTag.setOnClickListener(this);

    }

    //列表点击事件监听
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getActivity(), GroupDetail.class);
        Object itemAtPosition = groupList.getItemAtPosition(position);
        HashMap<String,String> map = (HashMap<String, String>) itemAtPosition;
        String tagName = map.get("tagName");
        Log.i("TAG", "onItemClick: tag=" + tagName);

        Bundle bundle = new Bundle();
        bundle.putString("tagName",tagName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //添加tag事件监听
    @Override
    public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View view = factory.inflate(R.layout.add_tag, null);
        final EditText edit = view.findViewById(R.id.addTagEdit);

        builder.setTitle("请输入自定义分类")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //向数据库中插入数据
                        imputTag = edit.getText().toString();
                        Log.i("TAG","imput:"+imputTag);
                        insertTag();
                    }
                }).setNegativeButton("取消", null);
        builder.create().show();
    }

    private void insertTag() {
        if(imputTag.equals("")){
            Toast.makeText(getActivity(),"分类名不能为空", Toast.LENGTH_SHORT).show();
        }else {
            SQLiteDatabase sqLiteDatabase = dbOpenHelperTag.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("tag",imputTag);
            sqLiteDatabase.insert(TB_TAG,null,values);
            Log.i("TAG","------insert--data---------");
            onResume();
        }
    }

    //列表长按点击事件监听
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示")
                .setMessage("请确认是否删除数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Object itemAtPosition = groupList.getItemAtPosition(position);
                        HashMap<String,String> map = (HashMap<String, String>) itemAtPosition;
                        String tagName = map.get("tagName");
                        if(tagName.equals("default")){
                            Toast.makeText(getActivity(),"默认分类无法删除", Toast.LENGTH_SHORT).show();
                        }else{
                            taglist.remove(position);
                            deleteData(tagName);
                            //刷新
                            adapter.notifyDataSetChanged();
                        }
                    }
                }).setNegativeButton("否",null);
        builder.create().show();
        return true;
    }

    //数据库删除数据
    private void deleteData(String tagName){
        SQLiteDatabase sqLiteDatabase1 = dbOpenHelperTag.getReadableDatabase();
        sqLiteDatabase1.delete(TB_TAG,"tag=?",new String[]{tagName});
        Log.i("TAG","------delete--tag--"+tagName+"-------");

        ContentValues values = new ContentValues();
        values.put("tag","default");
        SQLiteDatabase sqLiteDatabase2 = dbOpenHelperNote.getReadableDatabase();
        sqLiteDatabase2.update(TB_NAME,values,"tag=?",new String[]{tagName});
        Log.i("TAG","------update--tag--to--default-----");
    }

    //获取数据库的数据
    private void getData() {

        //获取tag列表
        Cursor cursorTag = dbOpenHelperTag.getReadableDatabase().query(TB_TAG,null,null,null,null,null,null);
        while (cursorTag.moveToNext()){
            tags.add(cursorTag.getString(1));
        }

        //根据tag关键字查询便签列表确定每个tag中包含的便签数
        for(int i = 0;i<tags.size();i++){
            int c = 0;
            Cursor cursorNote = dbOpenHelperNote.getReadableDatabase().query(TB_NAME,null,"tag=?",new String[]{tags.get(i)},null,null,null);
            Log.i("TAG","---------searching--data---------");
            Map<String,String> map = new HashMap<String, String>();
            //把查询结果保存到tagList中
            while (cursorNote.moveToNext()){
                c++;
                map.put("tagName",cursorNote.getString(3));
            }
            if(c==0){
                map.put("tagName",tags.get(i));
            }
            map.put("count",""+c);
            map.put("id",i+"");
            taglist.add(map);
        }
        Log.i("TAG",taglist.toString());
    }

    //返回后重新启动fragment并刷新
    @Override
    public void onResume() {
        super.onResume();
        tags = new ArrayList<String>();
        taglist = new ArrayList<Map<String, String>>();
        //获取数据库中的数据
        getData();
        adapter = new GroupAdapter(getActivity(),R.layout.list_item,taglist);
        groupList.setAdapter(adapter);
    }
}