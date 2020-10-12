package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView out;
    private EditText edit;
    private Button dollar,euro,won,config,rate;
    private String  d,e,w;
    private Float result;
    private Float d_exc=0.5f,e_exc=0.15f,w_exc=1.2f;

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
        rate = findViewById(R.id.rate_bt);

        //监听事件
        dollar.setOnClickListener(this);
        euro.setOnClickListener(this);
        won.setOnClickListener(this);
        config.setOnClickListener(this);
        rate.setOnClickListener(this);

    }

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
                openConfig();
                break;
            case R.id.rate_bt:
                openRate();
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
    public void openConfig(){

        //传递数据到页面二
        Intent intent = new Intent(this, MainActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putFloat("dollar_rate",d_exc);
        bundle.putFloat("euro_rate",e_exc);
        bundle.putFloat("won_rate",w_exc);
        intent.putExtras(bundle);

        //打开页面二并返回结果
        startActivityForResult(intent,100);
    }

    //打开新窗口
    public void openRate(){
        //打开页面三
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
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

    /*//fragment转换
    private void changeFragment(int key) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment f = null;
        if(key == 1){
            f = new Fragment1();
        }else if(key == 2){
            f = new Fragment2();
        }
        ft.replace(R.id.fragment,f);
        ft.commit();
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

    /*// 屏幕横竖屏切换
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if ( newConfig.orientation == Configuration.ORIENTATION_PORTRAIT ) {
            setContentView(R.layout.activity_main);
        } else{
            setContentView(R.layout.main_change);
        }
    }*/

}