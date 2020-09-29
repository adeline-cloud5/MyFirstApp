package com.example.mysecondapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView out;
    private EditText edit;
    private Button dollar,euro,won,config;
    private String  d,e,w;
    private double result;
    private Double d_exc = 0.15,e_exc = 0.12,w_exc = 0.11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取id
        out = findViewById(R.id.out2);
        edit = findViewById(R.id.edit2);
        dollar = findViewById(R.id.dollar_bt2);
        euro = findViewById(R.id.euro_bt2);
        won = findViewById(R.id.won_bt2);
        config = findViewById(R.id.config_bt2);

        //监听事件
        dollar.setOnClickListener(this);
        euro.setOnClickListener(this);
        won.setOnClickListener(this);
        config.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.dollar_bt2:
                change(0);
                break;
            case R.id.euro_bt2:
                change(1);
                break;
            case R.id.won_bt2:
                change(2);
                break;
            default:
                break;
        }

    }

    private void change(int country) {
        if (TextUtils.isEmpty(edit.getText().toString())) {
            Toast.makeText(MainActivity.this, "请输入数字！", Toast.LENGTH_SHORT).show();
        }
        if (country == 0) {
            d = edit.getText().toString();
            result = Float.parseFloat(d) * d_exc;
            out.setText(result+"");

        } else if (country == 1) {
            e = edit.getText().toString();
            result = Float.parseFloat(e) * e_exc;
            out.setText(result + "");
        } else {
            w = edit.getText().toString();
            result = Float.parseFloat(w) * w_exc;
            out.setText(result + "");
        }
    }
}
