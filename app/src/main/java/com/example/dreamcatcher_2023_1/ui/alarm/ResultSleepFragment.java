package com.example.dreamcatcher_2023_1.ui.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dreamcatcher_2023_1.databinding.FragmentEndSleepBinding;
import com.example.dreamcatcher_2023_1.databinding.FragmentResultSleepBinding;

public class ResultSleepFragment extends Fragment {

    FragmentResultSleepBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentResultSleepBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        return  root;
}
}
