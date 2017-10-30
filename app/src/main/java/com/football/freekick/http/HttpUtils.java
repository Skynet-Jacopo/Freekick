package com.football.freekick.http;


import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.Map;

/**
 * 网络请求工具类
 */
public class HttpUtils {


    /**
     * 普通get请求，回调返回字符串
     */
    public static Callback.Cancelable get(String url, Callback.CommonCallback<?> response) {
        RequestParams params = new RequestParams(url);
        return get(params, response);
    }

    /**
     * 带有参数的普通get请求，回调返回字符串
     */
    public static Callback.Cancelable get(String url, final Map<String, String> contentParams, Callback.CommonCallback<?> response) {
        RequestParams params = new RequestParams(url);
        for (Map.Entry<String, String> entry : contentParams.entrySet()) {
            params.addBodyParameter(entry.getKey(), entry.getValue());
        }
        return get(params, response);
    }

    /**
     * 带有参数的普通get请求，回调返回字符串
     */
    public static Callback.Cancelable get(RequestParams params, Callback.CommonCallback<?> response) {
        //       SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
//        if(!TextUtils.isEmpty(sp.getString("session_id",""))) {
//            params.setUseCookie(true);
//            params.addHeader("Cookie","PHPSESSID"+sp.getString("session_id",""));
//        }
        return x.http().get(params, response);
    }


    /**
     * 普通post请求，回调返回字符串
     */
    public static Callback.Cancelable post(String url, Callback.CommonCallback<?> response) {
        RequestParams params = new RequestParams(url);
        return post(params, response);
    }

    /**
     * 带有参数post请求，回调返回字符串
     */
    public static Callback.Cancelable post(String url, final Map<String, String> contentParams, Callback.CommonCallback<?> response) {
        RequestParams params = new RequestParams(url);
        for (Map.Entry<String, String> entry : contentParams.entrySet()) {
            params.addBodyParameter(entry.getKey(), entry.getValue());
        }
        return post(params, response);
    }

    /**
     * 带有参数post请求，回调返回字符串
     */
    public static Callback.Cancelable post(RequestParams params, Callback.CommonCallback<?> response) {
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
//        if (!TextUtils.isEmpty(sp.getString("session_id", ""))) {
//            params.setUseCookie(true);
//            params.addHeader("Cookie", "PHPSESSID" + sp.getString("session_id", ""));
//        }
        return x.http().post(params, response);
    }


    /**
     * 上传文件
     */
    public static Callback.Cancelable uploadFile(String url, String key, File file, Callback.CommonCallback<?> response) {
        RequestParams params = new RequestParams(url);
        params.addBodyParameter(key, file);
        return post(params, response);
    }



    /**
     * 多文件上传
     */
    public static Callback.Cancelable uploadFile(String url, final Map<String, File> contentParams, Callback.CommonCallback<?> response) {
        RequestParams params = new RequestParams(url);
        for (Map.Entry<String, File> entry : contentParams.entrySet()) {
            params.addBodyParameter(entry.getKey(), entry.getValue());
        }
        return post(params, response);
    }


    /**
     * 下载文件
     */
    public static Callback.Cancelable DownLoadFile(String url, String filepath, Callback.CommonCallback<?> callback) {
        RequestParams params = new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(filepath);
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

}
