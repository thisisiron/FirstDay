package com.example.iron.myapplication;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static com.example.iron.myapplication.DateSettingActivity.sqLiteHelper;


public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView userImage1;
    private ImageView userImage2;
    public static final boolean USERIMAGE_1 = false; // false if userImage1 is empty
    public static final boolean USERIMAGE_2 = false;
    byte[] existedImage1 = null;
    byte[] existedImage2 = null;

    private BackPressCloseHandler backPressCloseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        showFirstDay();

        // calc and show First day depended on the input time
        backPressCloseHandler = new BackPressCloseHandler(this);

        // load the image if a image already exists on a ImageView
        switch (sqLiteHelper.checkImageExist()) {

            case 0:
//                byte[] existedImage = sqLiteHelper.getImage();
//                Bitmap bitmap = BitmapFactory.decodeByteArray(existedImage, 0, existedImage.length);
//                userImage1.setImageBitmap(bitmap);
                System.out.println("0");
                break;
            case 1:
                System.out.println("1");
                break;
            case 2:
                System.out.println("2");
//                existedImage2 = sqLiteHelper.getImage(2);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(existedImage1, 0, existedImage1.length);
//                userImage1.setImageBitmap(bitmap);
                break;
            case 3:
                System.out.println("3");
                existedImage1 = sqLiteHelper.getImage(1);
                existedImage2 = sqLiteHelper.getImage(2);
                Bitmap bitmap1 = BitmapFactory.decodeByteArray(existedImage1, 0, existedImage1.length);
                userImage1.setImageBitmap(bitmap1);
                Bitmap bitmap2 = BitmapFactory.decodeByteArray(existedImage2, 0, existedImage2.length);
                userImage2.setImageBitmap(bitmap2);
                break;
            default:
                System.out.println("error");
                break;
        }

        userImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImage1.setImageDrawable(null);
                Crop.pickImage(MainActivity.this);
            }
        });

        userImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImage2.setImageDrawable(null);
                Crop.pickImage(MainActivity.this);
            }
        });

    }

    /* Android-Crop library */
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

    // set image on ImageView
    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            userImage1.setImageURI(Crop.getOutput(result));
            storeImage();
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // store image at DB
    private void storeImage(){
        try{
            sqLiteHelper.updateData(imageViewToByte(userImage1), 1);
            sqLiteHelper.printDBContext();
        } catch(Exception e){
            Log.e("Update error", e.getMessage());
        }
    }

    // when you store image to DB
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    // calculate First day and display it
    public void showFirstDay() {
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM LOVE");
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

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    private void init(){
        textView = (TextView)findViewById(R.id.calculatedDay);
        userImage1 = (ImageView)findViewById(R.id.user_image1);
        userImage2 = (ImageView)findViewById(R.id.user_image2);
    }

}

