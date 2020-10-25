package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

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

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private String url = "http://www.usd-cny.com/bankofchina.htm";
    private ArrayList<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
    private List<String> data = new ArrayList<String>();
    private ListView listView;
    private MyAdapter adapter;
    private Handler handler;
    private Message msg;
    private ProgressBar progressBar;
    private Timer timer = new Timer();
    private long DAY = 24*60*60*5000;
    private DBOpenHelper dbOpenHelper;
    public static final String TB_NAME = "tb_rates";

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        dbOpenHelper = new DBOpenHelper(ListActivity.this,TB_NAME,null,1);

        listView = findViewById(R.id.mylist);
        progressBar = findViewById(R.id.progressBar);

        //每日更新一次数据
        timeTask();

        //通过handle返回主线程更新UI，并创建adapter适配器
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
                    //获取数据库中的数据
                    getData();
                    adapter = new MyAdapter(ListActivity.this,R.layout.list_item,maplist);
                    listView.setAdapter(adapter);
                    //设置进度条不显示
                    progressBar.setVisibility(View.GONE);
                    //当列表中没有数据时显示空视图
                    listView.setEmptyView(findViewById(R.id.nodata));
                }
                super.handleMessage(msg);
            }
        };

        //创建点击事件监听器
        listView.setOnItemClickListener(this);
        //创建长按事件监听器
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                builder.setTitle("提示")
                        .setMessage("请确认是否删除数据")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("TAG","OnClick：删除数据");
                                Object itemAtPosition = listView.getItemAtPosition(position);
                                HashMap<String,String> map = (HashMap<String, String>) itemAtPosition;
                                String c = map.get("Country");
                                maplist.remove(position);
                                deleteData(dbOpenHelper.getReadableDatabase(),c);
                                adapter.notifyDataSetChanged();
                            }
                }).setNegativeButton("否",null)
                        .setNeutralButton("查看详情", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("TAG","OnClick：查看详情");
                                Intent intent = new Intent(ListActivity.this, EachList.class);
                                Object itemAtPosition = listView.getItemAtPosition(position);
                                HashMap<String,String> map = (HashMap<String, String>) itemAtPosition;
                                String c = map.get("Country");
                                String r = map.get("Rate");

                                Bundle bundle = new Bundle();
                                bundle.putString("Country",c);
                                bundle.putString("Rate",r);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                builder.create().show();
                return true;
            }
        });

    }

    //使用计时器每日获取一次数据
    private void timeTask() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        //第一次执行定时任务的时间
        Date date=calendar.getTime();
        Log.i("TAG",date.toString());

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startThread();
                //创建message对象并利用handler切换主线程
            }
        };

        timer.schedule(timerTask,date,DAY);
    }

    /*
    连接网络需要在AndroidManifest中配置：
    <uses-permission android:name="android.permission.INTERNET" />
    android:usesCleartextTraffic="true"
    */
    /*
    由于Jsoup不能在主线程上执行，故创建子线程从网络获取数据
    */
    public void startThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                internetData();
            }
        }).start();
    }

    //从网络获取数据
    private void internetData(){
        try {
            Document doc = Jsoup.connect(url).get();
            Log.i("TAG", "==========get===data=============");
            Element table = doc.getElementsByTag("table").first();
            Elements trs = table.getElementsByTag("tr");
            for (Element tr : trs) {
                Elements tds = tr.getElementsByTag("td");
                HashMap<String, String> map = new HashMap<String, String>();
                if (tds.size() > 0) {
                    String td1 = tds.get(0).text();
                    String td2 = tds.get(5).text();
                    /*data.add(td1 + "==>" + td2);
                    map.put("Country", td1);
                    map.put("Rate", td2);
                    maplist.add(map);*/

                    //向数据库中插入或更新数据
                    Cursor cursor = dbOpenHelper.getReadableDatabase().query(TB_NAME,null,"country=?",new String[]{td1},null,null,null);
                    if(cursor.getCount() == 0){
                        insertData(dbOpenHelper.getReadableDatabase(),td1,td2);
                    }else {
                        updateData(dbOpenHelper.getReadableDatabase(),td1,td2);
                    }
                }
            }
            msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断是否需要更新数据
    private boolean sameData(Cursor cursor,String rate){
        String r = cursor.getString(2);
        while (cursor.moveToNext()){
            if(r.equals(rate)){
                return false;
            }
        }
        return true;
    }

    //获取数据库的数据
    private void getData(){
        Cursor cursor = dbOpenHelper.getReadableDatabase().query(TB_NAME,null,null,null,null,null,null);
        Log.i("TAG","---------searching--data---------");

        //把查询结果保存到resultList中
        while (cursor.moveToNext()){
            Map<String,String> map = new HashMap<String, String>();
            map.put("country",cursor.getString(1));
            map.put("rate",cursor.getString(2));
            maplist.add(map);
        }
        Log.i("TAG",maplist.toString());
    }

    //向数据库中插入数据
    private void insertData(SQLiteDatabase sqLiteDatabase, String country, String rate){

        ContentValues values = new ContentValues();
        values.put("country",country);
        values.put("rate",rate);
        sqLiteDatabase.insert(TB_NAME,null,values);
        Log.i("TAG","------insert--data---------");
    }

    //数据库更新数据
    private void updateData(SQLiteDatabase sqLiteDatabase, String country,String rate){

        ContentValues values = new ContentValues();
        values.put("rate",rate);
        sqLiteDatabase.update(TB_NAME,values,"country=?",new String[]{country});
        Log.i("TAG","-------update--data----------");
    }

    //数据库删除数据
    private void deleteData(SQLiteDatabase sqLiteDatabase, String country){

        sqLiteDatabase.delete(TB_NAME,"country=?",new String[]{country});
        Log.i("TAG","------delete--data---------");
    }

    //列表点击事件监听
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        Intent intent = new Intent(this,EachList.class);
        Object itemAtPosition = listView.getItemAtPosition(position);
        HashMap<String,String> map = (HashMap<String, String>) itemAtPosition;
        String country = map.get("Country");
        String rate = map.get("Rate");
        Log.i("TAG", "onItemClick: country=" + country);
        Log.i("TAG", "onItemClick: rate=" + rate);

        Bundle bundle = new Bundle();
        bundle.putString("Country",country);
        bundle.putString("Rate",rate);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    //关闭数据库连接
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbOpenHelper!=null){
            dbOpenHelper.close();
        }
    }
}