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
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.AvailableMatches;
import com.football.freekick.beans.MatchesComing;
import com.football.freekick.views.imageloader.ImageLoaderUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 球賽內容頁,最終版,除打分頁以及參與球賽頁,都統一使用此界面
 * 1.已落實,我是主隊(不區分客隊是邀請落實的,還是參與進來的)(match status = m,join match status = confirmed)
 * 2.已落實,我是客隊(不區分是邀請落實的,還是參與進來的)(match status = m,join match status = confirmed)
 * 3.我已參與,主隊未確認(match status = w,join match status = confirmation_pending)
 * 4.我是主隊,有隊伍參與,我還未確認(match status = w,join match status = confirmation_pending)
 * 5.我被邀請,還未接受邀請(match status = i,join match status = invited)
 * 6.我已邀請別人,別人還未確認(match status = i,join match status = invited)
 * 7.我不是主隊,也不是客隊,只是來瀏覽的(match status = m,join match status = confirmed?(還不確定))
 */
public class MatchContentActivity1 extends BaseActivity {

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
    @Bind(R.id.fl_parent)
    FrameLayout mFlParent;
    @Bind(R.id.tv_btn)
    TextView mTvBtn;

    private String id;
    private String where;
    private int type;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_content1);
        mContext = MatchContentActivity1.this;
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id") == null ? "" : getIntent().getStringExtra("id");
        where = getIntent().getStringExtra("where") == null ? "" : getIntent().getStringExtra("where");
        type = getIntent().getIntExtra("type", 0);
        MatchesComing.MatchesBean matchesBean = (MatchesComing.MatchesBean) getIntent().getSerializableExtra("model");
        initView();//初始化廣告等
        initData();
    }

    private void initData() {
        switch (where) {
            case "partake":
                AvailableMatches.MatchesBean model = (AvailableMatches.MatchesBean) getIntent().getSerializableExtra
                        ("model");
                initDetail(model);
                break;
        }
    }

    /**
     * 從PartakeListFragment跳轉過來的
     *
     * @param model
     */
    private void initDetail(AvailableMatches.MatchesBean model) {

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
            .iv_bottom_2, R.id.tv_icon_share_left, R.id.tv_icon_notice_left, R.id.tv_icon_share_right, R.id
            .tv_icon_notice_right, R.id.tv_btn})
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
            case R.id.tv_btn:
                break;
        }
    }
}
