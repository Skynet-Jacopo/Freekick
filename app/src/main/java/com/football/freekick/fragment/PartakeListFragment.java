package com.football.freekick.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartakeListFragment extends Fragment {
    public static PartakeListFragment mPartakeListFragment;
    @Bind(R.id.text)
    TextView mText;
    @Bind(R.id.tv_icon_filtrate)
    TextView mTvIconFiltrate;
    @Bind(R.id.tv_friend)
    TextView mTvFriend;
    @Bind(R.id.tv_notice)
    TextView mTvNotice;
    @Bind(R.id.tv_icon_left)
    TextView mTvIconLeft;
    @Bind(R.id.tv_icon_date)
    TextView mTvIconDate;
    @Bind(R.id.tv_icon_right)
    TextView mTvIconRight;
    @Bind(R.id.tv_pitch_size)
    TextView mTvPitchSize;
    @Bind(R.id.tv_icon_down)
    TextView mTvIconDown;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    private PartakeFragment mPartakeFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partake_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPartakeFragment = (PartakeFragment) getFragmentManager().findFragmentByTag("PartakeFragment");

        initView();
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
