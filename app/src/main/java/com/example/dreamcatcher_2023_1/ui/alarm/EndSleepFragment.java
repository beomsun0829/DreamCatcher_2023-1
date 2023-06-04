package com.example.dreamcatcher_2023_1.ui.alarm;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.dreamcatcher_2023_1.R;
import com.example.dreamcatcher_2023_1.databinding.FragmentEndSleepBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EndSleepFragment extends Fragment {
    private AlarmViewModel alarmViewModel;
    FragmentEndSleepBinding binding;
    Button btnResult;
    EditText editMemo;
    TextView viewSleepTime, viewTotalSleepTime;
    String sleepTime, totalSleepTime,Memo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEndSleepBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnResult=binding.btnResult;
        viewSleepTime=binding.viewSleepTime;
        viewTotalSleepTime=binding.viewTotalSleepTime;
        editMemo=binding.editMemo;
        // ViewModel 인스턴스 생성
        alarmViewModel = new ViewModelProvider(requireActivity()).get(AlarmViewModel.class);

        int startHours = alarmViewModel.getStartHours().getValue();
        int startMinutes = alarmViewModel.getStartMinute().getValue();
        int endHours = alarmViewModel.getEndHours().getValue();
        int endMinutes = alarmViewModel.getEndMinute().getValue();

        sleepTime = startHours+":"+startMinutes+"~"+endHours+":"+endMinutes;
        totalSleepTime=(endHours-startHours)+"시간 " + (endMinutes-startMinutes)+" 분";
        viewSleepTime.setText(sleepTime);
        viewTotalSleepTime.setText(totalSleepTime);

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject sleepData = new JSONObject();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                String currentDate = dateFormat.format(new Date());
                try {
                    sleepData.put("date", currentDate);
                    sleepData.put("startHours", startHours);
                    sleepData.put("startMinutes", startMinutes);
                    sleepData.put("endHours", endHours);
                    sleepData.put("endMinutes", endMinutes);
                    sleepData.put("memo", binding.editMemo.getText().toString());
                    sleepData.put("rating", binding.ratingBar2.getRating());

                    JSONArray existingData = readFromFile(getContext());
                    existingData.put(sleepData);
                    writeToFile(existingData.toString(), getContext());

                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    ResultSleepFragment resultSleep = new ResultSleepFragment();
                    transaction.replace(R.id.layoutMain, resultSleep);
                    transaction.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Memo=editMemo.getText().toString();

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                ResultSleepFragment resultSleep = new ResultSleepFragment();

                alarmViewModel.setMemo(Memo);

                transaction.replace(R.id.layoutMain, resultSleep);
                transaction.commit();

            }
        });
        return  root;
    }

    private JSONArray readFromFile(Context context) {
        String result = "";
        try {
            InputStream inputStream = context.openFileInput("sleep_data.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String tempString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((tempString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(tempString);
                }

                inputStream.close();
                result = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            return new JSONArray();
        } catch (IOException e) {
            Log.e("Exception", "File read failed: " + e.toString());
        }

        try {
            return new JSONArray(result);
        } catch (JSONException e) {
            Log.e("Exception", "JSON parse failed: " + e.toString());
            return new JSONArray();
        }
    }

    private void writeToFile(String data, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("sleep_data.txt", Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            fileOutputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
