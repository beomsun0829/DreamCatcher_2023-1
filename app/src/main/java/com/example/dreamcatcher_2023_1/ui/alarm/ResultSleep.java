package com.example.dreamcatcher_2023_1.ui.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.dreamcatcher_2023_1.R;

public class ResultSleep extends AppCompatActivity {
    TextView viewMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_sleep);

        viewMemo = (TextView)findViewById(R.id.viewMemo);

       String memo = getIntent().getStringExtra("memo");
//        String monthStr = getIntent().getStringExtra("monStr");
//        int date = getIntent().getSExtra("date");
//        String monthStr = getIntent().getStringExtra("monStr");



        viewMemo.setText(memo);
    }
}