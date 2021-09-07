package com.example.sreevarsh.koach_ai_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text=findViewById(R.id.text1);

        Bundle extras=getIntent().getExtras();
        text.setText(extras.get("user").toString());


    }
}