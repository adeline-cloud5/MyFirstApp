package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    private TextView dollar,euro,won;
    private Float d,e,w;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //获取id
        dollar = findViewById(R.id.dollar_change);
        euro = findViewById(R.id.euro_change);
        won = findViewById(R.id.won_change);
        save = findViewById(R.id.save_bt);

        //监听事件
        save.setOnClickListener(this);

    }
    //传递参数
    @Override
    public void onClick(View v) {
        d = Float.valueOf(dollar.getText().toString());
        e = Float.valueOf(euro.getText().toString());
        w = Float.valueOf(won.getText().toString());
        if(!"".equals(d) && !"".equals(e) && !"".equals(w)){
            Intent intent = new Intent(MainActivity2.this,MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putFloat("dollar",d);
            bundle.putFloat("euro",e);
            bundle.putFloat("won",w);
            intent.putExtras(bundle);
            startActivity(intent);
        }else {
            Toast.makeText(MainActivity2.this, "请输入数字！", Toast.LENGTH_SHORT).show();
        }

        }
}