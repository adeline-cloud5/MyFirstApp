package com.example.notes;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TaskGroupFragment extends Fragment {

    private ListView groupList;
    private GroupAdapter adapter;
    private DBOpenHelper dbOpenHelper;
    public static final String TB_NAME = "notes";
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

        dbOpenHelper = new DBOpenHelper(getActivity(),TB_NAME,null,1);
        groupList = getView().findViewById(R.id.groupList);

        tags.add("生活");
        tags.add("学习");
        tags.add("工作");
        tags.add("默认");
        getData();

        adapter = new GroupAdapter(getActivity(),R.layout.list_item,taglist);
        groupList.setAdapter(adapter);

    }

    //获取数据库的数据
    private void getData() {
        Log.i("TAG",tags.size()+"");
        for(int i = 0;i<tags.size();i++){
            int c = 0;
            Cursor cursor = dbOpenHelper.getReadableDatabase().query(TB_NAME,null,"tag=?",new String[]{tags.get(i)},null,null,null);
            Log.i("TAG","---------searching--data---------");
            Map<String,String> map = new HashMap<String, String>();
            //把查询结果保存到resultList中
            while (cursor.moveToNext()){
                c++;
                map.put("tagName",cursor.getString(3));
            }
            if(c==0){
                map.put("tagName",tags.get(i));
            }
            map.put("count",""+c);
            taglist.add(map);
        }
        Log.i("TAG",taglist.toString());
    }

}