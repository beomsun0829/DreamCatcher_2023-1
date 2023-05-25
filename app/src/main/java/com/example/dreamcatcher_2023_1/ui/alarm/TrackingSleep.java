package com.example.dreamcatcher_2023_1.ui.alarm;

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

    TextView viewNowTime, viewAlarm, viewSetAlarm;
    Button btnStop;
    private TextView clockTextView;
    private Handler handler;
    private Runnable runnable;

    String monthStr = "";
    String dayOfWeekStr = "";
    int date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tracking_sleep);

        viewNowTime =(TextView) findViewById(R.id.viewCurrentTime);
        viewAlarm = (TextView) findViewById(R.id.viewAlarm);
        btnStop = (Button) findViewById(R.id.btnStop);

        String hours = getIntent().getStringExtra("Hours");
        viewSetAlarm.setText(hours);

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
                Intent resultSleepIntent = new Intent(getApplicationContext(), ResultSleep.class);
                resultSleepIntent.putExtra("date", date);
                startActivity(resultSleepIntent);

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

        int month = calendar.get(Calendar.MONTH) + 1; // 0부터 시작하므로 1을 더해줍니다.
        int date = calendar.get(Calendar.DATE);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);


        switch (month) {
            case 1:
                monthStr = "1월";
                break;
            case 2:
                monthStr = "2월";
                break;
            case 3:
                monthStr = "3월";
                break;
            case 4:
                monthStr = "4월";
                break;
            case 5:
                monthStr = "5월";
                break;
            case 6:
                monthStr = "6월";
                break;
            case 7:
                monthStr = "7월";
                break;
            case 8:
                monthStr = "8월";
                break;
            case 9:
                monthStr = "9월";
                break;
            case 10:
                monthStr = "10월";
                break;
            case 11:
                monthStr = "11월";
                break;
            case 12:
                monthStr = "12월";
                break;
        }

        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                dayOfWeekStr = "일요일";
                break;
            case Calendar.MONDAY:
                dayOfWeekStr = "월요일";
                break;
            case Calendar.TUESDAY:
                dayOfWeekStr = "화요일";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeekStr = "수요일";
                break;
            case Calendar.THURSDAY:
                dayOfWeekStr = "목요일";
                break;
            case Calendar.FRIDAY:
                dayOfWeekStr = "금요일";
                break;
            case Calendar.SATURDAY:
                dayOfWeekStr = "토요일";
                break;

        }


        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String currentTime = dateFormat.format(calendar.getTime());
        viewNowTime.setText(currentTime);
    }
}