package com.football.freekick.utils;

import android.graphics.Color;

import com.football.freekick.http.Url;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ly on 2017/11/30.
 */

public class MyUtil {

    public static String getImageUrl(String url) {
        if (url!=null&&url.contains("http")) {
            return url;
        } else {
            return Url.BaseImageUrl + url;
        }
    }
    public static String getUrl(String url) {
        if (url!=null&&url.contains("https")) {
            return url;
        } else {
            return "https://" + url;
        }
    }
    public static String getColorStr(String str) {
        if (str.contains("#")) {
            return str;
        } else {
            return "#" + str;
        }
    }
    public static int getColorInt(String str) {
        if (str == null){
            return Color.parseColor("#000000");
        }
        if (str.contains("#")) {
            return Color.parseColor(str);
        } else {
            return Color.parseColor("#" + str);
        }
    }
    /**
     * 正则表达式校验邮箱
     * @param emaile 待匹配的邮箱
     * @return 匹配成功返回true 否则返回false;
     */
    public static boolean checkEmail(String email){
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式
        Pattern p = Pattern.compile(RULE_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(email);
        //进行正则匹配
        return m.matches();
    }
}
