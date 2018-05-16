package com.example.iron.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    // insert data into DB
    public void insertData(String data){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO LOVE VALUES (NULL, ?, NULL, NULL)"; // insert date data into LOVE

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, data);
        statement.executeInsert();
    }

    public void updateData(byte[] image, int order, int id) {
        SQLiteDatabase database = getWritableDatabase();
        String imageOrder = "image" + order;
        String sql = "UPDATE LOVE SET " + imageOrder + " = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindBlob(1, image);
        statement.bindDouble(2, (double)id);

        statement.execute();
        database.close();
    }

//    public  void deleteData(int id) {
//        SQLiteDatabase database = getWritableDatabase();
//
//        String sql = "DELETE FROM FOOD WHERE id = ?";
//        SQLiteStatement statement = database.compileStatement(sql);
//        statement.clearBindings();
//        statement.bindDouble(1, (double)id);
//
//        statement.execute();
//        database.close();
//    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    // Check DB is exist
    public boolean isDBExist(){
        try{
            getData("SELECT * FROM LOVE");
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check image is exist
    public int checkImageExist(){
        try{
            Cursor cursor = getData("SELECT * FROM LOVE");
            while (cursor.moveToNext()) {
                System.out.println("DBdata: " + cursor.getBlob(2));
                // if both are null
                if(cursor.getBlob(2) == null && cursor.getBlob(3) == null) {
                    return 0;
                }
                // if only image 1 is exist
                else if(cursor.getBlob(2) != null && cursor.getBlob(3) == null) {
                    return 1;
                }
                // if only image 2 is exist
                else if(cursor.getBlob(2) == null && cursor.getBlob(3) != null) {
                    return 2;
                }
                // if both are exist
                else if(cursor.getBlob(2) != null && cursor.getBlob(3) != null){
                    return 3;
                } else {
                    return 4;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    // Get Image
    public byte[] getImage(int index){
        String sql = "SELECT * FROM LOVE";

        Cursor cursor = getData(sql);
        byte[] image = null;

        if(index == 1) {
            while (cursor.moveToNext()) {
                image = cursor.getBlob(2);
            }
        } else if(index == 2) {
            while (cursor.moveToNext()) {
                image = cursor.getBlob(3);
            }
        }
        return image;
    }

    // 디비 내용 확인하기
    public void printDBContext(){
        Cursor cursor = getData("SELECT * FROM LOVE");
        System.out.println("cursor: " + cursor);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String date = cursor.getString(1);
            byte[] image1 = cursor.getBlob(2);
            System.out.println("id : " + id);
            System.out.println("date : " + date);
            System.out.println("image1 : " + image1);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}