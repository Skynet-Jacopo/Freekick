package com.football.freekick.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.football.freekick.R;
import com.football.freekick.app.BaseFragment;
import com.football.freekick.baseadapter.ViewHolder;
import com.football.freekick.baseadapter.recyclerview.CommonAdapter;
import com.football.freekick.baseadapter.recyclerview.OnItemClickListener;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.ToastUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 消息界面
 */
public class MessageFragment extends BaseFragment {


    @Bind(R.id.recycler_message)
    RecyclerView mRecyclerMessage;
private Context mContext;
    private List<String> datas = new ArrayList<>();
    private CommonAdapter mAdapter;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            datas.add("我是名字"+i);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        if (mRecyclerMessage != null) {
            mRecyclerMessage.setHasFixedSize(true);
        }
        mRecyclerMessage.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new CommonAdapter<String>(mContext, R.layout.item_message,datas) {

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, String s) {
                int itemPosition = holder.getItemPosition();
                holder.setText(R.id.tv_name,s);
                holder.setText(R.id.tv_content,"我是內容我是內容我是內容我是內容我是內容我是內容我是內容我是內容我是內容");
                holder.setText(R.id.tv_msg_num,"99");
                holder.setText(R.id.tv_time, JodaTimeUtil.progressDate1(mContext,"2017-12-01T10:00:10+08:00"));
                if (itemPosition>9){
                    holder.setVisible(R.id.tv_msg_num,false);
                    holder.setVisible(R.id.tv_time,true);
                }else {
                    holder.setVisible(R.id.tv_msg_num,true);
                    holder.setVisible(R.id.tv_time,false);
                }
            }
        };
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                ToastUtil.toastShort(position+"");
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        mRecyclerMessage.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
