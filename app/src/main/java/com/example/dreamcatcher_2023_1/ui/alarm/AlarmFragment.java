package com.example.dreamcatcher_2023_1.ui.alarm;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.dreamcatcher_2023_1.R;
import com.example.dreamcatcher_2023_1.SleepReceiver;
import com.example.dreamcatcher_2023_1.databinding.FragmentAlarmBinding;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.SleepSegmentRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.Date;


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

        alarmHours=startHours;
        alarmMinute=startMinute;
        setPredictionTime(startHours, startMinute);

//TimePicker 동작 리스너
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hour, int minute) {
                //사용자 알람 시간 세팅
                alarmHours=hour;
                alarmMinute=minute;

                //예상 알람 시간 세팅
                setPredictionTime(hour, minute);
            }
        });

        buttonSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start Sleep tracking API
                runSleepEvent();

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
                bundle.putString("predictionTime", predictionTime);
                trackingSleep.setArguments(bundle);
            //fragment 전환
                transaction.replace(R.id.layoutMain, trackingSleep);
                transaction.addToBackStack(null);
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
            // 현재 시간을 가져오기
            long currentTimeMillis = System.currentTimeMillis();
            Date currentDate = new Date(currentTimeMillis);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);

            // 시간과 분을 가져오기
            startHours = calendar.get(Calendar.HOUR_OF_DAY);
            startMinute = calendar.get(Calendar.MINUTE);
    }
//예상 알람 시간 세팅(기준 10분)
    public void setPredictionTime(int hour, int minute){
        int f_hour= hour, s_hour=hour;
        int f_minute = minute-10 , s_minute=minute+10;
        String f_hourOfDay="AM", s_hourOfDay="AM";
        String f_minutes, s_minutes;

        if(f_minute<0){
            f_hour-=1;
            f_minute=f_minute+60;
            if(f_hour<0){
                f_hour+=12;
            }
        }
        else {
            f_hour=hour;
            f_minute=minute-10;
        }
        if(s_minute>60){
            s_hour+=1;
            s_minute-=60;
        }
        else {
            s_hour=hour;
            s_minute=minute+10;
        }
        if(f_hour<12) f_hourOfDay="AM";
        else f_hourOfDay="PM";

        if(s_hour<12) s_hourOfDay="AM";
        else s_hourOfDay="PM";

        if(f_minute<10) f_minutes="0"+f_minute;
        else{
            f_minutes=Integer.toString(f_minute);
        }
        if(s_minute<10) s_minutes="0"+s_minute;
        else s_minutes=Integer.toString(s_minute);
        predictionTime= f_hour+":"+f_minutes+" "+f_hourOfDay+" - "+s_hour+":"+s_minutes+" "+s_hourOfDay;
        viewPreTime.setText(predictionTime);
    }

    public void runSleepEvent() {
        Log.d("SleepReceiver", "runSleepEvent called");

        // Create a PendingIntent that starts a BroadcastReceiver in your app when sleep data is available.
        Intent intent = new Intent(requireActivity(), SleepReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        ActivityRecognitionClient activityRecognitionClient = ActivityRecognition.getClient(requireActivity());
        Task<Void> task = activityRecognitionClient.requestSleepSegmentUpdates(
                pendingIntent,
                SleepSegmentRequest.getDefaultSleepSegmentRequest()
        );

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("SleepReceiver", "Success run sleep event");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SleepReceiver", "Failed run sleep event");
                Log.d("SleepReceiver", String.valueOf(e));
            }
        });
    }

}


