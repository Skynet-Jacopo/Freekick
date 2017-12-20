package com.football.freekick.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.MainActivity;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.Advertisements;
import com.football.freekick.beans.CancelMatch;
import com.football.freekick.beans.MatchDetail;
import com.football.freekick.http.Url;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class NoticeDetailActivity extends BaseActivity {

    @Bind(R.id.tv_back)
    TextView mTvBack;
    @Bind(R.id.tv_friend)
    TextView mTvFriend;
    @Bind(R.id.tv_notice)
    TextView mTvNotice;
    @Bind(R.id.iv_top_1)
    ImageView mIvTop1;
    @Bind(R.id.iv_top_2)
    ImageView mIvTop2;
    @Bind(R.id.ll_top)
    LinearLayout mLlTop;
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
    @Bind(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.tv_icon_location)
    TextView mTvIconLocation;
    @Bind(R.id.tv_location)
    TextView mTvLocation;
    @Bind(R.id.ll_location)
    LinearLayout mLlLocation;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.iv_home_dress)
    ImageView mIvHomeDress;
    @Bind(R.id.tv_home_name)
    TextView mTvHomeName;
    @Bind(R.id.tv_home_num)
    TextView mTvHomeNum;
    @Bind(R.id.iv_visitor_dress)
    ImageView mIvVisitorDress;
    @Bind(R.id.tv_visitor_name)
    TextView mTvVisitorName;
    @Bind(R.id.tv_reduce)
    TextView mTvReduce;
    @Bind(R.id.tv_visitor_num)
    TextView mTvVisitorNum;
    @Bind(R.id.tv_add)
    TextView mTvAdd;
    @Bind(R.id.ll_team)
    LinearLayout mLlTeam;
    @Bind(R.id.fl_parent)
    FrameLayout mFlParent;
    @Bind(R.id.tv_btn_1)
    TextView mTvBtn1;
    @Bind(R.id.tv_btn_2)
    TextView mTvBtn2;
    @Bind(R.id.tv_btn_3)
    TextView mTvBtn3;

    private String match_id;
    private Context mContext;
    private MatchDetail.MatchBean mMatch;
    private String team_id;
    private String notification_type;

    private List<Advertisements.AdvertisementsBean> mAdvertisementsList;//广告列表
    private String district;
    private int district_id;
    private String region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        mContext = NoticeDetailActivity.this;
        ButterKnife.bind(this);
        match_id = getIntent().getStringExtra("match_id") == null ? "" : getIntent().getStringExtra("match_id");
        notification_type = getIntent().getStringExtra("notification_type") == null ? "" : getIntent().getStringExtra
                ("notification_type");
        team_id = PrefUtils.getString(App.APP_CONTEXT, "team_id", null);
        initView();//初始化廣告等
        initData();
    }

    private void initData() {
        loadingShow();
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "matches/";//+matchID
        Logger.d(url + match_id);
        OkGo.get(url + match_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
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
                                    district = App.mPitchesBeanList.get(i).getDistrict().getDistrict();
                                    region = App.mPitchesBeanList.get(i).getDistrict().getRegion();
                                    district_id = App.mPitchesBeanList.get(i).getDistrict().getId();
                                }
                            }
                            mTvDate.setText(JodaTimeUtil.getDate(mMatch.getPlay_start()));
                            mTvLocation.setText(mMatch.getLocation());
                            mTvTime.setText(JodaTimeUtil.getTime2(mMatch.getPlay_start()) + "-" + JodaTimeUtil
                                    .getTime2(mMatch.getPlay_end()));
                            mTvHomeName.setText(mMatch.getHome_team().getTeam_name() == null ? "" : mMatch
                                    .getHome_team().getTeam_name());
                            mIvHomeDress.setBackgroundColor(MyUtil.getColorInt(mMatch.getHome_team_color()));
                            mTvHomeNum.setText(mMatch.getSize() + "");


                            mTvBtn1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    intent.putExtra("which", 2);
                                    intent.putExtra("district_id",district_id+"");
                                    intent.putExtra("region",region);
                                    intent.putExtra("district",district);
//                                    DateTime dateTime = new DateTime(JodaTimeUtil.getMills(mMatch.getPlay_start()));
//                                    intent.putExtra("date",dateTime);
                                    intent.putExtra("start",mMatch.getPlay_start());
                                    intent.putExtra("end",mMatch.getPlay_end());
                                    intent.putExtra("size",mMatch.getSize()+"");
                                    startActivity(intent);
                                    finish();
                                }
                            });
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

        switch (notification_type) {
            case "N7b"://是在主隊確認客隊之後(球賽狀態是'已確認'), 客隊在球賽確認限期(confirm_end)后退出, 通知由主隊收到.
                mTvBtn1.setVisibility(View.VISIBLE);
                mTvBtn2.setVisibility(View.GONE);
                mTvBtn3.setVisibility(View.VISIBLE);
                break;
            case "N7c"://是在主隊確認客隊之後(球賽狀態是'已確認'), 客隊在球賽確認限期(confirm_end)前退出, 通知由主隊收到.
                mTvBtn1.setVisibility(View.VISIBLE);
                mTvBtn2.setVisibility(View.VISIBLE);
                mTvBtn3.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
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

    @OnClick({R.id.tv_back, R.id.tv_friend, R.id.tv_notice, R.id.ll_location, R.id.tv_btn_1, R.id.tv_btn_2, R.id
            .tv_btn_3})
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

            case R.id.tv_btn_1:
                break;
            case R.id.tv_btn_2:
                intent.setClass(mContext, MainActivity.class);
                intent.putExtra("which", 4);
                intent.putExtra("toPage","1");//未落實球賽頁
                startActivity(intent);
                finish();
                break;
            case R.id.tv_btn_3:
                cancelMatch(match_id);
                break;
        }
    }

    /**
     * 主隊取消比賽
     *
     * @param match_id
     */
    private void cancelMatch(String match_id) {
//        http://api.freekick.hk/api/en/matches/<matchID>/cancel
        String cancelMatchUrl = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "matches/" + match_id + "/cancel";
        Logger.d(cancelMatchUrl);
        loadingShow();
        OkGo.put(cancelMatchUrl)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        CancelMatch fromJson = gson.fromJson(s, CancelMatch.class);
                        if (fromJson.getMatch() != null) {
                            ToastUtil.toastShort(getString(R.string.cancel_success));
                            Intent intent = new Intent();
                            intent.setClass(mContext, MainActivity.class);
                            intent.putExtra("which", 4);
                            intent.putExtra("toPage","1");//未落實球賽頁
                            startActivity(intent);
                            finish();
                        } else if (fromJson.getErrors() != null) {
                            ToastUtil.toastShort(fromJson.getErrors());
                        } else {
                            ToastUtil.toastShort(getString(R.string.withdraw_failed));
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                        ToastUtil.toastShort(getString(R.string.withdraw_failed));
                    }
                });
    }
}
