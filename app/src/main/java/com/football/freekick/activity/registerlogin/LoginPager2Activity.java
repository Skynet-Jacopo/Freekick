package com.football.freekick.activity.registerlogin;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
                break;
            case R.id.tv_forget_pass_word:
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }
}
