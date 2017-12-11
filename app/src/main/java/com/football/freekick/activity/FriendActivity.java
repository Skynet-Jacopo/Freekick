package com.football.freekick.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 邀請頁面(應該叫分享界面吧?)
 */
public class FriendActivity extends BaseActivity {

    @Bind(R.id.tv_back)
    TextView     mTvBack;
    @Bind(R.id.tv_notice)
    TextView     mTvNotice;
    @Bind(R.id.ll_facebook)
    LinearLayout mLlFacebook;
    @Bind(R.id.ll_whatsapp)
    LinearLayout mLlWhatsapp;
    @Bind(R.id.ll_wechat)
    LinearLayout mLlWechat;
    @Bind(R.id.ll_short_msg)
    LinearLayout mLlShortMsg;
    @Bind(R.id.ll_facebook_wall)
    LinearLayout mLlFacebookWall;
    @Bind(R.id.ll_parent)
    LinearLayout mLlParent;

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        mContext = FriendActivity.this;
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvBack.setTypeface(App.mTypeface);
        mTvNotice.setTypeface(App.mTypeface);
    }

    @OnClick({R.id.tv_back, R.id.tv_notice, R.id.ll_facebook, R.id.ll_whatsapp, R.id.ll_wechat, R.id.ll_short_msg, R.id.ll_facebook_wall})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_notice:
                intent.setClass(mContext,NoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_facebook:
                break;
            case R.id.ll_whatsapp:
                break;
            case R.id.ll_wechat:
                break;
            case R.id.ll_short_msg:
                break;
            case R.id.ll_facebook_wall:
                break;
        }
    }
}
