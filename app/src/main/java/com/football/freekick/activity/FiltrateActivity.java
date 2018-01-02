package com.football.freekick.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.Area;
import com.football.freekick.utils.StringUtils;
import com.football.freekick.views.loopview.LoopView;
import com.football.freekick.views.loopview.OnItemSelectedListener;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 篩選界面
 */
public class FiltrateActivity extends BaseActivity {
    public static final int CHOOSE_TIME = 2;
    @Bind(R.id.text1)
    TextView mText1;
    @Bind(R.id.edt_start_time)
    TextView mEdtStartTime;
    @Bind(R.id.text2)
    TextView mText2;
    @Bind(R.id.edt_end_time)
    TextView mEdtEndTime;
    @Bind(R.id.tv_icon_location)
    TextView mTvIconLocation;
    @Bind(R.id.edt_location)
    TextView mEdtLocation;
    @Bind(R.id.ll_transparent)
    LinearLayout mLlTransparent;
    @Bind(R.id.tv_num_1)
    TextView mTvNum1;
    @Bind(R.id.tv_num_2)
    TextView mTvNum2;
    @Bind(R.id.tv_num_3)
    TextView mTvNum3;
    @Bind(R.id.tv_num_4)
    TextView mTvNum4;
    @Bind(R.id.tv_height_1)
    TextView mTvHeight1;
    @Bind(R.id.tv_height_2)
    TextView mTvHeight2;
    @Bind(R.id.tv_height_3)
    TextView mTvHeight3;
    @Bind(R.id.tv_height_4)
    TextView mTvHeight4;
    @Bind(R.id.tv_age_1)
    TextView mTvAge1;
    @Bind(R.id.tv_age_2)
    TextView mTvAge2;
    @Bind(R.id.tv_age_3)
    TextView mTvAge3;
    @Bind(R.id.tv_age_4)
    TextView mTvAge4;
    @Bind(R.id.tv_style_1)
    TextView mTvStyle1;
    @Bind(R.id.tv_style_2)
    TextView mTvStyle2;
    @Bind(R.id.tv_confirm)
    TextView mTvConfirm;
    @Bind(R.id.ll_parent)
    LinearLayout mLlParent;
    private String mStartTime = "";
    private String mEndTime = "";
    private String size = "";//默認球隊人數
    private String average_height = "";//默認平均高度
    private String age_range = "";//默認球隊年齡
    private String style = "";//默認風格
    private String district_id = "";


