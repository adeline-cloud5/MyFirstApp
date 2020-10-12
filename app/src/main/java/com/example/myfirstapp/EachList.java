package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class EachList extends AppCompatActivity {

    private TextView each_country,each_result;
    private EditText imput_rmb;
    private String country,input;
    private Float rate,result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_list);

        each_country = findViewById(R.id.each_country);
        each_result = findViewById(R.id.each_result);
        imput_rmb = findViewById(R.id.imput_rmb);

        //获取页面传来的数据
        Bundle bundle = getIntent().getExtras();
        country = bundle.getString("Country");
        rate = Float.valueOf(bundle.getString("Rate"))/100;

        //显示国家名称
        each_country.setText(country);

        //实时监测输入框的内容
        imput_rmb.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String i = s.toString();
                result = Float.parseFloat(i) * rate;
                each_result.setText(result.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }
}