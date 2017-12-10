package com.football.freekick.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.baseadapter.ViewHolder;
import com.football.freekick.baseadapter.recyclerview.CommonAdapter;
import com.football.freekick.beans.ConfirmMatch;
import com.football.freekick.beans.MatchDetail;
import com.football.freekick.http.Url;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 在參與的球隊中選一隊作賽
 */
public class ConfirmationPendingActivity extends BaseActivity {

    @Bind(R.id.tv_back)
    TextView     mTvBack;
    @Bind(R.id.tv_friend)
    TextView     mTvFriend;
    @Bind(R.id.tv_notice)
    TextView     mTvNotice;
    @Bind(R.id.iv_pic)
    ImageView    mIvPic;
    @Bind(R.id.tv_pitch_name)
    TextView     mTvPitchName;
    @Bind(R.id.tv_icon_location)
    TextView     mTvIconLocation;
    @Bind(R.id.tv_location)
    TextView     mTvLocation;
    @Bind(R.id.ll_location)
    LinearLayout mLlLocation;
    @Bind(R.id.tv_time)
    TextView     mTvTime;
    @Bind(R.id.iv_dress_home)
    ImageView    mIvDressHome;
    @Bind(R.id.tv_home_name)
    TextView     mTvHomeName;
    @Bind(R.id.iv_dress_visitor)
    ImageView    mIvDressVisitor;
    @Bind(R.id.tv_visitor_name)
    TextView     mTvVisitorName;
    @Bind(R.id.recycler_confirmation_pending)
    RecyclerView mRecyclerConfirmationPending;
    @Bind(R.id.ll_parent)
    LinearLayout mLlParent;
    private String                                      id;
    private MatchDetail.MatchBean                       mMatch;
    private Context                                     mContext;
    private List<MatchDetail.MatchBean.JoinMatchesBean> join_matches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_pending);
        mContext = ConfirmationPendingActivity.this;
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id") == null ? "" : getIntent().getStringExtra("id");
        initView();
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

    private void setData() {
        for (int i = 0; i < App.mPitchesBeanList.size(); i++) {
            if (mMatch.getPitch_id() == App.mPitchesBeanList.get(i).getId()) {
                mMatch.setLocation(App.mPitchesBeanList.get(i).getLocation());
                mMatch.setPitch_name(App.mPitchesBeanList.get(i).getName());
            }
        }
        mTvPitchName.setText(mMatch.getPitch_name());
        mTvLocation.setText(mMatch.getLocation());
        mTvTime.setText(JodaTimeUtil.getTime2(mMatch.getPlay_start()) + " - " + JodaTimeUtil
                .getTime2(mMatch.getPlay_end()));
        mIvDressHome.setBackgroundColor(MyUtil.getColorInt(mMatch.getHome_team_color()));
        mTvHomeName.setText(mMatch.getHome_team().getTeam_name() == null ? "" : mMatch.getHome_team().getTeam_name());


        join_matches = mMatch.getJoin_matches();
        if (mRecyclerConfirmationPending != null) {
            mRecyclerConfirmationPending.setHasFixedSize(true);
        }
        mRecyclerConfirmationPending.setLayoutManager(new LinearLayoutManager(mContext));
        CommonAdapter adapter = new CommonAdapter<MatchDetail.MatchBean.JoinMatchesBean>(mContext, R.layout
                .item_confirmation_pending, join_matches) {


            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, MatchDetail.MatchBean.JoinMatchesBean joinMatchesBean) {
                final int itemPosition = holder.getItemPosition();
                ImageView ivPic        = holder.getView(R.id.iv_pic);
                ImageLoaderUtils.displayImage(MyUtil.getImageUrl(joinMatchesBean.getTeam().getImage().getUrl()),
                        ivPic, R.drawable.icon_default);
                holder.setText(R.id.tv_team_name, joinMatchesBean.getTeam().getTeam_name());
                holder.setOnClickListener(R.id.tv_confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //從參與的隊伍中取一隊決定作賽
//                        http:// api.freekick.hk/api/en/join_matches/<joinmatchID>/confirm
                        confirm(itemPosition);
                    }
                });
            }
        };
        mRecyclerConfirmationPending.setAdapter(adapter);
    }

    /**
     * 主隊挑選參與隊確定作賽
     *
     * @param itemPosition
     */
    private void confirm(int itemPosition) {
        loadingShow();
        String url = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "join_matches/" + join_matches.get
                (itemPosition).getId() + "/confirm";
        Logger.d(url);
        OkGo.put(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson         gson     = new Gson();
                        ConfirmMatch fromJson = gson.fromJson(s, ConfirmMatch.class);
                        if (fromJson.getJoin_match() != null) {
                            ToastUtil.toastShort(getString(R.string.match_success));
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
                        loadingDismiss();
                        ToastUtil.toastShort(getString(R.string.confirm_failed));
                    }
                });
    }

    private void initView() {
        mTvBack.setTypeface(App.mTypeface);
        mTvFriend.setTypeface(App.mTypeface);
        mTvNotice.setTypeface(App.mTypeface);
        mTvIconLocation.setTypeface(App.mTypeface);
    }

    @OnClick({R.id.tv_back, R.id.tv_friend, R.id.tv_notice, R.id.ll_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                break;
            case R.id.tv_friend:
                break;
            case R.id.tv_notice:
                break;
            case R.id.ll_location:
                break;
        }
    }
}
