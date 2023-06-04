package com.example.dreamcatcher_2023_1.ui.alarm;

import android.media.MediaRecorder;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.dreamcatcher_2023_1.R;
import com.example.dreamcatcher_2023_1.databinding.FragmentTrackingSleepBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TrackingSleepFragment extends Fragment {
    private AlarmViewModel alarmViewModel;

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
    // ViewModel 인스턴스 생성
        alarmViewModel = new ViewModelProvider(requireActivity()).get(AlarmViewModel.class);

    // AlarmFragment에서 가져온 변수 설정
        int startHours = alarmViewModel.getStartHours().getValue();
        int startMinute = alarmViewModel.getStartMinute().getValue();
        String predictionTime = alarmViewModel.getPredictionTime().getValue();
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
                //record End
                endRecording();
                //종료 시간 측정
                sleepTimerEnd();

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                EndSleepFragment endSleep = new EndSleepFragment();
                //요일 전달
                alarmViewModel.setDayOfWeekStr(dayOfWeekStr);
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
                dayOfWeekStr = "Monday";
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

    private void sleepTimerEnd() {
        // 현재 시간을 가져오기
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        // 시간과 분을 가져오기
        endHours = calendar.get(Calendar.HOUR_OF_DAY);
        endMinute = calendar.get(Calendar.MINUTE);

        alarmViewModel.setEndHours(endHours);
        alarmViewModel.setEndMinute(endMinute);
    }

    private void endRecording(){
        if(AlarmFragment.recorder != null){
            AlarmFragment.recorder.stop();
            AlarmFragment.recorder.release();
            AlarmFragment.recorder = null;
        }
    }

}
