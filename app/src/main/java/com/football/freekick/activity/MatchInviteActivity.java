package com.football.freekick.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.MainActivity;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.baseadapter.ViewHolder;
import com.football.freekick.baseadapter.recyclerview.CommonAdapter;
import com.football.freekick.beans.Attention;
import com.football.freekick.beans.Follow;
import com.football.freekick.beans.Invite;
import com.football.freekick.beans.MatchDetail;
import com.football.freekick.beans.Recommended;
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
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 球賽邀請頁
 */
public class MatchInviteActivity extends BaseActivity {

    @Bind(R.id.tv_back)
    TextView mTvBack;
    @Bind(R.id.tv_friend)
    TextView mTvFriend;
    @Bind(R.id.tv_notice)
    TextView mTvNotice;
    @Bind(R.id.iv_pic)
    ImageView mIvPic;
    @Bind(R.id.tv_pitch_name)
    TextView mTvPitchName;
    @Bind(R.id.tv_icon_location)
    TextView mTvIconLocation;
    @Bind(R.id.tv_location)
    TextView mTvLocation;
    @Bind(R.id.ll_location)
    LinearLayout mLlLocation;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.iv_dress_home)
    ImageView mIvDressHome;
    @Bind(R.id.iv_dress_visitor)
    ImageView mIvDressVisitor;
    @Bind(R.id.recycler_recommended)
    RecyclerView mRecyclerRecommended;
    @Bind(R.id.ll_parent)
    LinearLayout mLlParent;
    @Bind(R.id.tv_home_name)
    TextView mTvHomeName;
    @Bind(R.id.tv_visitor_name)
    TextView mTvVisitorName;

    private Context mContext;
    private List<Recommended.TeamsBean> mList = new ArrayList<>();
    private CommonAdapter mAdapter;
    private String match_id;
    private MatchDetail.MatchBean mMatch;
    private String team_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_invite);
        mContext = MatchInviteActivity.this;
        ButterKnife.bind(this);
        team_id = PrefUtils.getString(App.APP_CONTEXT, "team_id", null);
        initView();
        initMatchData();
