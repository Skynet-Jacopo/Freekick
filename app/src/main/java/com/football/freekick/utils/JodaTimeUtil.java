package com.football.freekick.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
    public static String getTimeHourMinutes(String time) {
        time = time.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式

        Date d = null;
        try {
            d = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time1 = d.getTime();
        DateTime dateTime = new DateTime(time1);
        return dateTime.toString("HH:mm");
    }
}