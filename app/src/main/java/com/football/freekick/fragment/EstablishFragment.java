package com.football.freekick.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.football.freekick.R;
import com.football.freekick.activity.registerlogin.FirstPageActivity;
import com.football.freekick.language.SelectLanguageActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 建立球賽頁
 */
public class EstablishFragment extends Fragment {


    @Bind(R.id.tv_set)
    TextView mTvSet;
    @Bind(R.id.tv_start_login)
    TextView mTvStartLogin;

    public EstablishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_establish, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_set, R.id.tv_start_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_set:
                startActivityForResult(new Intent(getActivity(), SelectLanguageActivity.class), 1);
                break;
            case R.id.tv_start_login:
                startActivity(new Intent(getActivity(), FirstPageActivity.class));
                break;
        }
    }
}
