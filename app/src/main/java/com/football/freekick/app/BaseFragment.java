package com.football.freekick.app;

import android.app.Dialog;
import android.support.v4.app.Fragment;

import com.football.freekick.R;
import com.football.freekick.utils.LoadingDialogUtil;

/**
 * Created by liuqun on 12/22/2016.
 */
public class BaseFragment extends Fragment {

    private Dialog mLoadingDialog;

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
}
