package com.football.freekick.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.football.freekick.App;


/**
 * SharedPreferences配置文件读写封装
 *
 * @author wangjunke 2016.10.08
 */
public class SPUtil {
    public static final String NAME = "shared_data";
    private static SharedPreferences        sp;
    private static SharedPreferences.Editor editor;


    public SPUtil() {
        sp = App.APP_CONTEXT.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static void setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key, String value) {
        return sp.getString(key, value);
    }

    public static void removeString(String key) {
        editor.remove(key);
        editor.commit();
    }

    public static void setInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String key, int value) {
        return sp.getInt(key, value);
    }

    public static void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key, Boolean value) {
        return sp.getBoolean(key, value);
    }

    public static void setLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(String key) {
        return sp.getLong(key, 0L);
    }

    public static void setFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public static float getFloat(String key) {
        return sp.getFloat(key, 0f);
    }

    public static void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    public static boolean contains(String key) {
        return sp.contains(key);
    }
}
