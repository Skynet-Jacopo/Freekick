package com.football.freekick.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.football.freekick.activity.AdvertisementDetailActivity;
import com.football.freekick.activity.ChooseTimeActivity;
import com.football.freekick.app.BaseFragment;
import com.football.freekick.beans.Advertisements;
import com.football.freekick.beans.Area;
import com.football.freekick.event.MainEvent;
import com.football.freekick.http.Url;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.StringUtils;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.views.ClickableImageView;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.football.freekick.views.loopview.LoopView;
import com.football.freekick.views.loopview.OnItemSelectedListener;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * 參與球賽頁.
 */
public class PartakeFragment extends BaseFragment {

    public static final int CHOOSE_DATE = 1;
    public static final int CHOOSE_TIME = 2;
    @Bind(R.id.tv_date)
    TextView mTvDate;
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
    @Bind(R.id.iv_advertisement1)
    ClickableImageView mIvAdvertisement1;
    @Bind(R.id.iv_advertisement2)
    ClickableImageView mIvAdvertisement2;
    @Bind(R.id.iv_advertisement3)
    ClickableImageView mIvAdvertisement3;
    @Bind(R.id.iv_advertisement4)
    ClickableImageView mIvAdvertisement4;
    @Bind(R.id.tv_partake)
    TextView mTvPartake;
    @Bind(R.id.ll_parent)
    AutoLinearLayout mLlParent;

    private Context mContext;

    private List<String> mRegions;//大區
    private List<Area.RegionsBean> mAreaRegions;//解析出的大區
    private List<Area.RegionsBean.DistrictsBean> mDistricts;//解析出的小區
    private List<String> districtList;//小區
    private int regionPos;
    private int districtPos;

    private int defaultNum = 7;//默認作賽人數
    private int mPitchPos;//球場pos
    private int pitch_id;
    private String home_team_id;
    private String home_team_color;
    private List<Advertisements.AdvertisementsBean> mAdvertisementsList;//广告列表
    public String mStr;

    public String mStartTime = "00:00";//默認
    public String mEndTime = "00:00";//默認
    public DateTime dateTime = null;
    public String pitch_size = "7";//默認為7
    public String district_id = "";

