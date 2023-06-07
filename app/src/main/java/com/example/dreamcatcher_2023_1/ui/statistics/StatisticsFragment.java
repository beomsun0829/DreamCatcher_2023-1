package com.example.dreamcatcher_2023_1.ui.statistics;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dreamcatcher_2023_1.R;
import com.example.dreamcatcher_2023_1.databinding.FragmentStatisticsBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding binding;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StatisticsViewModel statisticsViewModel =
                new ViewModelProvider(this).get(StatisticsViewModel.class);

        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        // Parse sleep data from sleep_data.txt file
        String sleepDataString = readSleepDataFromFile();
        ArrayList<JSONObject> sleepRecords = new ArrayList<>();

        if (sleepDataString != null) {
            try {
                JSONArray sleepRecordsArray = new JSONArray(sleepDataString);

                for (int i = 0; i < sleepRecordsArray.length(); i++) {
                    JSONObject sleepRecord = sleepRecordsArray.getJSONObject(i);
                    sleepRecords.add(sleepRecord);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        // Get current date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Current date TextView
        TextView currentDate = root.findViewById(R.id.currentDate);
        currentDate.setText(String.format("%d/%d/%d", year, month+1, day));  // month is 0-indexed

        currentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        (view1, datePickerYear, datePickerMonth, datePickerDay) -> {
                            currentDate.setText(String.format("%d/%d/%d", datePickerYear, datePickerMonth+1, datePickerDay));
                            JSONObject sleepRecord = getSleepRecordForDate(sleepRecords, String.format("%d%02d%02d", datePickerYear, datePickerMonth+1, datePickerDay));
                            updateSleepRecordFields(sleepRecord, sleepRecords, datePickerYear, datePickerMonth, datePickerDay);
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private String readSleepDataFromFile() {
        File file = new File(getContext().getFilesDir(), "sleep_data.txt");

        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    private JSONObject getSleepRecordForDate(ArrayList<JSONObject> sleepRecords, String date) {
        for (JSONObject sleepRecord : sleepRecords) {
            try {
                if (sleepRecord.getString("date").equals(date)) {
                    return sleepRecord;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void updateSleepRecordFields(JSONObject sleepRecord, ArrayList<JSONObject> sleepRecords, int year, int month, int day) {
        if (sleepRecord != null) {
            try {
                int startHours = sleepRecord.getInt("startHours");
                int startMinutes = sleepRecord.getInt("startMinutes");
                int endHours = sleepRecord.getInt("endHours");
                int endMinutes = sleepRecord.getInt("endMinutes");

                TextView sleepTime = root.findViewById(R.id.sleepTime);
                String sleepTimeString = calculateSleepTime(startHours, startMinutes, endHours, endMinutes);
                sleepTime.setText(sleepTimeString);

                TextView bedTime = root.findViewById(R.id.bedTime);
                bedTime.setText(String.format("%d:%02d", startHours, startMinutes));

                TextView wakeUpTime = root.findViewById(R.id.wakeUpTime);
                wakeUpTime.setText(String.format("%d:%02d", endHours, endMinutes));

                //update PieChart
                updateSleepPieChart(startHours, endHours);

                //update BarChart
                updateWeeklySleepBarChart(sleepRecords, year, month, day);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            TextView sleepTime = root.findViewById(R.id.sleepTime);
            sleepTime.setText("-");

            TextView bedTime = root.findViewById(R.id.bedTime);
            bedTime.setText("-");

            TextView wakeUpTime = root.findViewById(R.id.wakeUpTime);
            wakeUpTime.setText("-");
        }
    }


    private String calculateSleepTime(int startHours, int startMinutes, int endHours, int endMinutes) {
        int sleepHours, sleepMinutes;
        if (endHours >= startHours) {
            sleepHours = endHours - startHours;
            sleepMinutes = endMinutes - startMinutes;
        } else {
            // Handle case where sleep crosses midnight
            sleepHours = 24 - startHours + endHours;
            sleepMinutes = endMinutes - startMinutes;
        }
        if (sleepMinutes < 0) {
            sleepHours--;
            sleepMinutes += 60;
        }
        return String.format("%d:%02d", sleepHours, sleepMinutes);
    }

    private void updateSleepPieChart(int startHours, int endHours) {
        PieChart pieChart = root.findViewById(R.id.pieChart);
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        // Assuming startHours and endHours are from 0 (midnight) to 23
        int sleepStart = startHours * 15; // each hour is 15 degrees
        int sleepEnd = endHours * 15;
        int sleepDuration = sleepEnd - sleepStart;
        if (sleepDuration < 0) {
            // This means the sleep crossed midnight
            sleepDuration += 360;
        }

        // Awake time is the remaining time in the 360 degree circle
        int awakeDuration = 360 - sleepDuration;

        // Now add entries
        // Sleep time
        pieEntries.add(new PieEntry(sleepDuration, "Sleep"));
        // Awake time
        pieEntries.add(new PieEntry(awakeDuration, "Awake"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(new int[] { Color.BLUE, Color.GRAY });
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        // Hide labels
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);

        pieChart.invalidate(); // refresh
    }

    private void updateWeeklySleepBarChart(ArrayList<JSONObject> sleepRecords, int year, int month, int day) {
        BarChart barChart = root.findViewById(R.id.barChart);
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        // Get the last 7 days' records
        for (int i = 0; i < 7; i++) {
            String date = String.format("%d%02d%02d", year, month+1, day-i);  // Note: month is 0-indexed
            JSONObject sleepRecord = getSleepRecordForDate(sleepRecords, date);
            if (sleepRecord != null) {
                try {
                    int startHours = sleepRecord.getInt("startHours");
                    int endHours = sleepRecord.getInt("endHours");

                    int sleepDuration = endHours - startHours;
                    if (sleepDuration < 0) {
                        // This means the sleep crossed midnight
                        sleepDuration += 24;
                    }
                    // Add this sleep duration to the bar entries
                    barEntries.add(new BarEntry(i, sleepDuration));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Sleep Duration");
        barDataSet.setColor(Color.BLUE);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barChart.invalidate();  // refresh
    }
}
