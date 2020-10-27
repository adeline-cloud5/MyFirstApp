package com.example.notes;

import android.app.AlertDialog;
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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private DBOpenHelper dbOpenHelper;
    public static final String TB_NAME = "notes";
    private ListAdapter adapter;
    private ArrayList<Map<String, String>> maplist = new ArrayList<Map<String, String>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dbOpenHelper = new DBOpenHelper(getActivity(),TB_NAME,null,1);
        listView = getView().findViewById(R.id.mylist);

        getData();
        adapter = new ListAdapter(getActivity(),R.layout.list_item,maplist);
        listView.setAdapter(adapter);
        //当列表中没有数据时显示空视图
        listView.setEmptyView(getView().findViewById(R.id.nodata));

        //创建点击事件监听器
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    //获取数据库的数据
    private void getData() {
        Cursor cursor = dbOpenHelper.getReadableDatabase().query(TB_NAME,null,null,null,null,null,null);
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

    //列表点击事件监听器
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Intent intent = new Intent(getActivity(), Detail.class);
        Object itemAtPosition = listView.getItemAtPosition(position);
        HashMap<String,String> map = (HashMap<String, String>) itemAtPosition;
        String title = map.get("title");
        String time = map.get("time");
        String idText = map.get("id");
        Log.i("TAG", "onItemClick: title=" + title);
        Log.i("TAG", "onItemClick: time=" + time);
        Log.i("TAG", "onItemClick: id=" + idText);

        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("time",time);
        bundle.putString("id",idText);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //列表长按事件监听器
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示")
                .setMessage("请确认是否删除数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Object itemAtPosition = listView.getItemAtPosition(position);
                        HashMap<String,String> map = (HashMap<String, String>) itemAtPosition;
                        String idText = map.get("id");
                        deleteData(idText);
                        //刷新
                        //adapter.notifyDataSetChanged();
                        openActivity(MainActivity.class);
                        Log.i("TAG","------刷新--------");
                    }
                }).setNegativeButton("否",null);
        builder.create().show();
        return true;
    }

    //打开新窗口
    public void openActivity(Class activity){
        Intent intent = new Intent(getActivity(),activity);
        startActivity(intent);
    }

}