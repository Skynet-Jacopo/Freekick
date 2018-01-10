package com.football.freekick.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.activity.ChangeTeamInfoActivity0;
import com.football.freekick.activity.NoticeActivity;
import com.football.freekick.activity.SettingDetailActivity;
import com.football.freekick.activity.registerlogin.FirstPageActivity;
import com.football.freekick.activity.registerlogin.LoginPage1Activity;
import com.football.freekick.activity.registerlogin.LoginPager2Activity;
import com.football.freekick.app.BaseFragment;
import com.football.freekick.beans.Area;
import com.football.freekick.beans.Logout;
import com.football.freekick.beans.RegisterResponse;
import com.football.freekick.beans.Settings;
import com.football.freekick.language.SelectLanguageActivity;
import com.football.freekick.utils.ActyUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.views.ToggleButton;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * 設定頁.
 */
public class SetUpFragment extends BaseFragment {


    public static final int CHANGE_LANGUAGE = 1;
    @Bind(R.id.tv_notice)
    TextView mTvNotice;
    @Bind(R.id.iv_logo)
    ImageView mIvLogo;
    @Bind(R.id.tv_team_name)
    TextView mTvTeamName;
    @Bind(R.id.tv_team_area)
    TextView mTvTeamArea;
    @Bind(R.id.tv_language)
    TextView mTvLanguage;
    @Bind(R.id.ll_change_language)
    LinearLayout mLlChangeLanguage;
    @Bind(R.id.tv_right1)
    TextView mTvRight1;
    @Bind(R.id.ll_clause)
    LinearLayout mLlClause;
    @Bind(R.id.tv_right2)
    TextView mTvRight2;
    @Bind(R.id.ll_support)
    LinearLayout mLlSupport;
    @Bind(R.id.tv_right3)
    TextView mTvRight3;
    @Bind(R.id.ll_about_us)
    LinearLayout mLlAboutUs;
    @Bind(R.id.tv_right4)
    TextView mTvRight4;
    @Bind(R.id.ll_contact_us)
    LinearLayout mLlContactUs;
    @Bind(R.id.tv_logout)
    TextView mTvLogout;
    @Bind(R.id.toggle_button)
    ToggleButton mToggleButton;
    @Bind(R.id.rl_team_info)
    RelativeLayout mRlTeamInfo;

    private Context mContext;
    private String about_us;
    private String terms_and_conditions;
    private String help;
    private String contact_us;

