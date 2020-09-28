package com.example.scoring;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private int score[] = {3,2};
    private int scoreA=0,scoreB=0,addA,addB;
    private Button bt1,bt2,bt3,bt4,bt5,bt6,bt7;
    private TextView viewA,viewB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取id
        bt1 = findViewById(R.id.button1);
        bt2 = findViewById(R.id.button2);
        bt3 = findViewById(R.id.button3);
        bt4 = findViewById(R.id.button4);
        bt5 = findViewById(R.id.button5);
        bt6 = findViewById(R.id.button6);
        bt7 = findViewById(R.id.button7);
        viewA = findViewById(R.id.scoreA);
        viewB = findViewById(R.id.scoreB);

        //监听事件
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);
        bt6.setOnClickListener(this);
        bt7.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                addScore(0,score[0]);
                break;
            case R.id.button2:
                addScore(0,score[1]);
                break;
            case R.id.button3:
                freeThrow(0);
                break;
            case R.id.button4:
                addScore(1,score[0]);
                break;
            case R.id.button5:
                addScore(1,score[1]);
                break;
            case R.id.button6:
                freeThrow(1);
                break;
            case R.id.button7:
                reset();
                break;
            default:
                break;
        }
    }
    //重置
    private void reset() {
        scoreA = 0;
        scoreB = 0;
        viewA.setText(Integer.toString(scoreA));
        viewB.setText(Integer.toString(scoreB));
    }
    //罚球
    private void freeThrow(int team) {
        if(team == 0){
            if (scoreA != 0 && scoreA-addA >= 0){
                scoreA -= addA;
            }
        } else if(team == 1){
            if (scoreB != 0 && scoreB-addB >= 0){
                scoreB -= addB;
            }
        }

        viewA.setText(Integer.toString(scoreA));
        viewB.setText(Integer.toString(scoreB));
    }
    //计分
    private void addScore(int team, int addScore) {
        if(team == 0){
            addA = addScore;
            scoreA += addA;
        }
        else if(team == 1){
            addB = addScore;
            scoreB += addB;
        }
        viewA.setText(Integer.toString(scoreA));
        viewB.setText(Integer.toString(scoreB));
    }
}
