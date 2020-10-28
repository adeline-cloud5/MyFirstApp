package com.example.notes;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class ListDetail extends AppCompatActivity implements View.OnClickListener{

    private ImageButton back_bt,tag_bt;
    private TextView timeDetail,tagText;
    private EditText titleDetail,editDetail;
    private String title,time,id,tag;
    private DBOpenHelper dbOpenHelperNote,dbOpenHelperTag;
    public static final String TB_NOTE = "notes";
    public static final String TB_TAG = "tags";
    private ArrayList<String> tags = new ArrayList<String>();
    public int checkeditem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);

        dbOpenHelperNote = new DBOpenHelper(this,TB_NOTE,null,1);
        dbOpenHelperTag = new DBOpenHelper(this,TB_TAG,null,1);
        back_bt = findViewById(R.id.back_bt);
        tag_bt = findViewById(R.id.tag_bt);
        titleDetail = findViewById(R.id.titleDetail);
        timeDetail = findViewById(R.id.timeDetail);
        editDetail = findViewById(R.id.editDetail);
        tagText = findViewById(R.id.tagText);

        //获取页面传来的数据
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        time = bundle.getString("time");
        id = bundle.getString("id");
        tag = bundle.getString("tag");
        titleDetail.setText(title);
        timeDetail.setText(time);
        editDetail.setText("detail");
        tagText.setText(getNoteData("tag"));
        editDetail.setText(getNoteData("detail"));

        getTagData();
        for(int i=0;i<tags.size();i++){
            if(tags.get(i).equals(getNoteData("tag"))){
                checkeditem=i;
            }
        }

        back_bt.setOnClickListener(this);
        tag_bt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_bt:
                saveAlertDialog();
                break;
            case R.id.tag_bt:
                tagAlertDialog();
                break;
            default:
                break;
        }
    }

    //选择标签提示框
    private void tagAlertDialog() {
        String[] tagList = new String[20];
        int i = 0;
        for(String tag:tags){
            tagList[i] = tag;
            i++;
        }
        String[] list = Arrays.copyOfRange(tagList,0,i);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ListDetail.this);
        builder.setTitle("选择标签")
                .setSingleChoiceItems(list, checkeditem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkeditem = which;
                        tagText.setText(tags.get(checkeditem));
                        dialog.dismiss();
                    }
                });
                /*.setSingleChoiceItems(tags,checkeditem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkeditem = which;
                        //Toast.makeText(this, "成功加入"+checkeditem+"标签", Toast.LENGTH_SHORT).show();
                        tagText.setText(tags.get(checkeditem));
                        Log.i("TAG",tags.get(checkeditem));
                        dialog.dismiss();
                    }
                });*/
        builder.create().show();
    }

    //保存提示框
    public void saveAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ListDetail.this);
        builder.setTitle("提示")
                .setMessage("请确认是否保存数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //获取修改后的数据
                        String detailText = editDetail.getText().toString();
                        String titleText = titleDetail.getText().toString();
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = sdf.format(calendar.getTime());

                        if(titleText.equals("")){
                            Toast.makeText(getApplicationContext(), "请输入标题！", Toast.LENGTH_SHORT).show();
                        }else if(detailText.equals("")){
                            Toast.makeText(getApplicationContext(), "请输入内容！", Toast.LENGTH_SHORT).show();
                        }else{
                            saveData(titleText,date,tags.get(checkeditem),detailText);
                            Log.i("TAG","OnClick：保存数据");
                            finish();
                        }
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        })
                .setNeutralButton("取消", null);
        builder.create().show();
    }

    /*//返回刷新标志
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/

    //保存数据到数据库
    private void saveData(String title,String date,String tag,String detail) {
        if(isChanged()){
            ContentValues values = new ContentValues();
            values.put("title",title);
            values.put("time",date);
            values.put("tag",tag);
            values.put("detail",detail);
            dbOpenHelperNote.getReadableDatabase().update(TB_NOTE,values,"id=?",new String[]{id});
            Log.i("TAG","-------update--data----------");
        }else{
            return;
        }
    }

    //判断页面数据是否被修改
    public boolean isChanged(){
        String preTitle = getNoteData("title");
        String preTag = getNoteData("tag");
        String preDetail = getNoteData("detail");
        String newTitle = titleDetail.getText().toString();
        String newDetail = editDetail.getText().toString();
        String newTag = tags.get(checkeditem);
        if(preTitle!=newTitle || preTag!=newTag || preDetail!=newDetail){
            Log.i("TAG","-------changed------");
            return true;
        }else{
            Log.i("TAG","-------not changed------");
            return false;
        }
    }

    //获取Note数据库的数据
    private String getNoteData(String what) {
        Cursor cursor = dbOpenHelperNote.getReadableDatabase().query(TB_NOTE,null,"id=?",new String[]{id},null,null,null);
        String id = null,tag = null,detail = null,title = null;
        while (cursor.moveToNext()){
            id = cursor.getString(0);
            title = cursor.getString(1);
            tag = cursor.getString(3);
            detail = cursor.getString(4);
        }
        Log.i("TAG",id+"-----"+tag+"----"+detail);
        if(what.equals("id")){
            return id;
        }else if(what.equals("tag")){
            return tag;
        }else if(what.equals("detail")){
            return detail;
        }else if(what.equals("title")){
            return title;
        }else {
            return null;
        }
    }

    //获取数据库的数据
    private void getTagData() {
        Cursor cursor = dbOpenHelperTag.getReadableDatabase().query(TB_TAG,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String tag = cursor.getString(1);
            Log.i("TAG","-----"+tag+"----");
            tags.add(tag);
        }
    }
}