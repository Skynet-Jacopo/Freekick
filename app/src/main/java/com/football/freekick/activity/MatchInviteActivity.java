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
import com.football.freekick.beans.Recommended;
import com.football.freekick.http.Url;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.google.gson.Gson;
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

    private Context mContext;
    private List<Recommended.TeamsBean> mList = new ArrayList<>();
    private CommonAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_invite);
        mContext = MatchInviteActivity.this;
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        if (mList != null) {
            mList.clear();
        }
        String match_id = getIntent().getStringExtra("match_id");
        Logger.d(Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "matches/" + match_id +
                "/get_recommended_joiner");
        OkGo.get(Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "matches/" + match_id + "/get_recommended_joiner")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        // TODO: 2017/11/19 暫用假數據
                        String str = "{\"teams\":[{\"id\":19,\"team_name\":\"Star\"," +
                                "\"image\":{\"url\":\"/uploads/team/image/19/upload-image-8843737-1509546403.\"}}]}";
                        Gson gson = new Gson();
                        Recommended recommended = gson.fromJson(str, Recommended.class);
                        if (recommended.getTeams() != null) {
                            List<Recommended.TeamsBean> teams = recommended.getTeams();
                            mList.addAll(teams);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }

    private void initView() {
        mTvBack.setTypeface(App.mTypeface);
        mTvFriend.setTypeface(App.mTypeface);
        mTvNotice.setTypeface(App.mTypeface);
        mTvIconLocation.setTypeface(App.mTypeface);

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
            public void convert(ViewHolder holder, Recommended.TeamsBean teamsBean) {
                final int itemPosition = holder.getItemPosition();
                ImageView ivPic = holder.getView(R.id.iv_pic);
                ImageLoaderUtils.displayImage(teamsBean.getImage().getUrl(), ivPic);
                holder.setText(R.id.tv_team_name, teamsBean.getTeam_name());
                holder.setOnClickListener(R.id.tv_invite, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        invite(itemPosition);
                    }
                });
                holder.setOnClickListener(R.id.tv_attention, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        payAttention(itemPosition);
                    }
                });
            }

        };
        mRecyclerRecommended.setAdapter(mAdapter);
    }

    /**
     * 關注
     *
     * @param position
     */
    // TODO: 2017/11/19 這裡是不是缺少字段(判斷是否已關注的)
    private void payAttention(int position) {
        Logger.d(Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "teams/" + mList.get(position).getId() +
                "/follow");
        OkGo.post(Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "teams/" + mList.get(position).getId() +
                "/follow")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }

    /**
     * 邀請
     *
     * @param position
     */
    private void invite(int position) {
        Logger.d(Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "teams/" + mList.get(position).getId() +
                "/unfollow");
        OkGo.delete(Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "teams/" + mList.get(position).getId() +
                "/unfollow")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }

    @OnClick({R.id.tv_back, R.id.tv_friend, R.id.tv_notice, R.id.ll_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
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
