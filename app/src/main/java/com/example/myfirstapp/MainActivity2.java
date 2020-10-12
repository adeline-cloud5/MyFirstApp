package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    private TextView dollar,euro,won;
    private Float d_exc,e_exc,w_exc;
    private Button save;
    SharedPreferences sp;
    private Map<String,Float> map=new HashMap<String,Float>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //获取id
        dollar = findViewById(R.id.dollar_change);
        euro = findViewById(R.id.euro_change);
        won = findViewById(R.id.won_change);
        save = findViewById(R.id.save_bt);

        //获取SharedPreferences对象
        sp = getSharedPreferences("myrate",MODE_PRIVATE);
        Float dollarRate = sp.getFloat("dollar_rate",0.0f);
        Float euroRate = sp.getFloat("euro_rate",0.0f);
        Float wonRate = sp.getFloat("won_rate",0.0f);

        //监听事件
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //保存数据
        d_exc = Float.valueOf(dollar.getText().toString());
        e_exc = Float.valueOf(euro.getText().toString());
        w_exc = Float.valueOf(won.getText().toString());
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("dollar_rate",d_exc);
        editor.putFloat("euro_rate",e_exc);
        editor.putFloat("won_rate",w_exc);
        editor.commit();
        Toast.makeText(MainActivity2.this,"已保存",Toast.LENGTH_SHORT).show();

        //传递数据到页面一
        Intent intent = new Intent();
        intent.putExtra("dollar_rate",d_exc);
        intent.putExtra("euro_rate",e_exc);
        intent.putExtra("won_rate",w_exc);
        setResult(20,intent);
        finish();

    }
}