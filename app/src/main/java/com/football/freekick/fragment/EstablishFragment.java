package com.football.freekick.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.CalenderActivity;
import com.football.freekick.R;
import com.football.freekick.activity.ChooseTimeActivity;
import com.football.freekick.activity.MatchInviteActivity;
import com.football.freekick.app.BaseFragment;
import com.football.freekick.beans.Advertisements;
import com.football.freekick.beans.Area;
import com.football.freekick.beans.Matches;
import com.football.freekick.beans.Pitches;
import com.football.freekick.http.Url;
import com.football.freekick.utils.DateUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.StringUtils;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.football.freekick.views.loopview.LoopView;
import com.football.freekick.views.loopview.OnItemSelectedListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * 建立球賽頁
 */
public class EstablishFragment extends BaseFragment {


    public static final int CHOOSE_DATE = 1;
    public static final int CHOOSE_TIME = 2;
    @Bind(R.id.tv_icon_date)
    TextView mTvIconDate;
    @Bind(R.id.ll_match_date)
    LinearLayout mLlMatchDate;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.ll_match_time)
    LinearLayout mLlMatchTime;
    @Bind(R.id.tv_pitch_size)
    TextView mTvPitchSize;
    @Bind(R.id.ll_pitch_size)
    LinearLayout mLlPitchSize;
    @Bind(R.id.tv_area)
    TextView mTvArea;
    @Bind(R.id.ll_area)
    LinearLayout mLlArea;
    @Bind(R.id.tv_pitch_name)
    TextView mTvPitchName;
    @Bind(R.id.ll_pitch_name)
    LinearLayout mLlPitchName;
    @Bind(R.id.tv_reduce)
    TextView mTvReduce;
    @Bind(R.id.tv_people_num)
    TextView mTvPeopleNum;
    @Bind(R.id.tv_add)
    TextView mTvAdd;
    @Bind(R.id.tv_establish)
    TextView mTvEstablish;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.ll_parent)
    AutoLinearLayout mLlParent;
    @Bind(R.id.iv_advertisement1)
    ImageView mIvAdvertisement1;
    @Bind(R.id.iv_advertisement2)
    ImageView mIvAdvertisement2;
    @Bind(R.id.iv_advertisement3)
    ImageView mIvAdvertisement3;
    @Bind(R.id.iv_advertisement4)
    ImageView mIvAdvertisement4;
    private Context mContext;
    private String mStartTime = "00:00";
    private String mEndTime = "00:00";
    private List<String> mRegions;//大區
    private List<Area.RegionsBean> mAreaRegions;//解析出的大區
    private List<Area.RegionsBean.DistrictsBean> mDistricts;//解析出的小區
    private List<String> districtList;//小區
    private int regionPos;
    private int districtPos;

    private int pitch_size = 7;//默認球場大小
    private int defaultNum = 7;//默認作賽人數
    private int mPitchPos;//球場pos
    private int pitch_id;
    private String home_team_id;
    private String home_team_color;
    private List<Advertisements.AdvertisementsBean> mAdvertisementsList;//广告列表
    private String district_id = "";
    private DateTime mDateTime;
    private List<Pitches.PitchesBean> mPitchesList;

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
        initView();
        initData();
    }

    /**
     * 初始化View
     */
    private void initView() {
        mDateTime = new DateTime();
        initAdvertisements();
    }

    /**
     * 初始化广告位图片
     */
    private void initAdvertisements() {
        if (App.mAdvertisementsBean.size() == 0) {
            getAdvertisements();
        } else {
            mAdvertisementsList = App.mAdvertisementsBean;
            String image = mAdvertisementsList.get(0).getImage();
            for (int i = 0; i < mAdvertisementsList.size(); i++) {
                if (mAdvertisementsList.get(i).getScreen().equals("create_1")) {
                    ImageLoaderUtils.displayImage(mAdvertisementsList.get(i).getImage(), mIvAdvertisement1);
                } else if (mAdvertisementsList.get(i).getScreen().equals("create_2")) {
                    ImageLoaderUtils.displayImage(mAdvertisementsList.get(i).getImage(), mIvAdvertisement2);
                } else if (mAdvertisementsList.get(i).getScreen().equals("create_3")) {
                    ImageLoaderUtils.displayImage(mAdvertisementsList.get(i).getImage(), mIvAdvertisement3);
                } else if (mAdvertisementsList.get(i).getScreen().equals("create_4")) {
                    ImageLoaderUtils.displayImage(mAdvertisementsList.get(i).getImage(), mIvAdvertisement4);
                } else {
                    ImageLoaderUtils.displayImage(image, mIvAdvertisement1);
                    ImageLoaderUtils.displayImage(image, mIvAdvertisement2);
                    ImageLoaderUtils.displayImage(image, mIvAdvertisement3);
                    ImageLoaderUtils.displayImage(image, mIvAdvertisement4);
                }
            }
        }
    }

    /**
     * (如果App中未能获取到)获取广告列表
     */
    private void getAdvertisements() {
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "advertisements";
        Logger.d(url);
        OkGo.get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Advertisements advertisements = gson.fromJson(s, Advertisements.class);
                        mAdvertisementsList = advertisements.getAdvertisements();

                        String image = mAdvertisementsList.get(0).getImage();
                        for (int i = 0; i < mAdvertisementsList.size(); i++) {
                            if (mAdvertisementsList.get(i).getScreen().equals("create_1")) {
                                ImageLoaderUtils.displayImage(mAdvertisementsList.get(i).getImage(), mIvAdvertisement1);
                            } else if (mAdvertisementsList.get(i).getScreen().equals("create_2")) {
                                ImageLoaderUtils.displayImage(mAdvertisementsList.get(i).getImage(), mIvAdvertisement2);
                            } else if (mAdvertisementsList.get(i).getScreen().equals("create_3")) {
                                ImageLoaderUtils.displayImage(mAdvertisementsList.get(i).getImage(), mIvAdvertisement3);
                            } else if (mAdvertisementsList.get(i).getScreen().equals("create_4")) {
                                ImageLoaderUtils.displayImage(mAdvertisementsList.get(i).getImage(), mIvAdvertisement4);
                            } else {
                                ImageLoaderUtils.displayImage(image, mIvAdvertisement1);
                                ImageLoaderUtils.displayImage(image, mIvAdvertisement2);
                                ImageLoaderUtils.displayImage(image, mIvAdvertisement3);
                                ImageLoaderUtils.displayImage(image, mIvAdvertisement4);
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.json(e.getMessage());
                    }
                });
    }

    private void initData() {
        //初始人數
        mTvPeopleNum.setText(defaultNum + getString(R.string.person));
        //初始化地區數據
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

        }
        mDistricts = mAreaRegions.get(0).getDistricts();

        //創建球隊所需
        // TODO: 2017/11/19 該從數據庫中取還是在SP中
        home_team_id = PrefUtils.getString(App.APP_CONTEXT, "team_id", "");
        home_team_color = PrefUtils.getString(App.APP_CONTEXT, "color1", "");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ll_pitch_name, R.id.ll_match_date, R.id.ll_match_time, R.id.ll_pitch_size, R.id.ll_area, R.id
            .tv_reduce, R.id.tv_add, R.id.tv_establish, R.id.iv_advertisement1, R.id.iv_advertisement2, R.id
            .iv_advertisement3, R.id.iv_advertisement4})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.ll_match_date:
                intent.setClass(mContext, CalenderActivity.class);
                intent.putExtra("dateTime",mDateTime);
                startActivityForResult(intent, CHOOSE_DATE);
                break;
            case R.id.ll_match_time:
                intent.setClass(mContext, ChooseTimeActivity.class);
                intent.putExtra("start_time", mStartTime);
                intent.putExtra("end_time", mEndTime);
                startActivityForResult(intent, CHOOSE_TIME);
                break;
            case R.id.ll_pitch_size:
                choosePitchSize();
                break;
            case R.id.ll_area:
                chooseArea();
                break;
            case R.id.ll_pitch_name:
                choosePitchName();
                break;
            case R.id.tv_reduce:
                if (defaultNum > 1) {
                    defaultNum -= 1;
                }
                mTvPeopleNum.setText(defaultNum + getString(R.string.person));
                break;
            case R.id.tv_add:
                defaultNum += 1;
                mTvPeopleNum.setText(defaultNum + getString(R.string.person));
                break;
            case R.id.iv_advertisement1:
                ToastUtil.toastShort("广告1");
                break;
            case R.id.iv_advertisement2:
                ToastUtil.toastShort("广告2");
                break;
            case R.id.iv_advertisement3:
                ToastUtil.toastShort("广告3");
                break;
            case R.id.iv_advertisement4:
                ToastUtil.toastShort("广告4");
                break;
            case R.id.tv_establish:
                submitEstablish();
                break;
        }
    }

    /**
     * 點擊 開場(創建比賽)
     */
    private void submitEstablish() {
        if (StringUtils.isEmpty(mTvDate)) {
            ToastUtil.toastShort(getString(R.string.please_choose_match_date));
            return;
        }
        if (StringUtils.isEmpty(mTvTime)) {
            ToastUtil.toastShort(getString(R.string.please_choose_match_time));
            return;
        }
//        if (StringUtils.isEmpty(mTvPitchName)) {
//            ToastUtil.toastShort(getString(R.string.please_choose_pitch));
//            return;
//        }
        String size = "";
        if (StringUtils.getEditText(mTvPitchSize).equals(getString(R.string.pitch_size_5))) {
            size = "5";
        } else if (StringUtils.getEditText(mTvPitchSize).equals(getString(R.string.pitch_size_7))) {
            size = "7";
        } else if (StringUtils.getEditText(mTvPitchSize).equals(getString(R.string.pitch_size_11))) {
            size = "11";
        }
        String play_start = "";
        play_start = StringUtils.getEditText(mTvDate) + " " + mStartTime + ":00";
        String play_end = StringUtils.getEditText(mTvDate) + " " + mEndTime + ":00";
        String confirm_end = DateUtil.getPreTime(play_start, "1");//play_start前一天

        loadingShow();
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("home_team_id", home_team_id);
        object1.addProperty("home_team_color", home_team_color);
        object1.addProperty("size", size);
        object1.addProperty("play_start", play_start);
        object1.addProperty("play_end", play_end);
        object1.addProperty("confirm_end", confirm_end);
        if (StringUtils.isEmpty(mTvPitchName)) {
            // TODO: 2017/11/29 這裡other_pitch應該怎麼傳
            object1.addProperty("other_pitch", "other_pitch");//if pitch_id is NULL, other_pitch is required to fill
        } else {
            object1.addProperty("pitch_id", pitch_id);
        }
        object.add("match", object1);
        Logger.json(object.toString());
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "matches";
        Logger.d(url);
        OkGo.post(url)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        Matches matches = gson.fromJson(s, Matches.class);
                        if (matches.getMatch() != null) {
                            Matches.MatchBean match = matches.getMatch();
                            String status = match.getStatus();
                            switch (status) {
                                case "w":
//                                    ToastUtil.toastShort("未成功匹配到");
                                    break;
                                case "m":
                                    ToastUtil.toastShort("成功匹配");
                                    break;
                                case "f":
                                    ToastUtil.toastShort("已完成");
                                    break;
                                case "c":
                                    ToastUtil.toastShort("已取消");
                                    break;
                                case "i":
                                    ToastUtil.toastShort("已邀請");
                                    break;
                            }
                            int id = match.getId();
                            Intent intent = new Intent(mContext, MatchInviteActivity.class);
                            intent.putExtra("match_id", id + "");
                            startActivity(intent);
                        } else if (matches.getMatch() == null && matches.getErrors() != null) {
                            ToastUtil.toastShort(matches.getErrors().get(0));
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
     * 選擇球場(App中請求下來了)
     */
    private void choosePitchName() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_choose_pitch, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        LoopView loopView = (LoopView) contentView.findViewById(R.id.loop_view);
        TextView tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);
        List<String> pitches = new ArrayList<>();
        mPitchesList = new ArrayList<>();
        for (int i = 0; i < App.mPitchesBeanList.size(); i++) {
            if (App.mPitchesBeanList.get(i).getSize() == pitch_size) {
                if (district_id.equals("")) {
                    pitches.add(App.mPitchesBeanList.get(i).getName());
                    mPitchesList.add(App.mPitchesBeanList.get(i));
                } else if ((App.mPitchesBeanList.get(i).getDistrict().getId() + "").equals(district_id)) {
                    pitches.add(App.mPitchesBeanList.get(i).getName());
                    mPitchesList.add(App.mPitchesBeanList.get(i));
                }
            }
        }

        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mPitchPos = index;
            }
        });
        if (pitches.size() <= 0) {
            ToastUtil.toastShort(getString(R.string.no_qualified_pitches));
            return;
        } else {
            loopView.setItems(pitches);
            loopView.setCurrentPosition(mPitchPos);
        }
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                mTvPitchName.setText(mPitchesList.get(mPitchPos).getName());
                pitch_id = mPitchesList.get(mPitchPos).getId();
                Logger.d("pitch_id--->" + pitch_id);
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
                mPitchPos = 0;
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
                mPitchPos = 0;
                districtPos = index;
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                district_id = mAreaRegions.get(regionPos).getDistricts().get(districtPos).getDistrict_id();
                mTvArea.setText(mAreaRegions.get(regionPos).getDistricts().get(districtPos).getDistrict().replace
                        ("$", " "));
                mTvPitchName.setText("");
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
        TextView tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);
        final RadioButton radio11 = (RadioButton) contentView.findViewById(R.id.radio_11);
        final RadioButton radio7 = (RadioButton) contentView.findViewById(R.id.radio_7);
        final RadioButton radio5 = (RadioButton) contentView.findViewById(R.id.radio_5);
        if (StringUtils.getEditText(mTvPitchSize).equals(getString(R.string.pitch_size_5))) {
            radio5.setChecked(true);
        } else if (StringUtils.getEditText(mTvPitchSize).equals(getString(R.string.pitch_size_7))) {
            radio7.setChecked(true);
        } else if (StringUtils.getEditText(mTvPitchSize).equals(getString(R.string.pitch_size_11))) {
            radio11.setChecked(true);
        }
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPitchPos = 0;
                popupWindow.dismiss();
                if (radio5.isChecked()) {
                    mTvPitchSize.setText(radio5.getText());
                    pitch_size = 5;
                } else if (radio7.isChecked()) {
                    mTvPitchSize.setText(radio7.getText());
                    pitch_size = 7;
                } else if (radio11.isChecked()) {
                    mTvPitchSize.setText(radio11.getText());
                    pitch_size = 11;
                }
                mTvPitchName.setText("");
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
            String day = data.getStringExtra("day");
            String month = data.getStringExtra("month");
            String year = data.getStringExtra("year");
            mDateTime = (DateTime) data.getSerializableExtra("dateTime");
//            ToastUtil.toastShort(year + "年" + month + "月" + day + "日");
//            mTvDate.setText(year + "-" + month + "-" + day);
            mTvDate.setText(mDateTime.toString("yyyy-MM-dd"));
        } else if (requestCode == CHOOSE_TIME && resultCode == RESULT_OK) {
            mStartTime = data.getStringExtra("start_time");
            mEndTime = data.getStringExtra("end_time");
            mTvTime.setText(getString(R.string.from) + " " + mStartTime + "\u3000" + getString(R.string.to) + " " +
                    mEndTime);
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
