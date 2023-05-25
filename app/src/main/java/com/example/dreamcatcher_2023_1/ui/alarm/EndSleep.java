package com.example.dreamcatcher_2023_1.ui.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dreamcatcher_2023_1.R;

public class EndSleep extends AppCompatActivity {

    Button btnResult;
    EditText editMemo;
    String memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_end_sleep);

        btnResult=(Button)findViewById(R.id.btnResult);
        editMemo=(EditText)findViewById(R.id.editMemo);

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memo = editMemo.getText().toString();

                Intent intent = new Intent(getApplicationContext(),ResultSleep.class);
                intent.putExtra("memo", memo);
                startActivity(intent);

            }
        });

    }
}