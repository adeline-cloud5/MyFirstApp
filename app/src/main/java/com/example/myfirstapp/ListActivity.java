package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private String url = "http://www.usd-cny.com/bankofchina.htm";
    private List<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
    private List<String> data = new ArrayList<String>();
    private ListView listView;
    private SimpleAdapter adapter;
    private Handler handler;
    private Message msg;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.mylist);

        //由于Jsoup不能在主线程上执行，故创建子线程从网络获取数据
        startThread();

        //从子线程获取数据后通过handle返回主线程更新UI，并创建adapter适配器
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    adapter = new SimpleAdapter(ListActivity.this,
                            maplist,
                            R.layout.list_item,
                            new String[]{"Country","Rate"},
                            new int[]{R.id.country,R.id.rate});
                    listView.setAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };
    }
    /*
    连接网络需要在AndroidManifest中配置：
    <uses-permission android:name="android.permission.INTERNET" />
    android:usesCleartextTraffic="true"
    */
    /*
    创建子线程获取和处理网页数据
    */
    public void startThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://www.usd-cny.com/bankofchina.htm";

                try {
                    Document doc = Jsoup.connect(url).get();
                    Log.i("TAG","==========get===message=============");
                    Element table = doc.getElementsByTag("table").first();
                    Elements trs = table.getElementsByTag("tr");
                    for(Element tr: trs){
                        Elements tds = tr.getElementsByTag("td");
                        HashMap<String,String> map = new HashMap<String, String>();
                        if(tds.size()>0){
                            String td1 = tds.get(0).text();
                            String td2 = tds.get(5).text();
                            data.add(td1+"==>"+td2);
                            map.put("Country",td1);
                            map.put("Rate",td2);
                            Log.i("TAG",map.toString());
                            maplist.add(map);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //创建message对象并利用bundle切换主线程
                msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}