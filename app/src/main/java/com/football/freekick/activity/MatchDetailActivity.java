package com.football.freekick.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.MatchDetail;
import com.football.freekick.beans.MatchesComing;
import com.football.freekick.http.Url;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
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

/**
 * 球賽詳情頁(通過接口請求)
 */
public class MatchDetailActivity extends BaseActivity {

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

    private String id;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);
        mContext = MatchDetailActivity.this;
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id") == null ? "" : getIntent().getStringExtra("id");
        initView();
        if (!id.equals(""))
            initData();
    }

    private void initData() {
        loadingShow();
        Logger.d(Url.MATCH_DETAIL + id);
        OkGo.get(Url.MATCH_DETAIL + id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        MatchDetail fromJson = gson.fromJson(s, MatchDetail.class);
                        MatchDetail.MatchBean match = fromJson.getMatch();

                        for (int i = 0; i < App.mPitchesBeanList.size(); i++) {
                            if (match.getPitch_id() == App.mPitchesBeanList.get(i).getId()) {
                                match.setLocation(App.mPitchesBeanList.get(i).getLocation());
                                match.setPitch_name(App.mPitchesBeanList.get(i).getName());
                            }
                        }
                        mTvDate.setText(JodaTimeUtil.getDate(match.getPlay_start()));
                        mTvLocation.setText(match.getLocation());
                        mTvTime.setText(JodaTimeUtil.getTimeHourMinutes(match.getPlay_start()) + "-" + JodaTimeUtil
                                .getTimeHourMinutes(match.getPlay_end()));
                        mTvHomeName.setText(match.getHome_team().getTeam_name()==null?"":match.getHome_team().getTeam_name());
                        mIvHomeDress.setBackgroundColor(MyUtil.getColorInt(match.getHome_team_color()));
                        mTvHomeNum.setText(match.getHome_team().getSize() + "");
                        List<MatchDetail.MatchBean.JoinMatchesBean> join_matches = match.getJoin_matches();
                        for (int i = 0; i < join_matches.size(); i++) {
                            if (join_matches.get(i).getStatus().equals("confirmed")) {
                                MatchDetail.MatchBean.JoinMatchesBean joinMatchesBean = join_matches.get(i);
                                mTvVisitorName.setText(joinMatchesBean.getTeam().getTeam_name());
                                mIvVisitorDress.setBackgroundColor(MyUtil.getColorInt(joinMatchesBean
                                        .getJoin_team_color()));
                                mTvVisitorNum.setText(joinMatchesBean.getTeam().getSize() + "");
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

        String image = App.mAdvertisementsBean.get(0).getImage();
        ImageLoaderUtils.displayImage(image, mIvTop1);
        ImageLoaderUtils.displayImage(image, mIvTop2);
        ImageLoaderUtils.displayImage(image, mIvBottom1);
        ImageLoaderUtils.displayImage(image, mIvBottom2);
        ImageLoaderUtils.displayImage(image, mIvLeft1);
        ImageLoaderUtils.displayImage(image, mIvLeft2);
        ImageLoaderUtils.displayImage(image, mIvLeft3);
        ImageLoaderUtils.displayImage(image, mIvRight1);
        ImageLoaderUtils.displayImage(image, mIvRight2);
        ImageLoaderUtils.displayImage(image, mIvRight3);
    }

    @OnClick({R.id.tv_back, R.id.tv_friend, R.id.tv_notice, R.id.iv_top_1, R.id.iv_top_2, R.id.iv_left_1, R.id
            .iv_left_2, R.id.iv_left_3, R.id.iv_right_1, R.id.iv_right_2, R.id.iv_right_3, R.id.iv_bottom_1, R.id
            .iv_bottom_2,R.id.ll_location})
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
                startActivity(intent);
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
}