    public SetUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_up, container, false);
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
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "settings";
        Logger.d(url);
        OkGo.get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Settings json = gson.fromJson(s, Settings.class);
                        List<Settings.SettingBean> settingBeanList = json.getSetting();
                        for (int i = 0; i < settingBeanList.size(); i++) {
                            String s_key = settingBeanList.get(i).getS_key();
                            switch (s_key) {
                                case "about_us":
                                    about_us = settingBeanList.get(i).getS_value();
                                    break;
                                case "contact_us":
                                    contact_us = settingBeanList.get(i).getS_value();
                                    break;
                                case "terms_and_conditions":
                                    terms_and_conditions = settingBeanList.get(i).getS_value();
                                    break;
                                case "help":
                                    help = settingBeanList.get(i).getS_value();
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }

    private void initView() {
        mTvNotice.setTypeface(App.mTypeface);
        mTvRight1.setTypeface(App.mTypeface);
        mTvRight2.setTypeface(App.mTypeface);
        mTvRight3.setTypeface(App.mTypeface);
        mTvRight4.setTypeface(App.mTypeface);
        mTvLanguage.setText(App.isChinese ? "繁體中文" : "English");
        boolean toggle_status = PrefUtils.getBoolean(App.APP_CONTEXT, "toggle_status", true);
        if (toggle_status) {
            mToggleButton.setToggleOn();
        } else {
            mToggleButton.setToggleOff();
        }
        Logger.d("mToggleButton" + mToggleButton.isToggleOn());
        mToggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    openNotice();
                } else {
                    closeNotice();
                }
            }
        });
        Logger.d("圖片--->" + MyUtil.getImageUrl(PrefUtils.getString(App.APP_CONTEXT, "logourl", null)));
        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(PrefUtils.getString(App.APP_CONTEXT, "logourl", null)),
                mIvLogo, R.drawable.icon_default);
        mTvTeamName.setText(PrefUtils.getString(App.APP_CONTEXT, "team_name", null));
        String district_id = PrefUtils.getString(App.APP_CONTEXT, "district_id", null);
        String string = getString(R.string.text_area).trim();
        Gson gson = new Gson();
        Area area = gson.fromJson(string, Area.class);
        if (district_id != null)
            for (int i = 0; i < area.getRegions().size(); i++) {
                for (int j = 0; j < area.getRegions().get(i).getDistricts().size(); j++) {
                    if (area.getRegions().get(i).getDistricts().get(j).getDistrict_id().equals(district_id)) {
                        mTvTeamArea.setText(area.getRegions().get(i).getDistricts().get(j).getDistrict().replace("$"," "));
                    }
                }
            }
    }

    /**
     * 關閉通知
     */
    private void closeNotice() {
        loadingShow();
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("mobile_no", PrefUtils.getString(App.APP_CONTEXT, "mobile_no", null));
        object1.addProperty("username", PrefUtils.getString(App.APP_CONTEXT, "username", null));
        object1.addProperty("push_notification", false);
        object.add("user", object1);
        String url = App.isChinese ? "http://api.freekick.hk/api/zh_HK/auth" : "http://api" +
                ".freekick.hk/api/en/auth";
        Logger.d(url);
        Logger.json(object.toString());
        OkGo.put(url)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        RegisterResponse json = gson.fromJson(s, RegisterResponse.class);
                        if (json.getData() != null) {
                            if (!json.getData().getPush_notification()) {
                                ToastUtil.toastShort(getString(R.string.notice_close_success));
                                mToggleButton.setToggleOff();
                                PrefUtils.putBoolean(App.APP_CONTEXT, "toggle_status", false);
                            } else {
                                ToastUtil.toastShort(getString(R.string.notice_close_failed));
                                mToggleButton.setToggleOn();
                                PrefUtils.putBoolean(App.APP_CONTEXT, "toggle_status", true);
                            }
                        } else {
                            ToastUtil.toastShort(getString(R.string.notice_close_failed));
                            mToggleButton.setToggleOn();
                            PrefUtils.putBoolean(App.APP_CONTEXT, "toggle_status", true);
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.json(e.getMessage());
                        loadingDismiss();
                        mToggleButton.setToggleOn();
                        PrefUtils.putBoolean(App.APP_CONTEXT, "toggle_status", true);
                    }
                });

    }

    /**
     * 打開通知
     */
    private void openNotice() {
        loadingShow();
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("mobile_no", PrefUtils.getString(App.APP_CONTEXT, "mobile_no", null));
        object1.addProperty("username", PrefUtils.getString(App.APP_CONTEXT, "username", null));
        object1.addProperty("push_notification", true);
        object.add("user", object1);
        String url = App.isChinese ? "http://api.freekick.hk/api/zh_HK/auth" : "http://api" +
                ".freekick.hk/api/en/auth";
        Logger.d(url);
        Logger.json(object.toString());
        OkGo.put(url)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        RegisterResponse json = gson.fromJson(s, RegisterResponse.class);
                        if (json.getData() != null) {
                            if (json.getData().getPush_notification()) {
                                ToastUtil.toastShort(getString(R.string.notice_open_success));
                                mToggleButton.setToggleOn();
                                PrefUtils.putBoolean(App.APP_CONTEXT, "toggle_status", true);
                            } else {
                                ToastUtil.toastShort(getString(R.string.notice_open_failed));
                                mToggleButton.setToggleOff();
                                PrefUtils.putBoolean(App.APP_CONTEXT, "toggle_status", false);
                            }
                        } else {
                            ToastUtil.toastShort(getString(R.string.notice_open_failed));
                            mToggleButton.setToggleOff();
                            PrefUtils.putBoolean(App.APP_CONTEXT, "toggle_status", false);
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.json(e.getMessage());
                        loadingDismiss();
                        mToggleButton.setToggleOff();
                        ToastUtil.toastShort(getString(R.string.notice_open_failed));
                        PrefUtils.putBoolean(App.APP_CONTEXT, "toggle_status", false);
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_logout, R.id.tv_notice, R.id.ll_change_language, R.id.ll_clause, R.id.ll_support, R.id
            .ll_about_us, R.id.ll_contact_us, R.id.iv_logo, R.id.tv_team_name, R.id.tv_team_area, R.id.rl_team_info})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.iv_logo:
            case R.id.tv_team_name:
            case R.id.tv_team_area:
            case R.id.rl_team_info:
                intent.setClass(mContext, ChangeTeamInfoActivity0.class);
                startActivity(intent);
                break;
            case R.id.tv_notice:
                intent.setClass(mContext, NoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_change_language:
                startActivityForResult(new Intent(getActivity(), SelectLanguageActivity.class), CHANGE_LANGUAGE);
                break;
            case R.id.ll_clause:
                intent.setClass(mContext, SettingDetailActivity.class);
                intent.putExtra("terms_and_conditions", terms_and_conditions);
                startActivity(intent);
                break;
            case R.id.ll_support:
                intent.setClass(mContext, SettingDetailActivity.class);
                intent.putExtra("help", help);
                startActivity(intent);
                break;
            case R.id.ll_about_us:
                intent.setClass(mContext, SettingDetailActivity.class);
                intent.putExtra("about_us", about_us);
                startActivity(intent);
                break;
            case R.id.ll_contact_us:
                intent.setClass(mContext, SettingDetailActivity.class);
                intent.putExtra("contact_us", contact_us);
                startActivity(intent);
                break;
            case R.id.tv_logout:
                PrefUtils.clearShare(App.APP_CONTEXT);
                logout();
                break;
        }
    }

    //登出
    private void logout() {
        loadingShow();
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "auth/sign_out";
        Logger.d(url);
        OkGo.delete(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        Logout logout = gson.fromJson(s, Logout.class);
                        if (logout.isSuccess()) {
                            startActivity(new Intent(mContext, FirstPageActivity.class));
                            ActyUtil.finishAllActivity();
                            LoginManager.getInstance().logOut();
//                            ToastUtil.toastShort(logout.getMessage());
                        } else {
                            ToastUtil.toastShort(logout.getMessage());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        loadingDismiss();
                        Logger.d(e.getMessage());
                    }
                });
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
