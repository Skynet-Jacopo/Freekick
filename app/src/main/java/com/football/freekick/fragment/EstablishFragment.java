package com.football.freekick.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.CalenderActivity;
import com.football.freekick.R;
import com.football.freekick.activity.ChooseTimeActivity;
import com.football.freekick.beans.Area;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.views.loopview.LoopView;
import com.football.freekick.views.loopview.OnItemSelectedListener;
import com.google.gson.Gson;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 建立球賽頁
 */
public class EstablishFragment extends Fragment {


    public static final int CHOOSE_DATE = 1;
    public static final int CHOOSE_TIME = 2;
    @Bind(R.id.tv_icon_date)
    TextView         mTvIconDate;
    @Bind(R.id.ll_match_date)
    LinearLayout     mLlMatchDate;
    @Bind(R.id.tv_time)
    TextView         mTvTime;
    @Bind(R.id.ll_match_time)
    LinearLayout     mLlMatchTime;
    @Bind(R.id.tv_pitch_size)
    TextView         mTvPitchSize;
    @Bind(R.id.ll_pitch_size)
    LinearLayout     mLlPitchSize;
    @Bind(R.id.tv_area)
    TextView         mTvArea;
    @Bind(R.id.ll_area)
    LinearLayout     mLlArea;
    @Bind(R.id.tv_pitch_name)
    TextView         mTvPitchName;
    @Bind(R.id.ll_pitch_name)
    LinearLayout     mLlPitchName;
    @Bind(R.id.tv_reduce)
    TextView         mTvReduce;
    @Bind(R.id.tv_people_num)
    TextView         mTvPeopleNum;
    @Bind(R.id.tv_add)
    TextView         mTvAdd;
    @Bind(R.id.tv_establish)
    TextView         mTvEstablish;
    @Bind(R.id.tv_date)
    TextView         mTvDate;
    @Bind(R.id.ll_parent)
    AutoLinearLayout mLlParent;
    private Context                              mContext;
    private String                               mStartTime;
    private String                               mEndTime;
    private List<String>                         mRegions;//大區
    private List<Area.RegionsBean>               mAreaRegions;//解析出的大區
    private List<Area.RegionsBean.DistrictsBean> mDistricts;//解析出的小區
    private List<String>                         districtList;//小區
    private int                                  regionPos;
    private int                                  districtPos;

    private int defaultNum = 7;//默認作賽人數

    public EstablishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_establish, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvIconDate.setTypeface(App.mTypeface);

        mTvPeopleNum.setText(defaultNum + getString(R.string.peoson));
        //初始化地區數據
        String string = getString(R.string.text_area).trim();
        Gson   gson   = new Gson();
        Area   area   = gson.fromJson(string, Area.class);
        mRegions = new ArrayList<>();
        mAreaRegions = area.getRegions();
        for (int i = 0; i < mAreaRegions.size(); i++) {
            String region = mAreaRegions.get(i).getRegion();
            String s      = null;
            if (region.contains("$")) {
                s = region.replace("$", " ");
                mRegions.add(s);
            } else {
                mRegions.add(region);
            }

        }
        mDistricts = mAreaRegions.get(0).getDistricts();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ll_match_date, R.id.ll_match_time, R.id.ll_pitch_size, R.id.ll_area, R.id.tv_reduce, R.id.tv_add, R.id.tv_establish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_match_date:
                startActivityForResult(new Intent(mContext, CalenderActivity.class), CHOOSE_DATE);
                break;
            case R.id.ll_match_time:
                startActivityForResult(new Intent(mContext, ChooseTimeActivity.class), CHOOSE_TIME);
                break;
            case R.id.ll_pitch_size:
                choosePitchSize();
                break;
            case R.id.ll_area:
                chooseArea();
                break;
            case R.id.tv_reduce:
                if (defaultNum>1){
                    defaultNum-=1;
                }
                mTvPeopleNum.setText(defaultNum+getString(R.string.peoson));
                break;
            case R.id.tv_add:
                defaultNum+=1;
                mTvPeopleNum.setText(defaultNum+getString(R.string.peoson));
                break;
            case R.id.tv_establish:
                break;
        }
    }

    /**
     * 選擇地區
     */
    private void chooseArea() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_choose_area, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        LoopView       loopView1 = (LoopView) contentView.findViewById(R.id.loop_view1);
        final LoopView loopView2 = (LoopView) contentView.findViewById(R.id.loop_view2);
        TextView       tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);

        loopView1.setItems(mRegions);
        loopView1.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                regionPos = index;
                districtList = new ArrayList<String>();
                mDistricts = mAreaRegions.get(index).getDistricts();
                for (int i = 0; i < mDistricts.size(); i++) {
                    String district = mDistricts.get(i).getDistrict();
                    String s        = null;
                    if (district.contains("$")) {
                        s = district.replace("$", " ");
                        districtList.add(s);
                    } else {
                        districtList.add(district);
                    }
                }
                loopView2.setItems(districtList);
            }
        });
        districtList = new ArrayList<>();
        mDistricts = mAreaRegions.get(0).getDistricts();
        for (int i = 0; i < mDistricts.size(); i++) {
            String district = mDistricts.get(i).getDistrict();
            String s        = null;
            if (district.contains("$")) {
                s = district.replace("$", " ");
                districtList.add(s);
            } else {
                districtList.add(district);
            }
        }
        loopView2.setItems(districtList);
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
                String district_id = mAreaRegions.get(regionPos).getDistricts().get(districtPos).getDistrict_id();
                mTvArea.setText(mAreaRegions.get(regionPos).getDistricts().get(districtPos).getDistrict());
                ToastUtil.toastShort(district_id);
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
        backgroundAlpha(0.5f);
    }

    /**
     * 選擇球場大小
     */
    private void choosePitchSize() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_choose_pitch_size, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        TextView          tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);
        final RadioButton radio11   = (RadioButton) contentView.findViewById(R.id.radio_11);
        final RadioButton radio7    = (RadioButton) contentView.findViewById(R.id.radio_7);
        final RadioButton radio5    = (RadioButton) contentView.findViewById(R.id.radio_5);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if (radio5.isChecked()) {
                    mTvPitchSize.setText(radio5.getText());
                } else if (radio7.isChecked()) {
                    mTvPitchSize.setText(radio7.getText());
                } else if (radio11.isChecked()) {
                    mTvPitchSize.setText(radio11.getText());
                }
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
        backgroundAlpha(0.5f);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_DATE && resultCode == RESULT_OK) {
            String day   = data.getStringExtra("day");
            String month = data.getStringExtra("month");
            String year  = data.getStringExtra("year");
//            ToastUtil.toastShort(year + "年" + month + "月" + day + "日");
            mTvDate.setText(year + "-" + month + "-" + day);
        } else if (requestCode == CHOOSE_TIME && resultCode == RESULT_OK) {
            mStartTime = data.getStringExtra("start_time");
            mEndTime = data.getStringExtra("end_time");
            mTvTime.setText(getString(R.string.from) + " " + mStartTime + "\u3000" + getString(R.string.to) + " " + mEndTime);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }
}
