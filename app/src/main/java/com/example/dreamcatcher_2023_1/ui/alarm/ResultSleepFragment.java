package com.example.dreamcatcher_2023_1.ui.alarm;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dreamcatcher_2023_1.databinding.FragmentEndSleepBinding;
import com.example.dreamcatcher_2023_1.databinding.FragmentResultSleepBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ResultSleepFragment extends Fragment {
    private AlarmViewModel alarmViewModel;
    private FragmentResultSleepBinding binding;
    private TextView viewMemo, viewWeek, viewDate;
    private ImageView calendar;
    private String monthStr = "", dayOfWeekStr = "";
    private int date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentResultSleepBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        viewMemo = binding.viewMemo;
        viewWeek = binding.viewWeek;
        viewDate = binding.viewDate;
        calendar = binding.calendar;
        updateClock();
        alarmViewModel = new ViewModelProvider(requireActivity()).get(AlarmViewModel.class);

        // EndSleep에서 전달된 값 받기
        String memo = alarmViewModel.getMemo().getValue();
        // 메모 설정
        viewMemo.setText(memo);
        viewWeek.setText(dayOfWeekStr);
        viewDate.setText(monthStr + " " + date + "-" + (date + 1));

        // 캘린더 이미지를 클릭했을 때 DatePickerDialog를 표시
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        return root;
    }

    // 현재 날짜 및 시간 설정
    private void updateClock() {
        Calendar calendar = Calendar.getInstance();

        int month = calendar.get(Calendar.MONTH) + 1; // 0부터 시작하므로 1을 더해줍니다.
        date = calendar.get(Calendar.DATE);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        switch (month) {
            case 1:
                monthStr = "January";
                break;
            case 2:
                monthStr = "February";
                break;
            case 3:
                monthStr = "March";
                break;
            case 4:
                monthStr = "April";
                break;
            case 5:
                monthStr = "May";
                break;
            case 6:
                monthStr = "June";
                break;
            case 7:
                monthStr = "July";
                break;
            case 8:
                monthStr = "August";
                break;
            case 9:
                monthStr = "September";
                break;
            case 10:
                monthStr = "October";
                break;
            case 11:
                monthStr = "November";
                break;
            case 12:
                monthStr = "December";
                break;
        }

        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                dayOfWeekStr = "Sunday";
                break;
            case Calendar.MONDAY:
                dayOfWeekStr = "Monday";
                break;
            case Calendar.TUESDAY:
                dayOfWeekStr = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeekStr = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayOfWeekStr = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayOfWeekStr = "Friday";
                break;
            case Calendar.SATURDAY:
                dayOfWeekStr = "Saturday";
                break;
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = timeFormat.format(calendar.getTime());
    }

    // DatePickerDialog 표시
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 날짜가 선택되었을 때 처리
                // 선택된 날짜를 이용하여 필요한 작업 수행
                // 예: 날짜를 TextView에 설정
                String selectedDate = String.format(Locale.getDefault(), "%d-%d-%d", year, monthOfYear + 1, dayOfMonth);
                viewDate.setText(selectedDate);
            }
        }, year, month, dayOfMonth);

        // DatePickerDialog를 표시
        datePickerDialog.show();
    }
}
