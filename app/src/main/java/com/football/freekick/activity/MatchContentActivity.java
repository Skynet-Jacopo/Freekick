package com.football.freekick.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.MatchesComing;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.StringUtils;
import com.football.freekick.views.imageloader.ImageLoaderUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 球賽內容頁
 */
public class MatchContentActivity extends BaseActivity {

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
    @Bind(R.id.tv_icon_share_left)
    TextView mTvIconShareLeft;
    @Bind(R.id.tv_icon_notice_left)
    TextView mTvIconNoticeLeft;
    @Bind(R.id.tv_icon_share_right)
    TextView mTvIconShareRight;
    @Bind(R.id.tv_icon_notice_right)
    TextView mTvIconNoticeRight;
    @Bind(R.id.tv_confirm)
    TextView mTvConfirm;
    private MatchesComing.MatchesBean mMatchesBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_content);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        int type = getIntent().getIntExtra("type", 1);
        mMatchesBean = getIntent().getParcelableExtra("matchesBean");
        switch (type){
            case 1://客隊
                mTvIconNoticeLeft.setVisibility(View.VISIBLE);
                mTvIconShareLeft.setVisibility(View.VISIBLE);
                mTvIconNoticeRight.setVisibility(View.GONE);
                mTvIconShareRight.setVisibility(View.GONE);
                break;
            case 2://主隊
                mTvIconNoticeLeft.setVisibility(View.GONE);
                mTvIconShareLeft.setVisibility(View.GONE);
                mTvIconNoticeRight.setVisibility(View.VISIBLE);
                mTvIconShareRight.setVisibility(View.VISIBLE);
                break;
        }

        mTvBack.setTypeface(App.mTypeface);
        mTvFriend.setTypeface(App.mTypeface);
        mTvNotice.setTypeface(App.mTypeface);
        mTvIconLocation.setTypeface(App.mTypeface);
        mTvIconNoticeLeft.setTypeface(App.mTypeface);
        mTvIconNoticeRight.setTypeface(App.mTypeface);
        mTvIconShareLeft.setTypeface(App.mTypeface);
        mTvIconShareRight.setTypeface(App.mTypeface);

        mTvDate.setText(JodaTimeUtil.getDate(mMatchesBean.getPlay_start()));
        mTvLocation.setText(mMatchesBean.getLocation());
        mTvTime.setText(JodaTimeUtil.getTimeHourMinutes(mMatchesBean.getPlay_start()) + "-" + JodaTimeUtil
                .getTimeHourMinutes(mMatchesBean.getPlay_end()));
        mTvHomeName.setText(mMatchesBean.getHome_team().getTeam_name());
        mIvHomeDress.setBackgroundColor(MyUtil.getColorInt(mMatchesBean.getHome_team_color()));
        mTvHomeNum.setText(mMatchesBean.getHome_team().getSize()+"");
        List<MatchesComing.MatchesBean.JoinMatchesBean> join_matches = mMatchesBean.getJoin_matches();
        for (int i = 0; i < join_matches.size(); i++) {
            if (join_matches.get(i).getStatus().equals("confirmed")){
                MatchesComing.MatchesBean.JoinMatchesBean joinMatchesBean = join_matches.get(i);
                mTvVisitorName.setText(joinMatchesBean.getTeam().getTeam_name());
                mIvVisitorDress.setBackgroundColor(MyUtil.getColorInt(joinMatchesBean.getJoin_team_color()));
                mTvVisitorNum.setText(joinMatchesBean.getTeam().getSize()+"");
            }
        }

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
            .iv_bottom_2, R.id.tv_icon_share_left, R.id.tv_icon_notice_left, R.id.tv_icon_share_right, R.id
            .tv_icon_notice_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_friend:
                break;
            case R.id.tv_notice:
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
            case R.id.tv_icon_share_left:
                break;
            case R.id.tv_icon_notice_left:
                break;
            case R.id.tv_icon_share_right:
                break;
            case R.id.tv_icon_notice_right:
                break;
        }
    }
}
