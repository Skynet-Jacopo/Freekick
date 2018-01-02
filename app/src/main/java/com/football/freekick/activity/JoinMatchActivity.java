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
import com.football.freekick.beans.AvailableMatches;
import com.football.freekick.beans.JoinMatch;
import com.football.freekick.http.Url;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.StringUtils;
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
 * 參與球賽界面(球賽內容頁風格)
 */
public class JoinMatchActivity extends BaseActivity {

    @Bind(R.id.tv_back)
    TextView     mTvBack;
    @Bind(R.id.tv_friend)
    TextView     mTvFriend;
    @Bind(R.id.tv_notice)
    TextView     mTvNotice;
    @Bind(R.id.ll_top)
    LinearLayout mLlTop;
    @Bind(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @Bind(R.id.tv_date)
    TextView     mTvDate;
    @Bind(R.id.tv_icon_location)
    TextView     mTvIconLocation;
    @Bind(R.id.ll_location)
    LinearLayout mLlLocation;
    @Bind(R.id.tv_time)
    TextView     mTvTime;
    @Bind(R.id.iv_home_dress)
    ImageView    mIvHomeDress;
    @Bind(R.id.tv_home_name)
    TextView     mTvHomeName;
    @Bind(R.id.tv_home_num)
    TextView     mTvHomeNum;
    @Bind(R.id.tv_visitor_name)
    TextView     mTvVisitorName;
    @Bind(R.id.tv_reduce)
    TextView     mTvReduce;
    @Bind(R.id.tv_visitor_num)
    TextView     mTvVisitorNum;
    @Bind(R.id.tv_add)
    TextView     mTvAdd;
    @Bind(R.id.tv_confirm)
    TextView     mTvConfirm;
    @Bind(R.id.tv_location)
    TextView     mTvLocation;
    @Bind(R.id.iv_visitor_dress)
    ImageView    mIvVisitorDress;
    @Bind(R.id.iv_top_1)
    ImageView    mIvTop1;
    @Bind(R.id.iv_top_2)
    ImageView    mIvTop2;
    @Bind(R.id.iv_left_1)
    ImageView    mIvLeft1;
    @Bind(R.id.iv_left_2)
    ImageView    mIvLeft2;
    @Bind(R.id.iv_left_3)
    ImageView    mIvLeft3;
    @Bind(R.id.iv_right_1)
    ImageView    mIvRight1;
    @Bind(R.id.iv_right_2)
    ImageView    mIvRight2;
    @Bind(R.id.iv_right_3)
    ImageView    mIvRight3;
    @Bind(R.id.iv_bottom_1)
    ImageView    mIvBottom1;
    @Bind(R.id.iv_bottom_2)
    ImageView    mIvBottom2;
    private AvailableMatches.MatchesBean mMatchesBean;
    private int                          visitorNum;
    private Context mContext;
    private List<Advertisements.AdvertisementsBean> mAdvertisementsList;//广告列表
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_match);
        mContext = JoinMatchActivity.this;
        ButterKnife.bind(this);
        mMatchesBean = (AvailableMatches.MatchesBean) getIntent().getSerializableExtra("matchesBean");
        initView();
    }

    private void initView() {
        mTvBack.setTypeface(App.mTypeface);
        mTvFriend.setTypeface(App.mTypeface);
        mTvNotice.setTypeface(App.mTypeface);
        mTvIconLocation.setTypeface(App.mTypeface);
        mTvDate.setText(JodaTimeUtil.getDate(mMatchesBean.getPlay_start()));
        mTvLocation.setText(mMatchesBean.getLocation());
        mTvTime.setText(JodaTimeUtil.getTime2(mMatchesBean.getPlay_start()) + "-" + JodaTimeUtil
                .getTime2(mMatchesBean.getPlay_end()));
        mTvHomeName.setText(mMatchesBean.getHome_team().getTeam_name());
        mTvHomeNum.setText(mMatchesBean.getSize() + "");
        mIvHomeDress.setBackgroundColor(MyUtil.getColorInt(mMatchesBean.getHome_team_color()));
        mIvVisitorDress.setBackgroundColor(MyUtil.getColorInt(PrefUtils.getString(App.APP_CONTEXT, "color2", null)));
        mTvVisitorName.setText(PrefUtils.getString(App.APP_CONTEXT, "team_name", null));
        mTvVisitorNum.setText(PrefUtils.getString(App.APP_CONTEXT, "size", null));

        visitorNum = Integer.parseInt(StringUtils.getEditText(mTvVisitorNum));
        mAdvertisementsList = App.mAdvertisementsBean;
        for (int i = 0; i <mAdvertisementsList.size(); i++) {
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
                            Intent intent = new Intent(mContext,AdvertisementDetailActivity.class);
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
                            Intent intent = new Intent(mContext,AdvertisementDetailActivity.class);
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
                            Intent intent = new Intent(mContext,AdvertisementDetailActivity.class);
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
                            Intent intent = new Intent(mContext,AdvertisementDetailActivity.class);
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
                            Intent intent = new Intent(mContext,AdvertisementDetailActivity.class);
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
                            Intent intent = new Intent(mContext,AdvertisementDetailActivity.class);
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
                            Intent intent = new Intent(mContext,AdvertisementDetailActivity.class);
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
                            Intent intent = new Intent(mContext,AdvertisementDetailActivity.class);
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
                            Intent intent = new Intent(mContext,AdvertisementDetailActivity.class);
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
                            Intent intent = new Intent(mContext,AdvertisementDetailActivity.class);
                            intent.putExtra("name", mAdvertisementsList.get(finalI9).getName());
                            intent.putExtra("url", mAdvertisementsList.get(finalI9).getUrl());
                            startActivity(intent);
                        }
                    });
                    break;
            }
        }
    }

    @OnClick({R.id.tv_back, R.id.tv_friend, R.id.tv_notice, R.id.tv_icon_location, R.id.ll_location, R.id.tv_reduce,
            R.id.tv_add, R.id.tv_confirm, R.id.iv_top_1, R.id.iv_top_2, R.id.iv_left_1, R.id.iv_left_2, R.id.iv_left_3, R.id.iv_right_1, R.id.iv_right_2, R.id.iv_right_3, R.id.iv_bottom_1, R.id.iv_bottom_2})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_friend:
                intent.setClass(mContext,FriendActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_notice:
                intent.setClass(mContext,NoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_location:
                intent.setClass(mContext,MapsActivity.class);
                intent.putExtra("longitude",mMatchesBean.getLongitude());
                intent.putExtra("latitude",mMatchesBean.getLatitude());
                intent.putExtra("location",mMatchesBean.getLocation());
                intent.putExtra("pitch_name",mMatchesBean.getPitch_name());
                startActivity(intent);
                break;
            case R.id.tv_reduce:
                if (visitorNum <= 1) {
                    return;
                } else {
                    visitorNum -= 1;
                }
                mTvVisitorNum.setText(visitorNum + "");
                break;
            case R.id.tv_add:
                visitorNum += 1;
                mTvVisitorNum.setText(visitorNum + "");
                break;
            case R.id.tv_confirm:
                joinMatch();//參與球賽
                break;
            case R.id.iv_top_1:
                break;
            case R.id.iv_top_2:
                break;
            case R.id.iv_left_1:
                break;
            case R.id.iv_left_2:
                break;
            case R.id.iv_left_3:
                break;
            case R.id.iv_right_1:
                break;
            case R.id.iv_right_2:
                break;
            case R.id.iv_right_3:
                break;
            case R.id.iv_bottom_1:
                break;
            case R.id.iv_bottom_2:
                break;
        }
    }

    /**
     * 參與約
     */
    private void joinMatch() {

        loadingShow();
        JsonObject object  = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("match_id", mMatchesBean.getId() + "");
        object1.addProperty("join_team_id", PrefUtils.getString(App.APP_CONTEXT, "team_id", null));
        object1.addProperty("join_team_color", PrefUtils.getString(App.APP_CONTEXT, "color2", null));
        object1.addProperty("size", StringUtils.getEditText(mTvVisitorNum));
        object.add("join_match", object1);

        Logger.json(object.toString());
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "join_matches";
        OkGo.post(url)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson      gson      = new Gson();
                        JoinMatch joinMatch = gson.fromJson(s, JoinMatch.class);
                        if (joinMatch.getJoin_match() != null) {
                            ToastUtil.toastShort(getString(R.string.join_success_please_wait_to_confirm));
                            setResult(RESULT_OK);
                            Intent intent = new Intent();
                            intent.setClass(mContext, MainActivity.class);//去到未落實球賽頁
                            intent.putExtra("which", 4);
                            intent.putExtra("toPage", "1");
                            startActivity(intent);
                            finish();
                        } else {

                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                        ToastUtil.toastShort(getString(R.string.match_fail));
                    }
                });
    }

}
