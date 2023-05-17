package com.example.dreamcatcher_2023_1.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dreamcatcher_2023_1.R;
import com.example.dreamcatcher_2023_1.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // 알람 레이아웃 인플레이션
        View alarmLayout = inflater.inflate(R.layout.activity_tracking_sleep, container, false);

        // 알람 레이아웃을 원하는 위치에 추가
        ViewGroup rootViewGroup = root.findViewById(R.id.layoutMain); // 알람 레이아웃을 추가할 부모 뷰그룹을 찾습니다.
        rootViewGroup.addView(alarmLayout);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}