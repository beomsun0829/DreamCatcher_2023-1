package com.example.dreamcatcher_2023_1.ui.alarm;

import android.os.Bundle;
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
import com.example.dreamcatcher_2023_1.databinding.FragmentEndSleepBinding;
import com.example.dreamcatcher_2023_1.databinding.FragmentTrackingSleepBinding;

public class EndSleepFragment extends Fragment {

    FragmentEndSleepBinding binding;
    Button btnResult;
    TextView viewSleepTime, viewTotalSleepTime;
    String sleepTime, totalSleepTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEndSleepBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnResult=binding.btnResult;
        viewSleepTime=binding.viewSleepTime;
        viewTotalSleepTime=binding.viewTotalSleepTime;


        Bundle bundle = getArguments();

        //AlarmFragment 에서 가져온 변수
        int startHours = bundle.getInt("startHours");       //측정 시작 hours 값
        int startMinute = bundle.getInt("startMinute");     //측정 시작 minute 값
        int endHours = bundle.getInt("endHours");       //사용자 등록 알람 hours 값
        int endMinutes = bundle.getInt("endMinute");     //사용자 등록 알람 minute 값

        sleepTime = startHours+":"+startMinute+"~"+endHours+":"+endMinutes;
        totalSleepTime=(endHours-startHours)+"시간 " + (endMinutes-startMinute)+" 분";
        viewSleepTime.setText(sleepTime);
        viewTotalSleepTime.setText(totalSleepTime);

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                ResultSleepFragment resultSleep = new ResultSleepFragment();
                transaction.replace(R.id.layoutMain, resultSleep);
                transaction.commit();
            }
        });

        return  root;
}
}
