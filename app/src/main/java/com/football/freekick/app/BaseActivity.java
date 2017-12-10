package com.football.freekick.app;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.football.freekick.R;
import com.football.freekick.language.LanguageObservable;
import com.football.freekick.utils.ActyUtil;
import com.football.freekick.utils.LoadingDialogUtil;
import com.football.freekick.utils.SystemBarTintManager;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by LiuQun on 2017/11/1.
 * Activity基礎類
 */

public class BaseActivity extends AutoLayoutActivity implements Observer {
    private Dialog mLoadingDialog;
    public static final String BaseUrl = "http://api.freekick.hk/api/";
    public static final String ZH_HK = "zh_HK/";
    public static final String EN = "en/";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActyUtil.addActivityToList(this);
        LanguageObservable.getInstance().addObserver(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.status_bar_color);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LanguageObservable.getInstance().deleteObserver(this);
        ActyUtil.removeActivityFromList(this);
    }
    @Override
    public void update(Observable observable, Object o) {
        recreate();
    }

    /**
     * Progress   Dialog
     */
    protected void loadingShow(String title) {
        mLoadingDialog = LoadingDialogUtil.createLoadingDialog(this, title);
    }
    protected void loadingShow() {
        mLoadingDialog = LoadingDialogUtil.createLoadingDialog(this, getString(R.string.loading));
    }

    protected void loadingDismiss() {
        LoadingDialogUtil.closeDialog(mLoadingDialog);
    }
}
