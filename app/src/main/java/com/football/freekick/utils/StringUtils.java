package com.football.freekick.utils;

import android.widget.EditText;
import android.widget.TextView;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class StringUtils {

    @SuppressWarnings("unused")
    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat dateFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat dateFormater3 = new SimpleDateFormat("HH:mm");
    private final static SimpleDateFormat dateFormater4 = new SimpleDateFormat("MM-dd HH:mm");
    private final static SimpleDateFormat year = new SimpleDateFormat("yyyy");
    private final static SimpleDateFormat day = new SimpleDateFormat("dd");
    private final static SimpleDateFormat month = new SimpleDateFormat("MM");

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断给定字符串是否null,如果null,返回友好提示
     *
     * @param input
     * @return
     */
    public static String null2String(String input) {
        if (input == null || "".equals(input)) {
            return "亲，服务器开小差，请刷新或返回试试";
        }
        return input;
    }

    /**
     * 判断给定List是否为空。 若输入字符串为null或空字符串，返回true
     *
     * @param list
     * @return boolean
     */
    public static boolean isEmpty(List list) {
        if (list == null)
            return true;

        if (list.size() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断给定EditText是否为空。 若输入字符串为null或空字符串，返回true
     *
     * @param et
     * @return
     */
    public static boolean isEmpty(EditText et) {
        if (et.getText().toString().trim().equals(""))
            return true;

        return false;
    }
    /**
     * 判断给定TextView是否为空。 若输入字符串为null或空字符串，返回true
     *
     * @param et
     * @return
     */
    public static boolean isEmpty(TextView et) {
        if (et.getText().toString().trim().equals(""))
            return true;

        return false;
    }

    /**
     * 获取EditText的文字
     *
     * @param et
     * @return
     */
    public static String getEditText(EditText et) {
        return et.getText().toString().trim();
    }

    /**
     * 获取TextView的文字
     *
     * @param tv
     * @return
     */
    public static String getEditText(TextView tv) {
        return tv.getText().toString().trim();
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(String obj) {
        try {
            return Integer.parseInt(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 将%E4%BD%A0类型转换为汉字
     *
     * @param s url编码类型
     * @return
     */
    public static String unescape(String s) {
        StringBuffer sbuf = new StringBuffer();
        int l = s.length();
        int ch = -1;
        int b, sumb = 0;
        for (int i = 0, more = -1; i < l; i++) {
            /* Get next byte b from URL segment s */
            switch (ch = s.charAt(i)) {
                case '%':
                    ch = s.charAt(++i);
                    int hb = (Character.isDigit((char) ch) ? ch - '0' : 10 + Character.toLowerCase((char) ch) - 'a') & 0xF;
                    ch = s.charAt(++i);
                    int lb = (Character.isDigit((char) ch) ? ch - '0' : 10 + Character.toLowerCase((char) ch) - 'a') & 0xF;
                    b = (hb << 4) | lb;
                    break;
                case '+':
                    b = ' ';
                    break;
                default:
                    b = ch;
            }
            /* Decode byte b as UTF-8, sumb collects incomplete chars */
            if ((b & 0xc0) == 0x80) { // 10xxxxxx (continuation byte)
                sumb = (sumb << 6) | (b & 0x3f); // Add 6 bits to sumb
                if (--more == 0)
                    sbuf.append((char) sumb); // Add char to sbuf
            } else if ((b & 0x80) == 0x00) { // 0xxxxxxx (yields 7 bits)
                sbuf.append((char) b); // Store in sbuf
            } else if ((b & 0xe0) == 0xc0) { // 110xxxxx (yields 5 bits)
                sumb = b & 0x1f;
                more = 1; // Expect 1 more byte
            } else if ((b & 0xf0) == 0xe0) { // 1110xxxx (yields 4 bits)
                sumb = b & 0x0f;
                more = 2; // Expect 2 more bytes
            } else if ((b & 0xf8) == 0xf0) { // 11110xxx (yields 3 bits)
                sumb = b & 0x07;
                more = 3; // Expect 3 more bytes
            } else if ((b & 0xfc) == 0xf8) { // 111110xx (yields 2 bits)
                sumb = b & 0x03;
                more = 4; // Expect 4 more bytes
            } else /* if ((b & 0xfe) == 0xfc) */ { // 1111110x (yields 1 bit)
                sumb = b & 0x01;
                more = 5; // Expect 5 more bytes
            }
            /* We don't test if the UTF-8 encoding is well-formed */
        }
        return sbuf.toString();
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        if (isEmpty(sdate))
            return null;
        try {
            return dateFormater.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将long类型转换为日期
     *
     * @param sdate
     * @return
     */
    public static String toDate(long sdate) {
        String time = "";
        try {
            time = dateFormater1.format(new Date(sdate));
            return time;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将long类型转换为日期
     *
     * @param sdate
     * @return
     */
    public static String toDateMill(long sdate) {
        String time = "";
        try {
            //时间戳转化为Sting或Date
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            time = format.format(sdate);
            return time;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.format(cal.getTime());
        String paramDate = dateFormater2.format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = dateFormater2.format(time);
        }
        return ftime;
    }

    /**
     * 以类似qq发表说说的友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendly_time_qq(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否同一年
        String curYear = year.format(cal.getTime());
        String paramYear = year.format(time);
        if (!curYear.equals(paramYear)) {
            ftime = dateFormater2.format(time);
            return ftime;
        }

        // 判断是否是同一天
        String curDate = day.format(cal.getTime());
        String paramDate = day.format(time);
        // 判断是否是同一月
        String curMonth = month.format(cal.getTime());
        String paramMonth = month.format(time);

        int lt = Integer.parseInt(paramDate);
        int ct = Integer.parseInt(curDate);
        int days = (int) (ct - lt);
        if (days == 0 && curMonth.equals(paramMonth)) {
            ftime = "今天 " + dateFormater3.format(time);
        } else if (days == 1 && curMonth.equals(paramMonth)) {
            ftime = "昨天 " + dateFormater3.format(time);
        } else if (days == 2 && curMonth.equals(paramMonth)) {
            ftime = "前天" + dateFormater3.format(time);
        } else if (days > 2 && curMonth.equals(paramMonth)) {
            ftime = dateFormater4.format(time);
        } else {// <0
            ftime = dateFormater4.format(time);
        }
        return ftime;
    }

    /**
     * 以类似qq发表说说的友好的方式显示时间,可显示明天后天
     *
     * @param sdate
     * @return
     */
    public static String friendly_time_qq2(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否同一年
        String curYear = year.format(cal.getTime());
        String paramYear = year.format(time);
        if (!curYear.equals(paramYear)) {
            ftime = dateFormater2.format(time);
            return ftime;
        }

        // 判断是否是同一天
        String curDate = day.format(cal.getTime());
        String paramDate = day.format(time);

        int lt = Integer.parseInt(paramDate);
        int ct = Integer.parseInt(curDate);
        int days = (int) (ct - lt);
        if (days == 0) {
            ftime = "今天 " + dateFormater3.format(time);
        } else if (days == 1) {
            ftime = "昨天 " + dateFormater3.format(time);
        } else if (days == 2) {
            ftime = "前天" + dateFormater3.format(time);
        } else if (days > 2) {
            ftime = dateFormater4.format(time);
        } else if (days == -1) {
            ftime = "明天 " + dateFormater3.format(time);
        } else if (days == -2) {
            ftime = "后天" + dateFormater3.format(time);
        } else if (days < -2) {
            ftime = dateFormater4.format(time);
        }
        return ftime;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.format(today);
            String timeDate = dateFormater2.format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 打印长字符串
     *
     * @param veryLongString
     */
    public static void printLongLog(String veryLongString) {
        int maxLogSize = 2000;
        for (int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;

            end = end > veryLongString.length() ? veryLongString.length() : end;

            System.out.println("长Json：\n" + veryLongString.substring(start, end));
        }
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 留小数点后一位
     *
     * @param num
     * @return
     */
    public static String numberFormat(Double num) {
        String result = "0";
        try {
            DecimalFormat df = new DecimalFormat("######0.0");
            result = df.format(num);
        } catch (Exception e) {
            result = "0";
        }
        return result;
    }


    /**
     * 设置double格式
     *
     * @param d
     * @return
     */
    public static String getFormatDouble100(double d) {
        DecimalFormat df = new DecimalFormat("#####0");
        String num = df.format(d * 100);
        return num;
    }

    /**
     * 设置double格式
     *
     * @param d
     * @return
     */
    public static String getFormatDouble(double d) {
        DecimalFormat df = new DecimalFormat("#####0");
        String num = df.format(d);
        return num;
    }

    /**
     * 分转元 int
     *
     * @param price
     * @return
     */
    public static String getPrice(int price) {
        String str = String.format(Locale.getDefault(), "%.2f", price / 100.00);
        return str;
    }

    /**
     * 分转元 String
     *
     * @param price
     * @return
     */
    public static String getPrice(String price) {
        String str = String.format(Locale.getDefault(), "%.2f", Integer.parseInt(price) / 100.00);
        return str;
    }

    /**
     * 分转元 to double
     *
     * @param price
     * @return
     */
    public static double getPriceDouble(int price) {
        double str = price / 100.00;
        return str;
    }

    //M5D加密
    public  static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
