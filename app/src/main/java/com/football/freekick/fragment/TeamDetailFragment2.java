package com.football.freekick.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.football.freekick.R;
import com.football.freekick.baseadapter.ViewHolder;
import com.football.freekick.baseadapter.recyclerview.CommonAdapter;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 球隊詳情右側頁面
 */
public class TeamDetailFragment2 extends Fragment {


    @Bind(R.id.recycler_match_completed)
    RecyclerView mRecyclerMatchCompleted;

    private Context mContext;
    private List<String> datas = new ArrayList<>();

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
        initView();
    }

    private void initView() {
        for (int i = 0; i < 10; i++) {
            datas.add("我是數據" + i);
        }

        if (mRecyclerMatchCompleted != null) {
            mRecyclerMatchCompleted.setHasFixedSize(true);
        }
        mRecyclerMatchCompleted.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerMatchCompleted.setNestedScrollingEnabled(false);
        CommonAdapter adapter = new CommonAdapter<String>(mContext, R.layout.item_match_completed, datas) {

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, String s) {
                ImageView ivHomeLogo    = holder.getView(R.id.iv_home_logo);
                ImageView ivVisitorLogo = holder.getView(R.id.iv_visitor_logo);
                holder.setText(R.id.tv_date, "17 Oct 2017");
                holder.setText(R.id.tv_home_name, "我是主隊");
                holder.setText(R.id.tv_location, "我是位置");
                holder.setText(R.id.tv_time, "21:00 - 23:00");
                holder.setText(R.id.tv_visitor_name, "我是客隊");
                holder.setText(R.id.tv_visitor_name, "我是客隊");
            }
        };
        mRecyclerMatchCompleted.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
