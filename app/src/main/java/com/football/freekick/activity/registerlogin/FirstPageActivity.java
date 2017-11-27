package com.football.freekick.activity.registerlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录注册页面
 */
public class FirstPageActivity extends BaseActivity {

    @Bind(R.id.tv_login)
    TextView mTvLogin;
    @Bind(R.id.tv_register)
    TextView mTvRegister;

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        mContext = FirstPageActivity.this;
        ButterKnife.bind(this);
        Logger.d( "getCountryNameValue--->"+App.mConfig.getCountryNameValue());
        Logger.d( "getLanguageValue--->"+App.mConfig.getLanguageValue());
        if (App.mConfig.getLanguageValue().equals("zh")){
            App.isChinese = true;
        }else {
            App.isChinese = false;
        }

    }

    @OnClick({R.id.tv_login, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
//                startActivity(new Intent(mContext,LoginPage1Activity.class));
                startActivity(new Intent(mContext,LoginPager2Activity.class));
                break;
            case R.id.tv_register:
                startActivity(new Intent(mContext,LoginPage1Activity.class));
                break;
        }
    }
}