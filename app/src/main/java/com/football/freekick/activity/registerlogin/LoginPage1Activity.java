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
import com.football.freekick.utils.StringUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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

        mFacebook.setReadPermissions("email");
        // If using in a fragment
//        mFacebook.setFragment(this);
        mCallbackManager = CallbackManager.Factory.create();

        mFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Gson gson = new Gson();
                mEdtLog.setText(gson.toJson(loginResult));
                Logger.json(gson.toJson(loginResult));
                getLoginInfo(loginResult.getAccessToken());
                List<String> str = new ArrayList<String>();
                str.add("public_profile");

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
                    LoginManager.getInstance().logInWithReadPermissions(LoginPage1Activity.this,Arrays.asList("public_profile"));
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,picture,locale,updated_time,timezone," +
                "age_range,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
