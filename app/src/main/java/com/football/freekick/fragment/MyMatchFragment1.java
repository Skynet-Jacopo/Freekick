package com.football.freekick.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.activity.ArticleActivity;
import com.football.freekick.adapter.MyMatchAdapter;
import com.football.freekick.baseadapter.ViewHolder;
import com.football.freekick.baseadapter.recyclerview.CommonAdapter;
import com.football.freekick.baseadapter.recyclerview.OnItemClickListener;
import com.football.freekick.beans.Article;
import com.football.freekick.http.Url;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMatchFragment1 extends LazyLoadFragment {



    @Bind(R.id.recycler_my_match)
    RecyclerView mRecyclerMyMatch;
    @Bind(R.id.tv_icon_lines)
    TextView mTvIconLines;
    @Bind(R.id.recycler_lines)
    RecyclerView mRecyclerLines;
    @Bind(R.id.tv_icon_focus)
    TextView mTvIconFocus;
    @Bind(R.id.recycler_focus)
    RecyclerView mRecyclerFocus;

    /**
     * 标志位，标志已经初始化完成
     */
    private boolean      isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean      mHasLoadedOnce;
    private LinearLayout mFragmentView;


    private List<Article.ArticleBean> news = new ArrayList<>();
    private List<Article.ArticleBean> point_of_view = new ArrayList<>();
    private Context mContext;
    private List<String> datas = new ArrayList<>();
    private String picUrl = "http://www.cnr.cn/china/xwwgf/201111/W020111128658021231674.jpg";
    private CommonAdapter mLineAdapter;
    private CommonAdapter mFocusAdapter;

    public MyMatchFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        if (mFragmentView == null) {
            mFragmentView = (LinearLayout) inflater.inflate(R.layout.fragment_my_match,
                    container, false);
            ButterKnife.bind(this, mFragmentView);
            isPrepared = true;
            lazyLoad();
        }
        //共用一个视图，需要先移除以前的view
        ViewGroup parent = (ViewGroup) mFragmentView.getParent();
        if (parent != null) parent.removeView(mFragmentView);
        ButterKnife.bind(this, mFragmentView);
        return mFragmentView;

    }
    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            //已加载的fragment不需要重新加载
            return;
        }
        initView();
        initData();
        initMyMatches();
        initLines();
        initFocus();
    }

    private void initFocus() {
        GridLayoutManager manager = new GridLayoutManager(mContext,3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerFocus.setLayoutManager(manager);
        mRecyclerFocus.setNestedScrollingEnabled(false);
        mFocusAdapter = new CommonAdapter<Article.ArticleBean>(mContext, R.layout.item_focus,point_of_view) {

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, Article.ArticleBean articleBean) {
                ImageView ivPic = holder.getView(R.id.iv_pic);
                ImageLoaderUtils.displayImage(articleBean.getImage(),ivPic);
                holder.setText(R.id.tv_title,articleBean.getSubject());
            }
        };
        mFocusAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Intent intent = new Intent(mContext, ArticleActivity.class);
                intent.putExtra("Article",point_of_view.get(position));
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        mRecyclerFocus.setAdapter(mFocusAdapter);
    }

    private void initLines() {
        GridLayoutManager manager = new GridLayoutManager(mContext,3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerLines.setLayoutManager(manager);
        mRecyclerLines.setNestedScrollingEnabled(false);
        mLineAdapter = new CommonAdapter<Article.ArticleBean>(mContext, R.layout.item_lines,news) {

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, Article.ArticleBean articleBean) {
                ImageView ivPic = holder.getView(R.id.iv_pic);
                ImageLoaderUtils.displayImage(articleBean.getImage(),ivPic);
                holder.setText(R.id.tv_title,articleBean.getSubject());
                holder.setText(R.id.tv_date,articleBean.getCreated_at());
            }
        };
        mLineAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Intent intent = new Intent(mContext, ArticleActivity.class);
                intent.putExtra("Article",news.get(position));
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        mRecyclerLines.setAdapter(mLineAdapter);
    }

    private void initMyMatches() {
        mRecyclerMyMatch.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerMyMatch.setNestedScrollingEnabled(false);
        final MyMatchAdapter matchAdapter = new MyMatchAdapter(datas,mContext);
        mRecyclerMyMatch.setAdapter(matchAdapter);
        matchAdapter.setClick(new MyMatchAdapter.Click() {
            @Override
            public void Clike(int state, View view, int position) {
                switch (state){
                    case 1://刪除
                        datas.remove(position);
                        matchAdapter.notifyDataSetChanged();
                        break;
                    case 2://點擊item
                        break;
                }
            }
        });
    }

    private void initData() {
        mHasLoadedOnce = true;
        for (int i = 0; i < 3; i++) {
            datas.add("我是數據"+i);
        }
        if (point_of_view != null) {
            point_of_view.clear();
        }
        if (news != null) {
            news.clear();
        }
        OkGo.get(Url.ARTICLES)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Article article = gson.fromJson(s, Article.class);
                        List<Article.ArticleBean> articleList = article.getArticle();
                        for (int i = 0; i < articleList.size(); i++) {
                            if (articleList.get(i).getCategory().equals(Url.NEWS)){
                                news.add(articleList.get(i));
                            }else if (articleList.get(i).getCategory().equals(Url.POINT_OF_VIEW)){
                                point_of_view.add(articleList.get(i));
                            }
                        }
                        mLineAdapter.notifyDataSetChanged();
                        mFocusAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }

    private void initView() {
        mTvIconFocus.setTypeface(App.mTypeface);
        mTvIconLines.setTypeface(App.mTypeface);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
