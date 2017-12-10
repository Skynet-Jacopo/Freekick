package com.football.freekick.activity.registerlogin;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.Pitches;
import com.football.freekick.http.Url;
import com.football.freekick.language.LanguageConfig;
import com.football.freekick.language.LanguageCountry;
import com.football.freekick.language.LanguageObservable;
import com.google.android.gms.common.ConnectionResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 登录注册页面
 */
public class FirstPageActivity extends BaseActivity {
    @Bind(R.id.tv_login)
    TextView mTvLogin;
    @Bind(R.id.tv_register)
    TextView mTvRegister;
    @Bind(R.id.tv_token)
    TextView mTvToken;

    private Context mContext;

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "259337742082";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCMDemo";
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;

    @Override
    public LoaderManager getLoaderManager() {
        return super.getLoaderManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        mContext = FirstPageActivity.this;
        ButterKnife.bind(this);
        Logger.d("getCountryNameValue--->" + App.mConfig.getCountryNameValue());
        Logger.d("getLanguageValue--->" + App.mConfig.getLanguageValue());

        if (App.mConfig.getLanguageValue().equals("zh")) {
            App.isChinese = true;
//            setLanguage("zh_TW","CN", this);
//            LanguageObservable.getInstance().notifyObservers();
        } else {
            App.isChinese = false;
//            setLanguage("en","US", this);
//            LanguageObservable.getInstance().notifyObservers();
        }

//        Logger.d("google可用么" + GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()));
//        Logger.d("getToken---->"+ FirebaseInstanceId.getInstance().getToken());
//        int success = ConnectionResult.SUCCESS;
        String token =FirebaseInstanceId.getInstance().getToken();
        Logger.d("token--->" + token);
        mTvToken.setText(token);
        getPitches();
    }

    @OnClick({R.id.tv_login, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
//                startActivity(new Intent(mContext,LoginPage1Activity.class));
                startActivity(new Intent(mContext, LoginPager2Activity.class));
                break;
            case R.id.tv_register:
                startActivity(new Intent(mContext, LoginPage1Activity.class));
                break;
        }
    }
    /**
     * 獲取場地
     */
    private void getPitches() {
        Logger.d("中文還是英文--->"+App.isChinese);
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "pitches";
        Logger.d(url);
        OkGo.get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Pitches pitches = gson.fromJson(s, Pitches.class);
                        App.mPitchesBeanList = pitches.getPitches();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }
//
//    private boolean checkPlayServices() {
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
////        int resultCode = apiAvailability.isGooglePlayServicesAvailable(app);
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
//        if (resultCode != ConnectionResult.SUCCESS) {
//            //TODO track user's device not support play service. should use pull to get msg.
//            return false;
//        }
//        return true;
//    }
// 更新app当前语言
public static void setLanguage(String language,String country, Context context) {
    if (null == language ||null == country || null == context) {
        return;
    }
    Locale locale = new Locale(language, country);
    Locale.setDefault(locale);
    Resources res = context.getResources();
    if (null != res) {
        Configuration config = res.getConfiguration();
        if (null != config) {
            config.locale = locale;
        }
        DisplayMetrics dm = res.getDisplayMetrics();
        if (null != config && null != dm) {
            res.updateConfiguration(config, dm);
        }
    }
}
}
