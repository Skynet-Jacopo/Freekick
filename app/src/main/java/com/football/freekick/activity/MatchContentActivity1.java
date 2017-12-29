package com.football.freekick.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.Advertisements;
import com.football.freekick.beans.CancelMatch;
import com.football.freekick.beans.ConfirmInvite;
import com.football.freekick.beans.Invite;
import com.football.freekick.beans.MatchDetail;
import com.football.freekick.beans.Ratings;
import com.football.freekick.beans.WithDraw;
import com.football.freekick.chat.FireChatHelper.ExtraIntent;
import com.football.freekick.chat.model.User;
import com.football.freekick.chat.ui.ChatActivity;
import com.football.freekick.http.Url;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 球賽內容頁,最終版,除打分頁以及參與球賽頁,都統一使用此界面
 * 1.已落實,我是主隊(不區分客隊是邀請落實的,還是參與進來的)(match status = m,join match status = confirmed)
 * 2.已落實,我是客隊(不區分是邀請落實的,還是參與進來的)(match status = m,join match status = confirmed)
 * 3.我已參與,主隊未確認(match status = w,join match status = confirmation_pending)
 * 4.我是主隊,有隊伍參與,我還未確認(match status = w,join match status = confirmation_pending)
 * 5.我被邀請,還未接受邀請(match status = i,join match status = invited)
 * 6.我已邀請別人,別人還未確認(match status = i,join match status = invited)
 * 7.我不是主隊,也不是客隊,只是來瀏覽的(match status = m,join match status = confirmed?(還不確定))
 * 8.我是主隊或客隊,球賽完成後在作賽記錄頁進入
 * 9.球賽詳情頁(無邀請,無主動參與隊伍)
 */
public class MatchContentActivity1 extends BaseActivity {

    public static final int REQUEST_CODE_REFRESH = 1;
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
    private MatchDetail.MatchBean mMatch;
    private String team_id;
    private String join_match_id = "";
    private String join_team_id = "";

    private DatabaseReference mUserRefDatabase;
    private User mCurrentUser;
    private User mRecipient;
    private String recipient_id = "";
    private String recipient_name = "";

