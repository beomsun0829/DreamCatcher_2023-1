package com.example.dreamcatcher_2023_1.ui.alarm;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.dreamcatcher_2023_1.R;
import com.example.dreamcatcher_2023_1.databinding.FragmentTrackingSleepBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;



public class TrackingSleepFragment extends Fragment {
    private AlarmViewModel alarmViewModel;

    TextView viewCurrentTime,viewAlarm;
    Button btnStop;
    ImageView imgAlarm;
    private Handler handler;
    private Runnable runnable;
    String monthStr = "";
    String dayOfWeekStr = "";
    int date, endHours,endMinute, totalTime;
    FragmentTrackingSleepBinding binding;
    private MediaPlayer mediaPlayer;
    int startHours,startMinute;
    boolean checkAlarm=false;
    Calendar calendar = Calendar.getInstance();
    int currentHours, currentMinute, alarmHours, alarmMinute;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTrackingSleepBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
    //객체 초기화
        viewCurrentTime=binding.viewCurrentTime;
        btnStop=binding.btnStop;
        viewAlarm=binding.viewAlarm;
        imgAlarm=binding.imgAlarm;
        handler = new Handler();

        // ViewModel 인스턴스 생성
        alarmViewModel = new ViewModelProvider(requireActivity()).get(AlarmViewModel.class);
        startHours= alarmViewModel.getStartHours().getValue();
        startMinute=alarmViewModel.getStartMinute().getValue();
        alarmHours=alarmViewModel.getAlarmHours().getValue();
        alarmMinute=alarmViewModel.getAlarmMinute().getValue();
    // AlarmFragment에서 가져온 변수 설정
        int startHours = alarmViewModel.getStartHours().getValue();
        int startMinute = alarmViewModel.getStartMinute().getValue();
        String predictionTime = alarmViewModel.getPredictionTime().getValue();





        //View 초기 설정
        //알람 이미지 불투명도 설정
        imgAlarm.setAlpha(0.2f);
    //예상 알람 시간 띄우기
        viewAlarm.setText(predictionTime);
    //현재 시간 측정
        runnable = new Runnable() {
            @Override
            public void run() {
                updateClock();
                startAlarm();
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
                //알람 종료
                stopAlarm();

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
        //현재 시간, 분 String 형식으로 받기
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = timeFormat.format(calendar.getTime());
        viewCurrentTime.setText(currentTime);
        //현재 시간, 분 int 형식으로 받기
        currentHours = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);
    }
//측정 종료
    private void sleepTimerEnd() {
        // 현재 시간을 가져오기
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        // 측정 종료 시간 설정
        endHours = calendar.get(Calendar.HOUR_OF_DAY);
        endMinute = calendar.get(Calendar.MINUTE);
        alarmViewModel.setEndHours(endHours);
        alarmViewModel.setEndMinute(endMinute);
    }
//녹음
    private void endRecording(){
        if(AlarmFragment.recorder != null){
            AlarmFragment.recorder.stop();
            AlarmFragment.recorder.release();
            AlarmFragment.recorder = null;
        }
    }
//알람
//알람 시작
private void startAlarm() {
    // 알람 시간에 도달했을 때 소리 재생
    if (alarmHours == currentHours && alarmMinute == currentMinute && checkAlarm == false) {
        checkAlarm = true;
        imgAlarm.setAlpha(1f);
    // imgAlarm 회전 애니메이션 설정
        Animation animation = new RotateAnimation(0f, 15f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(30);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        imgAlarm.startAnimation(animation);
    //버튼색상 변경
        Drawable drawable = getResources().getDrawable(R.drawable.btn_set_alarm);
        btnStop.setBackground(drawable);
        btnStop.setTextColor(Color.WHITE);

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.alarm_sound);
        mediaPlayer.setLooping(true);  // Set the MediaPlayer to loop the sound
        mediaPlayer.start();

        Toast.makeText(requireContext(), "기상시간 입니다!", Toast.LENGTH_LONG).show();
    }
}
//알람 종료
    private void stopAlarm() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            checkAlarm = false;
            imgAlarm.setAlpha(0.2f);
            Drawable drawable = getResources().getDrawable(R.drawable.btn_stop_alarm);
            imgAlarm.clearAnimation();
            btnStop.setBackground(drawable);
            btnStop.setTextColor(Color.BLACK);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
