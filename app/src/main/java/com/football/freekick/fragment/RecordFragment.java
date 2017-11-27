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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.baseadapter.ViewHolder;
import com.football.freekick.baseadapter.recyclerview.CommonAdapter;
import com.football.freekick.baseadapter.recyclerview.OnItemClickListener;
import com.football.freekick.http.Url;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作賽記錄頁.
 */
public class RecordFragment extends Fragment {


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
    private List<String> datas = new ArrayList<>();
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
        CommonAdapter attentionAdapter = new CommonAdapter<String>(mContext,R.layout.item_same_area,datas) {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, String s) {
                holder.setText(R.id.tv_team_name,s);
                ImageView ivLogo = holder.getView(R.id.iv_logo);
                ImageLoaderUtils.displayImage(Url.BaseImageUrl+PrefUtils.getString(App.APP_CONTEXT,"logourl",null),ivLogo);
            }
        };
        attentionAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                ToastUtil.toastShort("詳情");
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        mRecyclerAttention.setAdapter(attentionAdapter);
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
        CommonAdapter sameAreaAdapter = new CommonAdapter<String>(mContext,R.layout.item_same_area,datas) {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, String s) {
                holder.setText(R.id.tv_team_name,s);
                ImageView ivLogo = holder.getView(R.id.iv_logo);
                ImageLoaderUtils.displayImage(Url.BaseImageUrl+PrefUtils.getString(App.APP_CONTEXT,"logourl",null),ivLogo);
            }
        };
        sameAreaAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                ToastUtil.toastShort("詳情");
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        mRecyclerSameArea.setAdapter(sameAreaAdapter);
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
        CommonAdapter recorAdapter = new CommonAdapter<String>(mContext,R.layout.item_record,datas) {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, String s) {
                holder.setText(R.id.tv_location,s);
                holder.setText(R.id.tv_date,"17 Oct 2017");
                holder.setText(R.id.tv_time,"21:00 - 23:00");
                holder.setText(R.id.tv_home_name, PrefUtils.getString(App.APP_CONTEXT,"team_name",null));
                holder.setText(R.id.tv_visitor_name,"客隊名字");
                ImageView ivHomeLogo = holder.getView(R.id.iv_home_logo);
                ImageView ivVisitorLogo = holder.getView(R.id.iv_visitor_logo);
                ImageLoaderUtils.displayImage(Url.BaseImageUrl+PrefUtils.getString(App.APP_CONTEXT,"logourl",null),ivHomeLogo);
                ImageLoaderUtils.displayImage(Url.BaseImageUrl+PrefUtils.getString(App.APP_CONTEXT,"logourl",null),ivVisitorLogo);
            }
        };
        recorAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                ToastUtil.toastShort("詳情");
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        mRecyclerRecord.setAdapter(recorAdapter);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            datas.add("我是數據"+i);
        }
    }

    private void initView() {
        mTvIconSearch.setTypeface(App.mTypeface);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_pic, R.id.ll_same_area_more, R.id.ll_attention_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pic:
                ToastUtil.toastShort("圖片");
                break;
            case R.id.ll_same_area_more:
                ToastUtil.toastShort("同區更多");
                break;
            case R.id.ll_attention_more:
                ToastUtil.toastShort("關注更多");
                break;
        }
    }
}
