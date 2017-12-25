package com.football.freekick.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.activity.MatchContentActivity1;
import com.football.freekick.activity.TeamDetailActivity;
import com.football.freekick.app.BaseFragment;
import com.football.freekick.baseadapter.ViewHolder;
import com.football.freekick.baseadapter.recyclerview.CommonAdapter;
import com.football.freekick.baseadapter.recyclerview.OnItemClickListener;
import com.football.freekick.beans.MatchHistory;
import com.football.freekick.http.Url;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
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
import okhttp3.Call;
import okhttp3.Response;

/**
 * 球隊詳情右側頁面
 */
public class TeamDetailFragment2 extends BaseFragment {


    @Bind(R.id.recycler_match_completed)
    RecyclerView mRecyclerMatchCompleted;

    private Context mContext;
    private List<MatchHistory.MatchesBean> mMatches;
    private List<MatchHistory.MatchesBean> mListFinished;
    private CommonAdapter mRecorAdapter;

    public TeamDetailFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team_detail_fragment2, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String team_id = getArguments().getString("team_id");
        initView();
        initData(team_id);
    }

    private void initData(String team_id) {
        if (mMatches != null) {
            mMatches.clear();
        }
        if (mListFinished != null) {
            mListFinished.clear();
        }
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
    }

    private void initView() {
        mMatches = new ArrayList<>();
        mListFinished = new ArrayList<>();

        if (mRecyclerMatchCompleted != null) {
            mRecyclerMatchCompleted.setHasFixedSize(true);
        }
        mRecyclerMatchCompleted.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerMatchCompleted.setNestedScrollingEnabled(false);
        mRecorAdapter = new CommonAdapter<MatchHistory.MatchesBean>(mContext, R.layout.item_match_completed,
                mListFinished) {

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, final MatchHistory.MatchesBean matchesBean) {

                holder.setText(R.id.tv_location, matchesBean.getLocation());
                holder.setText(R.id.tv_date, JodaTimeUtil.getDate2(matchesBean.getPlay_start()));
                String start = JodaTimeUtil.getTime2(matchesBean.getPlay_start());
                String end = JodaTimeUtil.getTime2(matchesBean.getPlay_end());
                holder.setText(R.id.tv_time, start + " - " + end);
                holder.setText(R.id.tv_home_name, matchesBean.getHome_team().getTeam_name());
                ImageView ivHomeLogo = holder.getView(R.id.iv_home_logo);
                ImageLoaderUtils.displayImage(MyUtil.getImageUrl(matchesBean.getHome_team().getImage().getUrl()),
                        ivHomeLogo, R.drawable.icon_default);
                final List<MatchHistory.MatchesBean.JoinMatchesBean> join_matches = matchesBean.getJoin_matches();
                for (int i = 0; i < join_matches.size(); i++) {
                    if (join_matches.get(i).getStatus().equals("confirmed")) {
                        holder.setText(R.id.tv_visitor_name, join_matches.get(i).getTeam().getTeam_name());
                        ImageView ivVisitorLogo = holder.getView(R.id.iv_visitor_logo);

                        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(join_matches.get(i).getTeam().getImage()
                                .getUrl()), ivVisitorLogo, R.drawable.icon_default);
                        final int finalI = i;
                        holder.setOnClickListener(R.id.ll_visitor_team, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(mContext, TeamDetailActivity.class);
                                intent.putExtra("id",join_matches.get(finalI).getJoin_team_id()+"");
                                startActivity(intent);
                            }
                        });
                    }
                }
                holder.setOnClickListener(R.id.ll_home_team, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, TeamDetailActivity.class);
                        intent.putExtra("id",matchesBean.getHome_team().getId()+"");
                        startActivity(intent);
                    }
                });
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
        mRecyclerMatchCompleted.setAdapter(mRecorAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
