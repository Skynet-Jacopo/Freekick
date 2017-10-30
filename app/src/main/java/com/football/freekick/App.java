package com.football.freekick;

import android.app.Application;
import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.xutils.x;


/**
 * Created by liuqun on 11/23/2016.
 */
public class App extends Application {
    public static final String FREEKICK = "FREEKICK";
    public static Context     APP_CONTEXT;
    public static    HttpHeaders headers;
    @Override
    public void onCreate() {
        APP_CONTEXT = this;
        super.onCreate();

        initOkGo();
        x.Ext.init(this);//初始化xUtils
        x.Ext.setDebug(org.xutils.BuildConfig.DEBUG); // 开启debug会影响性能
        Logger
                .init(FREEKICK)                 // 自定义TAG名称
                .methodCount(3)                 // 方法栈打印的个数,默认为2
//                .hideThreadInfo()               // 隐藏线程信息,默认显示
                .logLevel(LogLevel.FULL)    ;    // default LogLevel.FULL
//                .methodOffset(3);             // 设置调用堆栈的函数偏移值,默认为0
//                .logAdapter(new AndroidLogAdapter()); //自顶一个打印适配器
    }

    private void initOkGo() {
        OkGo.init(this);
        //  OkGo.getInstance().setCertificates();
    }
}
