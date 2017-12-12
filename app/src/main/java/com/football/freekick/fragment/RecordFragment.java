package com.football.freekick.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.activity.FollowedTeamsActivity;
import com.football.freekick.activity.MatchContentActivity1;
import com.football.freekick.activity.SameAreaTeamActivity;
import com.football.freekick.activity.TeamDetailActivity;
import com.football.freekick.app.BaseFragment;
import com.football.freekick.baseadapter.ViewHolder;
import com.football.freekick.baseadapter.recyclerview.CommonAdapter;
import com.football.freekick.baseadapter.recyclerview.OnItemClickListener;
import com.football.freekick.beans.Followings;
import com.football.freekick.beans.MatchHistory;
import com.football.freekick.beans.SameArea;
import com.football.freekick.http.Url;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
 * 作賽記錄頁.
 */
public class RecordFragment extends BaseFragment {


    public static final int REQUEST_CODE_TO_REFRESH = 1;
    @Bind(R.id.tv_icon_search)
    TextView mTvIconSearch;
    @Bind(R.id.edt_search_team)
    EditText mEdtSearchTeam;
    @Bind(R.id.iv_pic)
    ImageView mIvPic;
    @Bind(R.id.recycler_record)
    RecyclerView mRecyclerRecord;
    @Bind(R.id.ll_same_area_more)
    LinearLayout mLlSameAreaMore;
    @Bind(R.id.recycler_same_area)
    RecyclerView mRecyclerSameArea;
    @Bind(R.id.ll_attention_more)
    LinearLayout mLlAttentionMore;
    @Bind(R.id.recycler_attention)
    RecyclerView mRecyclerAttention;

    private Context mContext;
    private List<MatchHistory.MatchesBean> mMatches;
    private List<MatchHistory.MatchesBean> mListFinished;
    private List<String> datas = new ArrayList<>();
    private CommonAdapter mRecorAdapter;
    private List<SameArea.TeamBean> mTeams;
    private CommonAdapter mSameAreaAdapter;
    private List<Followings.TeamsBean> mFollowingTeams;
    private CommonAdapter mAttentionAdapter;
    private String team_id;
    private String district_id;

