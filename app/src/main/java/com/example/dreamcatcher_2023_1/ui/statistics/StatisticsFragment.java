package com.example.dreamcatcher_2023_1.ui.statistics;

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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;


import java.util.ArrayList;

public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StatisticsViewModel statisticsViewModel =
                new ViewModelProvider(this).get(StatisticsViewModel.class);

        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Example data
        TextView sleepTime = root.findViewById(R.id.sleepTime);
        sleepTime.setText("8 hours");

        TextView sleepRegularity = root.findViewById(R.id.sleepRegularity);
        sleepRegularity.setText("90%");

        TextView timeToFallAsleep = root.findViewById(R.id.timeToSleep);
        timeToFallAsleep.setText("15 min");

        TextView bedTime = root.findViewById(R.id.bedTime);
        bedTime.setText("10:00 PM");

        TextView wakeUpTime = root.findViewById(R.id.wakeUpTime);
        wakeUpTime.setText("6:00 AM");

        // PieChart
        PieChart pieChart = root.findViewById(R.id.pieChart);
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(8f, ""));
        pieEntries.add(new PieEntry(16f, ""));
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(new int[] { Color.BLUE, Color.GRAY });
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        // Hide labels
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);

        pieChart.invalidate(); // refresh



        // LineChart
        LineChart lineChart = root.findViewById(R.id.lineChart);
        ArrayList<Entry> lineEntries = new ArrayList<>();
        lineEntries.add(new Entry(0f, 8f));
        lineEntries.add(new Entry(1f, 7f));
        lineEntries.add(new Entry(2f, 8f));
        lineEntries.add(new Entry(3f, 6f));
        lineEntries.add(new Entry(4f, 7f));
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "");
        lineDataSet.setColor(Color.BLUE);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

    // Hide labels, gridlines, and axis lines
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setDrawAxisLine(false);
        lineChart.getXAxis().setDrawLabels(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawAxisLine(false);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawAxisLine(false);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);

        lineChart.invalidate(); // refresh



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
