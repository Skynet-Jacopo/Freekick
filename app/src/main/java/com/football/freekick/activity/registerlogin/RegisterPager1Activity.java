package com.football.freekick.activity.registerlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.RegisterResponse;
import com.football.freekick.beans.SignInResponse;
import com.football.freekick.http.Url;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.SPUtil;
import com.football.freekick.utils.StringUtils;
import com.football.freekick.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.orhanobut.logger.Logger;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pager1);
        mContext = RegisterPager1Activity.this;
        ButterKnife.bind(this);
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
//        next();
        putNameAndPhone();
//        startActivity(new Intent(mContext, RegisterPager2Activity.class));
    }

    /**
     * 再請求一遍登錄接口
     */
//    private void next() {
//
//        JsonObject object = new JsonObject();
//        JsonObject object1 = new JsonObject();
////        object1.addProperty("email", mEmail);
//        object1.addProperty("email", mEmail);
////        object1.addProperty("password", mPassword);
//        object1.addProperty("password", mPassword);
//        object.add("user", object1);
//        Logger.json(object.toString());
//        OkGo.post(Url.LOGINURL)
//                .upJson(object.toString())
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        Logger.json(s);
//                        Gson gson = new Gson();
//                        SignInResponse sigin = gson.fromJson(s, SignInResponse.class);
//                        if (sigin.getData() != null) {
//                            SignInResponse.DataBean data = sigin.getData();
//                            if (data.getLogin_fail() == 0) {//登錄成功
//                                Headers headers = response.headers();
//                                String access_token = headers.get("access-token");
//                                String client = headers.get("client");
//                                String uid = headers.get("uid");
//                                String expiry = headers.get("expiry");
//                                Logger.d(access_token + "   " + client + "   " + uid + "   " + expiry);
////                                if (PrefUtils.getBoolean(App.APP_CONTEXT, "isFirstLogin", false)) {
//                                HttpHeaders header = new HttpHeaders();
//                                header.put("access-token", access_token);
//                                header.put("client", client);
//                                header.put("uid", uid);
//                                header.put("expiry", expiry);
//                                OkGo.getInstance().addCommonHeaders(header);
//                                PrefUtils.putString(App.APP_CONTEXT, "access_token", access_token);
//                                PrefUtils.putString(App.APP_CONTEXT, "client", client);
//                                PrefUtils.putString(App.APP_CONTEXT, "uid", uid);
//                                PrefUtils.putString(App.APP_CONTEXT, "expiry", expiry);
////                                } else {
////                                    PrefUtils.putBoolean(App.APP_CONTEXT, "isFirstLogin", true);
////                                }
//
//
//                                putNameAndPhone();
//                            } else {
//
//                            }
//                        } else {//data為null,登錄失敗
//                            if (sigin.getErrors() != null) {
//                                ToastUtil.toastShort(sigin.getErrors().get(0));
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(Call call, Response response, Exception e) {
//                        super.onError(call, response, e);
//                        Logger.d(e.getMessage());
//                    }
//                });
//    }

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
        OkGo.put(Url.REGISTER)
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
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        ToastUtil.toastShort("請在郵箱驗證登錄");
                        finish();
                        loadingDismiss();
                    }
                });
    }
}
