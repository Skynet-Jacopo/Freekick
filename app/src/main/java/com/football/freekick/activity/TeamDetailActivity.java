package com.football.freekick.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.MainActivity;
import com.football.freekick.R;
import com.football.freekick.adapter.MyFragmentAdapter;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.Attention;
import com.football.freekick.beans.Follow;
import com.football.freekick.beans.Invite;
import com.football.freekick.beans.MatchesComing;
import com.football.freekick.beans.NoMatches;
import com.football.freekick.beans.TeamDetail;
import com.football.freekick.commons.CustomViewpager;
import com.football.freekick.fragment.TeamDetailFragment1;
import com.football.freekick.fragment.TeamDetailFragment2;
import com.football.freekick.http.Url;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.views.RoundImageView;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
    @Bind(R.id.iv_pic)
    ImageView mIvPic;
    //    @Bind(R.id.tv_one_word)
//    TextView mTvOneWord;
    @Bind(R.id.tl_top)
    RelativeLayout mTlTop;
    @Bind(R.id.tv_fight)
    TextView mTvFight;
    @Bind(R.id.tv_follow)
    TextView mTvFollow;
    @Bind(R.id.rl_parent)
    RelativeLayout mRlParent;
    @Bind(R.id.iv_team_logo)
    RoundImageView mIvTeamLogo;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    private ArrayList<MatchesComing.MatchesBean> mListWait = new ArrayList<>();
    private MyFragmentAdapter fragmentPagerAdapter;
    private List<Fragment> listFragments;//定义要装fragment的列表
    private List<String> listTitles; //tab名称列表

    private TeamDetailFragment1 mFragment1;//正常
    private TeamDetailFragment2 mFragment2;//正常

    Context mContext;
    private String team_id;
    private String owner_team_id;
    private TeamDetail.TeamBean mTeam;
    private boolean mIsFollowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        mContext = TeamDetailActivity.this;
        ButterKnife.bind(this);
        team_id = getIntent().getStringExtra("id");
        owner_team_id = PrefUtils.getString(App.APP_CONTEXT, "team_id", null);

        initView();
        initData(team_id);
    }

    private void initView() {
        mTvBack.setTypeface(App.mTypeface);
        mTvFriend.setTypeface(App.mTypeface);
        mTvNotice.setTypeface(App.mTypeface);
        if (team_id.equals(owner_team_id)) {
            mTvFollow.setVisibility(View.GONE);
            mTvFight.setVisibility(View.GONE);
            mTvTitle.setText(getString(R.string.mine));
        } else {
            mTvFollow.setVisibility(View.VISIBLE);
            mTvFight.setVisibility(View.VISIBLE);
            mTvTitle.setText(getString(R.string.team_detail));
        }
    }

    private void initData(String team_id) {
        loadingShow();
        String url = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "teams/" + team_id;
        Logger.d(url);
        OkGo.get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        TeamDetail json = gson.fromJson(s, TeamDetail.class);
                        mTeam = json.getTeam();
//                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mTeam.getImage().getUrl()), mIvPic, R
//                                .drawable.icon_default);
                        mTvTeamName.setText(mTeam.getTeam_name());
//                        mTvOneWord.setText(mTeam.getDistrict().getRegion().substring(0, 1));
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mTeam.getImage().getUrl()), mIvTeamLogo, R
                                .drawable.icon_default);
                        initTabAndViewPager();
                        getFollowedTeams();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                    }
                });
    }

    /**
     * 獲取已關注球隊與同區球隊做比較,得出關注與否
     */
    private void getFollowedTeams() {
        loadingShow();
        String urlAttention = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "users/" + team_id + "/followings";
        Logger.d(urlAttention);
        OkGo.get(urlAttention)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        Attention attention = gson.fromJson(s, Attention.class);
                        List<Attention.TeamsBean> attentionTeams = attention.getTeams();
                        for (int i = 0; i < attentionTeams.size(); i++) {
                            if (attentionTeams.get(i).getId() == Integer.parseInt(team_id)) {
                                mIsFollowed = true;
                                mTvFollow.setText(getString(R.string.unfollow));
                            } else {
//                                mIsFollowed = false;
//                                mTvFollow.setText(getString(R.string.follow));
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                    }
                });

    }

    @OnClick({R.id.tv_back, R.id.tv_friend, R.id.tv_notice, R.id.tv_fight, R.id.tv_follow})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_friend:
                intent.setClass(mContext, FriendActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_notice:
                intent.setClass(mContext, NoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_fight:
                invitePopup();
                break;
            case R.id.tv_follow:
                if (mIsFollowed) {
                    //取消關注
                    unfollow();
                } else {
                    //關注
                    follow();
                }
                break;
        }
    }


    private void initTabAndViewPager() {
        listFragments = new ArrayList<>();
        Fragment fragment;
        Bundle bundle;
        fragment = new TeamDetailFragment1();
        bundle = new Bundle();
        bundle.putSerializable("mTeam", mTeam);
        fragment.setArguments(bundle);
        listFragments.add(fragment);
        fragment = new TeamDetailFragment2();
        bundle = new Bundle();
        bundle.putString("team_id", team_id);
        fragment.setArguments(bundle);
        listFragments.add(fragment);

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


    /**
     * 取消關注
     */
    private void unfollow() {
        loadingShow();
        Logger.d(Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "teams/" + team_id +
                "/unfollow");
        OkGo.delete(Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "teams/" + team_id +
                "/unfollow")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        Follow follow = gson.fromJson(s, Follow.class);
                        if (follow.getSuccess() != null) {
                            ToastUtil.toastShort(follow.getSuccess());
                            mTvFollow.setText(getString(R.string.follow));
                            mIsFollowed = false;
                        } else if (follow.getErrors() != null) {
                            ToastUtil.toastShort(follow.getErrors());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                    }
                });
    }

    /**
     * 關注
     */
    private void follow() {
        loadingShow();
        Logger.d(Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "teams/" + team_id +
                "/follow");
        OkGo.post(Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "teams/" + team_id +
                "/follow")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        Follow follow = gson.fromJson(s, Follow.class);
                        if (follow.getSuccess() != null) {
                            ToastUtil.toastShort(follow.getSuccess());
                            mTvFollow.setText(getString(R.string.unfollow));
                            mIsFollowed = true;
                        } else if (follow.getErrors() != null) {
                            ToastUtil.toastShort(follow.getErrors());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                    }
                });
    }

    /**
     * 邀請球隊參與Pop
     */
    private void invitePopup() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_invite_to_not_matched, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        TextView tviconclose = (TextView) contentView.findViewById(R.id.tv_icon_close);
        tviconclose.setTypeface(App.mTypeface);
        TextView tvnewmatch = (TextView) contentView.findViewById(R.id.tv_new_match);
        TextView tvpartakethismatch = (TextView) contentView.findViewById(R.id.tv_partake_this_match);
        tviconclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        tvnewmatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                //去創建球賽
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("which", 1);
                intent.putExtra("team_id", mTeam.getId());
                intent.putExtra("team_name", mTeam.getTeam_name());
                intent.putExtra("team_url", mTeam.getImage().getUrl());
                startActivity(intent);
                finish();
            }
        });
        tvpartakethismatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyFirstMatch();//取我的未落實球賽的第一場
                popupWindow.dismiss();
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置好参数之后再show
        popupWindow.showAtLocation(mRlParent, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.5f);
    }

    /**
     * 取我的未落實球賽的第一場
     */
    private void getMyFirstMatch() {
        final String team_id = PrefUtils.getString(App.APP_CONTEXT, "team_id", null);
        loadingShow();
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "matches/coming";
        Logger.d(url);
        OkGo.get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        loadingDismiss();
                        Gson gson = new Gson();
                        if (!s.contains("[") && !s.contains("]")) {
                            NoMatches noMatches = gson.fromJson(s, NoMatches.class);
//                            ToastUtil.toastShort(noMatches.getMatches());

                        } else {
                            MatchesComing json = gson.fromJson(s, MatchesComing.class);
                            List<MatchesComing.MatchesBean> matches = json.getMatches();
                            if (matches != null && matches.size() > 0)
                                for (int i = 0; i < matches.size(); i++) {
                                    for (int j = 0; j < App.mPitchesBeanList.size(); j++) {
                                        if (matches.get(i).getPitch_id() == App.mPitchesBeanList.get(j).getId()) {
                                            matches.get(i).setLocation(App.mPitchesBeanList.get(j).getLocation());
                                            matches.get(i).setPitch_name(App.mPitchesBeanList.get(j).getName());
                                        }
                                    }
                                    if (matches.get(i).getStatus().equals("w")
                                            && !gson.toJson(matches.get(i).getJoin_matches()).contains
                                            ("confirmation_pending")
                                            && !gson.toJson(matches.get(i).getJoin_matches()).contains("invited")) {
                                        if (matches.get(i).getHome_team().getId() == Integer.parseInt(team_id)) {
                                            mListWait.add(matches.get(i));
                                            //是否要把主動參與的隊伍去除之後取第一條未落實球賽
                                        }
                                    }
                                }
                        }
                        if (mListWait.size() <= 0) {
                            ToastUtil.toastShort(getString(R.string.there_is_not_available_matches));
                        } else {
                            showPopupInvite();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                    }
                });
    }

    /**
     * 取第一條未落實球賽的pop
     */
    private void showPopupInvite() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_invite_to_match, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        TextView tvnewmatch = (TextView) contentView.findViewById(R.id.tv_new_match);
        TextView tvstate = (TextView) contentView.findViewById(R.id.tv_state);
        TextView tvLocation = (TextView) contentView.findViewById(R.id.tv_location);
        TextView tvTime = (TextView) contentView.findViewById(R.id.tv_time);
        TextView tvHomeName = (TextView) contentView.findViewById(R.id.tv_home_name);
        ImageView ivHomeLogo = (ImageView) contentView.findViewById(R.id.iv_home_logo);
        TextView tvDate = (TextView) contentView.findViewById(R.id.tv_date);
        MatchesComing.MatchesBean matchesBean = mListWait.get(0);
        tvHomeName.setText(matchesBean.getHome_team().getTeam_name());
        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(matchesBean.getHome_team().getImage().getUrl()),
                ivHomeLogo, R.drawable.icon_default);

        String date = JodaTimeUtil.getDate2(matchesBean.getPlay_start());
        tvDate.setText(date);
        String start = JodaTimeUtil.getTime2(matchesBean.getPlay_start());
        String end = JodaTimeUtil.getTime2(matchesBean.getPlay_end());
        tvTime.setText(start + " - " + end);
        tvLocation.setText(matchesBean.getPitch_name());
        tvstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                invite();
            }
        });
        tvnewmatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                //去創建球賽
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("which", 1);
                startActivity(intent);
                finish();
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置好参数之后再show
        popupWindow.showAtLocation(mRlParent, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.5f);
    }

    /**
     * 邀請
     */
    private void invite() {
        //http://api.freekick.hk/api/en/matches/invite
        loadingShow();
        String inviteUrl = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "matches/" + mListWait.get(0)
                .getId() + "/invite";
        JsonObject object = new JsonObject();
        object.addProperty("invite_team_id", team_id + "");
        Logger.d(inviteUrl);
        Logger.d(object.toString());
        OkGo.post(inviteUrl)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        Invite invite = gson.fromJson(s, Invite.class);
                        if (invite.getSuccess() != null) {
                            ToastUtil.toastShort(invite.getSuccess());
                        } else if (invite.getErrors() != null) {
                            ToastUtil.toastShort(invite.getErrors());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                        ToastUtil.toastShort("服務器數據異常,邀請失敗");
                    }
                });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
