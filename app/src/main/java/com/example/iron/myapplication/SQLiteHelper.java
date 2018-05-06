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

    public void insertData(String date){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO LOVE VALUES (NULL, ?, NULL, NULL)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, date);
        statement.executeInsert();
    }

    public void updateData(byte[] image, int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE LOVE SET image1 = ? WHERE id = ?";
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
    public boolean isImageExist(){
        try{
            Cursor cursor = getData("SELECT * FROM LOVE");
            while (cursor.moveToNext()) {
                System.out.println("fuck: " + cursor.getBlob(2));
                if(cursor.getBlob(2) == null){
                    return false;
                } else {
                    return true;
                }
            } return false;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // Get Image
    public byte[] getImage(){
        Cursor cursor = getData("SELECT * FROM LOVE");
        byte[] image = null;

        while (cursor.moveToNext()) {
            image = cursor.getBlob(2);
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