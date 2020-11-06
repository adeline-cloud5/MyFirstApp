package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {

    private Fragment[] mFragments;
    private RadioGroup fragmentRadioGroup;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FloatingActionButton floatButton;
    private ImageButton settings;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigation;
    private View headView;
    private TextView user;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.mainToolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        fragmentRadioGroup = findViewById(R.id.TopRadioGroup);
        floatButton = findViewById(R.id.floatButton);
        settings = findViewById(R.id.settings);
        navigation = findViewById(R.id.nav_view);
        headView = navigation.getHeaderView(0);
        user = headView.findViewById(R.id.loginName);
        drawer = findViewById(R.id.drawer_layout);

        username = user.getText().toString();
        initDrawerLayout();
        initFragment();

        floatButton.setOnClickListener(this);
        settings.setOnClickListener(this);

    }

    //点击返回键隐藏侧滑菜单
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //item点击事件监听
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.login:
                Intent intent = new Intent(this,Login.class);
                startActivityForResult(intent,0);
                break;
            case R.id.register:
                Intent intent2 = new Intent(this,Register.class);
                startActivity(intent2);
                break;
            case R.id.more:
                break;
            default:
                break;
        }
        DrawerLayout drawer=findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //返回页面时获取数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 2){
            username = data.getStringExtra("username");
            Log.i("TAG","-----set---name----"+username+"----");
            user.setText(username);
        }
    }

    //按钮点击事件监听器
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floatButton:
                Intent intent = new Intent(this,AddNote.class);
                startActivity(intent);
                break;
            case R.id.settings:
                drawer.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }

    //初始化drawerLayout
    public void initDrawerLayout() {
        //关联drawerLayout和toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //取消左边三条横杠的图标
        toggle.setDrawerIndicatorEnabled(false);
        drawer.addDrawerListener(toggle);
        //初始化
        toggle.syncState();
        //toolbar.setNavigationOnClickListener(this);
        //给侧拉菜单添加监听事件
        navigation.setNavigationItemSelectedListener(this);
    }

    //控制fragment切换
    public void initFragment(){

        mFragments = new Fragment[2];
        fragmentManager = getSupportFragmentManager();
        mFragments[0] = fragmentManager.findFragmentById(R.id.TaskListFragment);
        mFragments[1] = fragmentManager.findFragmentById(R.id.ListGroupFragment);
        fragmentTransaction = fragmentManager.beginTransaction()
                .hide(mFragments[0]).hide(mFragments[1]);
        fragmentTransaction.show(mFragments[0]).commit();

        fragmentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fragmentTransaction = fragmentManager.beginTransaction()
                        .hide(mFragments[0]).hide(mFragments[1]);
                switch (checkedId){
                    case R.id.TaskListButton:
                        //刷新
                        mFragments[0].onResume();
                        //切换
                        fragmentTransaction.show(mFragments[0]).commit();
                        break;
                    case R.id.ListGroupButton:
                        //刷新
                        mFragments[1].onResume();
                        //切换
                        fragmentTransaction.show(mFragments[1]).commit();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
