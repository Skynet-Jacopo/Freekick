package com.football.freekick.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.MainActivity;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.CreateTeam;
import com.football.freekick.commons.colorpicker.ColorListener;
import com.football.freekick.commons.colorpicker.ColorPickerView;
import com.football.freekick.http.Url;
import com.football.freekick.utils.ImageUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.ToastUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class ChangeTeamInfoActivity2 extends BaseActivity {

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

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pager3);
        mContext = ChangeTeamInfoActivity2.this;
        ButterKnife.bind(this);
        initView();
        initData();
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
        if (!intent.getStringExtra("image").equals("")) {
            uploadImageToBase64(intent.getStringExtra("image"));
        }else {
            image = "";
        }

        color1 = intent.getStringExtra("color1");
        color2 = intent.getStringExtra("color2");
        Logger.d("color1--->" + color1 + "     color2--->" + color2);
        Logger.d("color1--->" + Color.parseColor("#" + color1) + "     color2--->" + Color.parseColor("#" + color2));
        mColorPickerHome.selectGiven(color1);
        mColorPickerVisitor.selectGiven(color2);
        mIvClothesHome.setBackgroundColor(MyUtil.getColorInt(color1));
        mIvClothesVisitor.setBackgroundColor(MyUtil.getColorInt(color2));
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

    private void initView() {
        color1 = mColorPickerHome.getColorHtml();
        color2 = mColorPickerVisitor.getColorHtml();
        mTvBack.setTypeface(App.mTypeface);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @OnClick({R.id.tv_back, R.id.tv_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_complete:
                changeTeam();
                break;
        }
    }
    /**
     * 操作圖片toBase64
     *
     * @param picLoaclUrl
     */
    private void uploadImageToBase64(String picLoaclUrl) {
        Bitmap bitmap = ImageUtil.getimage(picLoaclUrl);
        image = "data:image/jpeg;base64," + ImageUtil.bitmapToBase64(bitmap);
    }
    /**
     * 修改球隊
     */
    private void changeTeam() {
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
        if (image.equals("")) {

        } else {
            object1.addProperty("image", image);
        }
        object1.addProperty("prefer_district_id", district);
        object.add("team", object1);
        Logger.json(object.toString());
        Logger.d(App.headers.toString());
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "teams";
        Logger.d(url + "/" + PrefUtils.getString(App.APP_CONTEXT, "team_id", null));
        OkGo.put(url + "/" + PrefUtils.getString(App.APP_CONTEXT, "team_id", null))
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        CreateTeam createTeam = gson.fromJson(s, CreateTeam.class);
                        if (createTeam.getTeam() != null) {
                            CreateTeam.TeamBean team = createTeam.getTeam();
                            asyDatabase(team);//將更改的數據同步到FireDatabase
                            int id = team.getId();
                            String color1 = team.getColor1();
                            String color2 = team.getColor2();
                            String logourl = team.getImage().getUrl();
                            String team_name = team.getTeam_name();
                            int size = team.getSize();
                            // TODO: 2017/11/19 這裡是放在數據庫還是Sp中,欠考慮
                            PrefUtils.putString(App.APP_CONTEXT, "team_id", id + "");
                            PrefUtils.putString(App.APP_CONTEXT, "color1", color1 + "");
                            PrefUtils.putString(App.APP_CONTEXT, "color2", color2 + "");
                            PrefUtils.putString(App.APP_CONTEXT, "logourl", logourl + "");
                            PrefUtils.putString(App.APP_CONTEXT, "team_name", team_name + "");
                            PrefUtils.putString(App.APP_CONTEXT, "size", size + "");
                            PrefUtils.putString(App.APP_CONTEXT, "establish_year", team.getEstablish_year() + "");
                            PrefUtils.putString(App.APP_CONTEXT, "average_height", team.getAverage_height() + "");
                            PrefUtils.putString(App.APP_CONTEXT, "age_range_min", team.getAge_range_min() + "");
                            PrefUtils.putString(App.APP_CONTEXT, "age_range_max", team.getAge_range_max() + "");
                            PrefUtils.putString(App.APP_CONTEXT, "establish_year", team.getEstablish_year() + "");
                            PrefUtils.putString(App.APP_CONTEXT, "style", team.getStyle().get(0) + "");
                            PrefUtils.putString(App.APP_CONTEXT, "battle_preference", team.getBattle_preference().get(0) + "");
                            PrefUtils.putString(App.APP_CONTEXT, "district", team.getDistrict().getDistrict() + "");
                            PrefUtils.putString(App.APP_CONTEXT, "district_id", team.getDistrict().getId() + "");
                            ToastUtil.toastShort(getString(R.string.change_success));
//                            startActivity(new Intent(mContext, MainActivity.class).setFlags(Intent
// .FLAG_ACTIVITY_NEW_TASK));
                            //清除一下緩存,要不然圖片還是加載之前的,因為ImageLoader的緩存機制是同名文件不會走網絡加載
                            ImageLoader.getInstance().clearDiskCache();
                            ImageLoader.getInstance().clearMemoryCache();
                            Intent intent = new Intent(mContext, MainActivity.class);
                            intent.putExtra("which", 5);
                            startActivity(intent);
                            finish();
                        } else if (createTeam.getTeam() == null && createTeam.getErrors() != null) {
                            ToastUtil.toastShort(createTeam.getErrors().get(0));
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

    /**
     * 將更改的數據同步到FireDatabase
     * @param team
     */
    private void asyDatabase(CreateTeam.TeamBean team) {
        mDatabase.
                child("users").
                child(team.getId() + "").
                child("displayName").
                setValue(team.getUser().getUsername() + "（" + team.getTeam_name() + "）");
        mDatabase.
                child("users").
                child(team.getId() + "").
                child("team_url").
                setValue(team.getImage().getUrl());
    }

}
