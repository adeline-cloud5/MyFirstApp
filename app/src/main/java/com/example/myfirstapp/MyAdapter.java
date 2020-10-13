package com.example.myfirstapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter extends ArrayAdapter {

    public MyAdapter(@NonNull Context context, int resource, ArrayList<HashMap<String,String>> list) {
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
        TextView country = itemView.findViewById(R.id.country);
        TextView rate = itemView.findViewById(R.id.rate);
        country.setText(map.get("Country"));
        rate.setText(map.get("Rate"));

        return itemView;
    }
}
