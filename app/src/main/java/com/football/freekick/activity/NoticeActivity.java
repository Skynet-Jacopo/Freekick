package com.football.freekick.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.adapter.MyFragmentAdapter;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.fragment.MessageFragment;
import com.football.freekick.fragment.MyMatchFragment0;
import com.football.freekick.fragment.MyMatchFragment1;
import com.football.freekick.fragment.MyMatchFragment2;
import com.football.freekick.fragment.NoticeFragment;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息頁
 */
public class NoticeActivity extends BaseActivity {

    @Bind(R.id.tv_back)
    TextView mTvBack;
    @Bind(R.id.tl_top)
    RelativeLayout mTlTop;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewpager;
    @Bind(R.id.ll_parent)
    LinearLayout mLlParent;
    private MyFragmentAdapter fragmentPagerAdapter;
    private List<Fragment> listFragments;//定义要装fragment的列表
    private List<String> listTitles; //tab名称列表

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        mContext = NoticeActivity.this;
        ButterKnife.bind(this);
        mTvBack.setTypeface(App.mTypeface);
        StringBuilder builder = new StringBuilder();
        if (getIntent().getExtras() != null) {
            String message = getIntent().getStringExtra("Message");
            Logger.d("Message"+message);
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                builder.append("键：" + key + "-值：" + value + "\n");
            }
            Logger.d("收到的消息111--->"+builder.toString());
        }
        initTabAndViewPager();
    }
    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        StringBuilder builder = new StringBuilder();
        if (intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                Object value = intent.getExtras().get(key);
                builder.append("键：" + key + "-值：" + value + "\n");
            }
            Logger.d("收到的消息--->"+builder.toString());
        }
    }

    private void initTabAndViewPager() {
        listFragments = new ArrayList<>();
        Fragment fragment;
        Bundle bundle;
        fragment = new MessageFragment();
        bundle = new Bundle();
        fragment.setArguments(bundle);
        listFragments.add(fragment);

        fragment = new NoticeFragment();
        bundle = new Bundle();
        fragment.setArguments(bundle);
        listFragments.add(fragment);

        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        listTitles = new ArrayList<>();
        listTitles.add(getString(R.string.message));
        listTitles.add(getString(R.string.notification));

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
}
