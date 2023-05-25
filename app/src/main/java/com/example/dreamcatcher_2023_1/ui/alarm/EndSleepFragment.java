package com.example.dreamcatcher_2023_1.ui.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEndSleepBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnResult=binding.btnResult;

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
