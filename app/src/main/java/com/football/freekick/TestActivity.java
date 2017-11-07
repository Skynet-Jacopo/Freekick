package com.football.freekick;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.football.freekick.app.BaseActivity;
import com.football.freekick.language.SelectLanguageActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;

import okhttp3.Call;
import okhttp3.Response;

public class TestActivity extends BaseActivity {

    private TextView tv_language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        TextView tvtest   = (TextView) findViewById(R.id.tv_test);
        tv_language = (TextView) findViewById(R.id.tv_language);

        tvtest.setTypeface(typeface);
//        tvtest.setText(R.string.test);
        tvtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(TestActivity.this, SelectLanguageActivity.class), 1);
            }
        });
        Register          register = new Register();
        Register.UserBean userBean = new Register.UserBean();
        userBean.setEmail("905169916@qq.com");
        userBean.setMobile_no("18613614028");
        userBean.setPassword("123456");
        userBean.setPassword_confirmation("123456");
        userBean.setUsername("Jacopo");
        userBean.setRegister_type("mobile");
        register.setUser(userBean);
//        Gson gson = new Gson();
//        Logger.json(gson.toJson(register));
        JsonObject object = new JsonObject();

        JsonObject object1 = new JsonObject();
        object1.addProperty("email", "test@yopmail.com");
        object1.addProperty("password", "123456");
        object1.addProperty("password_confirmation", "123456");
        object1.addProperty("username", "test");
        object1.addProperty("register_type", "mobile");
        object1.addProperty("mobile_no", "11111111111");
        object.add("user", object1);

        Gson gson = new Gson();
        Logger.json(gson.toJson(object));
        Logger.d(object.toString());
        OkGo.post("http://api.freekick.hk/api/en/auth")
                .upJson(object.toString())
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


//        int largeMemoryClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getLargeMemoryClass();
//        Logger.d("largeMemoryClass--->"+largeMemoryClass);
//        Bitmap bitmap[] = new Bitmap[10000];
//        for (int i=0; i<bitmap.length; i++) {
//            bitmap[i] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String language     = data.getStringExtra("language");
            String languageName = data.getStringExtra("languageName");
            tv_language.setText(languageName);
            String languageWithCountry = data.getStringExtra("languageWithCountry");
            Logger.d("language---->" + language + "/nlanguageName---->" + languageName + "/nlanguageWithCountry--->" + languageWithCountry);
        }
    }
}
