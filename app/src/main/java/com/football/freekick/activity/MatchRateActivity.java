package com.football.freekick.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.MainActivity;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.Advertisements;
import com.football.freekick.beans.MatchDetail;
import com.football.freekick.beans.RateResponse;
import com.football.freekick.beans.Ratings;
import com.football.freekick.http.Url;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 球賽內容頁(打分),只有在接收到推送消息后進入,打完分后再不可見
 */
public class MatchRateActivity extends BaseActivity {

    @Bind(R.id.tv_back)
    TextView mTvBack;
    @Bind(R.id.tv_friend)
    TextView mTvFriend;
    @Bind(R.id.tv_notice)
    TextView mTvNotice;
    @Bind(R.id.ll_top)
    LinearLayout mLlTop;
    @Bind(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.tv_icon_location)
    TextView mTvIconLocation;
    @Bind(R.id.ll_location)
    LinearLayout mLlLocation;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.iv_1_1)
    ImageView mIv11;
    @Bind(R.id.iv_1_2)
    ImageView mIv12;
    @Bind(R.id.iv_1_3)
    ImageView mIv13;
    @Bind(R.id.iv_1_4)
    ImageView mIv14;
    @Bind(R.id.iv_1_5)
    ImageView mIv15;
    @Bind(R.id.iv_2_1)
    ImageView mIv21;
    @Bind(R.id.iv_2_2)
    ImageView mIv22;
    @Bind(R.id.iv_2_3)
    ImageView mIv23;
    @Bind(R.id.iv_2_4)
    ImageView mIv24;
    @Bind(R.id.iv_2_5)
    ImageView mIv25;
    @Bind(R.id.iv_3_1)
    ImageView mIv31;
    @Bind(R.id.iv_3_2)
    ImageView mIv32;
    @Bind(R.id.iv_3_3)
    ImageView mIv33;
    @Bind(R.id.iv_3_4)
    ImageView mIv34;
    @Bind(R.id.iv_3_5)
    ImageView mIv35;
    @Bind(R.id.iv_4_1)
    ImageView mIv41;
    @Bind(R.id.iv_4_2)
    ImageView mIv42;
    @Bind(R.id.iv_4_3)
    ImageView mIv43;
    @Bind(R.id.iv_4_4)
    ImageView mIv44;
    @Bind(R.id.iv_4_5)
    ImageView mIv45;
    @Bind(R.id.iv_5_1)
    ImageView mIv51;
    @Bind(R.id.iv_5_2)
    ImageView mIv52;
    @Bind(R.id.iv_5_3)
    ImageView mIv53;
    @Bind(R.id.iv_5_4)
    ImageView mIv54;
    @Bind(R.id.iv_5_5)
    ImageView mIv55;
    @Bind(R.id.tv_home_num)
    TextView mTvHomeNum;
    @Bind(R.id.tv_visitor_num)
    TextView mTvVisitorNum;
    @Bind(R.id.tv_confirm)
    TextView mTvConfirm;
    @Bind(R.id.tv_location)
    TextView mTvLocation;
    @Bind(R.id.iv_top_1)
    ImageView mIvTop1;
    @Bind(R.id.iv_top_2)
    ImageView mIvTop2;
    @Bind(R.id.iv_left_1)
    ImageView mIvLeft1;
    @Bind(R.id.iv_left_2)
    ImageView mIvLeft2;
    @Bind(R.id.iv_left_3)
    ImageView mIvLeft3;
    @Bind(R.id.iv_right_1)
    ImageView mIvRight1;
    @Bind(R.id.iv_right_2)
    ImageView mIvRight2;
    @Bind(R.id.iv_right_3)
    ImageView mIvRight3;
    @Bind(R.id.iv_bottom_1)
    ImageView mIvBottom1;
    @Bind(R.id.iv_bottom_2)
    ImageView mIvBottom2;
    private Context mContext;
    private String match_id;
    private MatchDetail.MatchBean mMatch;
    private List<Advertisements.AdvertisementsBean> mAdvertisementsList;//广告列表
    //        {"rating": {"team_id": "10","attack": "5","defense": "4","on_time": "4","technic": "4","personality":
    // "5"}}
    private String team_id;
    private int attack;
    private int defense;
    private int on_time;
    private int technic;
    private int personality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_rate);
        mContext = MatchRateActivity.this;
        ButterKnife.bind(this);
        match_id = getIntent().getStringExtra("match_id");
        team_id = PrefUtils.getString(App.APP_CONTEXT, "team_id", null);
        initView();
        initMatchData();
        getMatchRatings();
    }

    private void getMatchRatings() {
//        http://api.freekick.hk/api/en/matches/<matchID>/match_ratings
//        http://api.freekick.hk/api/zh_Hk/matches/<matchID>/match_ratings
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "matches/" + match_id + "/match_ratings";
        Logger.d(url);
        OkGo.get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        Ratings fromJson = gson.fromJson(s, Ratings.class);
                        List<Ratings.RatingsBean> ratings = fromJson.getRatings();
                        if (ratings.size() <= 0) {
                            mTvConfirm.setText(getString(R.string.confirm));
                            mTvConfirm.setClickable(true);
                            initDefultView();
                        } else {
                            mTvConfirm.setText(getString(R.string.confirm));
                            mTvConfirm.setClickable(true);
                            for (int i = 0; i < ratings.size(); i++) {
                                if (ratings.get(i).getTeam_id() == Integer.parseInt(team_id)) {
                                    mTvConfirm.setText(R.string.rate_already);
                                    mTvConfirm.setClickable(false);
                                    refreshView(ratings.get(i));
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        loadingDismiss();
                        Logger.d(e.getMessage());
                    }
                });
    }

    /**
     * 默認狀態
     */
    private void initDefultView() {
        attack = 5;
        defense = 5;
        on_time = 5;
        technic = 5;
        personality = 5;
        mIv11.setImageResource(R.drawable.icon_star_selected);
        mIv12.setImageResource(R.drawable.icon_star_selected);
        mIv13.setImageResource(R.drawable.icon_star_selected);
        mIv14.setImageResource(R.drawable.icon_star_selected);
        mIv15.setImageResource(R.drawable.icon_star_selected);
        mIv21.setImageResource(R.drawable.icon_star_selected);
        mIv22.setImageResource(R.drawable.icon_star_selected);
        mIv23.setImageResource(R.drawable.icon_star_selected);
        mIv24.setImageResource(R.drawable.icon_star_selected);
        mIv25.setImageResource(R.drawable.icon_star_selected);
        mIv31.setImageResource(R.drawable.icon_star_selected);
        mIv32.setImageResource(R.drawable.icon_star_selected);
        mIv33.setImageResource(R.drawable.icon_star_selected);
        mIv34.setImageResource(R.drawable.icon_star_selected);
        mIv35.setImageResource(R.drawable.icon_star_selected);
        mIv41.setImageResource(R.drawable.icon_star_selected);
        mIv42.setImageResource(R.drawable.icon_star_selected);
        mIv43.setImageResource(R.drawable.icon_star_selected);
        mIv44.setImageResource(R.drawable.icon_star_selected);
        mIv45.setImageResource(R.drawable.icon_star_selected);
        mIv51.setImageResource(R.drawable.icon_star_selected);
        mIv52.setImageResource(R.drawable.icon_star_selected);
        mIv53.setImageResource(R.drawable.icon_star_selected);
        mIv54.setImageResource(R.drawable.icon_star_selected);
        mIv55.setImageResource(R.drawable.icon_star_selected);
    }

    /**
     * 顯示打分記錄
     *
     * @param ratingsBean
     */
    private void refreshView(Ratings.RatingsBean ratingsBean) {
        int attack = ratingsBean.getAttack();
        int defense = ratingsBean.getDefense();
        int on_time = ratingsBean.getOn_time();
        int technic = ratingsBean.getTechnic();
        int personality = ratingsBean.getPersonality();
        switch (attack) {
            case 1:
                mIv11.setImageResource(R.drawable.icon_star_selected);
                mIv12.setImageResource(R.drawable.icon_star_unselected);
                mIv13.setImageResource(R.drawable.icon_star_unselected);
                mIv14.setImageResource(R.drawable.icon_star_unselected);
                mIv15.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 2:
                mIv11.setImageResource(R.drawable.icon_star_selected);
                mIv12.setImageResource(R.drawable.icon_star_selected);
                mIv13.setImageResource(R.drawable.icon_star_unselected);
                mIv14.setImageResource(R.drawable.icon_star_unselected);
                mIv15.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 3:
                mIv11.setImageResource(R.drawable.icon_star_selected);
                mIv12.setImageResource(R.drawable.icon_star_selected);
                mIv13.setImageResource(R.drawable.icon_star_selected);
                mIv14.setImageResource(R.drawable.icon_star_unselected);
                mIv15.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 4:
                mIv11.setImageResource(R.drawable.icon_star_selected);
                mIv12.setImageResource(R.drawable.icon_star_selected);
                mIv13.setImageResource(R.drawable.icon_star_selected);
                mIv14.setImageResource(R.drawable.icon_star_selected);
                mIv15.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 5:
                mIv11.setImageResource(R.drawable.icon_star_selected);
                mIv12.setImageResource(R.drawable.icon_star_selected);
                mIv13.setImageResource(R.drawable.icon_star_selected);
                mIv14.setImageResource(R.drawable.icon_star_selected);
                mIv15.setImageResource(R.drawable.icon_star_selected);
                break;
        }
        switch (defense) {
            case 1:
                mIv21.setImageResource(R.drawable.icon_star_selected);
                mIv22.setImageResource(R.drawable.icon_star_unselected);
                mIv23.setImageResource(R.drawable.icon_star_unselected);
                mIv24.setImageResource(R.drawable.icon_star_unselected);
                mIv25.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 2:
                mIv21.setImageResource(R.drawable.icon_star_selected);
                mIv22.setImageResource(R.drawable.icon_star_selected);
                mIv23.setImageResource(R.drawable.icon_star_unselected);
                mIv24.setImageResource(R.drawable.icon_star_unselected);
                mIv25.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 3:
                mIv21.setImageResource(R.drawable.icon_star_selected);
                mIv22.setImageResource(R.drawable.icon_star_selected);
                mIv23.setImageResource(R.drawable.icon_star_selected);
                mIv24.setImageResource(R.drawable.icon_star_unselected);
                mIv25.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 4:
                mIv21.setImageResource(R.drawable.icon_star_selected);
                mIv22.setImageResource(R.drawable.icon_star_selected);
                mIv23.setImageResource(R.drawable.icon_star_selected);
                mIv24.setImageResource(R.drawable.icon_star_selected);
                mIv25.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 5:
                mIv21.setImageResource(R.drawable.icon_star_selected);
                mIv22.setImageResource(R.drawable.icon_star_selected);
                mIv23.setImageResource(R.drawable.icon_star_selected);
                mIv24.setImageResource(R.drawable.icon_star_selected);
                mIv25.setImageResource(R.drawable.icon_star_selected);
                break;
        }
        switch (on_time) {

            case 1:
                mIv31.setImageResource(R.drawable.icon_star_selected);
                mIv32.setImageResource(R.drawable.icon_star_unselected);
                mIv33.setImageResource(R.drawable.icon_star_unselected);
                mIv34.setImageResource(R.drawable.icon_star_unselected);
                mIv35.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 2:
                mIv31.setImageResource(R.drawable.icon_star_selected);
                mIv32.setImageResource(R.drawable.icon_star_selected);
                mIv33.setImageResource(R.drawable.icon_star_unselected);
                mIv34.setImageResource(R.drawable.icon_star_unselected);
                mIv35.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 3:
                mIv31.setImageResource(R.drawable.icon_star_selected);
                mIv32.setImageResource(R.drawable.icon_star_selected);
                mIv33.setImageResource(R.drawable.icon_star_selected);
                mIv34.setImageResource(R.drawable.icon_star_unselected);
                mIv35.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 4:
                mIv31.setImageResource(R.drawable.icon_star_selected);
                mIv32.setImageResource(R.drawable.icon_star_selected);
                mIv33.setImageResource(R.drawable.icon_star_selected);
                mIv34.setImageResource(R.drawable.icon_star_selected);
                mIv35.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 5:
                mIv31.setImageResource(R.drawable.icon_star_selected);
                mIv32.setImageResource(R.drawable.icon_star_selected);
                mIv33.setImageResource(R.drawable.icon_star_selected);
                mIv34.setImageResource(R.drawable.icon_star_selected);
                mIv35.setImageResource(R.drawable.icon_star_selected);
                break;
        }
        switch (technic) {
            case 1:
                mIv41.setImageResource(R.drawable.icon_star_selected);
                mIv42.setImageResource(R.drawable.icon_star_unselected);
                mIv43.setImageResource(R.drawable.icon_star_unselected);
                mIv44.setImageResource(R.drawable.icon_star_unselected);
                mIv45.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 2:
                mIv41.setImageResource(R.drawable.icon_star_selected);
                mIv42.setImageResource(R.drawable.icon_star_selected);
                mIv43.setImageResource(R.drawable.icon_star_unselected);
                mIv44.setImageResource(R.drawable.icon_star_unselected);
                mIv45.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 3:
                mIv41.setImageResource(R.drawable.icon_star_selected);
                mIv42.setImageResource(R.drawable.icon_star_selected);
                mIv43.setImageResource(R.drawable.icon_star_selected);
                mIv44.setImageResource(R.drawable.icon_star_unselected);
                mIv45.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 4:
                mIv41.setImageResource(R.drawable.icon_star_selected);
                mIv42.setImageResource(R.drawable.icon_star_selected);
                mIv43.setImageResource(R.drawable.icon_star_selected);
                mIv44.setImageResource(R.drawable.icon_star_selected);
                mIv45.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 5:
                mIv41.setImageResource(R.drawable.icon_star_selected);
                mIv42.setImageResource(R.drawable.icon_star_selected);
                mIv43.setImageResource(R.drawable.icon_star_selected);
                mIv44.setImageResource(R.drawable.icon_star_selected);
                mIv45.setImageResource(R.drawable.icon_star_selected);
                break;
        }
        switch (personality) {
            case 1:
                mIv51.setImageResource(R.drawable.icon_star_selected);
                mIv52.setImageResource(R.drawable.icon_star_unselected);
                mIv53.setImageResource(R.drawable.icon_star_unselected);
                mIv54.setImageResource(R.drawable.icon_star_unselected);
                mIv55.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 2:
                mIv51.setImageResource(R.drawable.icon_star_selected);
                mIv52.setImageResource(R.drawable.icon_star_selected);
                mIv53.setImageResource(R.drawable.icon_star_unselected);
                mIv54.setImageResource(R.drawable.icon_star_unselected);
                mIv55.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 3:
                mIv51.setImageResource(R.drawable.icon_star_selected);
                mIv52.setImageResource(R.drawable.icon_star_selected);
                mIv53.setImageResource(R.drawable.icon_star_selected);
                mIv54.setImageResource(R.drawable.icon_star_unselected);
                mIv55.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 4:
                mIv51.setImageResource(R.drawable.icon_star_selected);
                mIv52.setImageResource(R.drawable.icon_star_selected);
                mIv53.setImageResource(R.drawable.icon_star_selected);
                mIv54.setImageResource(R.drawable.icon_star_selected);
                mIv55.setImageResource(R.drawable.icon_star_unselected);
                break;
            case 5:
                mIv51.setImageResource(R.drawable.icon_star_selected);
                mIv52.setImageResource(R.drawable.icon_star_selected);
                mIv53.setImageResource(R.drawable.icon_star_selected);
                mIv54.setImageResource(R.drawable.icon_star_selected);
                mIv55.setImageResource(R.drawable.icon_star_selected);
                break;
        }
    }

    private void initMatchData() {
        loadingShow();
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "matches/";//+matchID
        Logger.d(url + match_id);
        OkGo.get(url + match_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        MatchDetail fromJson = gson.fromJson(s, MatchDetail.class);
                        if (fromJson.getMatch() != null) {
                            mMatch = fromJson.getMatch();
                            for (int i = 0; i < App.mPitchesBeanList.size(); i++) {
                                if (mMatch.getPitch_id() == App.mPitchesBeanList.get(i).getId()) {
                                    mMatch.setLocation(App.mPitchesBeanList.get(i).getLocation());
                                    mMatch.setPitch_name(App.mPitchesBeanList.get(i).getName());
                                    mMatch.setLongitude(App.mPitchesBeanList.get(i).getLongitude());
                                    mMatch.setLatitude(App.mPitchesBeanList.get(i).getLatitude());
                                    mMatch.setPitch_image(App.mPitchesBeanList.get(i).getImage().getUrl());
                                }
                            }
                            loadingDismiss();
                            mTvDate.setText(JodaTimeUtil.getDate(mMatch.getPlay_start()));
                            mTvLocation.setText(mMatch.getLocation());
                            mTvTime.setText(JodaTimeUtil.getTime2(mMatch.getPlay_start()) + "-" + JodaTimeUtil
                                    .getTime2(mMatch.getPlay_end()));
                            mTvHomeNum.setText(mMatch.getSize() + "");
                            List<MatchDetail.MatchBean.JoinMatchesBean> join_matches = mMatch.getJoin_matches();
                            for (int i = 0; i < join_matches.size(); i++) {
                                if (mMatch.getStatus().equals("f") && join_matches.get(i).getStatus()
                                        .equals("confirmed")) {
                                    MatchDetail.MatchBean.JoinMatchesBean joinMatchesBean = join_matches.get(i);
                                    mTvVisitorNum.setText(joinMatchesBean.getTeam().getSize() + "");
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        loadingDismiss();
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }

    private void initView() {
        mTvBack.setTypeface(App.mTypeface);
        mTvFriend.setTypeface(App.mTypeface);
        mTvNotice.setTypeface(App.mTypeface);
        mTvIconLocation.setTypeface(App.mTypeface);

        mAdvertisementsList = App.mAdvertisementsBean;
        for (int i = 0; i < mAdvertisementsList.size(); i++) {
            switch (mAdvertisementsList.get(i).getScreen()) {
                case Url.MATCH_DETAIL_001:
                    if (mAdvertisementsList.get(i).getImage() != null && !mAdvertisementsList.get(i).getImage()
                            .equals(""))
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i).getImage()),
                                mIvTop1, R.drawable.icon_default);
                    else
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i)
                                .getDefault_image()), mIvTop1, R.drawable.icon_default);
                    final int finalI = i;
                    mIvTop1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, AdvertisementDetailActivity.class);
                            intent.putExtra("name", mAdvertisementsList.get(finalI).getName());
                            intent.putExtra("url", mAdvertisementsList.get(finalI).getUrl());
                            startActivity(intent);
                        }
                    });
                    break;
                case Url.MATCH_DETAIL_002:
                    if (mAdvertisementsList.get(i).getImage() != null && !mAdvertisementsList.get(i).getImage()
                            .equals(""))
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i).getImage()),
                                mIvTop2, R.drawable.icon_default);
                    else
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i)
                                .getDefault_image()), mIvTop2, R.drawable.icon_default);
                    final int finalI1 = i;
                    mIvTop2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, AdvertisementDetailActivity.class);
                            intent.putExtra("name", mAdvertisementsList.get(finalI1).getName());
                            intent.putExtra("url", mAdvertisementsList.get(finalI1).getUrl());
                            startActivity(intent);
                        }
                    });
                case Url.MATCH_DETAIL_003:
                    if (mAdvertisementsList.get(i).getImage() != null && !mAdvertisementsList.get(i).getImage()
                            .equals(""))
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i).getImage()),
                                mIvRight1, R.drawable.icon_default);
                    else
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i)
                                .getDefault_image()), mIvRight1, R.drawable.icon_default);
                    final int finalI2 = i;
                    mIvRight1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, AdvertisementDetailActivity.class);
                            intent.putExtra("name", mAdvertisementsList.get(finalI2).getName());
                            intent.putExtra("url", mAdvertisementsList.get(finalI2).getUrl());
                            startActivity(intent);
                        }
                    });
                    break;
                case Url.MATCH_DETAIL_004:
                    if (mAdvertisementsList.get(i).getImage() != null && !mAdvertisementsList.get(i).getImage()
                            .equals(""))
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i).getImage()),
                                mIvRight2, R.drawable.icon_default);
                    else
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i)
                                .getDefault_image()), mIvRight2, R.drawable.icon_default);
                    final int finalI3 = i;
                    mIvRight2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, AdvertisementDetailActivity.class);
                            intent.putExtra("name", mAdvertisementsList.get(finalI3).getName());
                            intent.putExtra("url", mAdvertisementsList.get(finalI3).getUrl());
                            startActivity(intent);
                        }
                    });
                    break;
                case Url.MATCH_DETAIL_005:
                    if (mAdvertisementsList.get(i).getImage() != null && !mAdvertisementsList.get(i).getImage()
                            .equals(""))
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i).getImage()),
                                mIvRight3, R.drawable.icon_default);
                    else
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i)
                                .getDefault_image()), mIvRight3, R.drawable.icon_default);
                    final int finalI4 = i;
                    mIvRight3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, AdvertisementDetailActivity.class);
                            intent.putExtra("name", mAdvertisementsList.get(finalI4).getName());
                            intent.putExtra("url", mAdvertisementsList.get(finalI4).getUrl());
                            startActivity(intent);
                        }
                    });
                    break;
                case Url.MATCH_DETAIL_006:
                    if (mAdvertisementsList.get(i).getImage() != null && !mAdvertisementsList.get(i).getImage()
                            .equals(""))
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i).getImage()),
                                mIvBottom1, R.drawable.icon_default);
                    else
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i)
                                .getDefault_image()), mIvBottom1, R.drawable.icon_default);
                    final int finalI5 = i;
                    mIvBottom1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, AdvertisementDetailActivity.class);
                            intent.putExtra("name", mAdvertisementsList.get(finalI5).getName());
                            intent.putExtra("url", mAdvertisementsList.get(finalI5).getUrl());
                            startActivity(intent);
                        }
                    });
                    break;
                case Url.MATCH_DETAIL_007:
                    if (mAdvertisementsList.get(i).getImage() != null && !mAdvertisementsList.get(i).getImage()
                            .equals(""))
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i).getImage()),
                                mIvBottom2, R.drawable.icon_default);
                    else
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i)
                                .getDefault_image()), mIvBottom2, R.drawable.icon_default);
                    final int finalI6 = i;
                    mIvBottom2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, AdvertisementDetailActivity.class);
                            intent.putExtra("name", mAdvertisementsList.get(finalI6).getName());
                            intent.putExtra("url", mAdvertisementsList.get(finalI6).getUrl());
                            startActivity(intent);
                        }
                    });
                    break;
                case Url.MATCH_DETAIL_008:
                    if (mAdvertisementsList.get(i).getImage() != null && !mAdvertisementsList.get(i).getImage()
                            .equals(""))
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i).getImage()),
                                mIvLeft1, R.drawable.icon_default);
                    else
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i)
                                .getDefault_image()), mIvLeft1, R.drawable.icon_default);
                    final int finalI7 = i;
                    mIvLeft1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, AdvertisementDetailActivity.class);
                            intent.putExtra("name", mAdvertisementsList.get(finalI7).getName());
                            intent.putExtra("url", mAdvertisementsList.get(finalI7).getUrl());
                            startActivity(intent);
                        }
                    });
                    break;
                case Url.MATCH_DETAIL_009:
                    if (mAdvertisementsList.get(i).getImage() != null && !mAdvertisementsList.get(i).getImage()
                            .equals(""))
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i).getImage()),
                                mIvLeft2, R.drawable.icon_default);
                    else
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i)
                                .getDefault_image()), mIvLeft2, R.drawable.icon_default);
                    final int finalI8 = i;
                    mIvLeft2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, AdvertisementDetailActivity.class);
                            intent.putExtra("name", mAdvertisementsList.get(finalI8).getName());
                            intent.putExtra("url", mAdvertisementsList.get(finalI8).getUrl());
                            startActivity(intent);
                        }
                    });
                    break;
                case Url.MATCH_DETAIL_010:
                    if (mAdvertisementsList.get(i).getImage() != null && !mAdvertisementsList.get(i).getImage()
                            .equals(""))
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i).getImage()),
                                mIvLeft3, R.drawable.icon_default);
                    else
                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i)
                                .getDefault_image()), mIvLeft3, R.drawable.icon_default);
                    final int finalI9 = i;
                    mIvLeft3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, AdvertisementDetailActivity.class);
                            intent.putExtra("name", mAdvertisementsList.get(finalI9).getName());
                            intent.putExtra("url", mAdvertisementsList.get(finalI9).getUrl());
                            startActivity(intent);
                        }
                    });
                    break;
            }
        }
    }

    @OnClick({R.id.tv_back, R.id.tv_friend, R.id.tv_notice, R.id.ll_location, R.id.iv_1_1, R.id.iv_1_2, R.id.iv_1_3,
            R.id.iv_1_4, R.id.iv_1_5, R.id.iv_2_1, R.id.iv_2_2, R.id.iv_2_3, R.id.iv_2_4, R.id.iv_2_5, R.id.iv_3_1, R
            .id.iv_3_2, R.id.iv_3_3, R.id.iv_3_4, R.id.iv_3_5, R.id.iv_4_1, R.id.iv_4_2, R.id.iv_4_3, R.id.iv_4_4, R
            .id.iv_4_5, R.id.iv_5_1, R.id.iv_5_2, R.id.iv_5_3, R.id.iv_5_4, R.id.iv_5_5, R.id.tv_confirm})
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
            case R.id.ll_location:
                intent.setClass(mContext, MapsActivity.class);
                intent.putExtra("longitude", mMatch.getLongitude());
                intent.putExtra("latitude", mMatch.getLatitude());
                intent.putExtra("location", mMatch.getLocation());
                intent.putExtra("pitch_name", mMatch.getPitch_name());
                startActivity(intent);
                break;
            case R.id.iv_1_1:
                mIv11.setImageResource(R.drawable.icon_star_selected);
                mIv12.setImageResource(R.drawable.icon_star_unselected);
                mIv13.setImageResource(R.drawable.icon_star_unselected);
                mIv14.setImageResource(R.drawable.icon_star_unselected);
                mIv15.setImageResource(R.drawable.icon_star_unselected);
                attack = 1;
                break;
            case R.id.iv_1_2:
                mIv11.setImageResource(R.drawable.icon_star_selected);
                mIv12.setImageResource(R.drawable.icon_star_selected);
                mIv13.setImageResource(R.drawable.icon_star_unselected);
                mIv14.setImageResource(R.drawable.icon_star_unselected);
                mIv15.setImageResource(R.drawable.icon_star_unselected);
                attack = 2;
                break;
            case R.id.iv_1_3:
                mIv11.setImageResource(R.drawable.icon_star_selected);
                mIv12.setImageResource(R.drawable.icon_star_selected);
                mIv13.setImageResource(R.drawable.icon_star_selected);
                mIv14.setImageResource(R.drawable.icon_star_unselected);
                mIv15.setImageResource(R.drawable.icon_star_unselected);
                attack = 3;
                break;
            case R.id.iv_1_4:
                mIv11.setImageResource(R.drawable.icon_star_selected);
                mIv12.setImageResource(R.drawable.icon_star_selected);
                mIv13.setImageResource(R.drawable.icon_star_selected);
                mIv14.setImageResource(R.drawable.icon_star_selected);
                mIv15.setImageResource(R.drawable.icon_star_unselected);
                attack = 4;
                break;
            case R.id.iv_1_5:
                mIv11.setImageResource(R.drawable.icon_star_selected);
                mIv12.setImageResource(R.drawable.icon_star_selected);
                mIv13.setImageResource(R.drawable.icon_star_selected);
                mIv14.setImageResource(R.drawable.icon_star_selected);
                mIv15.setImageResource(R.drawable.icon_star_selected);
                attack = 5;
                break;
            case R.id.iv_2_1:
                mIv21.setImageResource(R.drawable.icon_star_selected);
                mIv22.setImageResource(R.drawable.icon_star_unselected);
                mIv23.setImageResource(R.drawable.icon_star_unselected);
                mIv24.setImageResource(R.drawable.icon_star_unselected);
                mIv25.setImageResource(R.drawable.icon_star_unselected);
                defense = 1;
                break;
            case R.id.iv_2_2:
                mIv21.setImageResource(R.drawable.icon_star_selected);
                mIv22.setImageResource(R.drawable.icon_star_selected);
                mIv23.setImageResource(R.drawable.icon_star_unselected);
                mIv24.setImageResource(R.drawable.icon_star_unselected);
                mIv25.setImageResource(R.drawable.icon_star_unselected);
                defense = 2;
                break;
            case R.id.iv_2_3:
                mIv21.setImageResource(R.drawable.icon_star_selected);
                mIv22.setImageResource(R.drawable.icon_star_selected);
                mIv23.setImageResource(R.drawable.icon_star_selected);
                mIv24.setImageResource(R.drawable.icon_star_unselected);
                mIv25.setImageResource(R.drawable.icon_star_unselected);
                defense = 3;
                break;
            case R.id.iv_2_4:
                mIv21.setImageResource(R.drawable.icon_star_selected);
                mIv22.setImageResource(R.drawable.icon_star_selected);
                mIv23.setImageResource(R.drawable.icon_star_selected);
                mIv24.setImageResource(R.drawable.icon_star_selected);
                mIv25.setImageResource(R.drawable.icon_star_unselected);
                defense = 4;
                break;
            case R.id.iv_2_5:
                mIv21.setImageResource(R.drawable.icon_star_selected);
                mIv22.setImageResource(R.drawable.icon_star_selected);
                mIv23.setImageResource(R.drawable.icon_star_selected);
                mIv24.setImageResource(R.drawable.icon_star_selected);
                mIv25.setImageResource(R.drawable.icon_star_selected);
                defense = 5;
                break;
            case R.id.iv_3_1:
                mIv31.setImageResource(R.drawable.icon_star_selected);
                mIv32.setImageResource(R.drawable.icon_star_unselected);
                mIv33.setImageResource(R.drawable.icon_star_unselected);
                mIv34.setImageResource(R.drawable.icon_star_unselected);
                mIv35.setImageResource(R.drawable.icon_star_unselected);
                on_time = 1;
                break;
            case R.id.iv_3_2:
                mIv31.setImageResource(R.drawable.icon_star_selected);
                mIv32.setImageResource(R.drawable.icon_star_selected);
                mIv33.setImageResource(R.drawable.icon_star_unselected);
                mIv34.setImageResource(R.drawable.icon_star_unselected);
                mIv35.setImageResource(R.drawable.icon_star_unselected);
                on_time = 2;
                break;
            case R.id.iv_3_3:
                mIv31.setImageResource(R.drawable.icon_star_selected);
                mIv32.setImageResource(R.drawable.icon_star_selected);
                mIv33.setImageResource(R.drawable.icon_star_selected);
                mIv34.setImageResource(R.drawable.icon_star_unselected);
                mIv35.setImageResource(R.drawable.icon_star_unselected);
                on_time = 3;
                break;
            case R.id.iv_3_4:
                mIv31.setImageResource(R.drawable.icon_star_selected);
                mIv32.setImageResource(R.drawable.icon_star_selected);
                mIv33.setImageResource(R.drawable.icon_star_selected);
                mIv34.setImageResource(R.drawable.icon_star_selected);
                mIv35.setImageResource(R.drawable.icon_star_unselected);
                on_time = 4;
                break;
            case R.id.iv_3_5:
                mIv31.setImageResource(R.drawable.icon_star_selected);
                mIv32.setImageResource(R.drawable.icon_star_selected);
                mIv33.setImageResource(R.drawable.icon_star_selected);
                mIv34.setImageResource(R.drawable.icon_star_selected);
                mIv35.setImageResource(R.drawable.icon_star_selected);
                on_time = 5;
                break;
            case R.id.iv_4_1:
                mIv41.setImageResource(R.drawable.icon_star_selected);
                mIv42.setImageResource(R.drawable.icon_star_unselected);
                mIv43.setImageResource(R.drawable.icon_star_unselected);
                mIv44.setImageResource(R.drawable.icon_star_unselected);
                mIv45.setImageResource(R.drawable.icon_star_unselected);
                technic = 1;
                break;
            case R.id.iv_4_2:
                mIv41.setImageResource(R.drawable.icon_star_selected);
                mIv42.setImageResource(R.drawable.icon_star_selected);
                mIv43.setImageResource(R.drawable.icon_star_unselected);
                mIv44.setImageResource(R.drawable.icon_star_unselected);
                mIv45.setImageResource(R.drawable.icon_star_unselected);
                technic = 2;
                break;
            case R.id.iv_4_3:
                mIv41.setImageResource(R.drawable.icon_star_selected);
                mIv42.setImageResource(R.drawable.icon_star_selected);
                mIv43.setImageResource(R.drawable.icon_star_selected);
                mIv44.setImageResource(R.drawable.icon_star_unselected);
                mIv45.setImageResource(R.drawable.icon_star_unselected);
                technic = 3;
                break;
            case R.id.iv_4_4:
                mIv41.setImageResource(R.drawable.icon_star_selected);
                mIv42.setImageResource(R.drawable.icon_star_selected);
                mIv43.setImageResource(R.drawable.icon_star_selected);
                mIv44.setImageResource(R.drawable.icon_star_selected);
                mIv45.setImageResource(R.drawable.icon_star_unselected);
                technic = 4;
                break;
            case R.id.iv_4_5:
                mIv41.setImageResource(R.drawable.icon_star_selected);
                mIv42.setImageResource(R.drawable.icon_star_selected);
                mIv43.setImageResource(R.drawable.icon_star_selected);
                mIv44.setImageResource(R.drawable.icon_star_selected);
                mIv45.setImageResource(R.drawable.icon_star_selected);
                technic = 5;
                break;
            case R.id.iv_5_1:
                mIv51.setImageResource(R.drawable.icon_star_selected);
                mIv52.setImageResource(R.drawable.icon_star_unselected);
                mIv53.setImageResource(R.drawable.icon_star_unselected);
                mIv54.setImageResource(R.drawable.icon_star_unselected);
                mIv55.setImageResource(R.drawable.icon_star_unselected);
                personality = 1;
                break;
            case R.id.iv_5_2:
                mIv51.setImageResource(R.drawable.icon_star_selected);
                mIv52.setImageResource(R.drawable.icon_star_selected);
                mIv53.setImageResource(R.drawable.icon_star_unselected);
                mIv54.setImageResource(R.drawable.icon_star_unselected);
                mIv55.setImageResource(R.drawable.icon_star_unselected);
                personality = 2;
                break;
            case R.id.iv_5_3:
                mIv51.setImageResource(R.drawable.icon_star_selected);
                mIv52.setImageResource(R.drawable.icon_star_selected);
                mIv53.setImageResource(R.drawable.icon_star_selected);
                mIv54.setImageResource(R.drawable.icon_star_unselected);
                mIv55.setImageResource(R.drawable.icon_star_unselected);
                personality = 3;
                break;
            case R.id.iv_5_4:
                mIv51.setImageResource(R.drawable.icon_star_selected);
                mIv52.setImageResource(R.drawable.icon_star_selected);
                mIv53.setImageResource(R.drawable.icon_star_selected);
                mIv54.setImageResource(R.drawable.icon_star_selected);
                mIv55.setImageResource(R.drawable.icon_star_unselected);
                personality = 4;
                break;
            case R.id.iv_5_5:
                mIv51.setImageResource(R.drawable.icon_star_selected);
                mIv52.setImageResource(R.drawable.icon_star_selected);
                mIv53.setImageResource(R.drawable.icon_star_selected);
                mIv54.setImageResource(R.drawable.icon_star_selected);
                mIv55.setImageResource(R.drawable.icon_star_selected);
                personality = 5;
                break;
            case R.id.tv_confirm:
                rate();//打分
                break;
        }
    }

    /**
     * 打分
     */
    private void rate() {
//        http://api.freekick.hk/api/en/matches/<matchID>/ratings
//        http://api.freekick.hk/api/zh_HK/matches/<matchID>/ratings
//        {"rating": {"team_id": "10","attack": "5","defense": "4","on_time": "4","technic": "4","personality": "5"}}
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("team_id", team_id);
        object1.addProperty("attack", attack + "");
        object1.addProperty("defense", defense + "");
        object1.addProperty("on_time", on_time + "");
        object1.addProperty("technic", technic + "");
        object1.addProperty("personality", personality + "");
        object.add("rating", object1);
        loadingShow();
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "matches/" + match_id + "/ratings";
        Logger.d(url);
        Logger.json(object.toString());
        OkGo.post(url)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        RateResponse fromJson = gson.fromJson(s, RateResponse.class);
                        if (fromJson.getRating() != null) {
                            ToastUtil.toastShort(getString(R.string.rate_success));
                            Intent intent = new Intent();
                            intent.setClass(mContext, MainActivity.class);
                            intent.putExtra("which", 3);
                            startActivity(intent);
                            finish();
                        } else if (fromJson.getErrors() != null) {
                            ToastUtil.toastShort(fromJson.getErrors());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        loadingDismiss();
                        Logger.d(e.getMessage());
                    }
                });
    }
}
