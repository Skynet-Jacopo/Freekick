package com.football.freekick.activity.registerlogin;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.ForgetPassword;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.StringUtils;
import com.football.freekick.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 忘記密碼界面
 */
public class ForgetPasswordActivity extends BaseActivity {

    @Bind(R.id.edt_name)
    EditText mEdtName;
    @Bind(R.id.edt_email)
    EditText mEdtEmail;
    @Bind(R.id.edt_pass_word)
    EditText mEdtPassWord;
    @Bind(R.id.tv_confirm)
    TextView mTvConfirm;
    @Bind(R.id.tv_back)
    TextView mTvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_confirm, R.id.tv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                confirm();
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }

    private void confirm() {
        if (StringUtils.isEmpty(mEdtEmail)) {
            ToastUtil.toastShort(getString(R.string.please_enter_your_email));
            return;
        }
        if (!MyUtil.checkEmail(StringUtils.getEditText(mEdtEmail))) {
            ToastUtil.toastShort(getString(R.string.email_error));
            return;
        }
        loadingShow();
//        http://api.freekick.hk/api/en/users/change_password
//        http://api.freekick.hk/api/en/auth/password
        String url = App.isChinese ? "http://api.freekick.hk/api/zh_HK/users/change_password" : "http://api.freekick.hk/api/en/users/change_password";
        JsonObject object = new JsonObject();
        object.addProperty("email", StringUtils.getEditText(mEdtEmail));
        OkGo.put(url)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        loadingDismiss();
                        ForgetPassword fromJson = new Gson().fromJson(s, ForgetPassword.class);
                        if (fromJson != null && fromJson.isSuccess()) {
                            ToastUtil.toastShort(fromJson.getMessage());
                            finish();
                        }else {
                            ToastUtil.toastShort(getString(R.string.cannot_find_user));
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                        ToastUtil.toastShort(getString(R.string.cannot_find_user));
                    }
                });
    }
}
