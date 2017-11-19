package com.football.freekick.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ly on 2017/11/19.
 */

public class TimeUtil {
    public void compareToNowDate(Date date){
        Date nowdate=new Date();
        //format date pattern
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //convert to millions seconds
        long time=DateToLong(StringToDate(formatter.format(nowdate)));
        long serverTime=DateToLong(date);
        //convert to seconds
        long minTime=Math.abs(time-serverTime)/1000;
    }

    private long DateToLong(Date time){
        SimpleDateFormat st=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//yyyyMMddHHmmss
        return Long.parseLong(st.format(time));
    }

    private Date StringToDate(String s){
        Date time=null;
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            time=sd.parse(s);
        } catch (java.text.ParseException e) {
            System.out.println("输入的日期格式有误！");
            e.printStackTrace();
        }
        return time;
    }
}
