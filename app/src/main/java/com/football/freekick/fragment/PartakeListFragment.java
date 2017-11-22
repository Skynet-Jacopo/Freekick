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
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.adapter.PartakeAdapter;
import com.football.freekick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartakeListFragment extends Fragment {
    public static PartakeListFragment mPartakeListFragment;
    @Bind(R.id.text)
    TextView     mText;
    @Bind(R.id.tv_icon_filtrate)
    TextView     mTvIconFiltrate;
    @Bind(R.id.tv_friend)
    TextView     mTvFriend;
    @Bind(R.id.tv_notice)
    TextView     mTvNotice;
    @Bind(R.id.tv_icon_left)
    TextView     mTvIconLeft;
    @Bind(R.id.tv_icon_date)
    TextView     mTvIconDate;
    @Bind(R.id.tv_icon_right)
    TextView     mTvIconRight;
    @Bind(R.id.tv_pitch_size)
    TextView     mTvPitchSize;
    @Bind(R.id.tv_icon_down)
    TextView     mTvIconDown;
    @Bind(R.id.tv_date)
    TextView     mTvDate;
    @Bind(R.id.recycler_partake)
    RecyclerView mRecyclerPartake;
    private PartakeFragment mPartakeFragment;
    private Context         mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partake_list, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPartakeFragment = (PartakeFragment) getFragmentManager().findFragmentByTag("PartakeFragment");

        initView();
        initData();
    }

    private void initData() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add("我是球隊" + i);
        }
        PartakeAdapter adapter = new PartakeAdapter(datas, mContext);
        mRecyclerPartake.setLayoutManager(new LinearLayoutManager(mContext));
        if (mTvIconRight != null) {
            mRecyclerPartake.setHasFixedSize(true);
        }
        mRecyclerPartake.setAdapter(adapter);
        adapter.setClick(new PartakeAdapter.Click() {
            @Override
            public void Clike(View view, int position) {
                switch (view.getId()){
                    case R.id.ll_content:
                        ToastUtil.toastShort("點擊了item");
                        break;
                    case R.id.tv_state:
                        view.setBackground(getResources().getDrawable(R.drawable.selector_round_red_gray_bg));
                        break;
                }
            }
        });
    }

    private void initView() {
        mTvIconFiltrate.setTypeface(App.mTypeface);
        mTvFriend.setTypeface(App.mTypeface);
        mTvNotice.setTypeface(App.mTypeface);
        mTvIconLeft.setTypeface(App.mTypeface);
        mTvIconDate.setTypeface(App.mTypeface);
        mTvIconRight.setTypeface(App.mTypeface);
        mTvIconDown.setTypeface(App.mTypeface);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.text)
    public void onViewClicked() {
        Logger.d(mPartakeFragment.mStr);
    }

    @OnClick({R.id.tv_date, R.id.tv_icon_filtrate, R.id.tv_friend, R.id.tv_notice, R.id.tv_icon_left, R.id
            .tv_icon_date, R.id.tv_icon_right, R.id.tv_pitch_size, R.id.tv_icon_down, R.id.text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_icon_filtrate:
                ToastUtil.toastShort("篩選");
                break;
            case R.id.tv_friend:
                ToastUtil.toastShort("朋友");
                break;
            case R.id.tv_notice:
                ToastUtil.toastShort("通知");
                break;
            case R.id.tv_icon_left:
                ToastUtil.toastShort("左");
                break;
            case R.id.tv_date:
            case R.id.tv_icon_date:
                ToastUtil.toastShort("日期");
                break;
            case R.id.tv_icon_right:
                ToastUtil.toastShort("右");
                break;
            case R.id.tv_pitch_size:
                ToastUtil.toastShort("球場大小");
                break;
            case R.id.tv_icon_down:
                ToastUtil.toastShort("下");
                break;
            case R.id.text:
                Logger.d(mPartakeFragment.mStr);
                break;
        }
    }
}