    public boolean isPartake;//模擬點擊跳轉到球隊搜索頁面 true 獲取數據,false不獲取數據,主要是在onResume中應用

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_partake, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        getDataFromNotice();
    }

    private void getDataFromNotice() {
        Intent intent = getActivity().getIntent();
        if (intent.getStringExtra("district")==null){
            Logger.d("傳值-->");
        }else {
            district_id = intent.getStringExtra("district_id");
            String region = intent.getStringExtra("region");
            String district = intent.getStringExtra("district");
            regionPos = mRegions.indexOf(region);
            List<Area.RegionsBean.DistrictsBean> districts = mAreaRegions.get(regionPos).getDistricts();
            for (int i = 0; i < districts.size(); i++) {
                if (districts.get(i).getDistrict().equals(district)){
                    districtPos = i;
                }
            }
            Logger.d("regionPos-->"+regionPos+"  districtPos-->"+districtPos);
            mTvArea.setText(district);
            String start = intent.getStringExtra("start");
            String end = intent.getStringExtra("end");
            DateTime startDate = DateTime.parse(start.substring(0,start.length()-6));
            DateTime endDate = DateTime.parse(end.substring(0,end.length()-6));
            DateTime minusHours = startDate.minusHours(2);
            DateTime plusHours = endDate.plusHours(2);
//            Logger.d("minusHours.getDayOfYear()--->"+minusHours.getDayOfYear());
//            Logger.d("startDate.getDayOfYear()--->"+startDate.getDayOfYear());
//            Logger.d("plusHours.getDayOfYear()--->"+plusHours.getDayOfYear());
//            Logger.d("endDate.getDayOfYear()--->"+endDate.getDayOfYear());
            if (minusHours.getYearOfCentury()<startDate.getYearOfCentury()){
                dateTime = startDate;
                mStartTime = "00:00";
                mEndTime = plusHours.toString("HH:mm");
            }else if (plusHours.getYearOfCentury()>endDate.getYearOfCentury()){
                dateTime = startDate;
                mStartTime = minusHours.toString("HH:mm");
                mEndTime = "23:30";
            }else {
                if (minusHours.getDayOfYear()<startDate.getDayOfYear()){
                    dateTime = startDate;
                    mStartTime = "00:00";
                    mEndTime = plusHours.toString("HH:mm");
                }else if (plusHours.getDayOfYear()>endDate.getDayOfYear()){
                    dateTime = startDate;
                    mStartTime = minusHours.toString("HH:mm");
                    mEndTime = "23:30";
                }else if (minusHours.getDayOfYear()==startDate.getDayOfYear()&&plusHours.getDayOfYear()==endDate.getDayOfYear()){
                    dateTime = startDate;
                    mStartTime = minusHours.toString("HH:mm");
                    mEndTime = plusHours.toString("HH:mm");
                }
            }

            mTvDate.setText(dateTime.toString("yyyy-MM-dd"));
            mTvTime.setText(getString(R.string.from) + " " + mStartTime + "\u3000" + getString(R.string.to) + " " +
                    mEndTime);
            Logger.d("startDate--->"+startDate.toString());
            Logger.d("endDate--->"+endDate.toString());
            pitch_size = intent.getStringExtra("size");
            switch (pitch_size) {
                case "5":
                    mTvPitchSize.setText(getString(R.string.pitch_size_5));
                    break;
                case "7":
                    mTvPitchSize.setText(getString(R.string.pitch_size_7));
                    break;
                case "11":
                    mTvPitchSize.setText(getString(R.string.pitch_size_11));
                    break;
            }
            Logger.d("傳值-->"+district_id+" "+region+" "+district+" "+start+" "+end+" "+pitch_size);
            //不知為何,直接寫的話,值傳不到下個界面,所以加了一個定時器.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTvPartake.performClick();
                }
            }, 500);
        }

    }

    /**
     * 初始化View
     */
    private void initView() {
        mTvIconDate.setTypeface(App.mTypeface);
        dateTime = new DateTime();
        initAdvertisements();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden){
            mTvDate.setText("");
            mTvTime.setText("");
            mTvPitchSize.setText(getString(R.string.pitch_size_7));
            mTvArea.setText("");
            dateTime = null;
            mStartTime = "00:00";
            mEndTime = "00:00";
            regionPos = 0;
            districtPos = 0;
        }
        super.onHiddenChanged(hidden);
    }

    /**
     * 初始化广告位图片
     */
    private void initAdvertisements() {
        if (App.mAdvertisementsBean.size() <= 0) {
            getAdvertisements();
        } else {
            mAdvertisementsList = App.mAdvertisementsBean;
            for (int i = 0; i < mAdvertisementsList.size(); i++) {
                switch (mAdvertisementsList.get(i).getScreen()) {
                    case Url.JOIN_MATCH_001:
                        if (mAdvertisementsList.get(i).getImage() != null && !mAdvertisementsList.get(i).getImage()
                                .equals(""))
                            ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i).getImage()),
                                    mIvAdvertisement1, R.drawable.icon_default);
                        else
                            ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i)
                                    .getDefault_image()), mIvAdvertisement1, R.drawable.icon_default);

                        final int finalI = i;
                        mIvAdvertisement1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(mContext, AdvertisementDetailActivity.class);
                                intent.putExtra("name", mAdvertisementsList.get(finalI).getName());
                                intent.putExtra("url", mAdvertisementsList.get(finalI).getUrl());
                                startActivity(intent);
                            }
                        });
                        break;
                    case Url.JOIN_MATCH_002:
                        if (mAdvertisementsList.get(i).getImage() != null && !mAdvertisementsList.get(i).getImage()
                                .equals(""))
                            ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i).getImage()),
                                    mIvAdvertisement2, R.drawable.icon_default);
                        else
                            ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i)
                                    .getDefault_image()), mIvAdvertisement2, R.drawable.icon_default);

                        final int finalI1 = i;
                        mIvAdvertisement2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(mContext, AdvertisementDetailActivity.class);
                                intent.putExtra("name", mAdvertisementsList.get(finalI1).getName());
                                intent.putExtra("url", mAdvertisementsList.get(finalI1).getUrl());
                                startActivity(intent);
                            }
                        });
                        break;
                    case Url.JOIN_MATCH_003:
                        if (mAdvertisementsList.get(i).getImage() != null && !mAdvertisementsList.get(i).getImage()
                                .equals(""))
                            ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i).getImage()),
                                    mIvAdvertisement3, R.drawable.icon_default);
                        else
                            ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i)
                                    .getDefault_image()), mIvAdvertisement3, R.drawable.icon_default);
                        final int finalI2 = i;
                        mIvAdvertisement3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(mContext, AdvertisementDetailActivity.class);
                                intent.putExtra("name", mAdvertisementsList.get(finalI2).getName());
                                intent.putExtra("url", mAdvertisementsList.get(finalI2).getUrl());
                                startActivity(intent);
                            }
                        });
                        break;
                    case Url.JOIN_MATCH_004:
                        if (mAdvertisementsList.get(i).getImage() != null && !mAdvertisementsList.get(i).getImage()
                                .equals(""))
                            ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i).getImage()),
                                    mIvAdvertisement4, R.drawable.icon_default);
                        else
                            ImageLoaderUtils.displayImage(MyUtil.getImageUrl(mAdvertisementsList.get(i)
                                    .getDefault_image()), mIvAdvertisement4, R.drawable.icon_default);

                        final int finalI3 = i;
                        mIvAdvertisement4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(mContext, AdvertisementDetailActivity.class);
                                intent.putExtra("name", mAdvertisementsList.get(finalI3).getName());
                                intent.putExtra("url", mAdvertisementsList.get(finalI3).getUrl());
                                startActivity(intent);
                            }
                        });
                        break;
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

    @OnClick({R.id.ll_match_date, R.id.ll_match_time, R.id.ll_pitch_size, R.id.ll_area, R.id.iv_advertisement1, R.id
            .iv_advertisement2, R.id.iv_advertisement3, R.id.iv_advertisement4, R.id.tv_partake})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.ll_match_date:
                intent.setClass(mContext, CalenderActivity.class);
                intent.putExtra("dateTime",dateTime);
                startActivityForResult(intent, CHOOSE_DATE);
                break;
            case R.id.ll_match_time:
                intent.setClass(mContext,ChooseTimeActivity.class);
                intent.putExtra("start_time",mStartTime);
                intent.putExtra("end_time",mEndTime);
                startActivityForResult(intent, CHOOSE_TIME);
                break;
            case R.id.ll_pitch_size:
                choosePitchSize();
                break;
            case R.id.ll_area:
                chooseArea();
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
            case R.id.tv_partake:
                goToNextPager();
                break;
        }
    }

    private void goToNextPager() {
        if (StringUtils.isEmpty(mTvDate)) {
            ToastUtil.toastShort(getString(R.string.please_choose_match_date));
            return;
        }
        if (StringUtils.isEmpty(mTvTime)) {
            ToastUtil.toastShort(getString(R.string.please_choose_match_time));
            return;
        }
        if (StringUtils.isEmpty(mTvArea)) {
            ToastUtil.toastShort(getString(R.string.please_choose_the_district));
            return;
        }
        getDataToNextPage();
        EventBus.getDefault().post(new MainEvent(1));
        mStr = "你吃飯了么";
    }
    //將數據獲取給下個頁面
    private void getDataToNextPage() {

        isPartake = true;
        if (StringUtils.getEditText(mTvPitchSize).equals(getString(R.string.pitch_size_5))) {
            pitch_size = "5";
        } else if (StringUtils.getEditText(mTvPitchSize).equals(getString(R.string.pitch_size_7))) {
            pitch_size = "7";
        } else if (StringUtils.getEditText(mTvPitchSize).equals(getString(R.string.pitch_size_11))) {
            pitch_size = "11";
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
                Logger.d(mAreaRegions.get(regionPos).getDistricts().get(districtPos).getDistrict().replace
                        ("$", " ").length());
                mTvArea.setText(mAreaRegions.get(regionPos).getDistricts().get(districtPos).getDistrict().replace
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
            String day = data.getStringExtra("day");
            String month = data.getStringExtra("month");
            String year = data.getStringExtra("year");
            dateTime = (DateTime) data.getSerializableExtra("dateTime");
//            ToastUtil.toastShort(year + "年" + month + "月" + day + "日");
//            mTvDate.setText(year + "-" + month + "-" + day);
            mTvDate.setText(dateTime.toString("yyyy-MM-dd"));
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
