package com.example.iron.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class DateSettingActivity extends AppCompatActivity {


    EditText editText;
    Button btnSend;

    public static SQLiteHelper sqLiteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_setting);

        init();

        sqLiteHelper = new SQLiteHelper(this, "LoveDB.sqlite", null, 1 );

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS LOVE (Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, image1 BLOG, image2 BLOG)");



        // send first day and change intent when you click on send btn
        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                    sqLiteHelper.insertData(
                            editText.getText().toString()
                    );
                    Toast.makeText(getApplicationContext(), "success!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DateSettingActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }


//              디비 내용 확인하기
//                Cursor cursor = sqLiteHelper.getData("SELECT * FROM LOVE");
//                while(cursor.moveToNext()){
//                    int id = cursor.getInt(0);
//                    String date = cursor.getString(1);
//                    System.out.println("id "+ id);
//                    System.out.println("date "+ date);
//                }

            }
        });


    }

    private void init() {
        editText = (EditText)findViewById(R.id.editText);
        btnSend = (Button)findViewById(R.id.btnSend);
    }
}
