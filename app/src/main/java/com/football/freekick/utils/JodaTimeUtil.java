package com.football.freekick.utils;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ly on 2017/11/26.
 */

public class JodaTimeUtil {
    //    public static String getTimeHourMinutes(String time) {
//        time = time.replace("Z", " UTC");
//        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss:SSS Z");
//        //时间解析
//        DateTime dateTime2 = DateTime.parse(time, format);
//        String str = dateTime2.toString("HH:mm");
//        return str;
//    }
    public static boolean compare(String start,String end) {
        if (start == null||end == null)
            return false;
        DateTime startTime = new DateTime(2017,1,1,Integer.parseInt(start.substring(0,2)),Integer.parseInt(start.substring(3)));
        DateTime endTime = new DateTime(2017,1,1,Integer.parseInt(end.substring(0,2)),Integer.parseInt(end.substring(3)));
        if (endTime.getSecondOfDay() >startTime.getSecondOfDay()){
            return true;
        }else {
            return false;
        }
    }
    public static String getTimeHourMinutes(String time) {
        if (time == null)
            return "";
        time = time.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式

        Date d = null;
        try {
            d = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long     time1    = d.getTime();
        DateTime dateTime = new DateTime(time1);
        return dateTime.toString("HH:mm");
    }

    public static String getDate(String time) {
        time = time.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式

        Date d = null;
        try {
            d = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long     time1    = d.getTime();
        DateTime dateTime = new DateTime(time1);
        return dateTime.toString("yyyy-MM-dd");
    }
    public static String getDate3(String time) {
        time = time.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式

        Date d = null;
        try {
            d = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long     time1    = d.getTime();
        DateTime dateTime = new DateTime(time1);
        return dateTime.toString("d MMM yyyy");
    }
    public static String getTime(String time) {
        time = time.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式

        Date d = null;
        try {
            d = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long     time1    = d.getTime();
        DateTime dateTime = new DateTime(time1);
        return dateTime.toString("yyyy-MM-dd HH:mm");
    }

    public static String getDate2(String time) {
        time = time.substring(0,time.length()-6);
//        time = time.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");//注意格式化的表达式

        Date d = null;
        try {
            d = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long     time1    = d.getTime();
        DateTime dateTime = new DateTime(time1);
        return dateTime.toString("d MMM yyyy");
    }
    public static String getTime2(String time) {
        time = time.substring(0,time.length()-6);
//        time = time.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");//注意格式化的表达式

        Date d = null;
        try {
            d = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long     time1    = d.getTime();
        DateTime dateTime = new DateTime(time1);
        return dateTime.toString("HH:mm");
    }
}