    private ChildEventListener mChildEventListener;
    private List<Advertisements.AdvertisementsBean> mAdvertisementsList;//广告列表
    private int visitorNum;
    private String join_team_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_content1);
        mContext = MatchContentActivity1.this;
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id") == null ? "" : getIntent().getStringExtra("id");
//        where = getIntent().getStringExtra("where") == null ? "" : getIntent().getStringExtra("where");
        type = getIntent().getIntExtra("type", 0);
        team_id = PrefUtils.getString(App.APP_CONTEXT, "team_id", null);
        initView();//初始化廣告等
        initData();
    }

    //獲取發送者,收件者,還有chat_room的名字進入聊天界面
    private void initUserDatabase() {
        mUserRefDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mChildEventListener = getChildEventListener();
        mUserRefDatabase.limitToFirst(50).addChildEventListener(mChildEventListener);
    }

    private void initData() {
        loadingShow();
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "matches/";//+matchID
        Logger.d(url + id);
        OkGo.get(url + id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        MatchDetail fromJson = gson.fromJson(s, MatchDetail.class);
                        if (fromJson.getMatch() != null) {
                            mMatch = fromJson.getMatch();
                            setData();
                            initUserDatabase();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        loadingDismiss();
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        mFlParent.setVisibility(View.VISIBLE);
                    }
                });
    }

    /**
     * 將獲取下來的數據添加到界面
     */
    private void setData() {

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
        mTvHomeName.setText(mMatch.getHome_team().getTeam_name() == null ? "" : mMatch.getHome_team().getTeam_name());
        mIvHomeDress.setBackgroundColor(MyUtil.getColorInt(mMatch.getHome_team_color()));
        mTvHomeNum.setText(mMatch.getSize() + "");

        /**
         * 1.已落實,我是主隊(不區分客隊是邀請落實的,還是參與進來的)(match status = m,join match status = confirmed)
         * 2.已落實,我是客隊(不區分是邀請落實的,還是參與進來的)(match status = m,join match status = confirmed)
         * 3.我已參與,主隊未確認(match status = w,join match status = confirmation_pending)
         * 4.我是主隊,有隊伍參與,我還未確認(match status = w,join match status = confirmation_pending)
         * 5.我被邀請,還未接受邀請(match status = i,join match status = invited)
         * 6.我已邀請別人,別人還未確認(match status = i,join match status = invited)
         * 7.我不是主隊,也不是客隊,只是來瀏覽的(match status = m,join match status = confirmed?(還不確定))
         * 8.我是主隊或客隊,球賽完成後在作賽記錄頁進入
         * 9.球賽詳情頁(無邀請,無主動參與隊伍)
         */
        List<MatchDetail.MatchBean.JoinMatchesBean> join_matches = mMatch.getJoin_matches();
        switch (type) {
            case 1://已落實,我是主隊
                mTvIconNoticeLeft.setVisibility(View.GONE);
                mTvIconShareLeft.setVisibility(View.GONE);
                mTvIconShareRight.setVisibility(View.VISIBLE);
                mTvIconNoticeRight.setVisibility(View.VISIBLE);
                mTvBtn.setText(R.string.cancel_match);
                for (int i = 0; i < join_matches.size(); i++) {
                    if (join_matches.get(i).getStatus().equals("confirmed")) {
                        MatchDetail.MatchBean.JoinMatchesBean joinMatchesBean = join_matches.get(i);
                        mTvVisitorName.setText(joinMatchesBean.getTeam().getTeam_name());
                        mIvVisitorDress.setImageResource(R.drawable.ic_dress_visitor);
                        mIvVisitorDress.setBackgroundColor(MyUtil.getColorInt(joinMatchesBean
                                .getJoin_team_color()));
                        mTvVisitorNum.setText(joinMatchesBean.getTeam().getSize() + "");
                        recipient_id = join_matches.get(i).getJoin_team_id() + "";
                        recipient_name = joinMatchesBean.getTeam().getTeam_name();
                    }
                }
                mTvBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelMatch();//主隊取消球賽
                    }
                });
                mTvIconNoticeRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String chatRef = "";
                        if (mCurrentUser.getCreatedAt() > mRecipient.getCreatedAt()) {
                            chatRef = cleanEmailAddress(mCurrentUser.getEmail()) + "-" + cleanEmailAddress(mRecipient
                                    .getEmail());
                        } else {

                            chatRef = cleanEmailAddress(mRecipient.getEmail()) + "-" + cleanEmailAddress(mCurrentUser
                                    .getEmail());
                        }
                        Intent intent = new Intent();
                        intent.setClass(mContext, ChatActivity.class);
                        intent.putExtra(ExtraIntent.EXTRA_CURRENT_USER_ID, team_id);
                        intent.putExtra(ExtraIntent.EXTRA_RECIPIENT_ID, recipient_id);
                        intent.putExtra(ExtraIntent.EXTRA_CHAT_REF, chatRef);
                        intent.putExtra(ExtraIntent.RECIPIENT_NAME, recipient_name);
                        // Start new activity
                        startActivity(intent);
                    }
                });
                mFlParent.setVisibility(View.VISIBLE);
                break;
            case 2://已落實,我是客隊
                mTvIconNoticeLeft.setVisibility(View.VISIBLE);
                mTvIconShareLeft.setVisibility(View.VISIBLE);
                mTvIconShareRight.setVisibility(View.GONE);
                mTvIconNoticeRight.setVisibility(View.GONE);
                mTvBtn.setText(R.string.withdraw_the_match);
                for (int i = 0; i < join_matches.size(); i++) {
                    if (join_matches.get(i).getStatus().equals("confirmed") && join_matches.get(i).getJoin_team_id()
                            == Integer.parseInt(team_id)) {
                        join_match_id = join_matches.get(i).getId() + "";
                        MatchDetail.MatchBean.JoinMatchesBean joinMatchesBean = join_matches.get(i);
                        mTvVisitorName.setText(joinMatchesBean.getTeam().getTeam_name());
                        mIvVisitorDress.setImageResource(R.drawable.ic_dress_visitor);
                        mIvVisitorDress.setBackgroundColor(MyUtil.getColorInt(joinMatchesBean
                                .getJoin_team_color()));
                        mTvVisitorNum.setText(joinMatchesBean.getTeam().getSize() + "");
                    }
                }
                mTvBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!join_match_id.equals(""))
                            withdrawJoin(join_match_id);//客隊取消參與
                    }
                });
                mTvIconNoticeLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String chatRef = "";
                        if (mCurrentUser.getCreatedAt() > mRecipient.getCreatedAt()) {
                            chatRef = cleanEmailAddress(mCurrentUser.getEmail()) + "-" + cleanEmailAddress(mRecipient
                                    .getEmail());
                        } else {

                            chatRef = cleanEmailAddress(mRecipient.getEmail()) + "-" + cleanEmailAddress(mCurrentUser
                                    .getEmail());
                        }
                        Intent intent = new Intent();
                        intent.setClass(mContext, ChatActivity.class);
                        intent.putExtra(ExtraIntent.EXTRA_CURRENT_USER_ID, PrefUtils.getString(App.APP_CONTEXT,
                                "team_id", null));
                        intent.putExtra(ExtraIntent.EXTRA_RECIPIENT_ID, mMatch.getHome_team().getId() + "");
                        intent.putExtra(ExtraIntent.EXTRA_CHAT_REF, chatRef);
                        intent.putExtra(ExtraIntent.RECIPIENT_NAME, mMatch.getHome_team().getTeam_name());
                        // Start new activity
                        startActivity(intent);
                    }
                });
                mFlParent.setVisibility(View.VISIBLE);
                break;
            case 3://我已參與,主隊未確認
                mTvIconNoticeLeft.setVisibility(View.GONE);
                mTvIconShareLeft.setVisibility(View.GONE);
                mTvIconShareRight.setVisibility(View.GONE);
                mTvIconNoticeRight.setVisibility(View.GONE);
                mTvBtn.setText(R.string.withdraw_the_match);
                for (int i = 0; i < join_matches.size(); i++) {
                    if (join_matches.get(i).getStatus().equals("confirmation_pending") && join_matches.get(i)
                            .getJoin_team_id()
                            == Integer.parseInt(team_id)) {
                        join_match_id = join_matches.get(i).getId() + "";
                        MatchDetail.MatchBean.JoinMatchesBean joinMatchesBean = join_matches.get(i);
                        mTvVisitorName.setText(joinMatchesBean.getTeam().getTeam_name());
                        mIvVisitorDress.setImageResource(R.drawable.ic_dress_unknow);
                        mIvVisitorDress.setBackgroundColor(MyUtil.getColorInt(joinMatchesBean
                                .getJoin_team_color()));
                        mTvVisitorNum.setText(joinMatchesBean.getTeam().getSize() + "");
                    }
                }
                mTvBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!join_match_id.equals(""))
                            withdrawJoin(join_match_id);//客隊取消參與
                    }
                });
                mFlParent.setVisibility(View.VISIBLE);
                break;
            case 4://我是主隊,有隊伍參與,我還未確認(match status = w,join match status = confirmation_pending)
                mTvIconNoticeLeft.setVisibility(View.GONE);
                mTvIconShareLeft.setVisibility(View.GONE);
                mTvIconShareRight.setVisibility(View.GONE);
                mTvIconNoticeRight.setVisibility(View.GONE);
                mTvBtn.setText(R.string.cancel_match);

                mIvVisitorDress.setImageResource(R.drawable.ic_dress_unknow);
                mIvVisitorDress.setBackgroundColor(Color.parseColor("#37AC51"));
                mTvVisitorName.setText("");
                mTvVisitorNum.setText("？");
                mTvBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelMatch();//主隊取消球賽
                    }
                });
