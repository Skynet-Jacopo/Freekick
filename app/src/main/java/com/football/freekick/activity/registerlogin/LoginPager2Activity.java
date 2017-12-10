package com.football.freekick.activity.registerlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.Advertisements;
import com.football.freekick.beans.Login;
import com.football.freekick.beans.Pitches;
import com.football.freekick.http.Url;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.StringUtils;
import com.football.freekick.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

public class LoginPager2Activity extends BaseActivity {

    @Bind(R.id.fl_login_by_facebook)
    FrameLayout mFlLoginByFacebook;
    @Bind(R.id.edt_email)
    EditText mEdtEmail;
    @Bind(R.id.edt_pass_word)
    EditText mEdtPassWord;
    @Bind(R.id.tv_login)
    TextView mTvLogin;
    @Bind(R.id.tv_forget_pass_word)
    TextView mTvForgetPassWord;
    @Bind(R.id.tv_back)
    TextView mTvBack;
    @Bind(R.id.facebook)
    LoginButton mFacebook;
    private Context mContext;
    private CallbackManager mCallbackManager;
    private boolean isSecondRun;//記錄是否已登錄,以獲取廣告,場地等信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pager2);
        mContext = LoginPager2Activity.this;
        ButterKnife.bind(this);
//        mEdtEmail.setText("huo@yopmail.com");
//        mEdtEmail.setText("yue@yopmail.com");
        mEdtEmail.setText("lei@yopmail.com");
        mEdtPassWord.setText("123456");
        initView();

    }

    private void initView() {
        mTvBack.setTypeface(App.mTypeface);
        isSecondRun = PrefUtils.getBoolean(App.APP_CONTEXT, "isSecondRun", false);
        mCallbackManager = CallbackManager.Factory.create();

        mFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Gson gson = new Gson();
                Logger.json(gson.toJson(loginResult));
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @OnClick({R.id.fl_login_by_facebook, R.id.tv_login, R.id.tv_forget_pass_word, R.id.tv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_login_by_facebook:
                mFacebook.performClick();
                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.tv_forget_pass_word:
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }

    /**
     * 登錄
     */
    private void login() {
        if (StringUtils.isEmpty(mEdtEmail)) {
            ToastUtil.toastShort(getString(R.string.please_enter_your_email));
            return;
        }
        if (StringUtils.isEmpty(mEdtPassWord)) {
            ToastUtil.toastShort(getString(R.string.please_enter_your_password));
            return;
        }
        loadingShow();
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("email", StringUtils.getEditText(mEdtEmail));
        object1.addProperty("password", StringUtils.getEditText(mEdtPassWord));
        object.add("user", object1);
        Logger.json(object.toString());
        String url = App.isChinese ? "http://api.freekick.hk/api/zh_HK/auth/sign_in" :
                "http://api.freekick.hk/api/en/auth/sign_in";
        Logger.d(url);
        OkGo.post(url)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        Login login = gson.fromJson(s, Login.class);
                        if (login.getUser() != null) {
                            Login.UserBean user = login.getUser();
                            if (user.getLogin_fail() == 0) {
                                //登錄成功
                                Headers headers = response.headers();
                                String access_token = headers.get("access-token");
                                String client = headers.get("client");
                                String uid = headers.get("uid");
                                String expiry = headers.get("expiry");
                                Logger.d("access-token=" + access_token + "   client=" + client + "   uid=" + uid + "" +
                                        "   expiry=" + expiry);
                                HttpHeaders header = new HttpHeaders();
                                header.put("access-token", access_token);
                                header.put("client", client);
                                header.put("uid", uid);
                                header.put("expiry", expiry);
                                OkGo.getInstance().addCommonHeaders(header);
                                PrefUtils.putString(App.APP_CONTEXT, "access_token", access_token);
                                PrefUtils.putString(App.APP_CONTEXT, "client", client);
                                PrefUtils.putString(App.APP_CONTEXT, "uid", uid);
                                PrefUtils.putString(App.APP_CONTEXT, "expiry", expiry);

                                PrefUtils.putString(App.APP_CONTEXT, "mobile_no", user.getMobile_no() + "");
                                PrefUtils.putString(App.APP_CONTEXT, "username", user.getUsername() + "");
                                if (!isSecondRun) {
                                    //如果是第一次登錄或者卸載重裝過,獲取廣告,場地等信息
                                    getPitches();
                                    getAdvertisements();
                                    PrefUtils.putBoolean(App.APP_CONTEXT, "isSecondRun", true);
                                }
                                if (user.getTeams() != null && user.getTeams().size() <= 0) {//沒有球队則去註冊三頁
                                    Intent intent = new Intent(mContext, RegisterPager1Activity.class);
                                    intent.putExtra("email", StringUtils.getEditText(mEdtEmail));
                                    intent.putExtra("password", StringUtils.getEditText(mEdtPassWord));
                                    startActivity(intent);
                                } else {//有用戶名則直接進入應用
                                    List<Login.UserBean.TeamsBean> teams = user.getTeams();
                                    Login.UserBean.TeamsBean teamsBean = teams.get(teams.size() - 1);
                                    PrefUtils.putString(App.APP_CONTEXT, "team_id", teamsBean.getId() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "color1", teamsBean.getColor1() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "color2", teamsBean.getColor2() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "logourl", teamsBean.getImage().getUrl() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "team_name", teamsBean.getTeam_name() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "size", teamsBean.getSize() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "age_range_max", teamsBean.getAge_range_max
                                            () + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "age_range_min", teamsBean.getAge_range_min
                                            () + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "establish_year", teamsBean
                                            .getEstablish_year() + "");
                                    PrefUtils.putString(App.APP_CONTEXT, "average_height", teamsBean
                                            .getAverage_height() + "");
                                    if (teamsBean.getDistrict() != null) {
                                        PrefUtils.putString(App.APP_CONTEXT, "district", teamsBean.getDistrict()
                                                .getDistrict() + "");
                                        PrefUtils.putString(App.APP_CONTEXT, "district_id", teamsBean.getDistrict()
                                                .getId() + "");
                                    }

                                    startActivity(new Intent(mContext, OneTimePagerActivity.class));
                                }
                            } else {

                            }
                        } else {//data為null,登錄失敗
                            if (login.getErrors() != null) {
                                ToastUtil.toastShort(login.getErrors().get(0));
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                    }
                });
    }


    /**
     * 獲取場地
     */
    private void getPitches() {
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

    /**
     * 獲取廣告
     */
    private void getAdvertisements() {
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "advertisements";
        Logger.d(url);
        OkGo.get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Advertisements advertisements = gson.fromJson(s, Advertisements.class);
                        App.mAdvertisementsBean = advertisements.getAdvertisements();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.json(e.getMessage());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,
                resultCode, data);
    }
}
