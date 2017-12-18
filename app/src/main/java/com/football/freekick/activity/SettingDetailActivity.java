package com.football.freekick.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.utils.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 條約與條款,關於我們,聯繫我們
 */
public class SettingDetailActivity extends BaseActivity {

    @Bind(R.id.tv_back)
    TextView mTvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.web_view)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_detail);
        ButterKnife.bind(this);
        mTvBack.setTypeface(App.mTypeface);
        Intent intent = getIntent();
        String html = "";
        String terms_and_conditions = intent.getStringExtra("terms_and_conditions");
        String about_us = intent.getStringExtra("about_us");
        String contact_us = intent.getStringExtra("contact_us");
        String help = intent.getStringExtra("help");
        if (terms_and_conditions !=null){
            mTvTitle.setText(R.string.clause);
            html = terms_and_conditions;
        }else if (about_us !=null){
            mTvTitle.setText(R.string.about_us);
            html = about_us;
        }else if (contact_us !=null){
            mTvTitle.setText(R.string.contact_us);
            html = contact_us;
        }else if (help!=null){
            mTvTitle.setText(R.string.support);
            html = help;
        }
        initWebView(html);
    }

    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        finish();
    }

    private void initWebView(String html) {
        mWebView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
    }
}