//                for (int i = 0; i < join_matches.size(); i++) {
//                    if (mMatch.getHome_team().getId() == Integer.parseInt(team_id) && join_matches.get(i).getStatus()
//                            .equals("confirmation_pending")) {
//                        MatchDetail.MatchBean.JoinMatchesBean joinMatchesBean = join_matches.get(i);
//                        mTvVisitorName.setText(joinMatchesBean.getTeam().getTeam_name());
//                        mIvVisitorDress.setBackgroundColor(MyUtil.getColorInt(joinMatchesBean
//                                .getJoin_team_color()));
//                        mTvVisitorNum.setText(joinMatchesBean.getTeam().getSize() + "");
//                    }
//                }
//                mTvBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(mContext, ConfirmationPendingActivity.class);
//                        intent.putExtra("id", id);
//                        startActivityForResult(intent, REQUEST_CODE_REFRESH);
//                    }
//                });
                mFlParent.setVisibility(View.VISIBLE);
                break;
            case 5://我被邀請,還未接受邀請(match status = i,join match status = invited)
                mTvReduce.setVisibility(View.VISIBLE);
                mTvAdd.setVisibility(View.VISIBLE);
                mTvIconNoticeLeft.setVisibility(View.GONE);
                mTvIconShareLeft.setVisibility(View.GONE);
                mTvIconShareRight.setVisibility(View.GONE);
                mTvIconNoticeRight.setVisibility(View.GONE);
                mTvBtn.setText(R.string.accept_the_invitation);
                for (int i = 0; i < join_matches.size(); i++) {
                    if (mMatch.getStatus().equals("i") && mMatch.getJoin_matches().get(i).getJoin_team_id() == Integer
                            .parseInt(team_id)) {
                        MatchDetail.MatchBean.JoinMatchesBean joinMatchesBean = join_matches.get(i);
                        mTvVisitorName.setText(joinMatchesBean.getTeam().getTeam_name());
                        mIvVisitorDress.setBackgroundColor(MyUtil.getColorInt(joinMatchesBean
                                .getJoin_team_color()));
                        join_team_color = joinMatchesBean.getJoin_team_color();
                        mTvVisitorNum.setText(joinMatchesBean.getTeam().getSize() + "");
                        visitorNum = joinMatchesBean.getTeam().getSize();
                    }
                }
                mTvBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmInvite();
                    }
                });
                mFlParent.setVisibility(View.VISIBLE);
                break;
            case 6://我已邀請別人,別人還未確認(match status = i,join match status = invited)
                mTvIconNoticeLeft.setVisibility(View.GONE);
                mTvIconShareLeft.setVisibility(View.GONE);
                mTvIconShareRight.setVisibility(View.GONE);
                mTvIconNoticeRight.setVisibility(View.GONE);
                mTvBtn.setText(R.string.cancel_invite);
                for (int i = 0; i < join_matches.size(); i++) {
                    if (mMatch.getHome_team().getId() == Integer.parseInt(team_id) && join_matches.get(i).getStatus()
                            .equals("invited")) {
                        join_team_id = join_matches.get(i).getJoin_team_id() + "";
                        MatchDetail.MatchBean.JoinMatchesBean joinMatchesBean = join_matches.get(i);
                        mTvVisitorName.setText(joinMatchesBean.getTeam().getTeam_name());
                        mIvVisitorDress.setBackgroundColor(MyUtil.getColorInt(joinMatchesBean
                                .getJoin_team_color()));
                        mTvVisitorNum.setText(joinMatchesBean.getTeam().getSize() + "");
                    }
                }
                mTvBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showCancelInvitePop();
                    }
                });
                mFlParent.setVisibility(View.VISIBLE);
                break;
            case 7://我不是主隊,也不是客隊,只是來瀏覽的(match status = m,join match status = confirmed?(還不確定))
                mTvIconNoticeLeft.setVisibility(View.GONE);
                mTvIconShareLeft.setVisibility(View.GONE);
                mTvIconShareRight.setVisibility(View.GONE);
                mTvIconNoticeRight.setVisibility(View.GONE);
                mTvBtn.setVisibility(View.GONE);
                for (int i = 0; i < join_matches.size(); i++) {
                    if (mMatch.getStatus().equals("f") && join_matches.get(i).getStatus()
                            .equals("confirmed")) {
                        join_match_id = join_matches.get(i).getId() + "";
                        MatchDetail.MatchBean.JoinMatchesBean joinMatchesBean = join_matches.get(i);
                        mTvVisitorName.setText(joinMatchesBean.getTeam().getTeam_name());
                        mIvVisitorDress.setBackgroundColor(MyUtil.getColorInt(joinMatchesBean
                                .getJoin_team_color()));
                        mTvVisitorNum.setText(joinMatchesBean.getTeam().getSize() + "");
                    }
                }
                mFlParent.setVisibility(View.VISIBLE);
                break;
            case 8://我是主隊或客隊,球賽完成後在作賽記錄頁進入
                getMatchRatings();//判斷是否已打分
                mTvIconNoticeLeft.setVisibility(View.GONE);
                mTvIconShareLeft.setVisibility(View.GONE);
                mTvIconShareRight.setVisibility(View.GONE);
                mTvIconNoticeRight.setVisibility(View.GONE);
                for (int i = 0; i < join_matches.size(); i++) {
                    if (mMatch.getStatus().equals("f") && join_matches.get(i).getStatus()
                            .equals("confirmed")) {
                        join_match_id = join_matches.get(i).getId() + "";
                        MatchDetail.MatchBean.JoinMatchesBean joinMatchesBean = join_matches.get(i);
                        mTvVisitorName.setText(joinMatchesBean.getTeam().getTeam_name());
                        mIvVisitorDress.setBackgroundColor(MyUtil.getColorInt(joinMatchesBean
                                .getJoin_team_color()));
                        mTvVisitorNum.setText(joinMatchesBean.getTeam().getSize() + "");
                    }
                }

                break;
            case 9://球賽詳情頁(無邀請,無主動參與隊伍)
                mTvIconNoticeLeft.setVisibility(View.GONE);
                mTvIconShareLeft.setVisibility(View.GONE);
                mTvIconShareRight.setVisibility(View.GONE);
                mTvIconNoticeRight.setVisibility(View.GONE);
                mTvBtn.setText(R.string.cancel_match);
                mIvVisitorDress.setImageResource(R.drawable.ic_dress_unknow);
                mIvVisitorDress.setBackgroundColor(Color.parseColor("#37AC51"));
                mTvVisitorName.setText("");
                mTvVisitorNum.setText("？");
                mTvBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelMatch();//主隊取消球賽
                    }
                });
                mFlParent.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void getMatchRatings() {
//        http://api.freekick.hk/api/en/matches/<matchID>/match_ratings
//        http://api.freekick.hk/api/zh_Hk/matches/<matchID>/match_ratings
        loadingShow();
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "matches/" + id + "/match_ratings";
        Logger.d(url);
        OkGo.get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Ratings fromJson = gson.fromJson(s, Ratings.class);
                        List<Ratings.RatingsBean> ratings = fromJson.getRatings();
                        if (ratings.size() <= 0) {
                            mTvBtn.setText(R.string.rate);//打分
                            mTvBtn.setVisibility(View.VISIBLE);
                        } else {
                            mTvBtn.setText(R.string.rate);//打分
                            mTvBtn.setVisibility(View.VISIBLE);
                            for (int i = 0; i < ratings.size(); i++) {
                                if (ratings.get(i).getTeam_id() == Integer.parseInt(team_id)) {
                                    mTvBtn.setVisibility(View.GONE);
                                }
                            }
                        }
                        mTvBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(mContext, MatchRateActivity.class);
                                intent.putExtra("match_id", id);
                                startActivity(intent);
                            }
                        });
                        loadingDismiss();
                        mFlParent.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        loadingDismiss();
                        Logger.d(e.getMessage());
                        mFlParent.setVisibility(View.VISIBLE);
                    }
                });
    }

    private String cleanEmailAddress(String email) {
        //replace dot with comma since firebase does not allow dot
        return email.replace(".", "-");
    }

    private void initView() {
        mTvBack.setTypeface(App.mTypeface);
        mTvFriend.setTypeface(App.mTypeface);
        mTvNotice.setTypeface(App.mTypeface);
        mTvIconLocation.setTypeface(App.mTypeface);
        mTvIconNoticeLeft.setTypeface(App.mTypeface);
        mTvIconShareLeft.setTypeface(App.mTypeface);
        mTvIconNoticeRight.setTypeface(App.mTypeface);
        mTvIconShareRight.setTypeface(App.mTypeface);

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

    @OnClick({R.id.tv_back, R.id.tv_friend, R.id.tv_notice, R.id.ll_location, R.id.iv_top_1, R.id.iv_top_2, R.id
            .iv_left_1, R.id.iv_left_2, R.id.iv_left_3, R.id.iv_right_1, R.id.iv_right_2, R.id.iv_right_3, R.id
            .iv_bottom_1, R.id
            .iv_bottom_2, R.id.tv_icon_share_left, R.id.tv_icon_notice_left, R.id.tv_icon_share_right, R.id
            .tv_icon_notice_right, R.id.tv_btn, R.id.tv_reduce, R.id.tv_add})
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
//                showShare();
                shareMsg(getString(R.string.share_to),"快來看一看","快來看比賽了\u3000\u3000"+mMatch.getMatch_url(),null);
                break;
            case R.id.tv_icon_notice_left:

                break;
            case R.id.tv_icon_share_right:
//                showShare();
                shareMsg(getString(R.string.share_to),"快來看一看","快來看比賽了\u3000\u3000"+mMatch.getMatch_url(),null);
                break;
            case R.id.tv_icon_notice_right:
                break;
            case R.id.tv_btn:
                break;
        }
    }
    /**
     * 分享功能(文字圖片無法調和)
     * 微信朋友圈:用圖片可加文字,但此時其他三方應用就只有圖片,沒有文字,故而
     *
     * @param activityTitle
     *            Activity的名字
     * @param msgTitle
     *            消息标题
     * @param msgText
     *            消息内容
     * @param imgPath
     *            图片路径，不分享图片则传null
     */
    public void shareMsg(String activityTitle, String msgTitle, String msgText,
                         String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/*");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }
    /**
     * 主隊取消比賽(是不是只可以在已落實球賽進到這個界面)
     */
    private void cancelMatch() {
//        http://api.freekick.hk/api/en/matches/<matchID>/cancel
        String cancelMatchUrl = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "matches/" + mMatch.getId() +
                "/cancel";
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
                            setResult(RESULT_OK);
                            finish();
                        } else if (fromJson.getErrors() != null) {
                            ToastUtil.toastShort(fromJson.getErrors());
                        } else {
                            ToastUtil.toastShort(getString(R.string.cancel_failed));
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                        ToastUtil.toastShort(getString(R.string.cancel_failed));
                    }
                });
    }

    /**
     * 參與隊取消參與
     */
    private void withdrawJoin(String join_match_id) {
//        http://api.freekick.hk/api/en/join_matches/<joinmatchID>/withdraw
        String withdrawUrl = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "join_matches/" + join_match_id +
                "/withdraw";
        Logger.d(withdrawUrl);
        loadingShow();
        OkGo.put(withdrawUrl)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        WithDraw fromJson = gson.fromJson(s, WithDraw.class);
                        if (fromJson.getJoin_match() != null) {
                            ToastUtil.toastShort(getString(R.string.withdraw_success));
                            setResult(RESULT_OK);
                            finish();
                        } else if (fromJson.getError() != null) {
                            ToastUtil.toastShort(fromJson.getError());
                        } else {
                            ToastUtil.toastShort(getString(R.string.withdraw_failed));
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        ToastUtil.toastShort(getString(R.string.withdraw_failed));
                        loadingDismiss();
                    }
                });
    }

    /**
     * 接受邀請
     */
    private void confirmInvite() {
//        http://api.freekick.hk/api/en/teams/<team ID>/confirm_invite
        String confirmInviteUrl = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "teams/" + team_id +
                "/confirm_invite";
        JsonObject object = new JsonObject();
        object.addProperty("match_id", mMatch.getId() + "");
        object.addProperty("join_team_color", join_team_color);
        object.addProperty("size", visitorNum + "");
        Logger.d(confirmInviteUrl);
        loadingShow();
        OkGo.put(confirmInviteUrl)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        ConfirmInvite fromJson = gson.fromJson(s, ConfirmInvite.class);
                        if (fromJson.getJoin_match() != null) {
                            ToastUtil.toastShort(getString(R.string.comfirm_success));
                            setResult(RESULT_OK);
                            finish();
                        } else if (fromJson.getError() != null) {
                            ToastUtil.toastShort(fromJson.getError());
                        } else {
                            ToastUtil.toastShort(getString(R.string.confirm_failed));
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        ToastUtil.toastShort(getString(R.string.confirm_failed));
                        loadingDismiss();
                    }
                });
    }

    /**
     * 取消邀請的pop
     */
    private void showCancelInvitePop() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_cancel_invite, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        TextView tvno = (TextView) contentView.findViewById(R.id.tv_no);
        TextView tvyes = (TextView) contentView.findViewById(R.id.tv_yes);
        tvno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        tvyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                cancelInvite();
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
        popupWindow.showAtLocation(mFlParent, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.5f);
    }

    /**
     * 取消邀請
     */
    private void cancelInvite() {
        JsonObject object = new JsonObject();
        object.addProperty("invite_team_id", join_team_id + "");
        String cancelInviteUrl = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "matches/" + id +
                "/withdraw_invite";
        Logger.d(cancelInviteUrl);
        Logger.d(object.toString());
        OkGo.put(cancelInviteUrl)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Invite invite = gson.fromJson(s, Invite.class);
                        if (invite.getSuccess() != null) {
                            ToastUtil.toastShort(invite.getSuccess());
                            setResult(RESULT_OK);
                            finish();
                        } else if (invite.getErrors() != null) {
                            ToastUtil.toastShort(invite.getErrors());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }

    private ChildEventListener getChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    switch (type) {
                        case 1://已落實,我是主隊
                            if (dataSnapshot.getKey().equals(team_id)) {
                                mCurrentUser = dataSnapshot.getValue(User.class);
                            } else if (dataSnapshot.getKey().equals(recipient_id)) {
                                mRecipient = dataSnapshot.getValue(User.class);
                            }
                            break;
                        case 2://已落實,我是客隊
                            if (dataSnapshot.getKey().equals(team_id)) {
                                mCurrentUser = dataSnapshot.getValue(User.class);
                            } else if (dataSnapshot.getKey().equals(mMatch.getHome_team().getId() + "")) {
                                mRecipient = dataSnapshot.getValue(User.class);
                            }
                            break;
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
//        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://api.freekick.hk");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本\nhttp://api.freekick.hk");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://api.freekick.hk");
        oks.setImageUrl(MyUtil.getImageUrl(mMatch.getPitch_image()));
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://api.freekick.hk");

// 启动分享GUI
        oks.show(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mChildEventListener != null) {
            mUserRefDatabase.removeEventListener(mChildEventListener);
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REFRESH && resultCode == RESULT_OK) {
//            type =1;
//            initData();
            setResult(RESULT_OK);
            finish();
        }
    }
}
