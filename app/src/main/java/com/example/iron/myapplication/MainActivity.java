package com.example.iron.myapplication;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.graphics.drawable.BitmapDrawable;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class MainActivity extends AppCompatActivity {

    TextView textView;
    ImageView userImage1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        showFirstDay(); // calc and show First day depended on the input time

        userImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImage1.setImageDrawable(null);
                Crop.pickImage(MainActivity.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            userImage1.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

//    // when you store image to DB
//    public static byte[] imageViewToByte(ImageView image) {
//        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        return byteArray;
//    }

    public void showFirstDay(){
        Cursor cursor = DateSettingActivity.sqLiteHelper.getData("SELECT * FROM LOVE");
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String dateStr = cursor.getString(1);
            System.out.println("date "+ dateStr);

            // calculate day between the input day to today
            DdayCal ddayCal = new DdayCal();
            int calculatedDay = ddayCal.countdday(dateStr);
            textView.setText(Integer.toString(calculatedDay));
        }
    }


    private void init(){
        textView = (TextView)findViewById(R.id.calculatedDay);
        userImage1 = (ImageView)findViewById(R.id.user_image1);
    }

}

