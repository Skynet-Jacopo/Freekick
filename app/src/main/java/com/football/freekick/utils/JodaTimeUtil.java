package com.football.freekick.utils;

import android.content.Context;

import com.football.freekick.R;
import com.orhanobut.logger.Logger;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    public static boolean compare(String start, String end) {
        if (start == null || end == null)
            return false;
        DateTime startTime = new DateTime(2017, 1, 1, Integer.parseInt(start.substring(0, 2)), Integer.parseInt(start
                .substring(3)));
        DateTime endTime = new DateTime(2017, 1, 1, Integer.parseInt(end.substring(0, 2)), Integer.parseInt(end
                .substring(3)));
        if (endTime.getSecondOfDay() > startTime.getSecondOfDay()) {
            return true;
        } else {
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
        long time1 = d.getTime();
        DateTime dateTime = new DateTime(time1);
        return dateTime.toString("HH:mm");
    }

    public static String getDate(String time) {
        time = time.substring(0, time.length() - 6);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");//注意格式化的表达式

        Date d = null;
        try {
            d = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time1 = d.getTime();
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
        long time1 = d.getTime();
        DateTime dateTime = new DateTime(time1);
        return dateTime.toString("d MMM yyyy");
    }

    public static String getTime(String time) {
        time = time.substring(0, time.length() - 6);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");//注意格式化的表达式

        Date d = null;
        try {
            d = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time1 = d.getTime();
        DateTime dateTime = new DateTime(time1);
        return dateTime.toString("yyyy-MM-dd HH:mm");
    }

    public static String getDate2(String time) {
        time = time.substring(0, time.length() - 6);
//        time = time.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");//注意格式化的表达式

        Date d = null;
        try {
            d = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time1 = d.getTime();
        DateTime dateTime = new DateTime(time1);
        return dateTime.toString("dd MMM yyyy", Locale.ENGLISH);
    }

    public static String getTime2(String time) {
        time = time.substring(0, time.length() - 6);
//        time = time.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");//注意格式化的表达式

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

    /**
     * @param releaseDate format: 2012-04-24T10:00:10+08:00
     * @return 返回天数，大于14天则显示具体发帖时间
     */
    public static String progressDate(String releaseDate) {
        // releaseDate format: 2012-04-24T10:00:10+08:00
        String dateStr = "";
        if (isBlank(releaseDate) || releaseDate.length() < 19)
            return "";
        if (releaseDate.indexOf("+") == -1) {
            dateStr = releaseDate;
        } else {
            dateStr = releaseDate.substring(0, releaseDate.indexOf("+"));
        }

        Date date = new Date();
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            return "";
        }
        long between = (now.getTime() - date.getTime()) / 1000; // 2个时间相差多少秒

        long day = between / (24 * 3600);

        long hour = between % (24 * 3600) / 3600;

        long minute = between % 3600 / 60;

        long second = between % 60;

        String result = "";


        if (day > 14) {
            result = releaseDate.substring(0, releaseDate.indexOf("T"));
        } else if (day <= 14 && day > 0) {
            result = String.valueOf(day) + "天前";
        } else if (hour > 0) {
            result = String.valueOf(hour) + "小时前";
        } else if (minute > 0) {
            result = String.valueOf(minute) + "分钟前";
        } else {
            result = "1分钟前";
        }

        return result;
    }

    /**
     * @param releaseDate 毫秒數
     * @return 返回天数，大于7天则显示具体发帖时间
     */
    public static String progressDate1(Context context, long releaseDate) {
        releaseDate = releaseDate+8*3600*1000;
        DateTime dateTime = new DateTime(releaseDate);
        Logger.d(dateTime.toString("HH:mm:ss"));
        DateTime now = new DateTime();
        long between = (now.getMillis() - dateTime.getMillis()) / 1000; // 2个时间相差多少秒

        long day = between / (24 * 3600);

        long hour = between % (24 * 3600) / 3600;

        long minute = between % 3600 / 60;

        long second = between % 60;

        String result = "";


        if (day > 7) {
            result = dateTime.toString("yyyy-MM-dd");
        } else if (day <= 7 && day > 0) {
            if (day == 1) {
                result = context.getString(R.string.yesterday);
            } else {
                result = String.valueOf(day) + "天前";
            }
        } else {
            result = dateTime.toString("HH:mm:ss");
        }

        return result;
    }

    public static boolean isBlank(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }
}
