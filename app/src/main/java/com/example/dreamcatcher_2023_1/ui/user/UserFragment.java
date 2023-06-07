package com.example.dreamcatcher_2023_1.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dreamcatcher_2023_1.databinding.FragmentUserBinding;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UserFragment extends Fragment {
    private FragmentUserBinding binding;
    private boolean isDarkModeEnabled = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.foregroundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The switch is checked, start your foreground service here
                    Toast.makeText(getActivity(), "Foreground service starting", Toast.LENGTH_SHORT).show();
                } else {
                    // The switch is unchecked, stop your foreground service here
                    Toast.makeText(getActivity(), "Foreground service stopping", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.debugButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                debugSleepDataFile();
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void debugSleepDataFile(){
        File file = new File(getActivity().getFilesDir(), "sleep_data.txt");

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("[\n" +
                    "  {\n" +
                    "    \"date\": \"20230605\",\n" +
                    "    \"startHours\": 23,\n" +
                    "    \"startMinutes\": 38,\n" +
                    "    \"endHours\": 8,\n" +
                    "    \"endMinutes\": 22,\n" +
                    "    \"memo\": \"great\",\n" +
                    "    \"rating\": 3\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"date\": \"20230606\",\n" +
                    "    \"startHours\": 22,\n" +
                    "    \"startMinutes\": 59,\n" +
                    "    \"endHours\": 7,\n" +
                    "    \"endMinutes\": 21,\n" +
                    "    \"memo\": \"nice\",\n" +
                    "    \"rating\": 3\n" +
                    "  }\n" +
                    "]\n");
            fileWriter.close();
            Toast.makeText(getActivity(), "Sleep data file modified", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to modify sleep data file", Toast.LENGTH_SHORT).show();
        }
    }
}
