package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    private TextView username;

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
        username = findViewById(R.id.userName);
        initDrawerLayout();
        initFragment();

        floatButton.setOnClickListener(this);
        settings.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DrawerLayout layout = findViewById(R.id.drawer_layout);
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //item点击事件监听
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.login:
                Intent intent1 = new Intent(this,Login.class);
                startActivity(intent1);
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

    //按钮点击事件监听器
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floatButton:
                Intent intent = new Intent(this,AddNote.class);
                startActivity(intent);
                break;
            case R.id.settings:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }

    //初始化drawerLayout
    public void initDrawerLayout() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);//取消左边三条横杠的图标
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();// 需要将ActionDrawerToggle与DrawerLayout的状态同步
        //setNavigationOnClickListener一定要放在setSupportActionBar(toolbar)
        // 和 drawerLayout.addDrawerListener(toggle)之后,不然onclick无效
        toolbar.setNavigationOnClickListener(this);//自定义图标打开左侧菜单
        NavigationView navigation = findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(this);//给侧拉菜单添加监听事件
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
                        mFragments[0].onResume();
                        fragmentTransaction.show(mFragments[0]).commit();
                        break;
                    case R.id.ListGroupButton:
                        mFragments[1].onResume();
                        fragmentTransaction.show(mFragments[1]).commit();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
