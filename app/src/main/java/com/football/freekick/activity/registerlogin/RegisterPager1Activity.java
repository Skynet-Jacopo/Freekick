package com.football.freekick.activity.registerlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.Login;
import com.football.freekick.beans.RegisterResponse;
import com.football.freekick.beans.SignInResponse;
import com.football.freekick.http.Url;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.SPUtil;
import com.football.freekick.utils.StringUtils;
import com.football.freekick.utils.ToastUtil;
import com.google.firebase.iid.FirebaseInstanceId;
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

public class RegisterPager1Activity extends BaseActivity {

    @Bind(R.id.tv_user_icon)
    TextView mTvUserIcon;
    @Bind(R.id.tv_phone_icon)
    TextView mTvPhoneIcon;
    @Bind(R.id.tv_next)
    TextView mTvNext;
    @Bind(R.id.edt_user_name)
    EditText mEdtUserName;
    @Bind(R.id.edt_phone_num)
    EditText mEdtPhoneNum;

    private Context mContext;
    private String mEmail;
    private String mPassword;
    private String social_token;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pager1);
        mContext = RegisterPager1Activity.this;
        ButterKnife.bind(this);
        social_token = getIntent().getStringExtra("social_token");
        username = getIntent().getStringExtra("username");
        Logger.d(social_token+username);
        mEdtUserName.setText(username);
        initView();
        initData();
    }

    private void initView() {
        mTvUserIcon.setTypeface(App.mTypeface);
        mTvPhoneIcon.setTypeface(App.mTypeface);
    }

    private void initData() {
        mEmail = getIntent().getStringExtra("email");
        mPassword = getIntent().getStringExtra("password");
    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        if (social_token != null && !social_token.equals("")) {
//            putNameAndPhone();
            putNameAndPhoneByFacebook();
//            loginByFacebook();
        } else {
            next();
        }

//        putNameAndPhone();
//        startActivity(new Intent(mContext, RegisterPager2Activity.class));
    }

    private void putNameAndPhoneByFacebook() {
        if (StringUtils.isEmpty(mEdtUserName)) {
            ToastUtil.toastShort(getString(R.string.please_enter_your_name));
            return;
        }
        if (StringUtils.isEmpty(mEdtPhoneNum)) {
            ToastUtil.toastShort(getString(R.string.please_enter_your_phone));
            return;
        }
        loadingShow();
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("username", StringUtils.getEditText(mEdtUserName));
//        object1.addProperty("register_type", "mobile");
        object1.addProperty("mobile_no", StringUtils.getEditText(mEdtPhoneNum));
        object.add("user", object1);
        Logger.json(object.toString());
        String url = App.isChinese ? "http://api.freekick.hk/api/zh_HK/auth" : "http://api" +
                ".freekick.hk/api/en/auth";
        OkGo.put(url)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        RegisterResponse json = gson.fromJson(s, RegisterResponse.class);
                        if (json.getStatus().equals("success")) {
                            Intent intent = new Intent(mContext, RegisterPager2Activity.class);
                            intent.putExtra("email", mEmail);
                            intent.putExtra("password", mPassword);
                            PrefUtils.putString(App.APP_CONTEXT, "mobile_no", StringUtils.getEditText(mEdtPhoneNum));
                            PrefUtils.putString(App.APP_CONTEXT, "username", StringUtils.getEditText(mEdtUserName));
                            startActivity(intent);
                        } else if (json.getStatus().equals("error")) {
                            RegisterResponse.ErrorsBean errors = json.getErrors();
                            if (errors.getFull_messages() != null && errors.getFull_messages().size() != 0) {
                                ToastUtil.toastShort(errors.getFull_messages().get(0));
                            } else if (errors.getMobile_no() != null && errors.getMobile_no().size() != 0) {
                                ToastUtil.toastShort(errors.getMobile_no().get(0));
                            } else if (errors.getPassword() != null && errors.getPassword().size() != 0) {
                                ToastUtil.toastShort(errors.getPassword().get(0));
                            } else if (errors.getRegister_type() != null && errors.getRegister_type().size() != 0) {
                                ToastUtil.toastShort(errors.getRegister_type().get(0));
                            }
                        } else {
                            if (StringUtils.isEmpty(social_token))
                                ToastUtil.toastShort(getString(R.string.please_verify_your_account_first));
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        if (StringUtils.isEmpty(social_token))
                            ToastUtil.toastShort(getString(R.string.please_verify_your_account_first));
                        loadingDismiss();
                    }
                });
    }


    /**
     * 請求一遍登錄接口(有必要么?)
     */
    private void loginByFacebook() {
        String url;
        if (App.isChinese)
//            url = "http://api.freekick.hk/api/zh_HK/auth";
            url = "http://api.freekick.hk/api/zh_HK/social_authentication/authentication_success";
        else
            url = "http://api.freekick.hk/api/en/social_authentication/authentication_success";
        loadingShow();
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("social_token",social_token);
        object1.addProperty("android_device_token", FirebaseInstanceId.getInstance().getToken());
//        object1.addProperty("username",name);
        object1.addProperty("provider","facebook");
        object.add("user", object1);
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

                                putNameAndPhone();
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
     * 再請求一遍登錄接口
     */
    private void next() {

        loadingShow();
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("email", mEmail);
        object1.addProperty("password", mPassword);
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

                                putNameAndPhone();
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
     * 提交用戶名和電話
     */
    private void putNameAndPhone() {
        if (StringUtils.isEmpty(mEdtUserName)) {
            ToastUtil.toastShort(getString(R.string.please_enter_your_name));
            return;
        }
        if (StringUtils.isEmpty(mEdtPhoneNum)) {
            ToastUtil.toastShort(getString(R.string.please_enter_your_phone));
            return;
        }
        loadingShow();
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("email", mEmail);
        object1.addProperty("password", mPassword);
        object1.addProperty("password_confirmation", mPassword);
        object1.addProperty("username", StringUtils.getEditText(mEdtUserName));
        object1.addProperty("register_type", "mobile");
        object1.addProperty("mobile_no", StringUtils.getEditText(mEdtPhoneNum));
        object.add("user", object1);
        Logger.json(object.toString());
        String url = App.isChinese ? "http://api.freekick.hk/api/zh_HK/auth" : "http://api" +
                ".freekick.hk/api/en/auth";
        OkGo.put(url)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        RegisterResponse json = gson.fromJson(s, RegisterResponse.class);
                        if (json.getStatus().equals("success")) {
                            Intent intent = new Intent(mContext, RegisterPager2Activity.class);
                            intent.putExtra("email", mEmail);
                            intent.putExtra("password", mPassword);
                            PrefUtils.putString(App.APP_CONTEXT, "mobile_no", StringUtils.getEditText(mEdtPhoneNum));
                            PrefUtils.putString(App.APP_CONTEXT, "username", StringUtils.getEditText(mEdtUserName));
                            startActivity(intent);
                        } else if (json.getStatus().equals("error")) {
                            RegisterResponse.ErrorsBean errors = json.getErrors();
                            if (errors.getFull_messages() != null && errors.getFull_messages().size() != 0) {
                                ToastUtil.toastShort(errors.getFull_messages().get(0));
                            } else if (errors.getMobile_no() != null && errors.getMobile_no().size() != 0) {
                                ToastUtil.toastShort(errors.getMobile_no().get(0));
                            } else if (errors.getPassword() != null && errors.getPassword().size() != 0) {
                                ToastUtil.toastShort(errors.getPassword().get(0));
                            } else if (errors.getRegister_type() != null && errors.getRegister_type().size() != 0) {
                                ToastUtil.toastShort(errors.getRegister_type().get(0));
                            }
                        } else {
                            if (StringUtils.isEmpty(social_token))
                                ToastUtil.toastShort(getString(R.string.please_verify_your_account_first));
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        if (StringUtils.isEmpty(social_token))
                            ToastUtil.toastShort(getString(R.string.please_verify_your_account_first));
                        loadingDismiss();
                    }
                });
    }
}