    private List<String> mRegions;//大區
    private List<Area.RegionsBean> mAreaRegions;//解析出的大區
    private List<Area.RegionsBean.DistrictsBean> mDistricts;//解析出的小區
    private List<String> districtList;//小區
    private int regionPos;
    private int districtPos;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrate);
        mContext = FiltrateActivity.this;
        ButterKnife.bind(this);
        mStartTime = getIntent().getStringExtra("start_time");
        mEndTime = getIntent().getStringExtra("end_time");
        district_id = getIntent().getStringExtra("district_id");
        size = getIntent().getStringExtra("size");
        average_height = getIntent().getStringExtra("average_height");
        age_range = getIntent().getStringExtra("age_range");
        style = getIntent().getStringExtra("style");
        mEdtStartTime.setText(mStartTime);
        mEdtEndTime.setText(mEndTime);
        initView();
        initData();
    }

    private void initData() {
//        String string = getString(R.string.text_area).trim();
//        Gson gson = new Gson();
//        Area area = gson.fromJson(string, Area.class);
//        mRegions = area.getRegions();

        String string = getString(R.string.text_area).trim();
        Gson gson = new Gson();
        Area area = gson.fromJson(string, Area.class);
        mRegions = new ArrayList<>();
        mAreaRegions = area.getRegions();
        for (int i = 0; i < mAreaRegions.size(); i++) {
            String region = mAreaRegions.get(i).getRegion();
            String s = null;
            if (region.contains("$")) {
                s = region.replace("$", " ");
                mRegions.add(s);
            } else {
                mRegions.add(region);
            }
            for (int j = 0; j < mAreaRegions.get(i).getDistricts().size(); j++) {
                if (mAreaRegions.get(i).getDistricts().get(j).getDistrict_id().equals(district_id)) {
                    regionPos = i;
                    districtPos = j;
                    mEdtLocation.setText(mAreaRegions.get(i).getDistricts().get(j).getDistrict().replace
                            ("$", " "));
                }
            }
        }

        mDistricts = mAreaRegions.get(regionPos).getDistricts();
    }

    private void initView() {
        mTvIconLocation.setTypeface(App.mTypeface);
        if (!size.equals("")) {
            switch (size) {
                case "1":
                    mTvNum1.setTextColor(getResources().getColor(R.color.white));
                    mTvNum2.setTextColor(getResources().getColor(R.color.text_black));
                    mTvNum3.setTextColor(getResources().getColor(R.color.text_black));
                    mTvNum4.setTextColor(getResources().getColor(R.color.text_black));
                    mTvNum1.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                    mTvNum2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvNum3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvNum4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    break;
                case "2":
                    mTvNum1.setTextColor(getResources().getColor(R.color.text_black));
                    mTvNum2.setTextColor(getResources().getColor(R.color.white));
                    mTvNum3.setTextColor(getResources().getColor(R.color.text_black));
                    mTvNum4.setTextColor(getResources().getColor(R.color.text_black));
                    mTvNum1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvNum2.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                    mTvNum3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvNum4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    break;
                case "3":
                    mTvNum1.setTextColor(getResources().getColor(R.color.text_black));
                    mTvNum2.setTextColor(getResources().getColor(R.color.text_black));
                    mTvNum3.setTextColor(getResources().getColor(R.color.white));
                    mTvNum4.setTextColor(getResources().getColor(R.color.text_black));
                    mTvNum1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvNum2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvNum3.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                    mTvNum4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    break;
                case "4":
                    mTvNum1.setTextColor(getResources().getColor(R.color.text_black));
                    mTvNum2.setTextColor(getResources().getColor(R.color.text_black));
                    mTvNum3.setTextColor(getResources().getColor(R.color.text_black));
                    mTvNum4.setTextColor(getResources().getColor(R.color.white));
                    mTvNum1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvNum2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvNum3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvNum4.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                    break;
            }
        }
        if (!average_height.equals("")) {
            switch (average_height) {
                case "1":
                    mTvHeight1.setTextColor(getResources().getColor(R.color.white));
                    mTvHeight2.setTextColor(getResources().getColor(R.color.text_black));
                    mTvHeight3.setTextColor(getResources().getColor(R.color.text_black));
                    mTvHeight4.setTextColor(getResources().getColor(R.color.text_black));
                    mTvHeight1.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                    mTvHeight2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvHeight3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvHeight4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    break;
                case "2":
                    mTvHeight1.setTextColor(getResources().getColor(R.color.text_black));
                    mTvHeight2.setTextColor(getResources().getColor(R.color.white));
                    mTvHeight3.setTextColor(getResources().getColor(R.color.text_black));
                    mTvHeight4.setTextColor(getResources().getColor(R.color.text_black));
                    mTvHeight1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvHeight2.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                    mTvHeight3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvHeight4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    break;
                case "3":
                    mTvHeight1.setTextColor(getResources().getColor(R.color.text_black));
                    mTvHeight2.setTextColor(getResources().getColor(R.color.text_black));
                    mTvHeight3.setTextColor(getResources().getColor(R.color.white));
                    mTvHeight4.setTextColor(getResources().getColor(R.color.text_black));
                    mTvHeight1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvHeight2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvHeight3.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                    mTvHeight4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    break;
                case "4":
                    mTvHeight1.setTextColor(getResources().getColor(R.color.text_black));
                    mTvHeight2.setTextColor(getResources().getColor(R.color.text_black));
                    mTvHeight3.setTextColor(getResources().getColor(R.color.text_black));
                    mTvHeight4.setTextColor(getResources().getColor(R.color.white));
                    mTvHeight1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvHeight2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvHeight3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvHeight4.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                    break;
            }
        }
        if (!age_range.equals("")){
            switch (age_range){
                case "1":
                    mTvAge1.setTextColor(getResources().getColor(R.color.white));
                    mTvAge2.setTextColor(getResources().getColor(R.color.text_black));
                    mTvAge3.setTextColor(getResources().getColor(R.color.text_black));
                    mTvAge4.setTextColor(getResources().getColor(R.color.text_black));
                    mTvAge1.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                    mTvAge2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvAge3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvAge4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    break;
                case "2":
                    mTvAge1.setTextColor(getResources().getColor(R.color.text_black));
                    mTvAge2.setTextColor(getResources().getColor(R.color.white));
                    mTvAge3.setTextColor(getResources().getColor(R.color.text_black));
                    mTvAge4.setTextColor(getResources().getColor(R.color.text_black));
                    mTvAge1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvAge2.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                    mTvAge3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvAge4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    break;
                case "3":
                    mTvAge1.setTextColor(getResources().getColor(R.color.text_black));
                    mTvAge2.setTextColor(getResources().getColor(R.color.text_black));
                    mTvAge3.setTextColor(getResources().getColor(R.color.white));
                    mTvAge4.setTextColor(getResources().getColor(R.color.text_black));
                    mTvAge1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvAge2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvAge3.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                    mTvAge4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    break;
                case "4":
                    mTvAge1.setTextColor(getResources().getColor(R.color.text_black));
                    mTvAge2.setTextColor(getResources().getColor(R.color.text_black));
                    mTvAge3.setTextColor(getResources().getColor(R.color.text_black));
                    mTvAge4.setTextColor(getResources().getColor(R.color.white));
                    mTvAge1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvAge2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvAge3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                    mTvAge4.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                    break;
            }
        }
        if (!style.equals("")){
           switch (style){
               case "become_strong":
                   mTvStyle1.setTextColor(getResources().getColor(R.color.white));
                   mTvStyle2.setTextColor(getResources().getColor(R.color.text_black));
                   mTvStyle1.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                   mTvStyle2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                   break;
               case "for_fun":
                   mTvStyle1.setTextColor(getResources().getColor(R.color.text_black));
                   mTvStyle2.setTextColor(getResources().getColor(R.color.white));
                   mTvStyle1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                   mTvStyle2.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                   break;
           }
        }
    }

    @OnClick({R.id.edt_start_time, R.id.edt_end_time, R.id.edt_location, R.id.tv_icon_location, R.id.tv_num_1, R.id
            .tv_num_2, R.id.tv_num_3, R.id.tv_num_4, R.id.tv_height_1, R
            .id.tv_height_2, R.id.tv_height_3, R.id.tv_height_4, R.id.tv_age_1, R.id.tv_age_2, R.id.tv_age_3, R.id
            .tv_age_4, R.id.tv_style_1, R.id.tv_style_2, R.id.tv_confirm, R.id.ll_transparent})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.edt_start_time:
                intent.setClass(mContext, ChooseTimeActivity.class);
                intent.putExtra("start_time", mStartTime);
                intent.putExtra("end_time", mEndTime);
                startActivityForResult(intent, CHOOSE_TIME);
                break;
            case R.id.edt_end_time:
                intent.setClass(mContext, ChooseTimeActivity.class);
                intent.putExtra("start_time", mStartTime);
                intent.putExtra("end_time", mEndTime);
                startActivityForResult(intent, CHOOSE_TIME);
                break;
            case R.id.edt_location:
                chooseArea();
                break;
            case R.id.tv_icon_location:
