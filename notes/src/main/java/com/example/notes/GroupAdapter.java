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

public class GroupAdapter extends ArrayAdapter {

    public GroupAdapter(@NonNull Context context, int resource, ArrayList<Map<String, String>> list) {
        super(context, resource,list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.group_item,parent,false);
        }

        Map<String,String> map = (Map<String,String>) getItem(position);
        TextView tagName = itemView.findViewById(R.id.tagName);
        TextView count = itemView.findViewById(R.id.count);
        tagName.setText(map.get("tagName"));
        count.setText(map.get("count")+"篇");

        return itemView;
    }
}
