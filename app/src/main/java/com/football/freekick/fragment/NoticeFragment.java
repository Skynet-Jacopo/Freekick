package com.football.freekick.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.activity.NoticeDetailActivity;
import com.football.freekick.app.BaseFragment;
import com.football.freekick.baseadapter.ViewHolder;
import com.football.freekick.baseadapter.recyclerview.CommonAdapter;
import com.football.freekick.beans.Notification;
import com.football.freekick.http.Url;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.ToastUtil;
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
 * 通知頁面
 */
public class NoticeFragment extends BaseFragment {


    @Bind(R.id.recycler_notice)
    RecyclerView mRecyclerNotice;

    private Context mContext;
    private List<Notification.NotificationBean> mNotification;
    private CommonAdapter mAdapter;

    public NoticeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        URL: http://api.freekick.hk/api/en/users/user_received_notifications
        initView();
        initData();
    }

    private void initData() {
        if (mNotification != null) {
            mNotification.clear();
        }
        String url = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "users/user_received_notifications";
        Logger.d(url);
        OkGo.get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Notification fromJson = gson.fromJson(s, Notification.class);
                        mNotification.addAll(fromJson.getNotification());
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }

    private void initView() {
        mNotification = new ArrayList<>();
        if (mRecyclerNotice != null) {
            mRecyclerNotice.setHasFixedSize(true);
        }
        mRecyclerNotice.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new CommonAdapter<Notification.NotificationBean>(mContext, R.layout.item_notification,mNotification) {


            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, final Notification.NotificationBean notificationBean) {
                final int itemPosition = holder.getItemPosition();
                holder.setText(R.id.tv_time, JodaTimeUtil.getTime(notificationBean.getCreated_at()));
                final String body = notificationBean.getBody();
//                holder.setText(R.id.tv_content, Html.fromHtml(notificationBean.getBody()).toString());
                holder.setText(R.id.tv_content, body);
                holder.setTypeface(App.mTypeface,R.id.tv_icon_notice);
                holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtil.toastShort(""+itemPosition);
                        Intent intent = new Intent(mContext, NoticeDetailActivity.class);
                        intent.putExtra("body",body);
                        intent.putExtra("match_id",notificationBean.getMatch_id()+"");
                        startActivity(intent);
                    }
                });
            }
        };
        mRecyclerNotice.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
