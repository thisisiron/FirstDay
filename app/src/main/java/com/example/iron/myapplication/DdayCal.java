package com.example.iron.myapplication;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DdayCal {

    public int countdday(String dateStr) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateObj = simpleDateFormat.parse(dateStr);
            Calendar todayCal = Calendar.getInstance(); //오늘날자 가져오기
            Calendar ddayCal = Calendar.getInstance(); //오늘날자를 가져와 변경시킴

            ddayCal.setTime(dateObj);// D-day의 날짜를 입력
            Log.e("테스트",simpleDateFormat.format(todayCal.getTime()) + "");
            Log.e("테스트",simpleDateFormat.format(ddayCal.getTime()) + "");

            long today = todayCal.getTimeInMillis()/86400000; //->(24 * 60 * 60 * 1000) 24시간 60분 60초 * (ms초->초 변환 1000)
            long dday = ddayCal.getTimeInMillis()/86400000;
            long count = dday + 1 - today; // 오늘 날짜에서 dday 날짜를 빼주게 됩니다.
            Log.e("Test",Long.toString(today));
            Log.e("Test",Long.toString(dday));
            Log.e("Test",Long.toString(count));

            return (int) count + 1;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }




}