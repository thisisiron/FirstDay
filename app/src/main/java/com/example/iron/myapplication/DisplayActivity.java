package com.example.iron.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        // Get first day from the intent
        Intent intent = getIntent();
        String dateStr = intent.getStringExtra("firstDay");

        // calculate day between the input day to today
        DdayCal ddayCal = new DdayCal();
        int calculatedDay = ddayCal.countdday(dateStr);
        TextView textView = (TextView)findViewById(R.id.calculatedDay);
        textView.setText(Integer.toString(calculatedDay));

    }
}
