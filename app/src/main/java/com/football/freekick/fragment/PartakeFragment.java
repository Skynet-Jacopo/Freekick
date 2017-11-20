package com.football.freekick.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.R;
import com.football.freekick.event.MainEvent;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 參與球賽頁.
 */
public class PartakeFragment extends Fragment {


    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.tv_icon_date)
    TextView mTvIconDate;
    @Bind(R.id.ll_match_date)
    LinearLayout mLlMatchDate;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.ll_match_time)
    LinearLayout mLlMatchTime;
    @Bind(R.id.tv_pitch_size)
    TextView mTvPitchSize;
    @Bind(R.id.ll_pitch_size)
    LinearLayout mLlPitchSize;
    @Bind(R.id.tv_area)
    TextView mTvArea;
    @Bind(R.id.ll_area)
    LinearLayout mLlArea;
    @Bind(R.id.iv_advertisement1)
    ImageView mIvAdvertisement1;
    @Bind(R.id.iv_advertisement2)
    ImageView mIvAdvertisement2;
    @Bind(R.id.iv_advertisement3)
    ImageView mIvAdvertisement3;
    @Bind(R.id.iv_advertisement4)
    ImageView mIvAdvertisement4;
    @Bind(R.id.tv_partake)
    TextView mTvPartake;
    @Bind(R.id.ll_parent)
    AutoLinearLayout mLlParent;
    private Context mContext;

    public PartakeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partake, container, false);
        mContext = getContext();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ll_match_date, R.id.ll_match_time, R.id.ll_pitch_size, R.id.ll_area, R.id.iv_advertisement1, R.id
            .iv_advertisement2, R.id.iv_advertisement3, R.id.iv_advertisement4, R.id.tv_partake})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_match_date:
                break;
            case R.id.ll_match_time:
                break;
            case R.id.ll_pitch_size:
                break;
            case R.id.ll_area:
                break;
            case R.id.iv_advertisement1:
                break;
            case R.id.iv_advertisement2:
                break;
            case R.id.iv_advertisement3:
                break;
            case R.id.iv_advertisement4:
                break;
            case R.id.tv_partake:
                EventBus.getDefault().post(new MainEvent(1));
                break;
        }
    }
}
