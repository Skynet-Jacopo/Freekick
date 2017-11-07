package com.football.freekick.activity.registerlogin;

import android.os.Bundle;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterPager2Activity extends BaseActivity {

    @Bind(R.id.tv_upload_pic)
    TextView mTvUploadPic;
    @Bind(R.id.tv_next)
    TextView mTvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pager2);
        ButterKnife.bind(this);
        mTvUploadPic.setTypeface(App.mTypeface);
    }
}
