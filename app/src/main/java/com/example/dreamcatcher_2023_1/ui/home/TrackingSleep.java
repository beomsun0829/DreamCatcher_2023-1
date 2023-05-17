package com.example.dreamcatcher_2023_1.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dreamcatcher_2023_1.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TrackingSleep extends AppCompatActivity {

    TextView viewNowTime, viewAlarm;
    Button btnStop;
    private TextView clockTextView;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_sleep);

        viewNowTime =(TextView) findViewById(R.id.viewNowTime);
        viewAlarm = (TextView) findViewById(R.id.viewAlarm);
        btnStop = (Button) findViewById(R.id.btnStop);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                updateClock();
                handler.postDelayed(this, 1000);
            }
        };

        //측정 중지
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EndSleep.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 0); // 액티비티가 활성화될 때 시계 시작
    }
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); // 액티비티가 비활성화될 때 시계 정지
    }
    private void updateClock() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String currentTime = dateFormat.format(calendar.getTime());
        viewNowTime.setText(currentTime);
    }
}