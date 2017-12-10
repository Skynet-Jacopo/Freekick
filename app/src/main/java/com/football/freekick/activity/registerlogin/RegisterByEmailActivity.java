package com.football.freekick.activity.registerlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.RegisterResponse;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.StringUtils;
import com.football.freekick.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class RegisterByEmailActivity extends BaseActivity {

    @Bind(R.id.edt_name)
    EditText mEdtName;
    @Bind(R.id.edt_email)
    EditText mEdtEmail;
    @Bind(R.id.edt_pass_word)
    EditText mEdtPassWord;
    @Bind(R.id.tv_register)
    TextView mTvRegister;
    @Bind(R.id.tv_back)
    TextView mTvBack;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_by_email);
        mContext = RegisterByEmailActivity.this;
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvBack.setTypeface(App.mTypeface);

//        mEdtEmail.setText("liu@yopmail.com");
//        mEdtName.setText("楊過");
//        mEdtPassWord.setText("123456789");
    }

    @OnClick({R.id.tv_register, R.id.tv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                register();
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }

    /**
     * 注册
     */
    private void register() {
//        if (StringUtils.isEmpty(mEdtName)) {
//            ToastUtil.toastShort(getString(R.string.please_enter_your_name));
//            return;
//        }
        if (StringUtils.isEmpty(mEdtEmail)) {
            ToastUtil.toastShort(getString(R.string.please_enter_your_email));
            return;
        }
        if (!MyUtil.checkEmail(StringUtils.getEditText(mEdtEmail))){
            ToastUtil.toastShort(getString(R.string.email_error));
            return;
        }
        if (StringUtils.isEmpty(mEdtPassWord)) {
            ToastUtil.toastShort(getString(R.string.please_enter_your_password));
            return;
        }
        if (StringUtils.getEditText(mEdtPassWord).length()<6) {
            ToastUtil.toastShort(getString(R.string.password_error));
            return;
        }
        String url;
        if (App.isChinese)
            url = "http://api.freekick.hk/api/zh_HK/auth";
        else
            url = "http://api.freekick.hk/api/en/auth";
        loadingShow();
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
//        object1.addProperty("username", StringUtils.getEditText(mEdtName));
        object1.addProperty("email", mEdtEmail.getText().toString().trim());
        object1.addProperty("password", mEdtPassWord.getText().toString().trim());
        object1.addProperty("password_confirmation", mEdtPassWord.getText().toString().trim());
        object1.addProperty("provider", "email");
        object.add("user", object1);
        Logger.json(object.toString());
        String str = "{ \"user\": {\"email\": \"liu@yopmail.com\",  \"password\": \"123456789\", " +
                "\"password_confirmation\": \"123456789\", \"username\": \"楊過\", \"register_type\": " +
                "\"email\", \"mobile_no\": \"123456\"}}";
        OkGo.post(url)
                .upJson(object.toString())
//                .upJson(str)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        RegisterResponse registerResponse = gson.fromJson(s, RegisterResponse.class);
                        if (registerResponse.getStatus().equals("success")) {
                            ToastUtil.toastShort(getString(R.string.please_verify_your_account_first));
                            Intent intent = new Intent(mContext, RegisterPager1Activity.class);
                            intent.putExtra("email", StringUtils.getEditText(mEdtEmail));
                            intent.putExtra("password", StringUtils.getEditText(mEdtPassWord));
                            startActivity(intent);
                        }else if (registerResponse.getStatus().equals("error")){
                            RegisterResponse.ErrorsBean errors = registerResponse.getErrors();
                            if (errors.getFull_messages()!=null&&errors.getFull_messages().size()!=0){
                                ToastUtil.toastShort(errors.getFull_messages().get(0));
                            }else if (errors.getMobile_no()!=null&&errors.getMobile_no().size()!=0){
                                ToastUtil.toastShort(errors.getMobile_no().get(0));
                            }else if (errors.getPassword()!=null&&errors.getPassword().size()!=0){
                                ToastUtil.toastShort(errors.getPassword().get(0));
                            }else if (errors.getRegister_type()!=null&&errors.getRegister_type().size()!=0){
                                ToastUtil.toastShort(errors.getRegister_type().get(0));
                            }

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
