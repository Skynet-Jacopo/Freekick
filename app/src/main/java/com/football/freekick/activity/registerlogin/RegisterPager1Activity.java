package com.football.freekick.activity.registerlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterPager1Activity extends BaseActivity {

    @Bind(R.id.tv_user_icon)
    TextView mTvUserIcon;
    @Bind(R.id.tv_phone_icon)
    TextView mTvPhoneIcon;
    @Bind(R.id.tv_next)
    TextView mTvNext;

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pager1);
        mContext = RegisterPager1Activity.this;
        ButterKnife.bind(this);

        mTvUserIcon.setTypeface(App.mTypeface);
        mTvPhoneIcon.setTypeface(App.mTypeface);
    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        ToastUtil.toastShort("下一步");
        startActivity(new Intent(mContext,RegisterPager2Activity.class));
    }
}
