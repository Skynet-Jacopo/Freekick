package com.football.freekick.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
import com.football.freekick.beans.CancelMatch;
import com.football.freekick.beans.ConfirmInvite;
import com.football.freekick.beans.Invite;
import com.football.freekick.beans.MatchDetail;
import com.football.freekick.beans.WithDraw;
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

    public static final int REQUEST_CODE_REFRESH = 1;
    @Bind(R.id.tv_back)
    TextView     mTvBack;
    @Bind(R.id.tv_friend)
    TextView     mTvFriend;
    @Bind(R.id.tv_notice)
    TextView     mTvNotice;
    @Bind(R.id.iv_top_1)
    ImageView    mIvTop1;
    @Bind(R.id.iv_top_2)
    ImageView    mIvTop2;
    @Bind(R.id.ll_top)
    LinearLayout mLlTop;
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
    @Bind(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @Bind(R.id.tv_date)
    TextView     mTvDate;
    @Bind(R.id.tv_icon_location)
    TextView     mTvIconLocation;
    @Bind(R.id.tv_location)
    TextView     mTvLocation;
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
    @Bind(R.id.iv_visitor_dress)
    ImageView    mIvVisitorDress;
    @Bind(R.id.tv_visitor_name)
    TextView     mTvVisitorName;
    @Bind(R.id.tv_reduce)
    TextView     mTvReduce;
    @Bind(R.id.tv_visitor_num)
    TextView     mTvVisitorNum;
    @Bind(R.id.tv_add)
    TextView     mTvAdd;
    @Bind(R.id.ll_team)
    LinearLayout mLlTeam;
    @Bind(R.id.tv_icon_share_left)
    TextView     mTvIconShareLeft;
    @Bind(R.id.tv_icon_notice_left)
    TextView     mTvIconNoticeLeft;
    @Bind(R.id.tv_icon_share_right)
    TextView     mTvIconShareRight;
    @Bind(R.id.tv_icon_notice_right)
    TextView     mTvIconNoticeRight;
    @Bind(R.id.fl_parent)
    FrameLayout  mFlParent;
    @Bind(R.id.tv_btn)
    TextView     mTvBtn;

    private String                id;
    private String                where;
    private int                   type;
    private Context               mContext;
    private MatchDetail.MatchBean mMatch;
    private String                team_id;
    private String join_match_id = "";
    private String join_team_id = "";

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

    private void initData() {
        loadingShow();
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "matches/";//+matchID
        Logger.d(url + id);
        OkGo.get(url + id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson        gson     = new Gson();
                        MatchDetail fromJson = gson.fromJson(s, MatchDetail.class);
                        if (fromJson.getMatch() != null) {
                            mMatch = fromJson.getMatch();
                            setData();
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

    /**
     * 將獲取下來的數據添加到界面
     */
    private void setData() {

        for (int i = 0; i < App.mPitchesBeanList.size(); i++) {
            if (mMatch.getPitch_id() == App.mPitchesBeanList.get(i).getId()) {
                mMatch.setLocation(App.mPitchesBeanList.get(i).getLocation());
                mMatch.setPitch_name(App.mPitchesBeanList.get(i).getName());
            }
        }
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
         */
        List<MatchDetail.MatchBean.JoinMatchesBean> join_matches = mMatch.getJoin_matches();
        switch (type) {
            case 1://已落實,我是主隊
                mTvBtn.setText(R.string.cancel_match);
                for (int i = 0; i < join_matches.size(); i++) {
                    if (join_matches.get(i).getStatus().equals("confirmed")) {
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
                        cancelMatch();//主隊取消球賽
                    }
                });
                break;
            case 2://已落實,我是客隊
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
                break;
            case 3://我已參與,主隊未確認
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
                break;
            case 4://我是主隊,有隊伍參與,我還未確認(match status = w,join match status = confirmation_pending)
                mTvBtn.setText(R.string.decide_to_play);
                for (int i = 0; i < join_matches.size(); i++) {
                    if (mMatch.getHome_team().getId() == Integer.parseInt(team_id) && join_matches.get(i).getStatus()
                            .equals("confirmation_pending")) {
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
                        Intent intent = new Intent(mContext, ConfirmationPendingActivity.class);
                        intent.putExtra("id", id);
                        startActivityForResult(intent, REQUEST_CODE_REFRESH);
                    }
                });
                break;
            case 5://我被邀請,還未接受邀請(match status = i,join match status = invited)
                mTvBtn.setText(R.string.accept_the_invitation);
                for (int i = 0; i < join_matches.size(); i++) {
                    if (mMatch.getStatus().equals("i") && mMatch.getJoin_matches().get(i).getJoin_team_id() == Integer
                            .parseInt(team_id)) {
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
                        confirmInvite();
                    }
                });
                break;
            case 6://我已邀請別人,別人還未確認(match status = i,join match status = invited)
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
                break;
            case 7://我不是主隊,也不是客隊,只是來瀏覽的(match status = m,join match status = confirmed?(還不確定))
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
                break;

        }
    }

    private void initView() {
        mTvBack.setTypeface(App.mTypeface);
        mTvFriend.setTypeface(App.mTypeface);
        mTvNotice.setTypeface(App.mTypeface);
        mTvIconLocation.setTypeface(App.mTypeface);

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

    @OnClick({R.id.tv_back, R.id.tv_friend, R.id.tv_notice,R.id.ll_location, R.id.iv_top_1, R.id.iv_top_2, R.id.iv_left_1, R.id
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
                        Gson        gson     = new Gson();
                        CancelMatch fromJson = gson.fromJson(s, CancelMatch.class);
                        if (fromJson.getMatch() != null) {
                            ToastUtil.toastShort(getString(R.string.withdraw_success));
                            setResult(RESULT_OK);
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
                        Gson     gson     = new Gson();
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
        String     confirmInviteUrl = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "teams/" + team_id + "/confirm_invite";
        JsonObject object           = new JsonObject();
        object.addProperty("match_id", mMatch.getId() + "");
        Logger.d(confirmInviteUrl);
        loadingShow();
        OkGo.put(confirmInviteUrl)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson          gson     = new Gson();
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
        TextView tvno  = (TextView) contentView.findViewById(R.id.tv_no);
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
     *
     */
    private void cancelInvite() {
        JsonObject object = new JsonObject();
        object.addProperty("invite_team_id", join_team_id+ "");
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
                        Gson   gson   = new Gson();
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
