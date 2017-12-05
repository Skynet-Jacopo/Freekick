package com.football.freekick.utils;

import android.graphics.Color;

import com.football.freekick.http.Url;

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
}
