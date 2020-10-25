package com.example.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Map;

public class MyAdapter extends ArrayAdapter {

    public MyAdapter(@NonNull Context context, int resource, ArrayList<Map<String, String>> list) {
        super(context, resource, list);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Map<String,String> map = (Map<String,String>) getItem(position);
        TextView title = itemView.findViewById(R.id.taskTitle);
        TextView time = itemView.findViewById(R.id.taskTime);
        TextView id = itemView.findViewById(R.id.idText);
        title.setText(map.get("title"));
        time.setText(map.get("time"));
        id.setText(map.get("id"));

        return itemView;
    }
}
