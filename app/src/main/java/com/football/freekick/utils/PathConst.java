package com.football.freekick.utils;

import android.os.Environment;

/**
 * 程序路径类
 *
 * @author PathConst
 */
public class PathConst {

    public static final String PATH_SDCARD;
    public static final String PATH_IMG_CACHE;
    public static final String PATH_APP;
    public static final String PATH_LOG;

    static {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            PATH_SDCARD = Environment.getExternalStorageDirectory().getPath();//获取根目录
        } else {
            PATH_SDCARD = Environment.getRootDirectory().getAbsolutePath();//获取根目录
        }

        PATH_APP = PATH_SDCARD + "/HappyVolleyball/";
        PATH_IMG_CACHE = PATH_APP + "img_cache/";
        PATH_LOG = PATH_APP + "log/log.txt";
    }
}
