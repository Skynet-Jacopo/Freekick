package com.football.freekick.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.R;
import com.football.freekick.adapter.MyFragmentAdapter;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.commons.CustomViewpager;
import com.football.freekick.fragment.MyMatchFragment1;
import com.football.freekick.fragment.TeamDetailFragment1;
import com.football.freekick.fragment.TeamDetailFragment2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TeamDetailActivity extends BaseActivity {

    @Bind(R.id.tv_back)
    TextView mTvBack;
    @Bind(R.id.tv_friend)
    TextView mTvFriend;
    @Bind(R.id.tv_notice)
    TextView mTvNotice;
    @Bind(R.id.tv_team_name)
    TextView mTvTeamName;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    CustomViewpager mViewpager;

    private MyFragmentAdapter fragmentPagerAdapter;
    private List<Fragment> listFragments;//定义要装fragment的列表
    private List<String>      listTitles; //tab名称列表

    private TeamDetailFragment1 mFragment1;//正常
    private TeamDetailFragment2 mFragment2;//正常

    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        mContext = TeamDetailActivity.this;
        ButterKnife.bind(this);
        initTabAndViewPager();
    }

    @OnClick({R.id.tv_back, R.id.tv_friend, R.id.tv_notice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                break;
            case R.id.tv_friend:
                break;
            case R.id.tv_notice:
                break;
        }
    }


    private void initTabAndViewPager() {
        mFragment1 = new TeamDetailFragment1();
        mFragment2 = new TeamDetailFragment2();

        listFragments = new ArrayList<>();
        listFragments.add(mFragment1);
        listFragments.add(mFragment2);

        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        listTitles = new ArrayList<>();
        listTitles.add(getString(R.string.basic_information));
        listTitles.add(getString(R.string.finished_match));

        //为TabLayout添加tab名称
        for (String title : listTitles) {
            mTabLayout.addTab(mTabLayout.newTab().setText(title));
        }

        fragmentPagerAdapter = new MyFragmentAdapter(getSupportFragmentManager(), listFragments, listTitles);
        //viewpager加载adapter
        mViewpager.setAdapter(fragmentPagerAdapter);
        //TabLayout加载viewpager
        mTabLayout.setupWithViewPager(mViewpager);
        mTabLayout.setTabsFromPagerAdapter(fragmentPagerAdapter);
        setIndicator(mContext, mTabLayout, 40, 40);
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
        Field tabStrip  = null;
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

        int left  = (int) (getDisplayMetrics(context).density * leftDip);
        int right = (int) (getDisplayMetrics(context).density * rightDip);

        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
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
