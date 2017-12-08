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
import android.widget.TextView;

import com.football.freekick.R;
import com.football.freekick.adapter.MyFragmentAdapter;
import com.football.freekick.fragment.ChooseTimeFragment;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.StringUtils;
import com.football.freekick.utils.ToastUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import org.joda.time.DateTime;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 選擇時間界面
 */
public class ChooseTimeActivity extends AutoLayoutActivity {

    @Bind(R.id.tv_start_time)
    public TextView  mTvStartTime;
    @Bind(R.id.tv_end_time)
    public TextView  mTvEndTime;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewpager;
    @Bind(R.id.tv_confirm)
    TextView  mTvConfirm;
    private MyFragmentAdapter fragmentPagerAdapter;
    private List<Fragment>    listFragments;//定义要装fragment的列表
    private List<String>      listTitles; //tab名称列表

    private Context mContext;
    private String start_time;
    private String end_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);
        mContext = ChooseTimeActivity.this;
        ButterKnife.bind(this);
        start_time = getIntent().getStringExtra("start_time");
        end_time = getIntent().getStringExtra("end_time");
        mTvStartTime.setText(start_time);
        mTvEndTime.setText(end_time);
        initTabAndViewPager();
    }

    @OnClick(R.id.tv_confirm)
    public void onViewClicked() {
        start_time = StringUtils.getEditText(mTvStartTime);
        end_time = StringUtils.getEditText(mTvEndTime);
        if (JodaTimeUtil.compare(start_time,end_time)){
            Intent intent = getIntent();
            intent.putExtra("start_time", mTvStartTime.getText().toString().trim());
            intent.putExtra("end_time", mTvEndTime.getText().toString().trim());
            setResult(RESULT_OK, intent);
            finish();
        }else {
            ToastUtil.toastShort(getString(R.string.time_error));
        }
    }

    private void initTabAndViewPager() {
        listFragments = new ArrayList<>();
        Fragment fragment;
        Bundle   bundle;
        fragment = new ChooseTimeFragment();
        bundle = new Bundle();
        bundle.putString("state", "1");
        bundle.putString("start_time", start_time);
        fragment.setArguments(bundle);
        listFragments.add(fragment);
        fragment = new ChooseTimeFragment();
        bundle = new Bundle();
        bundle.putString("state", "2");
        bundle.putString("end_time", end_time);
        fragment.setArguments(bundle);
        listFragments.add(fragment);

        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        listTitles = new ArrayList<>();
        listTitles.add(getString(R.string.start_time));
        listTitles.add(getString(R.string.end_time));

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
        setIndicator(mContext, mTabLayout, 30, 30);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                ((ExchangeDetailFragment1) listFragments.get(position)).setRefresh();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static void setIndicator(Context context, TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field    tabStrip  = null;
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
