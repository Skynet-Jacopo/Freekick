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
import com.football.freekick.commons.colorpicker.ColorListener;
import com.football.freekick.commons.colorpicker.ColorPickerView;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterPager3Activity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView        mTvTitle;
    @Bind(R.id.tv_back)
    TextView        mTvBack;
    @Bind(R.id.iv_clothes_home)
    ImageView       mIvClothesHome;
    @Bind(R.id.color_picker_home)
    ColorPickerView mColorPickerHome;
    @Bind(R.id.iv_clothes_visitor)
    ImageView       mIvClothesVisitor;
    @Bind(R.id.color_picker_visitor)
    ColorPickerView mColorPickerVisitor;
    @Bind(R.id.tv_complete)
    TextView        mTvComplete;

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pager3);
        mContext = RegisterPager3Activity.this;
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mColorPickerHome.setSelectorPoint(50,10);
        mTvBack.setTypeface(App.mTypeface);
        mColorPickerHome.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(int color) {
                Logger.d("顏色---->"+color);
                Logger.d("顏色---->"+mColorPickerHome.getColorHtml());
                mIvClothesHome.setBackgroundColor(color);
            }
        });
        mColorPickerVisitor.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(int color) {
                Logger.d("顏色---->"+color);
                Logger.d("顏色---->"+mColorPickerVisitor.getColorHtml());
                mIvClothesVisitor.setBackgroundColor(color);
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
                startActivity(new Intent(mContext,OneTimePagerActivity.class));
                break;
        }
    }
}
