package com.example.myfirstapp;

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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView out;
    private EditText edit;
    private Button dollar,euro,won,config;
    private String  d,e,w;
    private Float result;
    private Float d_exc = 0.15f,e_exc = 0.12f,w_exc = 0.11f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取id
        out = findViewById(R.id.out);
        edit = findViewById(R.id.edit);
        dollar = findViewById(R.id.dollar_bt);
        euro = findViewById(R.id.euro_bt);
        won = findViewById(R.id.won_bt);
        config = findViewById(R.id.config_bt);

        /*//获取数据
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        d_exc = bundle.getFloat("dollar_rate");
        e_exc = bundle.getFloat("euro_rate");
        w_exc = bundle.getFloat("won_rate");*/

        //监听事件
        dollar.setOnClickListener(this);
        euro.setOnClickListener(this);
        won.setOnClickListener(this);
        config.setOnClickListener(this);

    }


    /*private void saveUsersInfo() {

        @SuppressLint("WrongConstant")
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", MODE_APPEND);
        PreferenceManager.getDefaultSharedPreferences(this);

        Float dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);
        Float euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        Float wonRate = sharedPreferences.getFloat("won_rate",0.0f);

        SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("dollar_rate", dollarRate);
        editor.putFloat("euro_rate", euroRate);
        editor.putFloat("won_rate", wonRate);
        editor.apply();

    }*/


    //加载菜单文件
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    //菜单监听
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu1:
                Toast.makeText(this,"menu1",Toast.LENGTH_SHORT).show();
            case R.id.menu2:
                Toast.makeText(this,"menu2",Toast.LENGTH_SHORT).show();
            case R.id.menu3:
                Toast.makeText(this,"menu3",Toast.LENGTH_SHORT).show();
            case R.id.menu4:
                Toast.makeText(this,"menu4",Toast.LENGTH_SHORT).show();
            default:
                break;
        }
        return true;
    }

    //鼠标点击事件
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.dollar_bt:
                change(0);
                break;
            case R.id.euro_bt:
                change(1);
                break;
            case R.id.won_bt:
                change(2);
                break;
            case R.id.config_bt:
                open();
                break;
            default:
                break;
        }
    }

    //汇率转换
    @SuppressLint("SetTextI18n")
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
            out.setText(result.toString());
        } else {
            w = edit.getText().toString();
            result = Float.parseFloat(w) * w_exc;
            out.setText(result.toString());
        }

    }

    //打开新窗口
    public void open(){

        //传递数据到页面二
        Intent intent = new Intent(this, MainActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putFloat("dollar_rate",d_exc);
        bundle.putFloat("euro_rate",e_exc);
        bundle.putFloat("won_rate",w_exc);
        intent.putExtras(bundle);

        //打开页面二
        startActivityForResult(intent,100);
    }

    //获取返回的值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==20){
            d_exc = data.getExtras().getFloat("dollar_rate");
            e_exc = data.getExtras().getFloat("euro_rate");
            w_exc = data.getExtras().getFloat("won_rate");
        }else{
            System.out.println("没有得到任何信息");
        }

    }

}