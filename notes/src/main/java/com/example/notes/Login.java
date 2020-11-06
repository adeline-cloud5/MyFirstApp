package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button login,register;
    private EditText userName,password;
    private DBOpenHelper dbOpenHelper;
    public static final String TB_NAME = "users";
    private String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbOpenHelper = new DBOpenHelper(this,TB_NAME,null,1);

        toolbar = findViewById(R.id.loginToolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.registerButton);
        userName = findViewById(R.id.loginUserEdit);
        password = findViewById(R.id.loginPwEdit);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //按钮监听
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                //登录
                checkData();
                break;
            case R.id.registerButton:
                //注册
                gotoRegister();
                break;
            default:
                break;
        }
    }

    //登录，检查用户名和密码
    private void checkData() {
        String pw = password.getText().toString();
        String username = userName.getText().toString();
        if(username.equals("")||pw.equals("")){
            Toast.makeText(getApplicationContext(), "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
        }else {
            int index = 0;
            Cursor cursor = dbOpenHelper.getReadableDatabase().query(TB_NAME,null,"username=?",new String[]{username},null,null,null);
            while (cursor.moveToNext()){
                str = cursor.getString(2);
                index++;
            }
            if(index == 0){
                Toast.makeText(getApplicationContext(), "用户不存在！", Toast.LENGTH_SHORT).show();
            }else if(index==1 && pw.equals(str)){
                //登陆成功
                //Intent intent = new Intent();
                Intent intent = getIntent();
                intent.putExtra("username",username);
                setResult(2,intent);
                finish();
                Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "用户名或密码错误！", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //注册，添加用户名和密码
    private void gotoRegister() {
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
    }
}