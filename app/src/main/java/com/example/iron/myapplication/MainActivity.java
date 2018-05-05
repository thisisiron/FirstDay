package com.example.iron.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {


    private static final int CROP_FROM_iMAGE = 0;
    private static final int SAVE_IMAGE_TO_SQLITE = 1;


    private ImageView iv_UserPhoto;
    private String absoultePath;


    private Uri uri;

    TextView textView;
    ImageView userImage1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get first day from the intent
        Intent intent = getIntent();

        init();

        showFirstDay(); // calc and show First day depended on the input time

        userImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CROP_FROM_iMAGE
                );
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // pick image from the gallery
        if(requestCode == CROP_FROM_iMAGE){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, CROP_FROM_iMAGE);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("requestCode : "+ requestCode);
        System.out.println("resultCoide : " + resultCode);
        if(resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode){
            case CROP_FROM_iMAGE: {
                uri = data.getData();
                Log.e("Test",uri.getPath().toString());

                // Crop image
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(uri, "image/*");
                intent.putExtra("outputX", 200); // CROP한 이미지의 x축 크기
                intent.putExtra("outputY", 200); // CROP한 이미지의 y축 크기
                intent.putExtra("aspectX", 1); // CROP 박스의 X축 비율
                intent.putExtra("aspectY", 1); // CROP 박스의 Y축 비율
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, SAVE_IMAGE_TO_SQLITE); // switch to SAVE_IMAGE_TO_SQLITE

                break;
            }

            case SAVE_IMAGE_TO_SQLITE: {

                final Bundle extras = data.getExtras();
                Bitmap image = extras.getParcelable("data"); // CROP된 BITMAP

                System.out.println("image: " + image);
                userImage1.setImageBitmap(image);
                break;
            }
        }





//            try{
//                DateSettingActivity.sqLiteHelper.updateData(
//                        imageViewToByte(userImage1),
//                        0
//                );
//            } catch(Exception e){
//                e.printStackTrace();
//            }
//
//            try {
//                InputStream inputStream = getContentResolver().openInputStream(uri);
//
//                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                userImage1.setImageBitmap(bitmap);
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

    }


//    @Override
//    public void onClick(View view) {
//
//        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                doTakeAlbumAction();
//            }
//        };
//
//        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        };
//
//        new AlertDialog.Builder(this)
//                .setTitle("Select Photo")
//                .setNegativeButton("album", albumListener)
//                .setPositiveButton("cancel", cancelListener)
//                .show();
//    }
//
//
//    public void doTakeAlbumAction() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
//        startActivityForResult(intent, PICK_FROM_ALBUM);
//    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//
//        switch (requestCode) {
//            case PICK_FROM_ALBUM: {
//                mImageCaptureUri = data.getData();
//                Log.d("SmartWheel", mImageCaptureUri.getPath().toString());
//                Intent intent = new Intent("com.android.camera.action.CROP");
//                intent.setDataAndType(mImageCaptureUri, "image/*");
//                intent.putExtra("outputX", 200); // CROP한 이미지의 x축 크기
//                intent.putExtra("outputY", 200); // CROP한 이미지의 y축 크기
//                intent.putExtra("aspectX", 1); // CROP 박스의 X축 비율
//                intent.putExtra("aspectY", 1); // CROP 박스의 Y축 비율
//                intent.putExtra("scale", true);
//                intent.putExtra("return-data", true);
//                startActivityForResult(intent, CROP_FROM_iMAGE); // CROP_FROM_CAMERA case문 이동
//
//                break;
//            }
//
//            case CROP_FROM_iMAGE: {
//                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
//                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
//                // 임시 파일을 삭제합니다.
//                if (resultCode != RESULT_OK) {
//                    return;
//                }
//
//                final Bundle extras = data.getExtras();
//                // CROP된 이미지를 저장하기 위한 FILE 경로
//                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
//                        "/SmartWheel/" + System.currentTimeMillis() + ".jpg";
//                if (extras != null) {
//                    Bitmap photo = extras.getParcelable("data"); // CROP된 BITMAP
//                    iv_UserPhoto.setImageBitmap(photo); // 레이아웃의 이미지칸에 CROP된 BITMAP을 보여줌
//                    storeCropImage(photo, filePath); // CROP된 이미지를 외부저장소, 앨범에 저장한다.
//                    absoultePath = filePath;
//                    break;
//                }
//
//                // 임시 파일 삭제
//                File f = new File(mImageCaptureUri.getPath());
//                if (f.exists()) {
//                    f.delete();
//                }
//            }
//
//        }
//    }


    /*
     * Bitmap을 저장하는 부분
     */
    private void storeCropImage(Bitmap bitmap, String filePath) {
        // SmartWheel 폴더를 생성하여 이미지를 저장하는 방식이다.
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel";
        File directory_SmartWheel = new File(dirPath);
        if (!directory_SmartWheel.exists()) // SmartWheel 디렉터리에 폴더가 없다면 (새로 이미지를 저장할 경우에 속한다.)
            directory_SmartWheel.mkdir();
        File copyFile = new File(filePath);
        BufferedOutputStream out = null;
        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            // sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신한다.
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(copyFile)));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


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

