package com.example.dreamcatcher_2023_1.ui.alarm;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.dreamcatcher_2023_1.R;
import com.example.dreamcatcher_2023_1.databinding.FragmentAlarmBinding;
import com.example.dreamcatcher_2023_1.databinding.FragmentTrackingSleepBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TrackingSleepFragment extends Fragment {
    TextView viewCurrentTime,viewAlarm;
    Button btnStop;
    private Handler handler;
    private Runnable runnable;

    String monthStr = "";
    String dayOfWeekStr = "";
    int date, endHours,endMinute;
    FragmentTrackingSleepBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTrackingSleepBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

    //객체 초기화
        viewCurrentTime=binding.viewCurrentTime;
        btnStop=binding.btnStop;
        viewAlarm=binding.viewAlarm;
        handler = new Handler();
        Bundle bundle = getArguments();

    //AlarmFragment 에서 가져온 변수
        int startHours = bundle.getInt("startHours");       //측정 시작 hours 값
        int startMinute = bundle.getInt("startMinute");     //측정 시작 minute 값
        int alarmHours = bundle.getInt("alarmHours");       //사용자 등록 알람 hours 값
        int alarmMinute = bundle.getInt("alarmMinute");     //사용자 등록 알람 minute 값
        String predictionTime = bundle.getString("predictionTime");     //예상 알람 시간
    //예상 알람 시간 띄우기
        viewAlarm.setText(predictionTime);

        runnable = new Runnable() {
            @Override
            public void run() {
                updateClock();
                handler.postDelayed(this, 1000);
            }
        };
//STOP 버튼 클릭 리스너
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //종료시간 측정
                recordSleepEnd();

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                EndSleepFragment endSleep = new EndSleepFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("startHours", startHours);
                bundle.putInt("startMinute", startMinute);
                bundle.putInt("endHours", endHours);
                bundle.putInt("endMinutes", endMinute);
                endSleep.setArguments(bundle);
                //fragment 전환

                transaction.replace(R.id.layoutMain, endSleep);
                transaction.commit();


            }
        });
        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 0); // 액티비티가 활성화될 때 시계 시작
    }
    @Override
    public void onPause() {
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
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = timeFormat.format(calendar.getTime());
        viewCurrentTime.setText(currentTime);
    }
    private void recordSleepEnd() {
        // 현재 시간을 가져오기
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // 시간과 분을 가져오기
        endHours = calendar.get(Calendar.HOUR_OF_DAY);
        endMinute = calendar.get(Calendar.MINUTE);
    }

}
