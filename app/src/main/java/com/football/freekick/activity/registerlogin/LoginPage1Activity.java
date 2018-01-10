package com.football.freekick.activity.registerlogin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.Login;
import com.football.freekick.beans.RegisterResponse;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.StringUtils;
import com.football.freekick.utils.ToastUtil;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.orhanobut.logger.Logger;

import org.joda.time.DateTime;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

public class LoginPage1Activity extends BaseActivity {

    @Bind(R.id.fl_login_by_facebook)
    FrameLayout mFlLoginByFacebook;
    @Bind(R.id.tv_login_by_email)
    TextView mTvLoginByEmail;
    @Bind(R.id.tv_back)
    TextView mTvBack;
    @Bind(R.id.tv_login)
    TextView mTvLogin;//已經是會員了 登入
    @Bind(R.id.facebook)
    LoginButton mFacebook;
    @Bind(R.id.edt_log)
    EditText mEdtLog;
    private CallbackManager mCallbackManager;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page1);
        mContext = LoginPage1Activity.this;
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvBack.setTypeface(App.mTypeface);

//        mFacebook.setReadPermissions("email");
        // If using in a fragment
//        mFacebook.setFragment(this);
        mCallbackManager = CallbackManager.Factory.create();
        mFacebook.setReadPermissions("email", "public_profile", "user_friends");
        mFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Gson gson = new Gson();
                mEdtLog.setText(gson.toJson(loginResult));
                Logger.json(gson.toJson(loginResult));
                getLoginInfo(loginResult.getAccessToken());
//                List<String> str = new ArrayList<String>();
//                str.add("public_profile");
//                final AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken()
// .getToken());
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

    @OnClick({R.id.fl_login_by_facebook, R.id.tv_login_by_email, R.id.tv_back, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_login_by_facebook:
                mFacebook.performClick();
//                registerByFaceBook("108927019898582","yue");
                break;
            case R.id.tv_login_by_email:
                startActivity(new Intent(mContext, RegisterByEmailActivity.class));
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_login://已經是會員了 登入
                startActivity(new Intent(mContext, LoginPager2Activity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,
                resultCode, data);
    }

    public void getFaceBookToken(View view) {
        AccessToken mAccessToken = AccessToken.getCurrentAccessToken();
        Log.e("token", "token :" + mAccessToken.getToken() + "," + "user_id" + mAccessToken.getUserId());
    }

    public void getLoginInfo(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object != null) {
                    String id = object.optString("id");   //比如:1565455221565
                    String name = object.optString("name");  //比如：Zhang San
                    String gender = object.optString("gender");  //性别：比如 male （男）  female （女）
                    String emali = object.optString("email");  //邮箱：比如：56236545@qq.com

                    //获取用户头像
                    JSONObject object_pic = object.optJSONObject("picture");
                    JSONObject object_data = object_pic.optJSONObject("data");
                    String photo = object_data.optString("url");

                    //获取地域信息
                    String locale = object.optString("locale");   //zh_CN 代表中文简体
                    Logger.json(object.toString());
                    Toast.makeText(mContext, "" + object.toString(), Toast.LENGTH_SHORT).show();
                    mEdtLog.setText(object.toString());
//                    LoginManager.getInstance().logInWithReadPermissions(LoginPage1Activity.this,Arrays.asList
// ("public_profile"));
                    registerByFaceBook(id, name);
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,picture,locale,updated_time,timezone," +
                "age_range,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * 通過Facebook註冊
     *
     * @param id
     * @param name
     */
    private void registerByFaceBook(final String id, final String name) {
//        { "user": { "social_token": "asdfghjqwertyuytrewq", "username": "test_user",  "provider":"facebook"}}
        String url;
        if (App.isChinese)
//            url = "http://api.freekick.hk/api/zh_HK/auth";
            url = "http://api.freekick.hk/api/zh_HK/social_authentication/authentication_success";
        else
            url = "http://api.freekick.hk/api/en/social_authentication/authentication_success";
        loadingShow();
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("social_token", id);
        object1.addProperty("android_device_token", FirebaseInstanceId.getInstance().getToken());
//        object1.addProperty("username",name);
        object1.addProperty("provider", "facebook");
        object.add("user", object1);
        OkGo.post(url)
                .upJson(object.toString())
//                .upJson(str)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
//                        loadingDismiss();
//                        Logger.json(s);
//                        Gson gson = new Gson();
//                        RegisterResponse registerResponse = gson.fromJson(s, RegisterResponse.class);
//                        if (registerResponse.getStatus().equals("success")) {
//
////                            ToastUtil.toastShort(getString(R.string.please_verify_your_account_first));
//                            Intent intent = new Intent(mContext, RegisterPager1Activity.class);
//                            intent.putExtra("social_token", id);
//                            intent.putExtra("username", name);
//                            startActivity(intent);
//                        }else if (registerResponse.getStatus().equals("error")){
//                            RegisterResponse.ErrorsBean errors = registerResponse.getErrors();
//                            if (errors.getFull_messages()!=null&&errors.getFull_messages().size()!=0){
//                                ToastUtil.toastShort(errors.getFull_messages().get(0));
//                            }else if (errors.getMobile_no()!=null&&errors.getMobile_no().size()!=0){
//                                ToastUtil.toastShort(errors.getMobile_no().get(0));
//                            }else if (errors.getPassword()!=null&&errors.getPassword().size()!=0){
//                                ToastUtil.toastShort(errors.getPassword().get(0));
//                            }else if (errors.getRegister_type()!=null&&errors.getRegister_type().size()!=0){
//                                ToastUtil.toastShort(errors.getRegister_type().get(0));
//                            }
//
//                        }

                        Logger.json(s);
                        Gson gson = new Gson();
                        Login login = gson.fromJson(s, Login.class);
                        if (login.getUser() != null) {
                            Login.UserBean user = login.getUser();
                            if (user.getLogin_fail() == 0) {
                                PrefUtils.putLong(App.APP_CONTEXT,"FacebookTime",new DateTime().getMillis());
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
                                if (user.getTeams() != null && user.getTeams().size() <= 0) {//沒有球队則去註冊三頁
                                    Intent intent = new Intent(mContext, RegisterPager1Activity.class);
                                    intent.putExtra("social_token", id);
                                    intent.putExtra("username", name);
                                    PrefUtils.putString(App.APP_CONTEXT, "username", name);
                                    loadingDismiss();
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
                                    loadingDismiss();
                                    startActivity(new Intent(mContext, OneTimePagerActivity.class));
                                }
                            } else {

                            }
                        } else {//data為null,登錄失敗
                            loadingDismiss();
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
}
