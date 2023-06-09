package com.example.dreamcatcher_2023_1.ui.alarm;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.dreamcatcher_2023_1.R;
import com.example.dreamcatcher_2023_1.databinding.FragmentAlarmBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmFragment extends Fragment {
    private AlarmViewModel alarmViewModel;
    private Button buttonSetAlarm;
    private TextView textViewCurrentTime, viewPreTime;
    private TimePicker timePicker;
    private FragmentAlarmBinding binding;
    String predictionTime, hourOfDay;
    private int hours, minutes;
    int startHours, startMinute, alarmHours, alarmMinute, checkAlarm;
    public static MediaRecorder recorder = null;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);
        //ViewModel 인스턴스 생성
        alarmViewModel = new ViewModelProvider(requireActivity()).get(AlarmViewModel.class);
        //Fragment인스턴스 초기화
        binding = FragmentAlarmBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        buttonSetAlarm = binding.buttonSetAlarm;
        timePicker = binding.timepicker;
        viewPreTime = binding.viewPreTime;
        alarmHours = currentHour;
        alarmMinute = currentMinute;

        setPredictionTime(alarmHours, alarmMinute);



        //TimePicker 동작 리스너
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hour, int minute) {
                //사용자 알람 시간 세팅
                alarmHours = hour;
                alarmMinute = minute;

                if (hour >= 12) {
                    hourOfDay = "PM";
                    alarmHours = hour - 12;
                    if (alarmHours == 0) alarmHours = 12;
                } else hourOfDay = "AM";

                //예상 알람 시간 세팅
                setPredictionTime(hour, minute);
            }
        });
        buttonSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 시간 가져오기

                // record
                startRecording();
                // 수면 시작 시간 기록
                sleepTimerStart();
                // 알람 설정
                setAlarm();
                // fragment transaction 객체 생성
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                TrackingSleepFragment trackingSleep = new TrackingSleepFragment();
                // startHours, startMinute, alarmHours, alarmMinute 값 설정
                alarmViewModel.setStartHours(startHours);
                alarmViewModel.setStartMinute(startMinute);
                alarmViewModel.setAlarmHours(alarmHours);
                alarmViewModel.setAlarmMinute(alarmMinute);
                alarmViewModel.setPredictionTime(predictionTime);
                // fragment 전환
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

    private void sleepTimerStart() {
        // 현재 시간을 가져오기
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // 시간과 분을 가져오기
        startHours = calendar.get(Calendar.HOUR_OF_DAY);
        startMinute = calendar.get(Calendar.MINUTE);
    }

    private void startRecording() {
        String dateStr = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        File recordFile = new File(getActivity().getFilesDir(), "/recorded_audio_" + dateStr + ".3gp");

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordFile.getAbsolutePath());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();
    }

    // 예상 알람 시간 세팅(기준 10분)
    private void setPredictionTime(int hour, int minute) {
        int f_hour = hour;
        int s_hour = hour;
        int f_minute = minute - 10;
        int s_minute = minute + 10;
        String f_hourOfDay = "AM";
        String s_hourOfDay = "AM";
        String f_minutes, s_minutes;

        if (f_minute < 0) {
            f_hour -= 1;
            f_minute = f_minute + 60;
            if (f_hour < 0) {
                f_hour += 12;
            }
        } else {
            f_hour = hour;
            f_minute = minute - 10;
        }

        if (s_minute >= 60) {
            s_hour += 1;
            s_minute -= 60;
        } else {
            s_hour = hour;
            s_minute = minute + 10;
        }

        if (f_hour >= 12) {
            f_hourOfDay = "PM";
            if (f_hour > 12) {
                f_hour -= 12;
            }
        } else if (f_hour == 0) {
            f_hour = 12;
        }

        if (s_hour >= 12) {
            s_hourOfDay = "PM";
            if (s_hour > 12) {
                s_hour -= 12;
            }
        } else if (s_hour == 0) {
            s_hour = 12;
        }

        if (f_minute < 10) {
            f_minutes = "0" + f_minute;
        } else {
            f_minutes = Integer.toString(f_minute);
        }

        if (s_minute < 10) {
            s_minutes = "0" + s_minute;
        } else {
            s_minutes = Integer.toString(s_minute);
        }

        predictionTime = f_hour + ":" + f_minutes + " " + f_hourOfDay + " - " + s_hour + ":" + s_minutes + " " + s_hourOfDay;
        viewPreTime.setText(predictionTime);
    }

    // 알람 설정
    private void setAlarm() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request for permission
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, 100);
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarmHours);
        calendar.set(Calendar.MINUTE, alarmMinute);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireActivity(), TrackingSleepFragment.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireActivity(), 0, intent, PendingIntent.FLAG_MUTABLE);

        // 알람 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        Toast.makeText(requireContext(), "알람이 설정되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
