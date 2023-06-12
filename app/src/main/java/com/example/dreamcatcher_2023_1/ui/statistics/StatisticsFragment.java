package com.example.dreamcatcher_2023_1.ui.statistics;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dreamcatcher_2023_1.databinding.FragmentStatisticsBinding;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        int month = Integer.parseInt(selectedDate.substring(4, 6)) - 1;
        int day = Integer.parseInt(selectedDate.substring(6, 8));

        JSONObject sleepRecord = getSleepRecordForDate(sleepRecords, selectedDate);
        updateSleepRecordFields(sleepRecord, sleepRecords, year, month, day);

        updateWeeklySleepBarChart(sleepRecords, year, month, day);
        updateDailySleepLineChart(year, month, day);
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
                int sleepRate = sleepRecord.getInt("rating");
                String userNotes = sleepRecord.getString("memo");

                String sleepTimeString = calculateSleepTime(startHours, startMinutes, endHours, endMinutes);
                binding.sleepTime.setText(sleepTimeString);
                binding.bedTime.setText(String.format("%d:%02d", startHours, startMinutes));
                binding.wakeUpTime.setText(String.format("%d:%02d", endHours, endMinutes));
                binding.sleepRate.setText(String.valueOf(sleepRate));
                binding.userNotes.setText(userNotes);

                // Update sleep stability
                String filePath = String.format("/data/data/com.example.dreamcatcher_2023_1/files/recorded_audio_%04d%02d%02d.mp3", year, month + 1, day);
                int sleepStability = calculateSleepStability(filePath);
                binding.sleepStability.setText(String.format("%d%%", sleepStability));

                // Update BarChart
                updateWeeklySleepBarChart(sleepRecords, year, month, day);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            binding.sleepTime.setText("-");
            binding.bedTime.setText("-");
            binding.wakeUpTime.setText("-");
            binding.sleepRate.setText("-");
            binding.userNotes.setText("수면 기록이 없습니다");
            binding.sleepStability.setText("-");
        }
    }

    private int calculateSleepStability(String filePath) {
        List<Float> audioData = extractAudioData(filePath);

        // Calculate the average amplitude
        float sum = 0f;
        for (float amplitude : audioData) {
            sum += Math.abs(amplitude);
        }
        float averageAmplitude = sum / audioData.size();
        int sleepStability = (int) ((1f - averageAmplitude) * 100f);

        return sleepStability;
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
        return String.format("%d시간 %02d분", sleepHours, sleepMinutes);
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
                        sleepDurationMinutes += 24 * 60;
                    }

                    float sleepDurationHours = sleepDurationMinutes / 60f;
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
        barDataSet.setHighLightAlpha(120);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(12f);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        binding.barChart.setData(barData);
        binding.barChart.setTouchEnabled(false);
        binding.barChart.getDescription().setEnabled(false);
        binding.barChart.animateY(1000, Easing.EaseInCubic);

        // XAxis
        XAxis xAxis = binding.barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(7f);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(12f);

        // YAxis
        YAxis leftAxis = binding.barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        xAxis.setTextSize(12f);
        binding.barChart.getAxisRight().setEnabled(false);

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
    }

    private void updateDailySleepLineChart(int year, int month, int day) {
        String filePath = String.format("/data/data/com.example.dreamcatcher_2023_1/files/recorded_audio_%04d%02d%02d.mp3", year, month + 1, day);

        List<Float> audioData = extractAudioData(filePath);

        LineDataSet lineDataSet = new LineDataSet(convertToEntries(audioData,100), "Amplitude");
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTextSize(12f);

        LineData lineData = new LineData(lineDataSet);
        binding.lineChart.setData(lineData);
        binding.lineChart.getDescription().setEnabled(false);
        binding.lineChart.animateY(1000, Easing.EaseInCubic);

        // XAxis
        XAxis xAxis = binding.lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        xAxis.setTextSize(12f);

        // YAxis
        YAxis leftAxis = binding.lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawLabels(false);
        xAxis.setTextSize(12f);
        binding.lineChart.getAxisRight().setEnabled(false);

        // Legend
        Legend legend = binding.lineChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setYOffset(10f);
        legend.setXOffset(10f);
        legend.setYEntrySpace(0f);
        legend.setTextSize(8f);

        // DisableTouch
        binding.lineChart.setTouchEnabled(false);
        binding.lineChart.setDragEnabled(false);
        binding.lineChart.setScaleEnabled(false);

        binding.lineChart.invalidate();  // refresh
    }

    private List<Float> extractAudioData(String filePath) {
        List<Float> audioData = new ArrayList<>();

        int bufferSize = 1024;

        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);

            byte[] buffer = new byte[bufferSize];

            int bytesRead;
            while ((bytesRead = dis.read(buffer, 0, bufferSize)) != -1) {
                ShortBuffer shortBuffer = ByteBuffer.wrap(buffer, 0, bytesRead)
                        .order(ByteOrder.LITTLE_ENDIAN)
                        .asShortBuffer();

                while (shortBuffer.hasRemaining()) {
                    audioData.add((float) shortBuffer.get() / Short.MAX_VALUE);
                }
            }

            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return audioData;
    }




    private List<Entry> convertToEntries(List<Float> audioData, int intervalSize) {
        List<Entry> entries = new ArrayList<>();

        int dataSize = audioData.size();
        int numIntervals = dataSize / intervalSize;

        for (int i = 0; i < numIntervals; i++) {
            int startIndex = i * intervalSize;
            int endIndex = (i + 1) * intervalSize;

            // Calculate the average value for the interval
            float sum = 0f;
            for (int j = startIndex; j < endIndex; j++) {
                sum += audioData.get(j);
            }
            float average = sum / intervalSize;

            entries.add(new Entry(i, average));
        }

        return entries;
    }


}
