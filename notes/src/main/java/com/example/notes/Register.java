package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button register;
    private EditText userName,password;
    private DBOpenHelper dbOpenHelper;
    public static final String TB_NAME = "users";
    private ArrayList<Map<String, String>> userlist = new ArrayList<Map<String, String>>();
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbOpenHelper = new DBOpenHelper(this,TB_NAME,null,1);

        toolbar = findViewById(R.id.registerToolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        register = findViewById(R.id.register_button);
        userName = findViewById(R.id.registerUserEdit);
        password = findViewById(R.id.registerPwEdit);

        register.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        allUsers();

    }

    //注册
    @Override
    public void onClick(View v) {

        String id = userName.getText().toString();
        String pw = password.getText().toString();

        Cursor cursor = dbOpenHelper.getReadableDatabase().query(TB_NAME,null,"username=?",new String[]{id},null,null,null);
        while (cursor.moveToNext()){
            str = cursor.getString(1);
        }
        if(id.equals("")||pw.equals("")){
            Toast.makeText(getApplicationContext(), "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
        }else if(id.equals(str)){
            Toast.makeText(getApplicationContext(), "此用户名已被注册！", Toast.LENGTH_SHORT).show();
        }else{
            SQLiteDatabase sqLiteDatabase = dbOpenHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("username",id);
            values.put("password",pw);
            sqLiteDatabase.insert(TB_NAME,null,values);
            Log.i("TAG","username:"+id+"-----password:"+pw);

            Intent intent = new Intent(this,Login.class);
            startActivity(intent);
        }
    }

    //获取所有用户
    public void allUsers(){
        Cursor cursor = dbOpenHelper.getReadableDatabase().query(TB_NAME,null,null,null,null,null,null);
        Log.i("TAG","---------searching--data---------");
        //把查询结果保存到resultList中
        while (cursor.moveToNext()){
            Map<String,String> map = new HashMap<String, String>();
            map.put("id",cursor.getString(0));
            map.put("username",cursor.getString(1));
            map.put("password",cursor.getString(2));
            userlist.add(map);
        }
        Log.i("TAG",userlist.toString());
    }
    //数据库删除数据
    private void deleteData(String username){
        SQLiteDatabase sqLiteDatabase = dbOpenHelper.getReadableDatabase();
        sqLiteDatabase.delete(TB_NAME,"username=?",new String[]{username});
        Log.i("TAG","------delete--data---------");
    }
}