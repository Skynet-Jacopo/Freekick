package com.football.freekick.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * APP管理工具类，如应用退出 重启 安装 卸载.判断程序运行状况等函数的定义
 *
 * @author wangjunke 2016.10.08
 */
public class AppUtil {
    /**
     * 检查权限
     *
     * @param permission android.permission.WRITE_EXTERNAL_STORAGE
     * @return manifest 已经定义了则返回true
     */
    public static boolean checkPermission(@NonNull Context context, @NonNull String permission) {
        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 退出程序
     */
    public static void quitSystem() {
        ActyUtil.finishAllActivity();
        /**
         * TODO 有service需要退出service
         */
    }

    /**
     * 重启程序
     */
    public static void restartSystem(final Activity acty) {
        quitSystem();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
              //  Intent intent = new Intent(acty, .class);
               // intent.putExtra("notPlaySplash", true);
            //    acty.startActivity(intent);
            }
        }, 500);
    }

    /**
     * 杀进程， 在有的平台会失效
     *
     * @param context
     * @param pckName
     */
    public static void shutDownPck(Context context, String pckName) {
        if (context != null) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            am.killBackgroundProcesses(pckName);
        }
    }

    /**
     * 获取应用标签
     *
     * @param context
     * @param info    ApplicationInfo
     * @return
     */
    public static String getAppLabel(Context context, ApplicationInfo info) {
        PackageManager pManager = context.getPackageManager();
        return pManager.getApplicationLabel(info).toString();
    }

    /**
     * 从指定包中 获取自定义标签值标签
     *
     * @param context
     * @param pckName 程序包名称
     * @param key     字段key
     * @return key对应的值
     */
    public static String getMetaData(Context context, String pckName, String key) {
        String myApiKey = null;
        try {
            Bundle bundle = getMetaData(context, pckName);
            if (bundle != null) {
                myApiKey = bundle.getString(key);
            }
        } catch (Exception e) {

        }
        return myApiKey;
    }

    /**
     * 从指定的包中 获取自定义标签值标签
     *
     * @param context
     * @param pckName 包名称
     * @return 自定义Bundle对象，没有找到返回null
     */
    public static Bundle getMetaData(Context context, String pckName) {
        Bundle bundle = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(pckName, PackageManager.GET_META_DATA);
            bundle = appInfo.metaData;
        } catch (Exception e) {
        }
        return bundle;
    }

    /**
     * 获取指定包的包信息
     *
     * @param context
     * @param pckName 包名称
     * @return 包信息
     */
    public static PackageInfo getPackageInfo(Context context, String pckName) {
        PackageInfo packInfo = null;
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // 0代表是获取版本信息
            packInfo = packageManager.getPackageInfo(pckName, 0);
        } catch (Exception e) {
//            LogUtil.d("FCUtil--getPackageInfo", LogUtil.ExceptionToString(e));
        }
        return packInfo;
    }

    /**
     * 获取本程序版本名称
     *
     * @param context
     * @return 版本名称
     */
    public static String getVersionName(Context context) {
        return getVersionName(context, context.getPackageName());
    }

    /**
     * 获取本程序版本号
     *
     * @param context
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        return getVersionCode(context, context.getPackageName());
    }

    /**
     * 获取指定程序的版本名称
     *
     * @param context
     * @param pckName 指定程序的包名
     * @return 版本名称
     */
    public static String getVersionName(Context context, String pckName) {
        String      versionName = null;
        PackageInfo packInfo    = getPackageInfo(context, pckName);
        if (packInfo != null)
            versionName = packInfo.versionName;
        return versionName;
    }

    /**
     * 获取指定程序的版本号
     *
     * @param context
     * @param pckName 指定程序的报名
     * @return 版本号
     */
    public static int getVersionCode(Context context, String pckName) {
        int         versionCode = 0;
        PackageInfo packInfo    = getPackageInfo(context, pckName);
        if (packInfo != null)
            versionCode = packInfo.versionCode;
        return versionCode;
    }

    /**
     * 根据包名获取已安装的PackageInfo
     *
     * @param context
     * @param pckName
     * @return PackageInfo
     */
    public static PackageInfo getInstallPckInfoByPckName(Context context, String pckName) {
        PackageInfo       packageInfo    = null;
        PackageManager    pckMgr         = context.getPackageManager();
        List<PackageInfo> lstPackageInfo = pckMgr.getInstalledPackages(0);
        for (PackageInfo pckInfo : lstPackageInfo) {
            if (pckInfo.packageName.equals(pckName)) {
                packageInfo = pckInfo;
            }
        }
        return packageInfo;
    }

    /**
     * 判断指定程序是否有Service运行
     *
     * @param context
     * @param pckName 程序包名
     * @return 是否有Service运行
     */
    public static boolean isServiceRun(Context context, String pckName) {
        ActivityManager                          manager            = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        boolean                                  flag               = false;
        List<ActivityManager.RunningServiceInfo> runningServiceInfo = manager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo serviceInfo : runningServiceInfo) {
            String strInfo = serviceInfo.service.getPackageName();
            if (strInfo.startsWith(pckName)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 判断指定包中的Activity是否正在运行
     *
     * @param context
     * @param pckName 指定包的包名
     * @return 是否有Activity运行
     */
    public static boolean isActivityRun(Context context, String pckName) {
        boolean               isActivityRun = false;
        ActivityManager       manager       = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> lstInfo       = manager.getRunningTasks(100);
        for (RunningTaskInfo info : lstInfo) {
            if (info.topActivity.getPackageName().startsWith(pckName) || info.baseActivity.getPackageName().startsWith(pckName)) {
                isActivityRun = true;
                break;
            }
        }
        return isActivityRun;
    }

    /**
     * 判断当前栈顶的Activity是否属于传入的包名
     *
     * @param context
     * @param pckName 指定包的包名
     * @return 栈顶的Activity是否属于传入的包名
     */
    public static boolean isTopActivityFromPck(Context context, String pckName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        RunningTaskInfo info    = manager.getRunningTasks(1).get(0);
        // String shortClassName = info.topActivity.getShortClassName(); // 类名
        // String className = info.topActivity.getClassName(); // 完整类名
        String activityPckName = info.topActivity.getPackageName(); // 包名
        return activityPckName.startsWith(pckName);
    }

    /**
     * 调用系统安装
     *
     * @param context
     * @param f       APK文件对象
     * @return
     */
    public static void installBySys(Context context, File f) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 调用系统卸载
     *
     * @param context
     * @param pckName 卸载Apk的包名
     * @return
     */
    public static String unInstallBySys(Context context, String pckName) {
        Intent intent = new Intent();
        Uri    uri    = Uri.parse("package:" + pckName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(uri);
        context.startActivity(intent);
        return null;
    }

    /**
     * 获取应用程序下所有Activity
     *
     * @param ctx
     * @return
     */
    public static ArrayList<ActivityInfo> getAppAllActivities(Context ctx) {
        ArrayList<ActivityInfo> result = new ArrayList<ActivityInfo>();
        Intent                  intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setPackage(ctx.getPackageName());
        for (ResolveInfo info : ctx.getPackageManager().queryIntentActivities(intent, 0)) {
            result.add(info.activityInfo);
        }
        return result;
    }

    /**
     * 检查有没有应用程序来接受处理发出的intent
     *
     * @param context
     * @param action
     * @return
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent         intent         = new Intent(action);
        List<ResolveInfo>    list           = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
