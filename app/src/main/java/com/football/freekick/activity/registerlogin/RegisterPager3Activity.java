package com.football.freekick.activity.registerlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.CreatTeam;
import com.football.freekick.commons.colorpicker.ColorListener;
import com.football.freekick.commons.colorpicker.ColorPickerView;
import com.football.freekick.http.Url;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class RegisterPager3Activity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_back)
    TextView mTvBack;
    @Bind(R.id.iv_clothes_home)
    ImageView mIvClothesHome;
    @Bind(R.id.color_picker_home)
    ColorPickerView mColorPickerHome;
    @Bind(R.id.iv_clothes_visitor)
    ImageView mIvClothesVisitor;
    @Bind(R.id.color_picker_visitor)
    ColorPickerView mColorPickerVisitor;
    @Bind(R.id.tv_complete)
    TextView mTvComplete;

    private Context mContext;
    private String team_name;
    private String district;
    private String establish_year;
    private String average_height;
    private String age_range_min;
    private String age_range_max;
    private String style;
    private String battle_preference;
    private String size;
    private String status;
    private String image;
    private String color1;
    private String color2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pager3);
        mContext = RegisterPager3Activity.this;
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {

        Intent intent = getIntent();
        team_name = intent.getStringExtra("team_name");
        district = intent.getStringExtra("district");
        establish_year = intent.getStringExtra("establish_year");
        average_height = intent.getStringExtra("average_height");
        age_range_min = intent.getStringExtra("age_range_min");
        age_range_max = intent.getStringExtra("age_range_max");
        style = intent.getStringExtra("style");
        battle_preference = intent.getStringExtra("battle_preference");
        size = intent.getStringExtra("size");
        status = intent.getStringExtra("status");
        image = "data:image/jpeg;base64,"+intent.getStringExtra("image");
//        image = "data:image/jpeg;base64,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";
    }

    private void initView() {
        color1 = mColorPickerHome.getColorHtml();
        color2 = mColorPickerVisitor.getColorHtml();
        mTvBack.setTypeface(App.mTypeface);
        mColorPickerHome.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(int color) {
                Logger.d("顏色---->" + color);
                Logger.d("顏色---->" + mColorPickerHome.getColorHtml());
                mIvClothesHome.setBackgroundColor(color);
                color1 = mColorPickerHome.getColorHtml();
            }
        });
        mColorPickerVisitor.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(int color) {
                Logger.d("顏色---->" + color);
                Logger.d("顏色---->" + mColorPickerVisitor.getColorHtml());
                mIvClothesVisitor.setBackgroundColor(color);
                color2 = mColorPickerVisitor.getColorHtml();
            }
        });
    }

    @OnClick({R.id.tv_back, R.id.tv_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_complete:
                creatTeam();
                break;
        }
    }

    /**
     * 創建球隊
     */
    private void creatTeam() {
        loadingShow();
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        JsonArray array = new JsonArray();
        JsonArray array1 = new JsonArray();
        object1.addProperty("team_name", team_name);
        object1.addProperty("establish_year", establish_year);
        object1.addProperty("average_height", average_height);
        object1.addProperty("age_range_min", age_range_min);
        object1.addProperty("age_range_max", age_range_max);
        array.add(style);
        object1.add("style", array);
        array1.add(battle_preference);
        object1.add("battle_preference", array1);
        object1.addProperty("size", size);
        object1.addProperty("color1", color1);
        object1.addProperty("color2", color2);
        object1.addProperty("status", status);
        object1.addProperty("image", image);
        object.add("team", object1);
        Logger.json(object.toString());
        Logger.d(App.headers.toString());
        OkGo.post(Url.CREATE_TEAM)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        CreatTeam creatTeam = gson.fromJson(s, CreatTeam.class);
                        if (creatTeam.getTeam() != null) {
                            CreatTeam.TeamBean team = creatTeam.getTeam();
                            int id = team.getId();
                            String color1 = team.getColor1();
                            String color2 = team.getColor2();
                            String logourl = team.getImage().getUrl();
                            String team_name = team.getTeam_name();
                            int size = team.getSize();
                            String district = team.getDistrict();
                            // TODO: 2017/11/19 這裡是放在數據庫還是Sp中,欠考慮
                            PrefUtils.putString(App.APP_CONTEXT,"team_id",id+"");
                            PrefUtils.putString(App.APP_CONTEXT,"color1",color1+"");
                            PrefUtils.putString(App.APP_CONTEXT,"color2",color2+"");
                            PrefUtils.putString(App.APP_CONTEXT,"logourl",logourl+"");
                            PrefUtils.putString(App.APP_CONTEXT,"team_name",team_name+"");
                            PrefUtils.putString(App.APP_CONTEXT,"size",size+"");
                            PrefUtils.putString(App.APP_CONTEXT,"district",district+"");
                            ToastUtil.toastShort(getString(R.string.create_team_success));
                            startActivity(new Intent(mContext, OneTimePagerActivity.class));
                        } else if (creatTeam.getTeam() == null && creatTeam.getErrors() != null) {
                            ToastUtil.toastShort(creatTeam.getErrors().get(0));
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
