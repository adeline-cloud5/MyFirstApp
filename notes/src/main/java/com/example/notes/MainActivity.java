package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    private Fragment[] mFragments;
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RadioButton listButton,groupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listButton = findViewById(R.id.TaskListButton);
        groupButton = findViewById(R.id.ListGroupButton);
        radioGroup = findViewById(R.id.TopRadioGroup);

        mFragments = new Fragment[2];
        fragmentManager = getSupportFragmentManager();
        mFragments[0] = fragmentManager.findFragmentById(R.id.TaskListFragment);
        mFragments[1] = fragmentManager.findFragmentById(R.id.ListGroupFragment);
        fragmentTransaction = fragmentManager.beginTransaction()
                .hide(mFragments[0]).hide(mFragments[1]);
        fragmentTransaction.show(mFragments[0]).commit();

        //控制fragment切换
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i("TAG","checkedId = "+checkedId);
                fragmentTransaction = fragmentManager.beginTransaction()
                        .hide(mFragments[0]).hide(mFragments[1]);
                switch (checkedId){
                    case R.id.TaskListButton:
                        fragmentTransaction.show(mFragments[0]).commit();
                        break;
                    case R.id.ListGroupButton:
                        fragmentTransaction.show(mFragments[1]).commit();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
