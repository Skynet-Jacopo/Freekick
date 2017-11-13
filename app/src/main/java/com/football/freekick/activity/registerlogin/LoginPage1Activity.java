package com.football.freekick.activity.registerlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;

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
    }

    @OnClick({R.id.fl_login_by_facebook, R.id.tv_login_by_email, R.id.tv_back, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_login_by_facebook:
                break;
            case R.id.tv_login_by_email:
                startActivity(new Intent(mContext,RegisterByEmailActivity.class));
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_login://已經是會員了 登入
                startActivity(new Intent(mContext,LoginPager2Activity.class));
                break;
        }
    }
}
