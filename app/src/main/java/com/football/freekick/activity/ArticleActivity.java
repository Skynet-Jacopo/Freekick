package com.football.freekick.activity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.Article;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArticleActivity extends BaseActivity {

    @Bind(R.id.web_view)
    WebView mWebView;
    @Bind(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        Article.ArticleBean article = (Article.ArticleBean) getIntent().getSerializableExtra("Article");
        mTvTitle.setText(article.getSubject());
        initWebView(article.getContent());
    }

    private void initWebView(String html) {
        mWebView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
    }
}
