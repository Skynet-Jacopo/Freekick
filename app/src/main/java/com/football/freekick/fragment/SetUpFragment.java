package com.football.freekick.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.language.SelectLanguageActivity;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.views.ToggleButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 設定頁.
 */
public class SetUpFragment extends Fragment {


    public static final int CHANGE_LANGUAGE = 1;
    @Bind(R.id.tv_notice)
    TextView     mTvNotice;
    @Bind(R.id.iv_logo)
    ImageView    mIvLogo;
    @Bind(R.id.tv_team_name)
    TextView     mTvTeamName;
    @Bind(R.id.tv_team_area)
    TextView     mTvTeamArea;
    @Bind(R.id.tv_language)
    TextView     mTvLanguage;
    @Bind(R.id.ll_change_language)
    LinearLayout mLlChangeLanguage;
    @Bind(R.id.tv_right1)
    TextView     mTvRight1;
    @Bind(R.id.ll_clause)
    LinearLayout mLlClause;
    @Bind(R.id.tv_right2)
    TextView     mTvRight2;
    @Bind(R.id.ll_support)
    LinearLayout mLlSupport;
    @Bind(R.id.tv_right3)
    TextView     mTvRight3;
    @Bind(R.id.ll_about_us)
    LinearLayout mLlAboutUs;
    @Bind(R.id.tv_right4)
    TextView     mTvRight4;
    @Bind(R.id.ll_contact_us)
    LinearLayout mLlContactUs;
    @Bind(R.id.tv_logout)
    TextView     mTvLogout;
    @Bind(R.id.toggle_button)
    ToggleButton mToggleButton;

    public SetUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_up, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvNotice.setTypeface(App.mTypeface);
        mTvRight1.setTypeface(App.mTypeface);
        mTvRight2.setTypeface(App.mTypeface);
        mTvRight3.setTypeface(App.mTypeface);
        mTvRight4.setTypeface(App.mTypeface);
        mToggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                ToastUtil.toastShort(on+"");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_logout, R.id.tv_notice, R.id.ll_change_language, R.id.ll_clause, R.id.ll_support, R.id
            .ll_about_us, R.id.ll_contact_us})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_notice:
                ToastUtil.toastShort("消息");
                break;
            case R.id.ll_change_language:
                startActivityForResult(new Intent(getActivity(), SelectLanguageActivity.class), CHANGE_LANGUAGE);
                break;
            case R.id.ll_clause:
                ToastUtil.toastShort("條款和條約");
                break;
            case R.id.ll_support:
                ToastUtil.toastShort("幫助和支持");
                break;
            case R.id.ll_about_us:
                ToastUtil.toastShort("關於我們");
                break;
            case R.id.ll_contact_us:
                ToastUtil.toastShort("聯繫我們");
                break;
            case R.id.tv_logout:
                ToastUtil.toastShort("登出");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE_LANGUAGE && resultCode == RESULT_OK) {
            String language = data.getStringExtra("language");
            mTvLanguage.setText(language);
        }
    }
}
