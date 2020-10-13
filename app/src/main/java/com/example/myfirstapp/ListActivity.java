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
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_FLING;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private String url = "http://www.usd-cny.com/bankofchina.htm";
    private List<HashMap<String, String>> maplist = new ArrayList<HashMap<String, String>>();
    private List<String> data = new ArrayList<String>();
    private ListView listView;
    private SimpleAdapter adapter;
    private Handler handler;
    private Message msg;
    private Timer timer = new Timer();
    private long DAY = 24*60*60*5000;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.mylist);
        //每日更新一次数据
        timeTask();
        //由于Jsoup不能在主线程上执行，故创建子线程从网络获取数据
        //startThread();
        //通过handle返回主线程更新UI，并创建adapter适配器
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    adapter = new SimpleAdapter(ListActivity.this,
                            maplist,
                            R.layout.list_item,
                            new String[]{"Country", "Rate"},
                            new int[]{R.id.country, R.id.rate});
                    listView.setAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };
        listView.setOnItemClickListener(this);
    }

    //使用计时器每日获取一次数据
    private void timeTask() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startThread();
            }
        };
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0); //凌晨1点
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        //第一次执行定时任务的时间
        Date date=calendar.getTime();
        Log.i("TAG",date.toString());
        timer.schedule(timerTask,date,DAY);
    }

    /*
    连接网络需要在AndroidManifest中配置：
    <uses-permission android:name="android.permission.INTERNET" />
    android:usesCleartextTraffic="true"
    */
    /*
    创建子线程获取和处理网页数据
    */
    public void startThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://www.usd-cny.com/bankofchina.htm";

                try {
                    Document doc = Jsoup.connect(url).get();
                    Log.i("TAG", "==========get===message=============");
                    Element table = doc.getElementsByTag("table").first();
                    Elements trs = table.getElementsByTag("tr");
                    for (Element tr : trs) {
                        Elements tds = tr.getElementsByTag("td");
                        HashMap<String, String> map = new HashMap<String, String>();
                        if (tds.size() > 0) {
                            String td1 = tds.get(0).text();
                            String td2 = tds.get(5).text();
                            data.add(td1 + "==>" + td2);
                            map.put("Country", td1);
                            map.put("Rate", td2);
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

    //列表点击事件监听
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(this, EachList.class);
        Object itemAtPosition = listView.getItemAtPosition(position);
        HashMap<String,String> map = (HashMap<String, String>) itemAtPosition;
        String country = map.get("Country");
        String rate = map.get("Rate");
        Log.i("TAG", "onItemClick: country=" + country);
        Log.i("TAG", "onItemClick: rate=" + rate);

        TextView each_country = view.findViewById(R.id.country);
        TextView each_rate = view.findViewById(R.id.rate);
        String country1 = String.valueOf(each_country.getText());
        String rate1 = String.valueOf(each_rate.getText());

        Bundle bundle = new Bundle();
        bundle.putString("Country",country1);
        bundle.putString("Rate",rate1);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}