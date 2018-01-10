package com.football.freekick.utils;

/**
 * Created by 小醋 on 2016/9/5.
 */

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 专门访问和设置SharePreference的工具类, 保存和配置一些设置信息
 *
 * @author Kevin
 *
 */
public class PrefUtils {

    private static final String SHARE_PREFS_NAME = "freekick";

    public static void putBoolean(Context ctx, String key, boolean value) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);

        pref.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context ctx, String key,
                                     boolean defaultValue) {
        System.out.println(ctx);
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);
        return pref.getBoolean(key, defaultValue);
    }

    public static void putString(Context ctx, String key, String value) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);

        pref.edit().putString(key, value).commit();
    }

    public static String getString(Context ctx, String key, String defaultValue) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);

        return pref.getString(key, defaultValue);
    }


    /*地图需要*/
    public static void putFloat(Context ctx, String key, float value) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);

        pref.edit().putFloat(key, value).commit();
    }

    public static float getFloat(Context ctx, String key, float defaultValue) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);

        return pref.getFloat(key, defaultValue);
    }


    public static void putInt(Context ctx, String key, int value) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);

        pref.edit().putInt(key, value).commit();
    }

    public static void putLong(Context ctx, String key, long value) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);

        pref.edit().putLong(key, value).commit();
    }
    public static int getInt(Context ctx, String key, int defaultValue) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);

        return pref.getInt(key, defaultValue);
    }
    public static long getLong(Context ctx, String key, long defaultValue) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);

        return pref.getLong(key, defaultValue);
    }
    public static void putCityName(Context ctx ,String cityName){
        putString(ctx,"cityName",cityName);

    }

    public static String getCityName(Context ctx){
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);
        return pref.getString("cityName","选择城市");
    }

    public static void putCityId(Context ctx ,int cityId){
        putInt(ctx,"cityId",cityId);

    }

    public static int getCityId(Context ctx){
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);
        return pref.getInt("cityId",172);
    }
    public  static  void clearShare(Context ctx){
        SharedPreferences shared = ctx.getSharedPreferences(SHARE_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.clear();
        editor.commit();
    }

}