//        intent.putExtra("invited_team_id",invited_team_id);
//        intent.putExtra("invited_team_name",invited_team_name);
//        intent.putExtra("invited_team_url",invited_team_url);
        if (getIntent().getStringExtra("invited_team_name") == null) {
            //沒有邀請球隊進行新球賽
            initRecommendData();//獲取推薦球隊列表
        }else {
            //邀請球隊進入新球賽
            initInvitedTeam();
        }

    }

    /**
     * 獲取球賽詳情
     */
    private void initMatchData() {
        if (mList != null) {
            mList.clear();
        }
        match_id = getIntent().getStringExtra("match_id");
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
     * 初始化邀請的單個球隊數據
     */
    private void initInvitedTeam() {
        if (mList != null) {
            mList.clear();
        }
        int invited_team_id = getIntent().getIntExtra("invited_team_id", 0);
        String invited_team_name = getIntent().getStringExtra("invited_team_name");
        String invited_team_url = getIntent().getStringExtra("invited_team_url");
        Recommended.TeamsBean teamsBean = new Recommended.TeamsBean();
        teamsBean.setId(invited_team_id);
        teamsBean.setTeam_name(invited_team_name);
        Recommended.TeamsBean.ImageBean imageBean = new Recommended.TeamsBean.ImageBean();
        imageBean.setUrl(invited_team_url);
        teamsBean.setImage(imageBean);

        mList.add(teamsBean);
        mAdapter.notifyDataSetChanged();

        getFollowedTeams();
    }

    private void initRecommendData() {
//        loadingShow();
        Logger.d(Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "matches/" + match_id +
                "/get_recommended_joiner");
        OkGo.get(Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "matches/" + match_id + "/get_recommended_joiner")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
//                        loadingDismiss();
                        // TODO: 2017/11/19 暫用假數據
                        String str = "{\"teams\":[{\"id\":19,\"team_name\":\"Star\"," +
                                "\"image\":{\"url\":\"/uploads/team/image/19/upload-image-8843737-1509546403.\"}}]}";
                        Gson gson = new Gson();
                        Recommended recommended = gson.fromJson(str, Recommended.class);
                        if (recommended.getTeams() != null) {
                            List<Recommended.TeamsBean> teams = recommended.getTeams();
                            mList.addAll(teams);
                            mAdapter.notifyDataSetChanged();
                            getFollowedTeams();
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
     * 獲取球賽的詳細信息展示
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
        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mMatch.getPitch_image()), mIvPic, R
                .drawable.icon_default);
        mTvPitchName.setText(mMatch.getPitch_name());
        mTvLocation.setText(mMatch.getLocation());
        mTvTime.setText(JodaTimeUtil.getTime2(mMatch.getPlay_start()) + "-" + JodaTimeUtil
                .getTime2(mMatch.getPlay_end()));
        mTvHomeName.setText(mMatch.getHome_team().getTeam_name() == null ? "" : mMatch.getHome_team().getTeam_name());
        mIvDressHome.setBackgroundColor(MyUtil.getColorInt(mMatch.getHome_team_color()));
        loadingDismiss();
    }

    private void initView() {
        mTvBack.setTypeface(App.mTypeface);
        mTvFriend.setTypeface(App.mTypeface);
        mTvNotice.setTypeface(App.mTypeface);
        mTvIconLocation.setTypeface(App.mTypeface);
        mTvHomeName.setText(PrefUtils.getString(App.APP_CONTEXT, "team_name", null));
        mIvDressHome.setBackgroundColor(Color.parseColor("#" + PrefUtils.getString(App.APP_CONTEXT, "color1", null)));
        mIvDressVisitor.setImageDrawable(getResources().getDrawable(R.drawable.ic_dress_unknow));

        if (mRecyclerRecommended != null) {
            mRecyclerRecommended.setHasFixedSize(true);
        }
        mRecyclerRecommended.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new CommonAdapter<Recommended.TeamsBean>(mContext, R.layout
                .item_recycler_recommended, mList) {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, final Recommended.TeamsBean teamsBean) {
                final int itemPosition = holder.getItemPosition();
                ImageView ivPic = holder.getView(R.id.iv_pic);
                ImageLoaderUtils.displayImage(teamsBean.getImage().getUrl(), ivPic);
                holder.setText(R.id.tv_team_name, teamsBean.getTeam_name());
                holder.setOnClickListener(R.id.tv_invite, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getIntent().getStringExtra("invited_team_name") == null){
                            invitePopup(itemPosition);
                        }else {
                            invite(0);//直接邀請這支隊伍進行這場球賽
                        }
                    }
                });
                if (teamsBean.isAttention()) {
                    holder.setText(R.id.tv_attention, getString(R.string.unfollow));
                    holder.setOnClickListener(R.id.tv_attention, new View.OnClickListener() {
                        //已關注
                        @Override
                        public void onClick(View view) {
                            unfollow(itemPosition);
                        }
                    });
                } else {
                    holder.setText(R.id.tv_attention, getString(R.string.follow));
                    holder.setOnClickListener(R.id.tv_attention, new View.OnClickListener() {
                        //關注
                        @Override
                        public void onClick(View view) {
                            follow(itemPosition);
                        }
                    });
                }
                holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(mContext,TeamDetailActivity.class);
                        intent.putExtra("id",teamsBean.getId()+"");
                        startActivity(intent);
                    }
                });
            }

        };
        mRecyclerRecommended.setAdapter(mAdapter);
    }
    @OnClick({R.id.tv_back, R.id.tv_friend, R.id.tv_notice, R.id.ll_location})
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
                break;
        }
    }

    /**
     * 邀請球隊參與Pop
     *
     * @param itemPosition
     */
    private void invitePopup(final int itemPosition) {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_choose_invite, null);

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
                startActivity(intent);
                finish();
            }
        });
        tvpartakethismatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invite(itemPosition);//取我的未落實球賽的第一場
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
        popupWindow.showAtLocation(mLlParent, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.5f);
    }

    /**
     * 邀請
     *
     * @param position
     */
    private void invite(int position) {
        //http://api.freekick.hk/api/en/matches/invite
        loadingShow();
        String inviteUrl = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "matches/" + match_id + "/invite";
        JsonObject object = new JsonObject();
        object.addProperty("invite_team_id", mList.get(position).getId() + "");
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
                            setResult(RESULT_OK);
                            Intent intent = new Intent(mContext, MainActivity.class);

                            intent.putExtra("which", 4);
                            intent.putExtra("toPage","1");
                            startActivity(intent);
                            finish();
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

    /**
     * 獲取已關注球隊與同區球隊做比較,得出關注與否
     */
    private void getFollowedTeams() {
//        loadingShow();
        String urlAttention = BaseUrl + (App.isChinese ? ZH_HK : EN) + "users/" + team_id + "/followings";
        Logger.d(urlAttention);
        OkGo.get(urlAttention)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Attention attention = gson.fromJson(s, Attention.class);
                        List<Attention.TeamsBean> attentionTeams = attention.getTeams();
                        for (int i = 0; i < attentionTeams.size(); i++) {
                            for (int j = 0; j < mList.size(); j++) {
                                if (attentionTeams.get(i).getId() == mList.get(j).getId()) {
                                    mList.get(j).setAttention(true);
                                } else {
//                                    mTeams.get(j).setAttention(false);
                                }
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        loadingDismiss();
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
     * 取消關注
     *
     * @param position
     */
    private void unfollow(final int position) {
        loadingShow();
        Logger.d(BaseUrl + (App.isChinese ? ZH_HK : EN) + "teams/" + mList.get(position).getId() +
                "/unfollow");
        OkGo.delete(BaseUrl + (App.isChinese ? ZH_HK : EN) + "teams/" + mList.get(position).getId() +
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
                            mList.get(position).setAttention(false);
                            mAdapter.notifyDataSetChanged();
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
     *
     * @param position
     */
    // TODO: 2017/11/19 這裡是不是缺少字段(判斷是否已關注的)
    private void follow(final int position) {
        loadingShow();
        Logger.d(BaseUrl + (App.isChinese ? ZH_HK : EN) + "teams/" + mList.get(position).getId() +
                "/follow");
        OkGo.post(BaseUrl + (App.isChinese ? ZH_HK : EN) + "teams/" + mList.get(position).getId() +
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
                            mList.get(position).setAttention(true);
                            mAdapter.notifyDataSetChanged();
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
}
