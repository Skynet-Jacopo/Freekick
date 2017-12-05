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
import com.football.freekick.beans.MatchesComing;
import com.football.freekick.beans.WithDraw;
import com.football.freekick.http.Url;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
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

/**
 * 球賽內容頁(已落實球賽)
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
    @Bind(R.id.tv_cancel_match)
    TextView mTvCancelMatch;
    @Bind(R.id.fl_parent)
    FrameLayout mFlParent;
    private MatchesComing.MatchesBean mMatchesBean;
    private int type;
    private int secondPos;//客隊在join_match中的位置
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_content);
        mContext = MatchContentActivity.this;
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        type = getIntent().getIntExtra("type", 1);
        secondPos = getIntent().getIntExtra("secondPos", 0);
        mMatchesBean = getIntent().getParcelableExtra("matchesBean");
        switch (type) {
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
        mTvHomeNum.setText(mMatchesBean.getHome_team().getSize() + "");
        List<MatchesComing.MatchesBean.JoinMatchesBean> join_matches = mMatchesBean.getJoin_matches();
        for (int i = 0; i < join_matches.size(); i++) {
            if (join_matches.get(i).getStatus().equals("confirmed")) {
                MatchesComing.MatchesBean.JoinMatchesBean joinMatchesBean = join_matches.get(i);
                mTvVisitorName.setText(joinMatchesBean.getTeam().getTeam_name());
                mIvVisitorDress.setBackgroundColor(MyUtil.getColorInt(joinMatchesBean.getJoin_team_color()));
                mTvVisitorNum.setText(joinMatchesBean.getTeam().getSize() + "");
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
            .tv_icon_notice_right, R.id.tv_cancel_match})
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
            case R.id.tv_cancel_match:
                showPopupCancel();
                break;
        }
    }

    private void showPopupCancel() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_yes_or_not, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        TextView tvNo = (TextView) contentView.findViewById(R.id.tv_no);
        TextView tvYes = (TextView) contentView.findViewById(R.id.tv_yes);
        TextView tvTitle = (TextView) contentView.findViewById(R.id.tv_title);
        switch (type) {
            case 1://客隊退出比賽
                tvTitle.setText(getString(R.string.withdraw_the_match));
                break;
            case 2://主隊退出比賽
                tvTitle.setText(getString(R.string.cancel_the_match));
                break;
        }
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (type) {
                    case 1://客隊退出比賽
                        withdrawJoin();
                        break;
                    case 2://主隊退出比賽
                        cancelMatch();
                        break;
                }
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
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 主隊取消比賽
     */
    private void cancelMatch() {
//        http://api.freekick.hk/api/en/matches/<matchID>/cancel
        String cancelMatchUrl = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "matches/" + mMatchesBean.getId
                () + "/cancel";
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
    private void withdrawJoin() {
//        http://api.freekick.hk/api/en/join_matches/<joinmatchID>/withdraw
        String withdrawUrl = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "join_matches/" + mMatchesBean
                .getJoin_matches().get(secondPos).getJoin_team_id() + "/withdraw";
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
}
