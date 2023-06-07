package com.example.dreamcatcher_2023_1.ui.statistics;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

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
    private ArrayList<JSONObject> sleepRecords;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StatisticsViewModel statisticsViewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        sleepRecords = getSleepData();

        final Calendar c = Calendar.getInstance();
        String selectedDate = String.format("%d%02d%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
        updateAllData(selectedDate);

        binding.currentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        (view1, datePickerYear, datePickerMonth, datePickerDay) -> {
                            String formattedDate = String.format("%d%02d%02d", datePickerYear, datePickerMonth+1, datePickerDay);
                            binding.currentDate.setText(formattedDate);
                            updateAllData(formattedDate);
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private ArrayList<JSONObject> getSleepData() {
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
        return sleepRecords;
    }

    private void updateAllData(String selectedDate) {
        int year = Integer.parseInt(selectedDate.substring(0, 4));
        int month = Integer.parseInt(selectedDate.substring(4, 6)) - 1; // adjusting month index
        int day = Integer.parseInt(selectedDate.substring(6, 8));

        JSONObject sleepRecord = getSleepRecordForDate(sleepRecords, selectedDate);
        updateSleepRecordFields(sleepRecord, sleepRecords, year, month, day);

        updateWeeklySleepBarChart(sleepRecords, year, month, day);
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

                String sleepTimeString = calculateSleepTime(startHours, startMinutes, endHours, endMinutes);
                binding.sleepTime.setText(sleepTimeString);

                binding.bedTime.setText(String.format("%d:%02d", startHours, startMinutes));

                binding.wakeUpTime.setText(String.format("%d:%02d", endHours, endMinutes));

                //update BarChart
                updateWeeklySleepBarChart(sleepRecords, year, month, day);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            binding.sleepTime.setText("-");

            binding.bedTime.setText("-");

            binding.wakeUpTime.setText("-");
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

    private void updateWeeklySleepBarChart(ArrayList<JSONObject> sleepRecords, int year, int month, int day) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        String[] days = new String[7];

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        cal.add(Calendar.DATE, -6);

        for (int i = 0; i < 7; i++) {
            cal.add(Calendar.DATE, 1); // increment the date
            String date = String.format("%04d%02d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
            JSONObject sleepRecord = getSleepRecordForDate(sleepRecords, date);
            if (sleepRecord != null) {
                try {
                    int startHours = sleepRecord.getInt("startHours");
                    int startMinutes = sleepRecord.getInt("startMinutes");
                    int endHours = sleepRecord.getInt("endHours");
                    int endMinutes = sleepRecord.getInt("endMinutes");

                    int startTotalMinutes = startHours * 60 + startMinutes;
                    int endTotalMinutes = endHours * 60 + endMinutes;

                    int sleepDurationMinutes = endTotalMinutes - startTotalMinutes;
                    if (sleepDurationMinutes < 0) {
                        // This means the sleep crossed midnight
                        sleepDurationMinutes += 24 * 60;
                    }

                    float sleepDurationHours = sleepDurationMinutes / 60f; // Convert to hours with decimal
                    barEntries.add(new BarEntry(i, sleepDurationHours));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                barEntries.add(new BarEntry(i, 0, new ColorDrawable(Color.TRANSPARENT)));
            }
            days[i] = date.substring(4,6) + "/" + date.substring(6,8);
        }


        BarDataSet barDataSet = new BarDataSet(barEntries, "Sleep Duration");
        barDataSet.setColor(Color.BLUE);

        // Customizing the Bar Chart
        barDataSet.setHighlightEnabled(true);
        barDataSet.setBarBorderWidth(0.9f);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(12f);

        BarData barData = new BarData(barDataSet);
        binding.barChart.setData(barData);

        // XAxis
        XAxis xAxis = binding.barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(7f);
        xAxis.setDrawGridLines(false);

        // Legend
        Legend legend = binding.barChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setYOffset(10f);
        legend.setXOffset(10f);
        legend.setYEntrySpace(0f);
        legend.setTextSize(8f);

        binding.barChart.invalidate();  // refresh
        binding.barChart.setTouchEnabled(false);
    }

}
