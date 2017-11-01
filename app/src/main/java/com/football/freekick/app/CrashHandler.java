package com.football.freekick.app;

import android.content.Context;
import android.os.Looper;

import com.football.freekick.utils.CalendarUtil;
import com.football.freekick.utils.FileUtil;
import com.football.freekick.utils.ToastUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 异常捕获
 *
 * @author wangjunke 2016.10.08
 */
final class CrashHandler implements UncaughtExceptionHandler {
    private static final    Object       LOCK     = new Object();
    private static volatile CrashHandler INSTANCE = null;
    private static final    String       TAG      = "CrashHandler";
    private UncaughtExceptionHandler mDefaultHandler;
    private Context                  mContext;
    private String mExceptionFileName = "exception.log";

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                INSTANCE = new CrashHandler(context);
            }
        }
        return INSTANCE;
    }

    private CrashHandler(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();

        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            // Sleep一会后结束程序
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //不要把线程都杀了，否则连日志都看不了
            // android.os.Process.killProcess(android.os.Process.myPid());
            //如果把这句话注释掉，有异常都不会退出
            System.exit(10);
        }
    }

    /**
     * 处理捕获到的异常
     *
     * @param ex
     * @return
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //在这里处理崩溃逻辑,将不再显示FC对话框
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                ToastUtil.toastShort("很抱歉，程序出现异常，即将退出");
                Looper.loop();
            }
        }.start();

        File file = new File(mContext.getCacheDir().getPath() + "/crash/" + mExceptionFileName);
        if (!file.exists()) {
            FileUtil.createFile(file.getPath());
        }

        return true;
    }



    /**
     * 将异常日志写入文件
     */
    private void writeExceptionToFile(String message, File crashFile) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(CalendarUtil.getDateTime());
        stringBuffer.append("\n");
        stringBuffer.append(message);
        stringBuffer.append("\n");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(crashFile, true));
            writer.append(stringBuffer);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
