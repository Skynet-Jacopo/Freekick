package com.football.freekick.activity;

import android.content.Context;
import android.content.Intent;
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
import com.football.freekick.baseadapter.recyclerview.OnItemClickListener;
import com.football.freekick.beans.Follow;
import com.football.freekick.beans.Followings;
import com.football.freekick.beans.Invite;
import com.football.freekick.beans.MatchesComing;
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
import okhttp3.Call;
import okhttp3.Response;

/**
 * 已關注球隊更多界面
 */
public class FollowedTeamsActivity extends BaseActivity {

    @Bind(R.id.tv_back)
    TextView     mTvBack;
    @Bind(R.id.recycler_followed)
    RecyclerView mRecyclerFollowed;
    @Bind(R.id.ll_parent)
    LinearLayout mLlParent;
    private List<Followings.TeamsBean> mFollowingTeams;
    private ArrayList<MatchesComing.MatchesBean> mListWait = new ArrayList<>();
    private Context       mContext;
    private CommonAdapter mAdapter;
    private String        team_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followed_teams);
        mContext = FollowedTeamsActivity.this;
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        team_id = PrefUtils.getString(App.APP_CONTEXT, "team_id", null);
        getFollowedTeams();
    }

    /**
     * 獲取已關注球隊
     */
    private void getFollowedTeams() {
        if (mFollowingTeams != null) {
            mFollowingTeams.clear();
        }
        loadingShow();
        String urlAttention = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "users/" + team_id + "/followings";
        Logger.d(urlAttention);
        OkGo.get(urlAttention)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson       gson     = new Gson();
                        Followings fromJson = gson.fromJson(s, Followings.class);
                        if (fromJson.getTeams().size() > 0) {
                            mFollowingTeams.addAll(fromJson.getTeams());
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                    }
                });

    }

    private void initView() {
        mTvBack.setTypeface(App.mTypeface);
        mFollowingTeams = new ArrayList<>();
        if (mRecyclerFollowed != null) {
            mRecyclerFollowed.setHasFixedSize(true);
        }
        mRecyclerFollowed.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new CommonAdapter<Followings.TeamsBean>(mContext, R.layout
                .item_same_area_more, mFollowingTeams) {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, Followings.TeamsBean teamsBean) {
                final int itemPosition = holder.getItemPosition();
                ImageView ivPic        = holder.getView(R.id.iv_pic);
                holder.setText(R.id.tv_attention, getString(R.string.unfollow));
                ImageLoaderUtils.displayImage(MyUtil.getImageUrl(teamsBean.getImage().getUrl()), ivPic, R.drawable
                        .icon_default);
                holder.setText(R.id.tv_team_name, teamsBean.getTeam_name());
                holder.setOnClickListener(R.id.tv_attention, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        unfollow(itemPosition);
                    }
                });
                holder.setOnClickListener(R.id.tv_fight, new View.OnClickListener() {
                    //約戰
                    @Override
                    public void onClick(View view) {
                        invitePopup(itemPosition);
                    }
                });
            }

        };
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Intent intent = new Intent(mContext, TeamDetailActivity.class);
                intent.putExtra("id", mFollowingTeams.get(position).getId() + "");
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        mRecyclerFollowed.setAdapter(mAdapter);
    }

    /**
     * 取消關注
     *
     * @param position
     */
    private void unfollow(final int position) {
        loadingShow();
        Logger.d(Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "teams/" + mFollowingTeams.get(position).getId() +
                "/unfollow");
        OkGo.delete(Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "teams/" + mFollowingTeams.get(position).getId() +
                "/unfollow")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson   gson   = new Gson();
                        Follow follow = gson.fromJson(s, Follow.class);
                        if (follow.getSuccess() != null) {
                            ToastUtil.toastShort(follow.getSuccess());
                            mFollowingTeams.remove(position);
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
     * 邀請球隊參與Pop
     *
     * @param itemPosition
     */
    private void invitePopup(final int itemPosition) {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_invite_to_not_matched, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        TextView tviconclose = (TextView) contentView.findViewById(R.id.tv_icon_close);
        tviconclose.setTypeface(App.mTypeface);
        TextView tvnewmatch         = (TextView) contentView.findViewById(R.id.tv_new_match);
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
                getMyFirstMatch(itemPosition);//取我的未落實球賽的第一場
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
     * 取我的未落實球賽的第一場
     *
     * @param itemPosition
     */
    private void getMyFirstMatch(final int itemPosition) {
        final String team_id = PrefUtils.getString(App.APP_CONTEXT, "team_id", null);
        loadingShow();
        OkGo.get(Url.MATCHES_COMING)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        loadingDismiss();
                        Gson                            gson    = new Gson();
                        MatchesComing                   json    = gson.fromJson(s, MatchesComing.class);
                        List<MatchesComing.MatchesBean> matches = json.getMatches();
                        if (matches != null && matches.size() > 0)
                            for (int i = 0; i < matches.size(); i++) {
                                for (int j = 0; j < App.mPitchesBeanList.size(); j++) {
                                    if (matches.get(i).getPitch_id() == App.mPitchesBeanList.get(j).getId()) {
                                        matches.get(i).setLocation(App.mPitchesBeanList.get(j).getLocation());
                                        matches.get(i).setPitch_name(App.mPitchesBeanList.get(j).getName());
                                    }
                                }
                                if (matches.get(i).getStatus().equals("w")) {
                                    if (matches.get(i).getHome_team().getId() == Integer.parseInt(team_id)) {
                                        mListWait.add(matches.get(i));
                                        //是否要把主動參與的隊伍去除之後取第一條未落實球賽
                                    }
                                }
                            }
                        if (mListWait.size() <= 0) {
                            ToastUtil.toastShort(getString(R.string.there_is_not_available_matches));
                        } else {
                            showPopupInvite(itemPosition);
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
     * 取第一條未落實球賽的pop
     *
     * @param itemPosition
     */
    private void showPopupInvite(final int itemPosition) {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_invite_to_match, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        TextView                  tvnewmatch  = (TextView) contentView.findViewById(R.id.tv_new_match);
        TextView                  tvstate     = (TextView) contentView.findViewById(R.id.tv_state);
        TextView                  tvLocation  = (TextView) contentView.findViewById(R.id.tv_location);
        TextView                  tvTime      = (TextView) contentView.findViewById(R.id.tv_time);
        TextView                  tvHomeName  = (TextView) contentView.findViewById(R.id.tv_home_name);
        ImageView                 ivHomeLogo  = (ImageView) contentView.findViewById(R.id.iv_home_logo);
        TextView                  tvDate      = (TextView) contentView.findViewById(R.id.tv_date);
        MatchesComing.MatchesBean matchesBean = mListWait.get(0);
        tvHomeName.setText(matchesBean.getHome_team().getTeam_name());
        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(matchesBean.getHome_team().getImage().getUrl()),
                ivHomeLogo, R.drawable.icon_default);

        String date = JodaTimeUtil.getDate2(matchesBean.getPlay_start());
        tvDate.setText(date);
        String start = JodaTimeUtil.getTime2(matchesBean.getPlay_start());
        String end   = JodaTimeUtil.getTime2(matchesBean.getPlay_end());
        tvTime.setText(start + " - " + end);
        tvLocation.setText(matchesBean.getLocation());
        tvstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                invite(itemPosition);
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
        String     inviteUrl = Url.MATCHES_INVITE;
        JsonObject object    = new JsonObject();
        object.addProperty("invite_team_id", mFollowingTeams.get(position).getId() + "");
        Logger.d(inviteUrl);
        Logger.d(object.toString());
        OkGo.post(inviteUrl)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson   gson   = new Gson();
                        Invite invite = gson.fromJson(s, Invite.class);
                        if (invite.getSuccess() != null) {
                            ToastUtil.toastShort(invite.getSuccess());
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
}
