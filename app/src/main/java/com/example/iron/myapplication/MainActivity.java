package com.example.iron.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int result = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // send first day and change intent when you click on send btn
    public void sendMessage(View view){

        EditText editText = (EditText)findViewById(R.id.editText);
        String dateStr = editText.getText().toString();
        Intent intent = new Intent(this,DisplayActivity.class);
        intent.putExtra("firstDay", dateStr);
        startActivity(intent);

    }

}