    public RecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        district_id = PrefUtils.getString(App.APP_CONTEXT, "district_id", null);
        initView();
        initData();
        initRecordList();//初始化作戰記錄列表
        initSameAreaList();//初始化同區球隊列表
        initAttention();//初始化已關注球隊列表
    }

    private void initAttention() {
        if (mRecyclerAttention != null) {
            mRecyclerAttention.setHasFixedSize(true);
        }
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerAttention.setLayoutManager(manager);
        mAttentionAdapter = new CommonAdapter<Followings.TeamsBean>(mContext, R.layout.item_same_area,
                mFollowingTeams) {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, Followings.TeamsBean teamsBean) {
                holder.setText(R.id.tv_team_name, teamsBean.getTeam_name());
                ImageView ivLogo = holder.getView(R.id.iv_logo);
                ImageLoaderUtils.displayImage(MyUtil.getImageUrl(teamsBean.getImage().getUrl()), ivLogo, R.drawable
                        .icon_default);
            }
        };
        mAttentionAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Intent intent = new Intent(mContext, TeamDetailActivity.class);
                intent.putExtra("id", mFollowingTeams.get(position).getId() + "");
                startActivityForResult(intent, REQUEST_CODE_TO_REFRESH);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        mRecyclerAttention.setAdapter(mAttentionAdapter);
    }

    /**
     * 初始化同區球隊列表
     */
    private void initSameAreaList() {
        if (mRecyclerSameArea != null) {
            mRecyclerSameArea.setHasFixedSize(true);
        }
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerSameArea.setLayoutManager(manager);
        mSameAreaAdapter = new CommonAdapter<SameArea.TeamBean>(mContext, R.layout.item_same_area, mTeams) {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, SameArea.TeamBean teamsBean) {
                holder.setText(R.id.tv_team_name, teamsBean.getTeam_name());
                ImageView ivLogo = holder.getView(R.id.iv_logo);
                ImageLoaderUtils.displayImage(MyUtil.getImageUrl(teamsBean.getImage().getUrl()), ivLogo, R.drawable
                        .icon_default);
            }
        };
        mSameAreaAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Intent intent = new Intent(mContext, TeamDetailActivity.class);
                intent.putExtra("id", mTeams.get(position).getId() + "");
                startActivityForResult(intent, REQUEST_CODE_TO_REFRESH);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        mRecyclerSameArea.setAdapter(mSameAreaAdapter);
    }

    /**
     * 初始化作戰記錄列表
     */
    private void initRecordList() {
        if (mRecyclerRecord != null) {
            mRecyclerRecord.setHasFixedSize(true);
        }
        mRecyclerRecord.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerRecord.setNestedScrollingEnabled(false);
        mRecorAdapter = new CommonAdapter<MatchHistory.MatchesBean>(mContext, R.layout.item_record, mListFinished) {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, MatchHistory.MatchesBean matchesBean) {
                holder.setText(R.id.tv_location, matchesBean.getLocation());

                holder.setText(R.id.tv_date, JodaTimeUtil.getDate2(matchesBean.getPlay_start()));
                String start = JodaTimeUtil.getTime2(matchesBean.getPlay_start());
                String end = JodaTimeUtil.getTime2(matchesBean.getPlay_end());
                holder.setText(R.id.tv_time, start + " - " + end);
                holder.setText(R.id.tv_home_name, matchesBean.getHome_team().getTeam_name());
                ImageView ivHomeLogo = holder.getView(R.id.iv_home_logo);
                ImageLoaderUtils.displayImage(MyUtil.getImageUrl(matchesBean.getHome_team().getImage().getUrl()),
                        ivHomeLogo, R.drawable.icon_default);
                List<MatchHistory.MatchesBean.JoinMatchesBean> join_matches = matchesBean.getJoin_matches();

                for (int i = 0; i < join_matches.size(); i++) {
                    if (join_matches.get(i).getStatus().equals("confirmed")) {
                        holder.setText(R.id.tv_visitor_name, join_matches.get(i).getTeam().getTeam_name());
                        ImageView ivVisitorLogo = holder.getView(R.id.iv_visitor_logo);

                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(join_matches.get(i).getTeam().getImage()
                                .getUrl()), ivVisitorLogo, R.drawable.icon_default);
                    }
                }
            }
        };
        mRecorAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Intent intent = new Intent(mContext, MatchContentActivity1.class);
                intent.putExtra("id", mListFinished.get(position).getId() + "");
                intent.putExtra("type", 7);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        mRecyclerRecord.setAdapter(mRecorAdapter);
    }

    private void initData() {
        if (mMatches != null) {
            mMatches.clear();
        }
        if (mListFinished != null) {
            mListFinished.clear();
        }
        if (mTeams != null) {
            mTeams.clear();
        }
        loadingShow();
        team_id = PrefUtils.getString(App.APP_CONTEXT, "team_id", null);
        String url = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "teams/" + team_id + "/matches_history";
        Logger.d(url);
        OkGo.get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        String str = "{\"matches\":[{\"id\":7,\"play_start\":\"2017-11-13T12:00:00.000Z\"," +
                                "\"play_end\":\"2017-11-22T01:00:00.000Z\",\"pitch_id\":1," +
                                "\"home_team_color\":\"ffffff\",\"status\":\"w\",\"home_team\":{\"id\":33,\"size\":5," +
                                "\"image\":{\"url\":\"/uploads/team/image/33/upload-image-9724761-1510149726.\"}}," +
                                "\"join_matches\":[{\"join_team_id\":48,\"status\":\"confirmation_pending\"," +
                                "\"join_team_color\":\"ffc300\",\"team\":{\"team_name\":\"Lions9dd875\",\"size\":5," +
                                "\"image\":{\"url\":null},\"district\":{\"id\":72,\"district\":\"Yuen Long\"," +
                                "\"region\":\"New Territories\"}}}]}]}";
                        Gson gson = new Gson();
                        MatchHistory json = gson.fromJson(s, MatchHistory.class);
                        mMatches.addAll(json.getMatches());
                        for (int i = 0; i < mMatches.size(); i++) {
                            for (int j = 0; j < App.mPitchesBeanList.size(); j++) {
                                if (mMatches.get(i).getPitch_id() == App.mPitchesBeanList.get(j).getId()) {
                                    mMatches.get(i).setLocation(App.mPitchesBeanList.get(j).getLocation());
                                    mMatches.get(i).setPitch_name(App.mPitchesBeanList.get(j).getName());
                                }
                            }
                            if (mMatches.get(i).getStatus().equals("f") && new Gson().toJson(mMatches.get(i)
                                    .getJoin_matches()).contains("confirmed")) {
                                //已完成球賽 match status = f,join math status = confirmed
                                mListFinished.add(mMatches.get(i));
                            }
                        }
                        mRecorAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                    }
                });
        //接口B7 已改做 http://api.freekick.hk/api/en/teams/get_all_teams  查所有球隊, 代替原來柑同區球隊
        String urlSameArea = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "teams/get_all_teams";
        Logger.d(urlSameArea);
        OkGo.get(urlSameArea)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        SameArea json = gson.fromJson(s, SameArea.class);
                        List<SameArea.TeamBean> team = json.getTeam();
                        if (team.size() > 0)
                            for (int i = 0; i < team.size(); i++) {
                                if (team.get(i).getDistrict() != null && Integer.parseInt(district_id) == team.get(i)
                                        .getDistrict().getId()) {
                                    mTeams.add(team.get(i));
                                }
                            }
                        mSameAreaAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });

        String urlAttention = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "users/" + team_id + "/followings";
        Logger.d(urlAttention);
        OkGo.get(urlAttention)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Followings fromJson = gson.fromJson(s, Followings.class);
                        if (fromJson.getTeams().size() > 0) {
                            mFollowingTeams.addAll(fromJson.getTeams());
                        }
                        mAttentionAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }

    private void initView() {
        mTvIconSearch.setTypeface(App.mTypeface);
        mMatches = new ArrayList<>();
        mListFinished = new ArrayList<>();
        mTeams = new ArrayList<>();
        mFollowingTeams = new ArrayList<>();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_pic, R.id.ll_same_area_more, R.id.ll_attention_more})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.iv_pic:
                ToastUtil.toastShort("圖片");
                break;
            case R.id.ll_same_area_more:
                intent.setClass(mContext, SameAreaTeamActivity.class);
                startActivityForResult(intent, REQUEST_CODE_TO_REFRESH);
                break;
            case R.id.ll_attention_more:
                intent.setClass(mContext, FollowedTeamsActivity.class);
                startActivityForResult(intent, REQUEST_CODE_TO_REFRESH);
                break;
        }
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
                        Gson gson = new Gson();
                        Followings fromJson = gson.fromJson(s, Followings.class);
                        if (fromJson.getTeams().size() > 0) {
                            mFollowingTeams.addAll(fromJson.getTeams());
                        }
                        mAttentionAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TO_REFRESH) {
            getFollowedTeams();
        }
    }
}
