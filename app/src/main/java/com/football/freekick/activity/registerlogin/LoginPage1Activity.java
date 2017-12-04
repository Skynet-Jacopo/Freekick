package com.football.freekick.activity.registerlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @OnClick({R.id.fl_login_by_facebook, R.id.tv_login_by_email, R.id.tv_back, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_login_by_facebook:
                mFacebook.performClick();
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
}
