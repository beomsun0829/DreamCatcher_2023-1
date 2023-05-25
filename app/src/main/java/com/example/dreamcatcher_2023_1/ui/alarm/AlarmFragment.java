package com.example.dreamcatcher_2023_1.ui.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.dreamcatcher_2023_1.R;
import com.example.dreamcatcher_2023_1.databinding.FragmentAlarmBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmFragment extends Fragment {

    private Button buttonSetAlarm;
    private TextView textViewCurrentTime, viewPreTime;
    private TimePicker timePicker;
    private FragmentAlarmBinding binding;
    String predictionTime;
    private int hours, minutes;
    int startHours, startMinute, alarmHours,alarmMinute;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//Fragment인스턴스 초기화
        binding = FragmentAlarmBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        buttonSetAlarm = binding.buttonSetAlarm;
        timePicker = binding.timepicker;
        viewPreTime = binding.viewPreTime;

//TimePicker 동작 리스너
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hour, int minute) {
                //사용자 알람 시간 세팅
                alarmHours=hour;
                alarmMinute=minute;
                //예상 알람 시간 시간 세팅(수정 중)
                hours = hour;
                minutes = minute;
                int f_hours = hour;
                int s_hours = hour;
                int f_minute= minute-10;
                int s_minute = minute+10;
                String f_hourOfDay ="PM", s_hourOfDay="AM";

                 predictionTime= f_hours+":"+f_minute+" "+f_hourOfDay+" - "+s_hours+":"+s_minute+" "+s_hourOfDay;
                viewPreTime.setText(predictionTime);

            }
        });

        buttonSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //수면 시작 시간 기록
                recordSleepStart();
            //fragment transaction 객체 생성
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                TrackingSleepFragment trackingSleep = new TrackingSleepFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("startHours", startHours);
                bundle.putInt("startMinute", startMinute);
                bundle.putInt("alarmHours", alarmHours);
                bundle.putInt("alarmMinute", alarmMinute);
                trackingSleep.setArguments(bundle);
            //fragment 전환
                transaction.replace(R.id.layoutMain, trackingSleep);
                transaction.commit();


            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void recordSleepStart() {
        // 현재 시간을 가져와서 형식화
//        startTracking = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

            // 현재 시간을 가져오기
            long currentTimeMillis = System.currentTimeMillis();
            Date currentDate = new Date(currentTimeMillis);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);

            // 시간과 분을 가져오기
            startHours = calendar.get(Calendar.HOUR_OF_DAY);
            startMinute = calendar.get(Calendar.MINUTE);

    }
    public void setPredictionTime(int hours, int minutes, String hourOfDay){

    }
}
