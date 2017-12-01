package com.football.freekick.fragment;


import android.app.Activity;
import android.content.Context;
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
import com.football.freekick.adapter.MyFragmentAdapter;
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
public class MineFragment extends Fragment {


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
        initData();
    }

    private void initData() {
        if (mMatches != null) {
            mMatches.clear();
        }
        if (mListWait != null) {
            mListWait.clear();
        }
        if (mListMatch != null) {
            mListMatch.clear();
        }
        if (mListInvite != null) {
            mListInvite.clear();
        }
        OkGo.get(Url.MATCHES_COMING)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        MatchesComing json = gson.fromJson(s, MatchesComing.class);
                        mMatches.addAll(json.getMatches());
                        for (int i = 0; i < mMatches.size(); i++) {
                            for (int j = 0; j < App.mPitchesBeanList.size(); j++) {
                                if (mMatches.get(i).getPitch_id() == App.mPitchesBeanList.get(j).getId()){
                                    mMatches.get(i).setLocation(App.mPitchesBeanList.get(j).getLocation());
                                    mMatches.get(i).setPitch_name(App.mPitchesBeanList.get(j).getName());
                                }
                            }
                            if (mMatches.get(i).getStatus().equals("w")) {
                                mListWait.add(mMatches.get(i));
                            } else if (mMatches.get(i).getStatus().equals("m")) {
                                mListMatch.add(mMatches.get(i));
                            } else if (mMatches.get(i).getStatus().equals("i")) {
                                mListInvite.add(mMatches.get(i));
                            }
                        }
                        initTabAndViewPager();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
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
        switch (view.getId()) {
            case R.id.tv_friend:
                break;
            case R.id.tv_notice:
                break;
        }
    }

    private void initTabAndViewPager() {
        listFragments = new ArrayList<>();
        Fragment fragment;
        Bundle bundle;
        fragment = new MyMatchFragment1();
        bundle = new Bundle();
        bundle.putParcelableArrayList("mMatches", mListMatch);
        fragment.setArguments(bundle);
        listFragments.add(fragment);

        fragment = new MyMatchFragment1();
        bundle = new Bundle();
        bundle.putParcelableArrayList("mMatches", mListWait);
        fragment.setArguments(bundle);
        listFragments.add(fragment);

        fragment = new MyMatchFragment1();
        bundle = new Bundle();
        bundle.putParcelableArrayList("mMatches", mListInvite);
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

}
