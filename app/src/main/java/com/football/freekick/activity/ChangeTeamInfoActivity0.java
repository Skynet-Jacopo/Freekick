package com.football.freekick.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.activity.registerlogin.RegisterPager1Activity;
import com.football.freekick.activity.registerlogin.RegisterPager2Activity;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.RegisterResponse;
import com.football.freekick.http.Url;
import com.football.freekick.utils.PrefUtils;
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
 * 更新用戶名和電話
 */
public class ChangeTeamInfoActivity0 extends BaseActivity {

    @Bind(R.id.tv_user_icon)
    TextView mTvUserIcon;
    @Bind(R.id.tv_phone_icon)
    TextView mTvPhoneIcon;
    @Bind(R.id.tv_next)
    TextView mTvNext;
    @Bind(R.id.edt_user_name)
    EditText mEdtUserName;
    @Bind(R.id.edt_phone_num)
    EditText mEdtPhoneNum;

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_team_info0);
        mContext = ChangeTeamInfoActivity0.this;
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        mTvUserIcon.setTypeface(App.mTypeface);
        mTvPhoneIcon.setTypeface(App.mTypeface);

    }

    private void initData() {
        String mobile_no = PrefUtils.getString(App.APP_CONTEXT, "mobile_no", null);
        String username = PrefUtils.getString(App.APP_CONTEXT, "username", null);
        mEdtPhoneNum.setText(mobile_no);
        mEdtUserName.setText(username);
    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
//        next();
        putNameAndPhone();
//        startActivity(new Intent(mContext, RegisterPager2Activity.class));
    }

    /**
     * 提交用戶名和電話
     */
    private void putNameAndPhone() {
        if (StringUtils.isEmpty(mEdtUserName)) {
            ToastUtil.toastShort(getString(R.string.please_enter_your_name));
            return;
        }
        if (StringUtils.isEmpty(mEdtPhoneNum)) {
            ToastUtil.toastShort(getString(R.string.please_enter_your_phone));
            return;
        }
        loadingShow();
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("username", StringUtils.getEditText(mEdtUserName));
        object1.addProperty("mobile_no", StringUtils.getEditText(mEdtPhoneNum));
        object.add("user", object1);
        Logger.json(object.toString());
        OkGo.put(Url.REGISTER)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        RegisterResponse json = gson.fromJson(s, RegisterResponse.class);
                        if (json.getStatus().equals("success")) {
                            RegisterResponse.DataBean data = json.getData();
                            PrefUtils.putString(App.APP_CONTEXT, "mobile_no", data.getMobile_no() + "");
                            PrefUtils.putString(App.APP_CONTEXT, "username", data.getUsername() + "");
                            Intent intent = new Intent(mContext, ChangeTeamInfoActivity1.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                    }
                });
    }
}
