package com.football.freekick;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvtest = (TextView) findViewById(R.id.tv_test);

        Register          register = new Register();
        Register.UserBean userBean = new Register.UserBean();
        userBean.setEmail("905169916@qq.com");
        userBean.setMobile_no("18613614028");
        userBean.setPassword("123456");
        userBean.setPassword_confirmation("123456");
        userBean.setRegister_type("mobile");
        register.setUser(userBean);
        Gson gson = new Gson();
        Logger.json(gson.toJson(register));
        OkGo.post("http://localhost:3000/api/auth")
                .upJson(gson.toJson(register))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }
}
