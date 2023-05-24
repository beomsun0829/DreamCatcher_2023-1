package com.example.dreamcatcher_2023_1.ui.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dreamcatcher_2023_1.R;
import com.example.dreamcatcher_2023_1.databinding.FragmentAlarmBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlarmFragment extends Fragment {

    private Button buttonSetAlarm;
    private TextView textViewCurrentTime;
    private FragmentAlarmBinding binding;
    String currentTime;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        AlarmViewModel alarmViewModel =
                new ViewModelProvider(this).get(AlarmViewModel.class);

        binding = FragmentAlarmBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAlarm;
        alarmViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        buttonSetAlarm = binding.buttonSetAlarm;

        buttonSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 시간을 기록
                recordCurrentTime();

                // TrackingSleepActivity로 이동하는 Intent 생성
                Intent intent = new Intent(requireContext(), TrackingSleep.class);
                intent.putExtra("currentTime", currentTime); // currentTime 값을 인텐트에 추가

                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void recordCurrentTime() {
        // 현재 시간을 가져와서 형식화
         currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

    }
}
