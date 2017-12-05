package com.football.freekick;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.football.freekick.beans.Advertisements;
import com.football.freekick.beans.Pitches;
import com.football.freekick.http.Url;
import com.football.freekick.language.LanguageConfig;
import com.football.freekick.language.LanguageCountry;
import com.football.freekick.language.LanguageSwitcher;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.SPUtil;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by liuqun on 11/23/2016.
 */
public class App extends Application {
    public static final String FREEKICK = "FREEKICK";
    public static Context APP_CONTEXT;
    public static HttpHeaders headers;
    public static final String[] PROJECT_LANGUAGES = {
            LanguageCountry.LANGUAGE_OPTION_EN,
            LanguageCountry.LANGUAGE_OPTION_ZH_TW
    };
    public static Typeface mTypeface;
    public static LanguageConfig mConfig;
    public static boolean isChinese = false;
    public static List<Advertisements.AdvertisementsBean> mAdvertisementsBean;
    public static List<Pitches.PitchesBean> mPitchesBeanList;

    @Override
    public void onCreate() {
        APP_CONTEXT = this;
        super.onCreate();
        LanguageSwitcher.getInstance().initLanguage(getApplicationContext(), PROJECT_LANGUAGES);
        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        initOkGo();
        initImageLoaderOption();
        FacebookSdk.sdkInitialize(getApplicationContext());
        Logger
                .init(FREEKICK)                 // 自定义TAG名称
                .methodCount(3)                 // 方法栈打印的个数,默认为2
//                .hideThreadInfo()               // 隐藏线程信息,默认显示
                .logLevel(LogLevel.FULL);    // default LogLevel.FULL
//                .methodOffset(3);             // 设置调用堆栈的函数偏移值,默认为0
//                .logAdapter(new AndroidLogAdapter()); //自顶一个打印适配器
        mConfig = LanguageConfig.newInstance(APP_CONTEXT);
        getAdvertisements();//獲取廣告
        getPitches();//獲取場地
    }

    /**
     * 獲取場地
     */
    private void getPitches() {
        OkGo.get(Url.PITCHES)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Pitches pitches = gson.fromJson(s, Pitches.class);
                        mPitchesBeanList = pitches.getPitches();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }

    /**
     * 獲取廣告
     */
    private void getAdvertisements() {
        OkGo.get(Url.ADVERTISEMENTS)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Advertisements advertisements = gson.fromJson(s, Advertisements.class);
                        mAdvertisementsBean = advertisements.getAdvertisements();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }

    private void initOkGo() {
        OkGo.init(this);
        headers = new HttpHeaders();
        headers.put("Content-Type", "application/json");
        headers.put("access-token", PrefUtils.getString(App.APP_CONTEXT, "access_token", null));
        headers.put("client", PrefUtils.getString(App.APP_CONTEXT, "client", null));
        headers.put("uid", PrefUtils.getString(App.APP_CONTEXT, "uid", null));
        headers.put("expiry", PrefUtils.getString(App.APP_CONTEXT, "expiry", null));
        Logger.d("access-token--->" + PrefUtils.getString(App.APP_CONTEXT, "access_token", null));
        Logger.d("client--->" + PrefUtils.getString(App.APP_CONTEXT, "client", null));
        Logger.d("uid--->" + PrefUtils.getString(App.APP_CONTEXT, "uid", null));
        Logger.d("expiry--->" + PrefUtils.getString(App.APP_CONTEXT, "expiry", null));
        OkGo.getInstance().addCommonHeaders(headers);
    }

    private void initImageLoaderOption() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                // .memoryCacheExtraOptions(800, 600) // max width, max height
                .threadPoolSize(5).threadPriority(Thread.NORM_PRIORITY - 1).memoryCache(new WeakMemoryCache())
                //.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache
                // .discCache(new UnlimitedDiscCache(cacheDir))
                // .discCacheExtraOptions(800, 600, CompressFormat.JPEG, 75) // Can slow ImageLoader, use it carefully
                // // (Better don't use it)
                // .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                // .denyCacheImageMultipleSizesInMemory()
                // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs().build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);


//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.drawable.lizhi1)
//                .showImageOnLoading(R.drawable.lizhi1)
//                .showImageOnFail(R.drawable.lizhi1)
//                .displayer(new RoundedBitmapDisplayer(getResources().getDimensionPixelOffset(R.dimen.dimens_mid)))
//                .cacheInMemory(true) // 打开内存缓存
//                .cacheOnDisk(true) // 打开硬盘缓存
//                .resetViewBeforeLoading(true)// 在ImageView加载前清除它上面之前的图片
//                .build();
//        // ImageLoader的配置
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
//                .memoryCacheSize(5 * 1024 * 1024)// 设置内存缓存为5M
//                .defaultDisplayImageOptions(options)// 设置默认的显示选项
//                .build();
//        // 初始化ImageLoader
//        ImageLoader.getInstance().init(config);
    }
}
