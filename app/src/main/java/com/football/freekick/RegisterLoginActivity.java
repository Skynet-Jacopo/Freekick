package com.football.freekick;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.football.freekick.activity.registerlogin.RegisterPager1Activity;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterLoginActivity extends BaseActivity {

    @Bind(R.id.tv_facebook_register)
    TextView mTvFacebookRegister;
    @Bind(R.id.tv_email_register)
    TextView mTvEmailRegister;
    @Bind(R.id.tv_login)
    TextView mTvLogin;

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);
        mContext = RegisterLoginActivity.this;
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_facebook_register, R.id.tv_email_register, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_facebook_register:
                ToastUtil.toastShort("FaceBook註冊");
                break;
            case R.id.tv_email_register:
                ToastUtil.toastShort("電郵註冊");
                break;
            case R.id.tv_login:
                ToastUtil.toastShort("登錄");
                startActivity(new Intent(mContext,RegisterPager1Activity.class));
                break;
        }
    }
}
