package com.football.freekick.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;

import java.io.File;

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

    @OnClick({R.id.ll_parent,R.id.tv_invite,R.id.tv_back, R.id.tv_notice, R.id.ll_facebook, R.id.ll_whatsapp, R.id.ll_wechat, R.id.ll_short_msg, R.id.ll_facebook_wall})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.ll_parent:
                shareMsg(getString(R.string.invite), "", "https://wwww.freekick.hk", null);
                break;
            case R.id.tv_invite:
                shareMsg(getString(R.string.invite), "", "https://wwww.freekick.hk", null);
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

    /**
     * 分享功能(文字圖片無法調和)
     * 微信朋友圈:用圖片可加文字,但此時其他三方應用就只有圖片,沒有文字,故而
     *
     * @param activityTitle Activity的名字
     * @param msgTitle      消息标题
     * @param msgText       消息内容
     * @param imgPath       图片路径，不分享图片则传null
     */
    public void shareMsg(String activityTitle, String msgTitle, String msgText,
                         String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/*");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }
}
