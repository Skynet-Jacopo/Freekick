package com.football.freekick.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.Area;
import com.football.freekick.utils.StringUtils;
import com.football.freekick.utils.ToastUtil;
import com.google.gson.Gson;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 篩選界面
 */
public class FiltrateActivity extends BaseActivity {

    @Bind(R.id.text1)
    TextView mText1;
    @Bind(R.id.edt_start_time)
    EditText mEdtStartTime;
    @Bind(R.id.text2)
    TextView mText2;
    @Bind(R.id.edt_end_time)
    EditText mEdtEndTime;
    @Bind(R.id.tv_icon_location)
    TextView mTvIconLocation;
    @Bind(R.id.edt_location)
    EditText mEdtLocation;
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
    private String mStartTime = "00:00";
    private String mEndTime = "00:00";
    private String size = "2";//默認球隊人數
    private String average_height = "2";//默認平均高度
    private String age_range = "2";//默認球隊年齡
    private String style = "1";//默認風格
    private List<Area.RegionsBean> mRegions;
    private String district_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrate);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initData() {
        String string = getString(R.string.text_area).trim();
        Gson gson = new Gson();
        Area area = gson.fromJson(string, Area.class);
        mRegions = area.getRegions();
    }

    private void initView() {
        mTvIconLocation.setTypeface(App.mTypeface);
    }

    @OnClick({R.id.tv_icon_location, R.id.tv_num_1, R.id.tv_num_2, R.id.tv_num_3, R.id.tv_num_4, R.id.tv_height_1, R
            .id.tv_height_2, R.id.tv_height_3, R.id.tv_height_4, R.id.tv_age_1, R.id.tv_age_2, R.id.tv_age_3, R.id
            .tv_age_4, R.id.tv_style_1, R.id.tv_style_2, R.id.tv_confirm, R.id.ll_transparent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_icon_location:
                ToastUtil.toastShort("定位");
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
        for (int i = 0; i < mRegions.size(); i++) {
            for (int j = 0; j < mRegions.get(i).getDistricts().size(); j++) {
                if (location.contains(mRegions.get(i).getDistricts().get(j).getDistrict())){
                    district_id = mRegions.get(i).getDistricts().get(j).getDistrict_id();
                }
            }
        }

        Intent intent = getIntent();
        if (!StringUtils.isEmpty(mEdtStartTime)){
            mStartTime = StringUtils.getEditText(mEdtStartTime);
        }
        if (!StringUtils.isEmpty(mEdtEndTime)){
            mEndTime = StringUtils.getEditText(mEdtEndTime);
        }
        intent.putExtra("mStartTime",mStartTime);
        intent.putExtra("mEndTime",mEndTime);
        intent.putExtra("district_id",district_id);
        intent.putExtra("size",size);
        intent.putExtra("average_height",average_height);
        intent.putExtra("age_range",age_range);
        intent.putExtra("style",style);
        setResult(RESULT_OK,intent);
        finish();
    }
}
