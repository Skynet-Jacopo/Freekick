package com.football.freekick.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.activity.FriendActivity;
import com.football.freekick.activity.NoticeActivity;
import com.football.freekick.adapter.MyFragmentAdapter;
import com.football.freekick.adapter.MyMatchAdapter0;
import com.football.freekick.app.BaseFragment;
import com.football.freekick.beans.Article;
import com.football.freekick.beans.MatchesComing;
import com.football.freekick.http.Url;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 我的球賽頁.
 */
public class MineFragment extends BaseFragment {


    @Bind(R.id.tv_friend)
    TextView mTvFriend;
    @Bind(R.id.tv_notice)
    TextView mTvNotice;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewpager;

    private MyFragmentAdapter fragmentPagerAdapter;
    private List<Fragment> listFragments;//定义要装fragment的列表
    private List<String> listTitles; //tab名称列表

    Context mContext;
    private ArrayList<MatchesComing.MatchesBean> mMatches = new ArrayList<>();
    private ArrayList<MatchesComing.MatchesBean> mListWait = new ArrayList<>();
    private ArrayList<MatchesComing.MatchesBean> mListMatch = new ArrayList<>();
    private ArrayList<MatchesComing.MatchesBean> mListInvite = new ArrayList<>();


    private ArrayList<Article.ArticleBean> news = new ArrayList<>();
    private ArrayList<Article.ArticleBean> point_of_view = new ArrayList<>();
    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initData();
        initDatas();//初始化動態和焦點
    }

    /**
     * 初始化動態和焦點
     */
    private void initDatas() {
        if (point_of_view != null) {
            point_of_view.clear();
        }
        if (news != null) {
            news.clear();
        }
        loadingShow();
        OkGo.get(Url.ARTICLES)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        Article article = gson.fromJson(s, Article.class);
                        List<Article.ArticleBean> articleList = article.getArticle();
                        for (int i = 0; i < articleList.size(); i++) {
                            if (articleList.get(i).getCategory().equals(Url.NEWS)) {
                                news.add(articleList.get(i));
                            } else if (articleList.get(i).getCategory().equals(Url.POINT_OF_VIEW)) {
                                point_of_view.add(articleList.get(i));
                            }
                        }
                        initTabAndViewPager();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_friend, R.id.tv_notice})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_friend:
                intent.setClass(mContext,FriendActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_notice:
                intent.setClass(mContext,NoticeActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initTabAndViewPager() {
        listFragments = new ArrayList<>();
        Fragment fragment;
        Bundle bundle;
        fragment = new MyMatchFragment0();
        bundle = new Bundle();
        bundle.putParcelableArrayList("news", news);
        bundle.putParcelableArrayList("point_of_view", point_of_view);
        fragment.setArguments(bundle);
        listFragments.add(fragment);

        fragment = new MyMatchFragment1();
        bundle = new Bundle();
        bundle.putParcelableArrayList("news", news);
        bundle.putParcelableArrayList("point_of_view", point_of_view);
        fragment.setArguments(bundle);
        listFragments.add(fragment);

        fragment = new MyMatchFragment2();
        bundle = new Bundle();
        bundle.putParcelableArrayList("news", news);
        bundle.putParcelableArrayList("point_of_view", point_of_view);
        fragment.setArguments(bundle);
        listFragments.add(fragment);

        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        listTitles = new ArrayList<>();
        listTitles.add(getString(R.string.implemented_match));
        listTitles.add(getString(R.string.not_mplement_match));
        listTitles.add(getString(R.string.invited_match));

        //为TabLayout添加tab名称
        for (String title : listTitles) {
            mTabLayout.addTab(mTabLayout.newTab().setText(title));
        }

        fragmentPagerAdapter = new MyFragmentAdapter(getChildFragmentManager(), listFragments, listTitles);
        //viewpager加载adapter
        mViewpager.setAdapter(fragmentPagerAdapter);
        //TabLayout加载viewpager
        mTabLayout.setupWithViewPager(mViewpager);
        mTabLayout.setTabsFromPagerAdapter(fragmentPagerAdapter);
        setIndicator(mContext, mTabLayout, 20, 20);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static void setIndicator(Context context, TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout ll_tab = null;
        try {
            ll_tab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) (getDisplayMetrics(context).density * leftDip);
        int right = (int) (getDisplayMetrics(context).density * rightDip);

        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams
                    .MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric;
    }

    public static float getPXfromDP(float value, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                context.getResources().getDisplayMetrics());
    }
    public void setRefreshFragment1(){
        ((MyMatchFragment0)listFragments.get(0)).setRefresh();
    }
}
