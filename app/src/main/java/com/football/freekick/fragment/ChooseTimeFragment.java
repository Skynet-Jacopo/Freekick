package com.football.freekick.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.football.freekick.R;
import com.football.freekick.activity.ChooseTimeActivity;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.utils.Validate;
import com.football.freekick.views.loopview.LoopView;
import com.football.freekick.views.loopview.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 選擇開始時間和結束時間界面
 */
public class ChooseTimeFragment extends Fragment {


    @Bind(R.id.loop_view1)
    LoopView mLoopView1;
    @Bind(R.id.loop_view2)
    LoopView mLoopView2;
    private List<String> hours   = new ArrayList<>();
    private List<String> minutes = new ArrayList<>();
    private String mState;
    private String hour = "00";
    private String minute = "00";

    public ChooseTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_time, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mState = getArguments().getString("state");
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hours.add("0" + i);
            } else {
                hours.add(i + "");
            }
        }
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minutes.add("0" + i);
            } else {
                minutes.add(i + "");
            }
        }
        mLoopView1.setItems(hours);
        mLoopView2.setItems(minutes);
        mLoopView1.setDividerColor(Color.TRANSPARENT);
        mLoopView2.setDividerColor(Color.TRANSPARENT);
        mLoopView1.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                hour = hours.get(index);
                minute = minutes.get(index);
                if (Validate.noNull(hour) && Validate.noNull(minute)){
                    switch (mState) {
                        case "1":
                            ((ChooseTimeActivity) getActivity()).mTvStartTime.setText(hour + ":"+minute);
                            break;
                        case "2":
                            ((ChooseTimeActivity) getActivity()).mTvEndTime.setText(hour + ":"+minute);
                            break;
                    }
                } else {
                    ToastUtil.toastLong("请选择有效时间");
                }
            }
        });
        mLoopView2.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                hour = hours.get(index);
                minute = minutes.get(index);
                if (Validate.noNull(hour) && Validate.noNull(minute)){
                    switch (mState) {
                        case "1":
                            ((ChooseTimeActivity) getActivity()).mTvStartTime.setText(hour + ":"+minute);
                            break;
                        case "2":
                            ((ChooseTimeActivity) getActivity()).mTvEndTime.setText(hour + ":"+minute);
                            break;
                    }
                }else {
                    ToastUtil.toastLong("请选择有效时间");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
