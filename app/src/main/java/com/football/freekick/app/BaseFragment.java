package com.football.freekick.app;

import android.app.Dialog;
import android.support.v4.app.Fragment;

import com.football.freekick.R;
import com.football.freekick.utils.LoadingDialogUtil;
import com.football.freekick.utils.LoadingDialogUtil1;

/**
 * Created by liuqun on 12/22/2016.
 */
public class BaseFragment extends Fragment {

    private Dialog mLoadingDialog;
    public static final String BaseUrl = "http://api.freekick.hk/api/";
    public static final String ZH_HK = "zh_HK/";
    public static final String EN = "en/";
    /**
     * Progress   Dialog
     */
    protected void loadingShow(String title) {
        mLoadingDialog = LoadingDialogUtil.createLoadingDialog(getActivity(), title);
    }

    protected void loadingShow() {
        mLoadingDialog = LoadingDialogUtil.createLoadingDialog(getActivity(), getString(R.string.loading));
    }

    protected void loadingDismiss() {
        LoadingDialogUtil.closeDialog(mLoadingDialog);
    }

    protected void loadingShow1() {
        mLoadingDialog = LoadingDialogUtil1.createLoadingDialog(getActivity(), getString(R.string.loading));
    }

    protected void loadingDismiss1() {
        LoadingDialogUtil1.closeDialog(mLoadingDialog);
    }
}
