package com.football.freekick.activity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.Article;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.views.imageloader.ImageLoaderUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArticleActivity extends BaseActivity {

    @Bind(R.id.web_view)
    WebView mWebView;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.iv_pic)
    ImageView mIvPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        Article.ArticleBean article = getIntent().getParcelableExtra("Article");
        String image = article.getImage();
        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(image), mIvPic,R.drawable.icon_default);
        mTvTitle.setText(article.getSubject());
        initWebView(article.getContent());
    }

    private void initWebView(String html) {
        mWebView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
    }
}
