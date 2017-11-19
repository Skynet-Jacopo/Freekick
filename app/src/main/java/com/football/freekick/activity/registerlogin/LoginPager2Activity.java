package com.football.freekick.activity.registerlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.SignInResponse;
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
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pager2);
        mContext = LoginPager2Activity.this;
        ButterKnife.bind(this);
        mEdtEmail.setText("huo@yopmail.com");
        mEdtPassWord.setText("123456");
        initView();
    }

    private void initView() {
        mTvBack.setTypeface(App.mTypeface);
    }

    @OnClick({R.id.fl_login_by_facebook, R.id.tv_login, R.id.tv_forget_pass_word, R.id.tv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_login_by_facebook:
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
        OkGo.post(Url.LOGINURL)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        SignInResponse sigin = gson.fromJson(s, SignInResponse.class);
                        if (sigin.getData() != null) {
                            SignInResponse.DataBean data = sigin.getData();
                            if (data.getLogin_fail() == 0) {//登錄成功

                                Headers headers = response.headers();
                                String access_token = headers.get("access-token");
                                String client = headers.get("client");
                                String uid = headers.get("uid");
                                String expiry = headers.get("expiry");
                                Logger.d(access_token + "   " + client + "   " + uid + "   " + expiry);
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
                                if (data.getUsername() == null) {//沒有用戶名則去註冊三頁
                                    Intent intent = new Intent(mContext, RegisterPager1Activity.class);
                                    intent.putExtra("email", StringUtils.getEditText(mEdtEmail));
                                    intent.putExtra("password", StringUtils.getEditText(mEdtPassWord));
                                    startActivity(intent);
                                } else {//有用戶名則直接進入應用
                                    startActivity(new Intent(mContext, OneTimePagerActivity.class));
                                }
                            } else {

                            }
                        } else {//data為null,登錄失敗
                            if (sigin.getErrors() != null) {
                                ToastUtil.toastShort(sigin.getErrors().get(0));
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