//                ToastUtil.toastShort("定位");
                break;
            case R.id.tv_num_1:
                mTvNum1.setTextColor(getResources().getColor(R.color.white));
                mTvNum2.setTextColor(getResources().getColor(R.color.text_black));
                mTvNum3.setTextColor(getResources().getColor(R.color.text_black));
                mTvNum4.setTextColor(getResources().getColor(R.color.text_black));
                mTvNum1.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                mTvNum2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvNum3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvNum4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                size = "1";
                break;
            case R.id.tv_num_2:
                mTvNum1.setTextColor(getResources().getColor(R.color.text_black));
                mTvNum2.setTextColor(getResources().getColor(R.color.white));
                mTvNum3.setTextColor(getResources().getColor(R.color.text_black));
                mTvNum4.setTextColor(getResources().getColor(R.color.text_black));
                mTvNum1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvNum2.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                mTvNum3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvNum4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                size = "2";
                break;
            case R.id.tv_num_3:
                mTvNum1.setTextColor(getResources().getColor(R.color.text_black));
                mTvNum2.setTextColor(getResources().getColor(R.color.text_black));
                mTvNum3.setTextColor(getResources().getColor(R.color.white));
                mTvNum4.setTextColor(getResources().getColor(R.color.text_black));
                mTvNum1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvNum2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvNum3.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                mTvNum4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                size = "3";
                break;
            case R.id.tv_num_4:
                mTvNum1.setTextColor(getResources().getColor(R.color.text_black));
                mTvNum2.setTextColor(getResources().getColor(R.color.text_black));
                mTvNum3.setTextColor(getResources().getColor(R.color.text_black));
                mTvNum4.setTextColor(getResources().getColor(R.color.white));
                mTvNum1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvNum2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvNum3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvNum4.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                size = "4";
                break;
            case R.id.tv_height_1:
                mTvHeight1.setTextColor(getResources().getColor(R.color.white));
                mTvHeight2.setTextColor(getResources().getColor(R.color.text_black));
                mTvHeight3.setTextColor(getResources().getColor(R.color.text_black));
                mTvHeight4.setTextColor(getResources().getColor(R.color.text_black));
                mTvHeight1.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                mTvHeight2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvHeight3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvHeight4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                average_height = "1";
                break;
            case R.id.tv_height_2:
                mTvHeight1.setTextColor(getResources().getColor(R.color.text_black));
                mTvHeight2.setTextColor(getResources().getColor(R.color.white));
                mTvHeight3.setTextColor(getResources().getColor(R.color.text_black));
                mTvHeight4.setTextColor(getResources().getColor(R.color.text_black));
                mTvHeight1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvHeight2.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                mTvHeight3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvHeight4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                average_height = "2";
                break;
            case R.id.tv_height_3:
                mTvHeight1.setTextColor(getResources().getColor(R.color.text_black));
                mTvHeight2.setTextColor(getResources().getColor(R.color.text_black));
                mTvHeight3.setTextColor(getResources().getColor(R.color.white));
                mTvHeight4.setTextColor(getResources().getColor(R.color.text_black));
                mTvHeight1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvHeight2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvHeight3.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                mTvHeight4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                average_height = "3";
                break;
            case R.id.tv_height_4:
                mTvHeight1.setTextColor(getResources().getColor(R.color.text_black));
                mTvHeight2.setTextColor(getResources().getColor(R.color.text_black));
                mTvHeight3.setTextColor(getResources().getColor(R.color.text_black));
                mTvHeight4.setTextColor(getResources().getColor(R.color.white));
                mTvHeight1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvHeight2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvHeight3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvHeight4.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                average_height = "4";
                break;
            case R.id.tv_age_1:
                mTvAge1.setTextColor(getResources().getColor(R.color.white));
                mTvAge2.setTextColor(getResources().getColor(R.color.text_black));
                mTvAge3.setTextColor(getResources().getColor(R.color.text_black));
                mTvAge4.setTextColor(getResources().getColor(R.color.text_black));
                mTvAge1.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                mTvAge2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvAge3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvAge4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                age_range = "1";
                break;
            case R.id.tv_age_2:
                mTvAge1.setTextColor(getResources().getColor(R.color.text_black));
                mTvAge2.setTextColor(getResources().getColor(R.color.white));
                mTvAge3.setTextColor(getResources().getColor(R.color.text_black));
                mTvAge4.setTextColor(getResources().getColor(R.color.text_black));
                mTvAge1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvAge2.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                mTvAge3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvAge4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                age_range = "2";
                break;
            case R.id.tv_age_3:
                mTvAge1.setTextColor(getResources().getColor(R.color.text_black));
                mTvAge2.setTextColor(getResources().getColor(R.color.text_black));
                mTvAge3.setTextColor(getResources().getColor(R.color.white));
                mTvAge4.setTextColor(getResources().getColor(R.color.text_black));
                mTvAge1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvAge2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvAge3.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                mTvAge4.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                age_range = "3";
                break;
            case R.id.tv_age_4:
                mTvAge1.setTextColor(getResources().getColor(R.color.text_black));
                mTvAge2.setTextColor(getResources().getColor(R.color.text_black));
                mTvAge3.setTextColor(getResources().getColor(R.color.text_black));
                mTvAge4.setTextColor(getResources().getColor(R.color.white));
                mTvAge1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvAge2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvAge3.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvAge4.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                age_range = "4";
                break;
            case R.id.tv_style_1:
                mTvStyle1.setTextColor(getResources().getColor(R.color.white));
                mTvStyle2.setTextColor(getResources().getColor(R.color.text_black));
                mTvStyle1.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                mTvStyle2.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                style = "become_strong";
                break;
            case R.id.tv_style_2:
                mTvStyle1.setTextColor(getResources().getColor(R.color.text_black));
                mTvStyle2.setTextColor(getResources().getColor(R.color.white));
                mTvStyle1.setBackground(getResources().getDrawable(R.drawable.shape_corner_gray_bg));
                mTvStyle2.setBackground(getResources().getDrawable(R.drawable.shape_corner_green));
                style = "for_fun";
                break;
            case R.id.tv_confirm:
                setData();

                break;
            case R.id.ll_transparent:
                finish();
                break;
        }
    }

    private void setData() {
        String location = StringUtils.getEditText(mEdtLocation);
//        for (int i = 0; i < mRegions.size(); i++) {
//            for (int j = 0; j < mRegions.get(i).getDistricts().size(); j++) {
//                if (location.contains(mRegions.get(i).getDistricts().get(j).getDistrict())) {
//                    district_id = mRegions.get(i).getDistricts().get(j).getDistrict_id();
//                }
//            }
//        }

        Intent intent = getIntent();
        if (!StringUtils.isEmpty(mEdtStartTime)) {
            mStartTime = StringUtils.getEditText(mEdtStartTime);
        }
        if (!StringUtils.isEmpty(mEdtEndTime)) {
            mEndTime = StringUtils.getEditText(mEdtEndTime);
        }
        intent.putExtra("mStartTime", mStartTime);
        intent.putExtra("mEndTime", mEndTime);
        intent.putExtra("district_id", district_id);
        intent.putExtra("size", size);
        intent.putExtra("average_height", average_height);
        intent.putExtra("age_range", age_range);
        intent.putExtra("style", style);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 選擇地區
     */
    private void chooseArea() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_choose_area, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        LoopView loopView1 = (LoopView) contentView.findViewById(R.id.loop_view1);
        final LoopView loopView2 = (LoopView) contentView.findViewById(R.id.loop_view2);
        TextView tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);

        loopView1.setItems(mRegions);
        loopView1.setCurrentPosition(regionPos);
        loopView1.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                regionPos = index;
                districtPos = 0;
                districtList = new ArrayList<String>();
                mDistricts = mAreaRegions.get(index).getDistricts();
                for (int i = 0; i < mDistricts.size(); i++) {
                    String district = mDistricts.get(i).getDistrict();
                    String s = null;
                    if (district.contains("$")) {
                        s = district.replace("$", " ");
                        districtList.add(s);
                    } else {
                        districtList.add(district);
                    }
                }
                loopView2.setItems(districtList);
                loopView2.setCurrentPosition(districtPos);
            }
        });
        districtList = new ArrayList<>();
        mDistricts = mAreaRegions.get(regionPos).getDistricts();
        for (int i = 0; i < mDistricts.size(); i++) {
            String district = mDistricts.get(i).getDistrict();
            String s = null;
            if (district.contains("$")) {
                s = district.replace("$", " ");
                districtList.add(s);
            } else {
                districtList.add(district);
            }
        }
        loopView2.setItems(districtList);
        loopView2.setCurrentPosition(districtPos);
        loopView2.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                districtPos = index;
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                district_id = mAreaRegions.get(regionPos).getDistricts().get(districtPos).getDistrict_id();
//                Logger.d(mAreaRegions.get(regionPos).getDistricts().get(districtPos).getDistrict().replace
//                        ("$", " ").length());
                mEdtLocation.setText(mAreaRegions.get(regionPos).getDistricts().get(districtPos).getDistrict().replace
                        ("$", " "));
//                ToastUtil.toastShort(district_id);
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置好参数之后再show
        popupWindow.showAtLocation(mLlParent, Gravity.CENTER, 0, 0);
//        backgroundAlpha(0.5f);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_TIME && resultCode == RESULT_OK) {
            mStartTime = data.getStringExtra("start_time");
            mEndTime = data.getStringExtra("end_time");
            mEdtStartTime.setText(mStartTime);
            mEdtEndTime.setText(mEndTime);
        }
    }
}
